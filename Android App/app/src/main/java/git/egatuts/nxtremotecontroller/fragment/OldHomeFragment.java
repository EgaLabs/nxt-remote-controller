/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2014 EgaTuts & Esaú García - All Rights Reserved                *
 *                                                                               *
 * Open-source code licensed under the MIT License (the "License").              *
 *                                                                               *
 * Permission is hereby granted, free of charge, to any person obtaining a copy  *
 * of this software and associated documentation files (the "Software"), to deal *
 * in the Software without restriction, including without limitation the rights  *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell     *
 * copies of the Software, and to permit persons to whom the Software is         *
 * furnished to do so, subject to the following conditions:                      *
 *                                                                               *
 * The above copyright notice and this permission notice shall be included in    *
 * all copies or substantial portions of the Software.                           *
 *                                                                               *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR    *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,      *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                                       *
 *                                                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                        *
 *                                                                                                                                                           *
 * And the corresponding file at:                                                                                                                            *
 *                                                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/HomeFragment.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*package git.egatuts.nxtremotecontroller.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;

import java.io.IOException;

import git.egatuts.nxtremotecontroller.application.BaseApplication;
import git.egatuts.nxtremotecontroller.ControllerActivity;
import git.egatuts.nxtremotecontroller.MainActivity;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.BaseActivity;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothConstants;
import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.device.PairedDeviceAdapter;
import git.egatuts.nxtremotecontroller.device.PairedDeviceItemClickListener;
import git.egatuts.nxtremotecontroller.views.BaseIndeterminateProgressDialog;

public class OldHomeFragment extends BaseFragment {

  private View view;
  private RecyclerView recycler_view;
  private LinearLayoutManager linear_layout_manager;
  private PairedDeviceAdapter paired_devices_adapter;
  private boolean connecting = false;
  private PairedDevice connecting_device;
  private RippleView connecting_device_view;
  private RippleView.AnimationFinishListener listener;
  private Handler connection_handler;
  private NXTConnector nxt_connector;
  private int progress_color;

  public OldHomeFragment () {
    listener = new RippleView.AnimationFinishListener() {
      @Override
      public void onFinish () {
        OldHomeFragment.this.startControllerActivity(connecting_device);
        connecting_device_view.setAnimationFinishListener(null);
      }
    };
  }

  private String getStr (int resId) {
    return getResources().getString(resId);
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.prepareConnection();
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent_container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.home_fragment, parent_container, false);
    paired_devices_adapter = new PairedDeviceAdapter(bluetooth_utils.getDevices());
    paired_devices_adapter.setContext(getActivity());
    recycler_view = (RecyclerView) view.findViewById(R.id.paired_devices);

    linear_layout_manager = new LinearLayoutManager(parent_container.getContext());
    linear_layout_manager.setOrientation(LinearLayoutManager.VERTICAL);
    linear_layout_manager.scrollToPosition(0);

