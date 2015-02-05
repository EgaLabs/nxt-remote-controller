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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                              *
 *                                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                               *
 *                                                                                                                                                                   *
 *  And the corresponding file at:                                                                                                                                   *
 *                                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/activity/ControllerActivity.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.IOException;

import git.egatuts.nxtremotecontroller.utils.GlobalUtils;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.fragment.LocalControllerFragment;
import git.egatuts.nxtremotecontroller.fragment.OnlineControllerFragment;

/*
 *  Main activity that is created to handle a BluetoothSocket and control locally and remotely the NXT robot.
 *  Uses the TabHost and TabWidget APIs and gets some theme attributes to customize
 *  the appearance of the tabs, because drawables can't access theme attributes :(
 */
public class ControllerActivity extends BaseActivity implements ActivityPendingTransition {

  private PairedDevice device;
  private PaintDrawable tabSelected;
  private PaintDrawable tabSelectedAndPressed;
  private FragmentTabHost tabHost;
  private StateListDrawable tabDrawableList;
  private ColorStateList tabColorList;
  private NXTConnector connector;
  private Handler handler;
  private AlertDialog.Builder builder;
  private boolean firstTime = true;
  private boolean aborted;

  /*
   *  Getter and setter for the NXTConnector.
   */
  public void setConnector (NXTConnector connector) {
    this.connector = connector;
  }

  public NXTConnector getConnector () {
    return this.connector;
  }

