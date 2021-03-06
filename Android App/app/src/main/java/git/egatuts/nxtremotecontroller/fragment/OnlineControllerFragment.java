/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  Copyright (c) 2014 EgaTuts & Esaú García - All Rights Reserved                 *
 *                                                                                 *
 *  Open-source code licensed under the MIT License (the "License").               *
 *                                                                                 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy   *
 *  of this software and associated documentation files (the "Software"), to deal  *
 *  in the Software without restriction, including without limitation the rights   *
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell      *
 *  copies of the Software, and to permit persons to whom the Software is          *
 *  furnished to do so, subject to the following conditions:                       *
 *                                                                                 *
 *  The above copyright notice and this permission notice shall be included in     *
 *  all copies or substantial portions of the Software.                            *
 *                                                                                 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR     *
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,       *
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE    *
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      *
 *  THE SOFTWARE.                                                                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                    *
 *                                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                     *
 *                                                                                                                                                                         *
 *  And the corresponding file at:                                                                                                                                         *
 *                                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/OnlineControllerFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.ControllerActivity;
import git.egatuts.nxtremotecontroller.client.Client;
import git.egatuts.nxtremotecontroller.client.ClientAdapter;
import git.egatuts.nxtremotecontroller.client.ClientViewHolder;
import git.egatuts.nxtremotecontroller.exception.LoginException;
import git.egatuts.nxtremotecontroller.exception.MalformedTokenException;
import git.egatuts.nxtremotecontroller.listener.GPSLocationTracker;
import git.egatuts.nxtremotecontroller.utils.GlobalUtils;
import git.egatuts.nxtremotecontroller.utils.TokenRequester;
import git.egatuts.nxtremotecontroller.views.BaseProgressDialog;

/*
 *  Fragment used in the ControllerActivity to control the robot remotely from a computer or other phone connected to the same NET
 *  as the phone that is holding the NodeJS server and the application to connect to the robot.
 *  This fragment will send audio and video record to the remote controller and will have access to the
 *  robot sensors and the phone flash (if possible).
 */
public class OnlineControllerFragment extends ControllerBaseFragment {

  private RecyclerView recyclerView;
  private RelativeLayout actionsView;
  private Button initStreamingView;
  private Button stopStreamingView;
  private ClientAdapter clientsAdapter;
  private BaseProgressDialog progressDialog;
  private long showingTime;
  private int networkType;
  private double latitude;
  private double longitude;
  private GPSLocationTracker tracker;
  private TokenRequester requester;
  private String token;
  private Socket socket;
  private Client controlledBy;
  private String calling;

  /*
   *  Refreshes the fragment by changing the tab to the first and then again to this.
   */
  public void refreshFragment () {
    final OnlineControllerFragment self = this;
    this.getBaseActivity().runOnUiThread(new Runnable() {
      @Override
      public void run () {
        ((ControllerActivity) self.getBaseActivity()).setTab(0);
        ((ControllerActivity) self.getBaseActivity()).setTab(1);
      }
    });
  }

  /*
   *  Returns the owners mail by assuming the device has a google account configured.
   */
  public String getOwnerEmail () {
    AccountManager manager = AccountManager.get(this.getActivity());
    Account[] accounts = manager.getAccountsByType("com.google");
    return accounts[0].name;
  }

  /*
   *  Returns a location requester using GPSLocationTracker and setting up the listener.
   *  When location is obtained it stops the geolocation service and starts the passed thread.
   */
  public GPSLocationTracker getLocationRequester (final Thread thread) {
    final OnlineControllerFragment self = this;
    final GPSLocationTracker locationTracker = new GPSLocationTracker(this.getActivity());
    locationTracker.setOnChangeLocation(new GPSLocationTracker.OnChangeLocation() {
      @Override
      public void onChange (Location location) {
        self.latitude = location.getLatitude();
        self.longitude = location.getLongitude();
        locationTracker.stopLocationService();
        thread.start();
      }
    });
    return locationTracker;
  }