    recycler_view.setAdapter(paired_devices_adapter);
    recycler_view.setLayoutManager(linear_layout_manager);
    recycler_view.setItemAnimator(new DefaultItemAnimator());
    recycler_view.addOnItemTouchListener(new PairedDeviceItemClickListener(getActivity(), new PairedDeviceItemClickListener.OnItemClickListener() {
      @Override
      public void onItemClick (View view, int position) {
        connecting_device = paired_devices_adapter.get(position);
        connecting_device_view = (RippleView) view;
        if (!connecting_device.isMajorToy()) {
          new AlertDialog.Builder(getActivity())
                  .setTitle(R.string.device_type_toy_title)
                  .setMessage(String.format(getStr(R.string.device_type_toy_message), connecting_device.getName()))
                  .setPositiveButton(android.R.string.yes, null)
                  .show();
          return;
        }
        if (!connecting_device.isToyRobot()) {
          new AlertDialog.Builder(getActivity())
                  .setTitle(R.string.device_type_robot_title)
                  .setMessage(String.format(getStr(R.string.device_type_robot_message), connecting_device.getName()))
                  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int id) {
                      if (!connecting_device_view.isRunning()) {
                        OldHomeFragment.this.startControllerActivity(connecting_device);
                      } else {
                        connecting_device_view.setAnimationFinishListener(listener);
                      }
                    }
                  })
                  .setNegativeButton(android.R.string.no, null)
                  .show();
          return;
        }
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run () {
            if (!connecting_device_view.isRunning()) {
              OldHomeFragment.this.startControllerActivity(connecting_device);
            } else {
              connecting_device_view.setAnimationFinishListener(listener);
            }
          }
        }, 0);
      }
    }));
    return view;
  }

  public void showMessage (int resId) {
    this.showMessage(getResources().getString(resId));
  }

  public void showMessage (String msg) {
    if (progress_dialog.isShowing()) progress_dialog.dismiss();
    if (!nxt_connector.isErrorEmpty()) nxt_connector.resetError();
    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
  }

  public void prepareConnection () {
    final BaseActivity activity = (BaseActivity) this.getActivity();
    final OldHomeFragment itself = this;
    progress_dialog = new BaseIndeterminateProgressDialog(activity);
    progress_dialog.setCancelable(true);
    progress_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss (DialogInterface dialog) {
        if (nxt_connector.isConnecting() && nxt_connector.isErrorEmpty()) {
          showMessage(R.string.aborted_connection);
          nxt_connector.stopConnectThread();
        }
      }
    });
    TypedValue typed_value = new TypedValue();
    getActivity().getTheme().resolveAttribute(R.attr.button_float_background, typed_value, true);
    progress_color = typed_value.data;
    connection_handler = new Handler() {
      @Override
      public void handleMessage (Message msg) {
        switch (msg.what) {
          case BluetoothConstants.ACTION_STATE_CHANGED:
            switch (nxt_connector.getState()) {
              case BluetoothConstants.STATE_DISCONNECTED:
              break;
              case BluetoothConstants.STATE_CONNECTED:
                itself.showMessage(String.format(getResources().getString(R.string.successful_connected), connecting_device.getName()));
                itself.saveConnectionData(connecting_device, nxt_connector);
                Intent intent = new Intent(activity, ControllerActivity.class);
                intent.putExtra("device", connecting_device);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.controller_transition_in, R.anim.controller_transition_out);
              break;
              case BluetoothConstants.STATE_CONNECTING:
                progress_dialog.show();
                progress_dialog.setColor(progress_color);
                progress_dialog.setText(R.string.connecting_device);
              break;
            }
          break;
          case BluetoothConstants.ACTION_ERROR_ENCOUNTERED:
            switch (nxt_connector.getError()) {
              case BluetoothConstants.ERROR_CONNECTION_REQUEST_FAILED:
                itself.showMessage(String.format(getResources().getString(R.string.connecting_error), connecting_device.getName()));
              break;
              case BluetoothConstants.ERROR_CREATING_SOCKET:
                itself.showMessage(String.format(getResources().getString(R.string.create_socket_error), connecting_device.getName()));
              break;
            }
          break;
        }
      }
    };
    nxt_connector = new NXTConnector(connection_handler);
  }

  public void saveConnectionData (PairedDevice device, NXTConnector connector) {
    BaseApplication app = (BaseApplication) this.getActivity().getApplication();
    app.setDevice(device);
    app.setNXTConnector(connector);
  }

  public void startControllerActivity (PairedDevice device) {
    if (nxt_connector.isConnected()) return;
    MainActivity activity = (MainActivity) getActivity();
    BaseApplication application = (BaseApplication) activity.getApplication();
    try {
      nxt_connector.openConnection(device);
    } catch (IOException e) {
      e.printStackTrace();
      nxt_connector.setError(BluetoothConstants.ERROR_UNEXPECTED);
    }
  }

}*/