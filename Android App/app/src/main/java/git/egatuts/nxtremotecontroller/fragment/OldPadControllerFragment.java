/*package git.egatuts.nxtremotecontroller.fragment;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import git.egatuts.nxtremotecontroller.ControllerActivity;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.bluetooth.threads.ConnectedThread;
import git.egatuts.nxtremotecontroller.device.PairedDevice;

public class OldPadControllerFragment extends BaseFragment {

  private ConnectedThread connected_thread;
  private ControllerActivity activity;
  private PairedDevice device;
  private BluetoothSocket socket;
  private NXTConnector nxt_connector;

  @Override
  public void onResume () {
    super.onResume();
    this.activity = (ControllerActivity) this.getActivity();
    this.device = this.activity.getDevice();
    this.nxt_connector = this.activity.getConnector();
    this.socket = this.nxt_connector.getSocket();
    this.nxt_connector.establishConnection(this.socket, this.device);
  }

  private class DirectionButtonOnTouchListener implements View.OnTouchListener {

    private double lmod;
    private double rmod;

    public DirectionButtonOnTouchListener (double l, double r) {
      lmod = l;
      rmod = r;
    }

    @Override
    public boolean onTouch (View v, MotionEvent event) {
      int action = event.getAction();
      if (action == MotionEvent.ACTION_DOWN) {
        byte power = (byte) 80;
        byte l = (byte) (power * lmod);
        byte r = (byte) (power * rmod);
        OldPadControllerFragment.this.nxt_connector.motors(l, r, true, false);
      } else if ((action == MotionEvent.ACTION_UP) || (action == MotionEvent.ACTION_CANCEL)) {
        OldPadControllerFragment.this.nxt_connector.motors((byte) 0, (byte) 0, false, false);
      }
      return true;
    }
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent_container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.pad_layout, parent_container, false);
    Button up = (Button) view.findViewById(R.id.up);
    Button left = (Button) view.findViewById(R.id.left);
    Button bottom = (Button) view.findViewById(R.id.bottom);
    Button right = (Button) view.findViewById(R.id.right);
    /*up.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch (View v, MotionEvent event) {
        int action = event.getAction();
        PadControllerFragment self = PadControllerFragment.this;
        if (action == MotionEvent.ACTION_DOWN) {
          byte power = (byte) 80;
          self.nxt_connector.motors((byte) 80, (byte) 80, true, true);
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
          self.nxt_connector.motors((byte) 0, (byte) 0, false, true);
        }
        return true;
      }
    });*/
    /*up.setOnTouchListener(new DirectionButtonOnTouchListener(1, 1));
    left.setOnTouchListener(new DirectionButtonOnTouchListener(-0.6, 0.6));
    bottom.setOnTouchListener(new DirectionButtonOnTouchListener(-1, -1));
    right.setOnTouchListener(new DirectionButtonOnTouchListener(0.6, -0.6));
    return view;
  }

  @Override
  public void onDestroy () {
    this.nxt_connector.closeConnections();
    super.onDestroy();
  }

}*/