  /*
   *  Returns the token requester that does the POST request.
   */
  public TokenRequester getTokenRequester () {
    final OnlineControllerFragment self = this;
    final TokenRequester requester = new TokenRequester();
    requester.setRunnable(new Runnable() {
      @Override
      public void run () {
        String url = self.getPreferencesEditor().getString("preference_server_login", self.getString(R.string.preference_value_login));
        try {
          URL location = new URL(url);
          HttpsURLConnection s_connection = null;
          HttpURLConnection connection = null;
          InputStream input;
          int responseCode;
          boolean isHttps = url.contains("https");
          DataOutputStream writeStream;
          if (isHttps) {
            s_connection = (HttpsURLConnection) location.openConnection();
            s_connection.setRequestMethod("POST");
            writeStream = new DataOutputStream(s_connection.getOutputStream());
          } else {
            connection = (HttpURLConnection) location.openConnection();
            connection.setRequestMethod("POST");
            writeStream = new DataOutputStream(connection.getOutputStream());
          }
          StringBuilder urlParams = new StringBuilder();
          urlParams.append("name=").append(Build.MODEL)
                  .append("&email=").append(self.getOwnerEmail())
                  .append("&host=").append(true)
                  .append("&longitude=").append(self.longitude)
                  .append("&latitude=").append(self.latitude);
          writeStream.writeBytes(urlParams.toString());
          writeStream.flush();
          writeStream.close();
          if (isHttps) {
            input = s_connection.getInputStream();
            responseCode = s_connection.getResponseCode();
          } else {
            input = connection.getInputStream();
            responseCode = connection.getResponseCode();
          }
          if (input != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String line;
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
              result += line;
            }
            input.close();
            requester.finishRequest(result);
          }
        } catch (IOException e) {
          //e.printStackTrace();
          requester.cancelRequest(e);
        }
      }
    });
    return requester;
  }

  /*
   *  Removes all the clients from the adapter (used when disconnected from the server).
   */
  public void removeAllClients () {
    Client[] clients = this.clientsAdapter.getAll();
    for (int i = 1; i < clients.length; i++) {
      this.clientsAdapter.remove(i);
    }
  }

  /*
   *  Returns the show animation used to show the actions or the recycler view.
   */
  public AlphaAnimation getShowAnimation (final View view0, final long time) {
    AlphaAnimation show = new AlphaAnimation(0.0f, 1.0f);
    show.setFillAfter(true);
    show.setDuration(time);
    show.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart (Animation animation) {
        view0.setVisibility(View.VISIBLE);
      }
      @Override public void onAnimationEnd (Animation animation) {}
      @Override public void onAnimationRepeat (Animation animation) {}
    });
    return show;
  }

  /*
   *  Returns the hide animation used to hide the actions or the recycler view.
   */
  public AlphaAnimation getHideAnimation (final View view0, final View view1, final long time) {
    AlphaAnimation hide = new AlphaAnimation(1.0f, 0.0f);
    hide.setDuration(time);
    hide.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart (Animation animation) {
        view0.setVisibility(View.VISIBLE);
      }
      @Override
      public void onAnimationEnd (Animation animation) {
        view0.setVisibility(View.GONE);
        view1.startAnimation(getShowAnimation(view1, time));
      }
      @Override public void onAnimationRepeat (Animation animation) {
      }
    });
    return hide;
  }

  /*
   *  Executes the hide animation that hides the recycler view and shows the actions.
   */
  public void showActions () {
    final OnlineControllerFragment self = this;
    final AlphaAnimation hide = this.getHideAnimation(this.recyclerView, this.actionsView, 300);
    this.getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run () {
        self.recyclerView.startAnimation(hide);
      }
    });
  }

  /*
   *  Starts the animation that hides the actions and shows the recycler view.
   */
  public void hideActions () {
    final OnlineControllerFragment self = this;
    final AlphaAnimation hide = this.getHideAnimation(this.actionsView, this.recyclerView, 300);
    this.getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run () {
        self.actionsView.startAnimation(hide);
      }
    });
  }

  /*
   *  Starts a new SocketIO connection with the server defined in preferences_server_address.
   */
  public void startSocketConnection () {
    final OnlineControllerFragment self = this;
    final GlobalUtils globalUtils = this.getGlobalUtils();
    if (this.socket != null && this.socket.connected()) {
      return;
    }
    this.progressDialog.show();
    this.progressDialog.setText(R.string.connecting_socket_server);
    String url = this.getPreferencesEditor().getString("preference_server_address", this.getString(R.string.preference_value_address));
    Uri uri = Uri.parse(url)
            .buildUpon()
            .appendQueryParameter("token", this.token)
            .build();
    try {
      if (url.contains("https://")) {
        IO.setDefaultSSLContext(SSLContext.getDefault());
      }
      IO.Options options = new IO.Options();
      options.forceNew = true;
      options.reconnection = true;
      this.socket = IO.socket(uri.toString(), options);

      /*
       *  When the socket is connected.
       */
      socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
        @Override
        public void call (Object... args) {
          self.removeAllClients();
          self.progressDialog.dismiss();
        }

      /*
       *  When the socket is disconnected.
       */
      }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
        @Override
        public void call (Object... args) {
          self.removeAllClients();
        }

      /*
       *  When the socket receives an error.
       */
      }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
        @Override
        public void call (Object... args) {
          try {
            String error = (String) args[0];
            if (error.contains("JsonWebToken") || error.contains("TokenExpiredError")) {
              self.getBaseActivity().getPreferencesEditor().saveString("preference_server_token", "");
              globalUtils.showToast(R.string.restoring_token);
              self.refreshFragment();
              return;
            }
          } catch (ClassCastException e) {
            //e.printStackTrace();
          }
          self.progressDialog.dismiss();
          globalUtils.showToast(R.string.unknown_error);
        }

      /*
       *  When a new client is disconnected.
       */
      }).on("leave_member", new Emitter.Listener() {
        @Override
        public void call (Object... args) {
          try {
            JSONObject members = ((JSONObject) args[0]).getJSONObject("members");
            Iterator<String> membersList = members.keys();
            while (membersList.hasNext()) {
              String id = membersList.next();
              Client client = Client.fromJSON(members.getJSONObject(id));
              client.setId(id);
              final int index = self.clientsAdapter.exists(client);
              if (index == -1) {
                continue;
              }
              if (self.calling != null && self.calling.equals(self.clientsAdapter.get(index).getPeerId())) {
                self.hideActions();
                self.calling = "";
                self.controlledBy = null;
                self.getConnector().motorBC(0, 0, false, false);
              }
              self.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run () {
                  self.clientsAdapter.remove(index);
                }
              });
            }
          } catch (JSONException e) {
            //e.printStackTrace();
          }
        }

      /*
       *  When a new client is connected (also fired on first connection with all the active clients)
       */
      }).on("join_member", new Emitter.Listener() {
        @Override
        public void call (Object... args) {
          try {
            JSONObject members = ((JSONObject) args[0]).getJSONObject("members");
            Iterator<String> membersList = members.keys();
            while (membersList.hasNext()) {
              String id = membersList.next();
              JSONObject member = members.getJSONObject(id);
              final Client client = Client.fromJSON(member);
              client.setId(id);
              if (self.clientsAdapter.exists(client) > -1) {
                continue;
              }
              self.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run () {
                  self.clientsAdapter.add(client);
                }
              });
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

       /*
        *  When the client has answered our call request.
        */
      }).on("answered", new Emitter.Listener() {
        @Override
        public void call (Object... args) {
          self.progressDialog.dismiss();
          try {
            JSONObject data = (JSONObject) args[0];
            String from = data.getString("from");
            boolean accepted = data.getBoolean("state");
            if (from.equals(self.calling)) {
              globalUtils.showToast(accepted ? R.string.call_accepted : R.string.call_rejected);
              if (accepted) {
                self.controlledBy = self.clientsAdapter.getByPeer(from);
                self.showActions();
              }
            }
          } catch (JSONException e) {
            //e.printStackTrace();
          }
        }

      /*
       *  When the client sends us a motor order.
       */
      }).on("motors", new Emitter.Listener() {
        @Override
        public void call (Object... args) {
          try {
            JSONObject data = (JSONObject) args[0];
            String sender = data.getString("from");
            if (!sender.equals(self.controlledBy.getPeerId())) return;
            double b = data.getDouble("b");
            double c = data.getDouble("c");
            self.getConnector().motorBC(c, b, false, false);
          } catch (JSONException e) {
            //e.printStackTrace();
          }
        }
      });/*.on("flash", new Emitter.Listener() {
        @Override
        public void call (Object... args) {
          try {
            JSONObject data = (JSONObject) args[0];
            String sender = data.getString("from");
            boolean state = data.getBoolean("state");
            if (!sender.equals(self.controlledBy.getPeerId())) return;
            if (!globalUtils.isFlashAvailable()) return;
            Camera camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(state ? Camera.Parameters.FLASH_MODE_ON : Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.startPreview();
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      });*/
      socket.connect();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      // We are fucked if this ends here!
    }
  }

  /*
   *  When the fragment creates the view. If wifi or GPS (that is needed) are not enabled it will stop execution of logic.
   */
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    final OnlineControllerFragment self = this;
    this.progressDialog = this.getShortProgressDialog();
    View view;
    Button button;

    if (!this.getWifiManager().isWifiEnabled()) {
      view = inflater.inflate(R.layout.online_error_layout, parent, false);
      button = (Button) view.findViewById(R.id.redo_action);
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v) {
          self.refreshFragment();
        }
      });
      return view;
    }

    view = inflater.inflate(R.layout.online_layout, parent, false);
    this.recyclerView = (RecyclerView) view.findViewById(R.id.clients);
    this.actionsView = (RelativeLayout) view.findViewById(R.id.actions);

    this.initStreamingView = (Button) view.findViewById(R.id.init_streaming);
    this.initStreamingView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        String url = self.getPreferencesEditor().getString("preference_server_peer", self.getString(R.string.preference_value_peer));
        Uri uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("from", GlobalUtils.md5(self.token))
                .appendQueryParameter("to", self.calling)
                .build();
        self.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString())));
        self.socket.emit("init_stream", self.calling);
      }
    });

    this.stopStreamingView = (Button) view.findViewById(R.id.stop_streaming);
    this.stopStreamingView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        self.socket.emit("stop_stream", self.calling);
        self.calling = "";
        self.controlledBy = null;
        self.hideActions();
      }
    });

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    linearLayoutManager.scrollToPosition(0);

    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    this.clientsAdapter = new ClientAdapter(this, new ArrayList<Client>() {{
      add(new Client(null, null, null, "Ejemplo", "ejemplo@gmail.com", 41.2133293d, 1.5253539d, "El Vendrell, Cataluña", "El Vendrell, Tarragona, Cataluña, España"));
    }});
    this.clientsAdapter.setOnClickListener(new ClientAdapter.OnClickListener() {
      @Override
      public void onClick (ClientViewHolder view, int index) {
        Client client = self.clientsAdapter.get(index);
        if (client.getPeerId() != null && !client.getPeerId().equals(""))
          self.requestCall(client, view, index);
      }
    });
    recyclerView.setAdapter(this.clientsAdapter);
    return view;
  }

  /*
   *  When the app is resumed we check if connection was lost and all necessary stuff.
   */
  @Override
  public void onResume () {
    super.onResume();
    if (!this.getWifiManager().isWifiEnabled()) {
      return;
    }
    final OnlineControllerFragment self = this;
    this.token = this.getPreferencesEditor().getString("preference_server_token");
    this.tracker = new GPSLocationTracker(this.getActivity());
    if (token.equals("")) {
      if (!tracker.isGPSEnabled() && !tracker.isNetworkEnabled()) {
        this.getGlobalUtils().createAlertDialog(R.string.geolocation_required_title, R.string.geolocation_required_message)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick (DialogInterface dialog, int which) {
                    ((ControllerActivity) self.getBaseActivity()).setTab(0);
                  }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick (DialogInterface dialog, int which) {
                    self.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                  }
                })
                .setCancelable(false)
                .show();
        return;
      }
      this.progressDialog.show();
      this.progressDialog.setText(R.string.getting_access_token);
      this.requester = this.getTokenRequester();
      this.requester.setOnFinishListener(new TokenRequester.OnFinishListener() {
        @Override
        public void onFinish (String data) {
          try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("state") && jsonObject.getInt("state") == -1) {
              self.requester.cancelRequest(new LoginException());
              return;
            }
            final String token = jsonObject.getString("token");
            if (token != null || token != "") {
              self.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run () {
                  self.getBaseActivity().getPreferencesEditor().saveString("preference_server_token", token);
                  self.token = token;
                  self.startSocketConnection();
                }
              });
            } else {
              self.requester.cancelRequest(new MalformedTokenException());
            }
          } catch (JSONException e) {
            self.requester.cancelRequest(e);
          }
        }
      });
      this.requester.setOnExceptionListener(new TokenRequester.OnExceptionListener() {
        @Override
        public void onError (Exception e) {
          //e.printStackTrace();
          if (e instanceof ConnectException) {
            if (e.getLocalizedMessage().contains("ECONNREFUSED")) {
              self.getGlobalUtils().showToast(R.string.connection_error_refused, true);
            } else if (e.getLocalizedMessage().contains("EHOSTUNREACH")) {
              self.getGlobalUtils().showToast(R.string.connection_error_unreach, true);
            }
          } else if (e instanceof FileNotFoundException) {
            self.getGlobalUtils().showToast(R.string.connection_error_token_not_found, true);
          } else if (e instanceof MalformedURLException) {
            self.getGlobalUtils().showToast(R.string.connection_error_malformed, true);
          } else if (e instanceof MalformedTokenException) {
            self.getGlobalUtils().showToast(R.string.connection_error_token_malformed, true);
          } else if (e instanceof LoginException) {
            self.getGlobalUtils().showToast(R.string.connection_error_login, true);
          }
        }
      });
      this.tracker = this.getLocationRequester(this.requester);
      this.tracker.startLocationService();
      return;
    }
    this.startSocketConnection();
  }

  /*
   *  Starts a call with the specified client.
   */
  public void requestCall (Client client, ClientViewHolder view, int index) {
    this.calling = client.getPeerId();
    this.socket.emit("call", client.getPeerId());
    this.progressDialog.show();
    this.progressDialog.setText(R.string.calling_client);
    /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getActivity());
    builder.setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Remote controlling.")
            .setContentText("Your robot is now being controlled by: " + this.clientsAdapter.get(index).getName())
            .setAutoCancel(false);
    ((NotificationManager) this.getActivity().getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build());*/
  }

  /*
   *  When the view is destroyed.
   */
  @Override
  public void onDestroyView () {
    if (this.socket != null) this.socket.disconnect();
    super.onDestroyView();
  }

}