  /*
   *  This method returns the view of a new tab using the backgrounds created in the onCreate method.
   */
  public View createTabView (Context context, int resId, String text) {
    View view = LayoutInflater.from(context).inflate(resId, null);
    TextView title = (TextView) view.findViewById(R.id.tab_title);
    title.setText(text);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      title.setBackground(this.tabDrawableList.getConstantState().newDrawable());
    } else {
      title.setBackgroundDrawable(this.tabDrawableList.getConstantState().newDrawable());
    }
    title.setTextColor(this.tabColorList);
    return view;
  }

  public View createTabView (Context context, int resId, int text) {
    return this.createTabView(context, resId, this.getGlobalUtils().getStringResource(text));
  }

  public View createTabView (int resId, String text) {
    return this.createTabView(this, resId, text);
  }

  public View createTabView (int resId, int text) {
    return this.createTabView(this, resId, text);
  }

  /*
   *  Method used to add a tab view and add it to the tab host.
   */
  public void addTab (FragmentTabHost tabHost, View tabView, String spec, Class<?> defClass) {
    TabHost.TabSpec tabSpec = tabHost.newTabSpec(spec).setIndicator(tabView);
    tabHost.addTab(tabSpec, defClass, null);
  }

  public void addTab (FragmentTabHost tabHost, View tabView, int resId, Class<?> defClass) {
    this.addTab(tabHost, tabView, this.getGlobalUtils().getStringResource(resId), defClass);
  }

  public void addTab (View tabView, String spec, Class<?> defClass) {
    this.addTab(this.tabHost, tabView, spec, defClass);
  }

  public void addTab (View tabView, int resId, Class<?> defClass) {
    this.addTab(this.tabHost, tabView, resId, defClass);
  }

  /*
   *  We set the backward transition.
   */
  @Override
  public int[] onForward (Intent intent) {
    return new int[] {};
  }

  @Override
  public int[] onBackward () {
    return new int[] { R.anim.controller_transition_back_in, R.anim.controller_transition_back_out };
  }

  /*
   *  We enable support for our custom transitions.
   */
  @Override
  public void finish () {
    super.finish(this);
  }

  /*
   *  We kill the threads on back press.
   */
  @Override
  public void onBackPressed () {
    this.connector.closeAllThreads();
    super.onBackPressed();
  }

  /*
   *  We close the socket on destroy.
   */
  @Override
  public void onDestroy () {
    BluetoothSocket socket = this.connector.getSocket();
    if (socket != null) {
      try {
        this.connector.getSocket().close();
      } catch (IOException e) {
        //e.printStackTrace();
      }
    }
    super.onDestroy();
  }

  /*
   *  When the activity is created we enable the toolbar and the TabHost.
   */
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setActiveTheme(super.getPreferenceTheme());
    super.setContentView(R.layout.controller_layout);
    toolbar = (Toolbar) this.findViewById(R.id.toolbar);
    super.setSupportToolbar();
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        ControllerActivity.this.onBackPressed();
      }
    });

    /*
     *  We get the device from the extra data of the intent.
     */
    this.device = this.getIntent().getParcelableExtra("device");

    /*
     *  We create the final variables we will use in the listeners, etc.
     */
    final ControllerActivity self = this;
    final GlobalUtils utils = this.getGlobalUtils();

    /*
     *  We create the AlertDialog.Builder we will show to ask the user to reconnect with the device.
     */
    this.builder = utils.createAlertDialog(
            utils.getStringResource(R.string.connecting_reconnect_title),
            utils.format(R.string.connecting_reconnect_message, this.device.getName())
    ).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int which) {
        self.finish();
      }
    }).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int which) {
        self.resume();
      }
    });

    /*
     *  Now we define the progress dialog we will show while we are connecting with the device.
     *  When the progress dialog has been cancelled it means the connection process has been cancelled.
     *  But on dismiss we have to check if the connection failed or it succeed.
     */
    this.progressDialog = this.getLongProgressDialog();
    this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel (DialogInterface dialog) {
        self.aborted = true;
        utils.showToast(R.string.connecting_aborted);
        self.connector.closeConnectThread();
        self.finish();
      }
    });

    /*
     *  Now we declare the handler that will handle (so obviously) the messages
     *  sent by the threads that are in the background connecting with the device.
     */
    this.handler = new Handler () {
      @Override
      public void handleMessage (Message msg) {
        if ((self.connector.getConnectThread() == null || self.aborted) && self.connector.getConnectedThread() == null) {
          return;
        }
        int category = msg.what;
        int state;
        int error;
        if (category == NXTConnector.WHAT_CHANGE_STATE) {
          progressDialog.show();
          state = msg.arg1;
          error = msg.arg2;
          if (NXTConnector.isPreparingConnection(state)) {
            progressDialog.setText(R.string.connecting_preparing_connection);
          } else if (NXTConnector.isCreatingSocket(state)) {
            progressDialog.setText(R.string.connecting_creating_socket);
          } else if (NXTConnector.isConnecting(state)) {
            progressDialog.setText(R.string.connecting_connecting);
          } else if (NXTConnector.isConnected(state)) {
            progressDialog.dismiss();
            utils.showToast(R.string.connecting_connected, device.getName());
            self.connected();
          }
        } else if (category == NXTConnector.WHAT_ERROR_ENCOUNTERED) {
          progressDialog.dismiss();
          self.connector.closeAllThreads();
          error = msg.arg1;
          state = msg.arg2;
          boolean notReconnect = false;
          if (NXTConnector.connectionClosed(state, error)) {
            utils.showToast(R.string.connecting_connection_closed);
            notReconnect = true;
            ControllerActivity.this.finish();
          } else if (NXTConnector.connectionLost(state, error)) {
            utils.showToast(R.string.connecting_connection_lost);
            if (!self.connector.getBluetoothUtils().isEnabled()) {
              notReconnect = true;
              ControllerActivity.this.finish();
            }
          } else if (NXTConnector.connectionSocketFailed(state, error)) {
            utils.showToast(R.string.connecting_socket_error);
          } else if (NXTConnector.connectionRequestFailed(state, error)) {
            utils.showToast(R.string.connecting_request_failed);
          } else if (NXTConnector.connectionUnexpectedFailed(state, error)) {
            utils.showToast(R.string.connecting_unexpected_error);
          }
          if (!notReconnect) {
            self.builder.show();
          }
        }
      }
    };

    /*
     *  Now we create the connector with the device and the handler
     *  which we will use to connect and send messages to the robot.
     */
    this.connector = new NXTConnector(this, this.device, this.handler);

    /*
     *  We set the title with the device name.
     */
    String title = utils.getStringResource(R.string.controller_window_title);
    this.setTitle(title + this.device.getName());

    /*
     *  We set the colors we will use with the drawables used in the tabs.
     */
    final int underlineColor = utils.getAttribute(R.attr.toolbar_color);
    final int backgroundColor = utils.getAttribute(R.attr.toolbar_background);
    final int lightBackgroundColor = GlobalUtils.mixColors(backgroundColor, 0x55FFFFFF);

    /*
     *  Now we create the drawables used in all the states of the tabs and then we assign
     *  them to a StateListDrawable to add it to the tabs.
     */
    ShapeDrawable.ShaderFactory tabSelectedFactory = new ShapeDrawable.ShaderFactory() {
      @Override
      public Shader resize (int width, int height) {
        return new LinearGradient(
                0, 0,       /* Origin of the background (top left corner) */
                0, height,  /* End of the background (bottom left corner) */
                new int[] {
                        backgroundColor, backgroundColor,  /* The first gradient doesn't change color so it's like a rectangle shape */
                        underlineColor, underlineColor     /* The same for the second one */
                },
                new float[] {
                        0, 51f / 55f,  /* The first background covers 51dp out of 55dp */
                        51f / 55f, 1   /* And the second one takes the rest of the space */
                },
                Shader.TileMode.REPEAT  /* The repeat mode doesn't mind but this would look prettier in case of error */
        );
      }
    };
    PaintDrawable tabSel = new PaintDrawable();
    tabSel.setShape(new RectShape());
    tabSel.setShaderFactory(tabSelectedFactory);

    ShapeDrawable.ShaderFactory tabSelectedAndPressedFactory = new ShapeDrawable.ShaderFactory() {
      @Override
      public Shader resize (int width, int height) {
        return new LinearGradient(
                0, 0,       /* Origin of the background (top left corner) */
                0, height,  /* End of the background (bottom left corner) */
                new int[]{
                        lightBackgroundColor, lightBackgroundColor,  /* The first gradient doesn't change color so it's like a rectangle shape */
                        underlineColor, underlineColor               /* The same for the second one */
                },
                new float[]{
                        0, 51f / 55f,  /* The first background covers 51dp out of 55dp */
                        51f / 55f, 1   /* And the second one takes the rest of the space */
                },
                Shader.TileMode.REPEAT  /* The repeat mode doesn't mind but this would look prettier in case of error */
        );
      }
    };
    PaintDrawable tabSelAndPress = new PaintDrawable();
    tabSelAndPress.setShape(new RectShape());
    tabSelAndPress.setShaderFactory(tabSelectedAndPressedFactory);

    /*
     *  Now we create the states lists for the drawables and the colors.
     */
    StateListDrawable drawableList = new StateListDrawable();
    drawableList.addState(new int[] { -android.R.attr.state_selected, android.R.attr.state_pressed }, new ColorDrawable(lightBackgroundColor));
    drawableList.addState(new int[] { -android.R.attr.state_selected, android.R.attr.state_pressed, android.R.attr.state_focused }, new ColorDrawable(lightBackgroundColor));
    drawableList.addState(new int[] { android.R.attr.state_selected, -android.R.attr.state_pressed }, tabSel);
    drawableList.addState(new int[] { android.R.attr.state_selected, -android.R.attr.state_pressed, -android.R.attr.state_focused }, tabSel);
    drawableList.addState(new int[] { android.R.attr.state_selected, android.R.attr.state_pressed }, tabSelAndPress);
    drawableList.addState(new int[] { android.R.attr.state_selected, android.R.attr.state_pressed, android.R.attr.state_focused }, tabSelAndPress);
    drawableList.addState(new int[] {}, new ColorDrawable(backgroundColor));

    int darkColor = utils.getAttribute(R.attr.toolbar_color);
    int lightColor = Color.argb(0xAA, Color.red(darkColor), Color.green(darkColor), Color.blue(darkColor));
    int[][] states = new int[][]{
            new int[] { -android.R.attr.state_selected, android.R.attr.state_pressed },
            new int[] { -android.R.attr.state_selected, android.R.attr.state_pressed, android.R.attr.state_focused },
            new int[] { android.R.attr.state_selected, -android.R.attr.state_pressed },
            new int[] { android.R.attr.state_selected, -android.R.attr.state_pressed, -android.R.attr.state_focused },
            new int[] { android.R.attr.state_selected, android.R.attr.state_pressed},
            new int[] { android.R.attr.state_selected, android.R.attr.state_pressed, android.R.attr.state_focused },
            new int[] { -android.R.attr.state_selected, -android.R.attr.state_pressed, -android.R.attr.state_focused },
            new int[] {}
    };
    int[] colors = new int[] {
            lightColor,  /* Text color when pressed and not selected */
            lightColor,  /* Text color when pressed (with focused fallback) */
            darkColor,   /* Text color when selected and not pressed */
            darkColor,   /* Text color when selected and not pressed (with focused fallback) */
            darkColor,   /* Text color when selected and pressed */
            darkColor,   /* Text color when selected and pressed (with focused fallback) */
            lightColor,  /* Normal color when not pressed, selected or focused */
            lightColor   /* Default text color */
    };
    ColorStateList colorList = new ColorStateList(states, colors);

    /*
     *  We assign the drawable and the list to the activity instance to be accessible everywhere.
     */
    this.tabSelected = tabSel;
    this.tabSelectedAndPressed = tabSelAndPress;
    this.tabDrawableList = drawableList;
    this.tabColorList = colorList;

    /*
     *  Now we setup the tab host and add the tabs to the view.
     */
    this.tabHost = (FragmentTabHost) this.findViewById(R.id.tabhost);
    this.tabHost.setup(this, super.fragmentManager, R.id.tabcontent);
    this.tabHost.getTabWidget().setDividerDrawable(null);
    this.addTab(
            this.createTabView(R.layout.controller_tab, R.string.controller_tab_local_title),
            R.string.controller_tab_local_spec,
            LocalControllerFragment.class);
    this.addTab(
            this.createTabView(R.layout.controller_tab, R.string.controller_tab_online_title),
            R.string.controller_tab_online_spec,
            OnlineControllerFragment.class);
  }

  /*
   *  This method is used to resume the activity.
   */
  private void resume () {
    this.connector.startConnectThread();
  }

  /*
   *  This method is used to maintain the connection.
   */
  private void connected () {
    this.connector.stopConnectThread();
    this.connector.startConnectedThread();
  }

  /*
   *  When the activity is resumed check if there is an active connection, if exists we resume it.
   */
  @Override
  public void onResume () {
    super.onResume();
    if (this.firstTime) {
      this.firstTime = false;
      this.resume();
    }
  }

  /*
   *  Tab selector.
   */
  public void setTab (int index) {
    this.tabHost.setCurrentTab(index);
  }

}
