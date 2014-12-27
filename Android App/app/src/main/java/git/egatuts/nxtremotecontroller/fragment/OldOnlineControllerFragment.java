/*package git.egatuts.nxtremotecontroller.fragment;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import git.egatuts.nxtremotecontroller.ControllerActivity;
import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.bluetooth.threads.ConnectedThread;
import git.egatuts.nxtremotecontroller.device.PairedDevice;

public class OldOnlineControllerFragment extends Fragment {

  private ConnectedThread connected_thread;
  private ControllerActivity activity;
  private PairedDevice device;
  private BluetoothSocket socket;
  private NXTConnector nxt_connector;

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.activity = (ControllerActivity) this.getActivity();
    this.device = this.activity.getDevice();
    this.nxt_connector = this.activity.getConnector();
    this.socket = this.nxt_connector.getSocket();
    this.connected_thread = new ConnectedThread(this.socket, this.nxt_connector);
  }

  /*@Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent_container, Bundle savedInstanceState) {
    TextView view = new TextView(this.activity);
    view.setText(getClass().getCanonicalName());
    parent_container.addView(view);
    return view;
  }

}*/