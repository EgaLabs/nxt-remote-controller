package git.egatuts.nxtremotecontroller.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothCallback.OnDiscoveryListener;
import git.egatuts.nxtremotecontroller.device.PairedDevice;

public class BluetoothReceiver extends BroadcastReceiver
{
  
  private BluetoothCallback.OnDiscoveryListener onDiscoveryListener;
  
  public void onReceive (Context context, Intent intent)
  {
    String action = intent.getAction();
    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
      BluetoothDevice bluetooth_device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
      if (bluetooth_device != null) onDiscoveryListener.onDiscover(PairedDevice.from(bluetooth_device), bluetooth_device, intent);
    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
      onDiscoveryListener.onFinish();
    }
  }
  
  public void setOnDiscoveryListener (OnDiscoveryListener listener)
  {
    onDiscoveryListener = listener;
  }
  
  public OnDiscoveryListener getOnDiscoveryListener ()
  {
    return onDiscoveryListener;
  }
  
  public static IntentFilter getIntentFilter ()
  {
    IntentFilter filter = new IntentFilter();
    filter.addAction(BluetoothDevice.ACTION_FOUND);
    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    return filter;
  }
  
}