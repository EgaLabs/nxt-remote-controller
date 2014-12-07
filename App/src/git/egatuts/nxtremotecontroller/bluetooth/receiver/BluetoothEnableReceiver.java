package git.egatuts.nxtremotecontroller.bluetooth.receiver;

import git.egatuts.nxtremotecontroller.bluetooth.listener.BaseListener;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;

public class BluetoothEnableReceiver extends BaseReceiver {

  /*
   * Constructors.
   */
  public void init () {
    this.BROADCAST_CALLBACKS_STATES.put("ON_STATE_CHANGE", true);
  }

  public BluetoothEnableReceiver (Context context) {
    super(context);
    init();
  }

  public BluetoothEnableReceiver (Context context, BaseListener listener) {
    super(context, listener);
    init();
  }

  /*
   * Overwritten getIntentFilter method. Filters discovery start, finish and device found.
   */
  @Override
  public IntentFilter getIntentFilter () {
    return new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
  }

}