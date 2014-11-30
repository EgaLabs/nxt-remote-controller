package git.egatuts.nxtremotecontroller.bluetooth;

import git.egatuts.nxtremotecontroller.device.PairedDevice;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

public class BluetoothCallback
{
  
  public static interface OnDiscoveryListener
  {
    public void onDiscover (PairedDevice paired_device, BluetoothDevice bluetooth_device, Intent intent);
  }
  
  public static interface OnConnectionListener
  {
    public void onConnect ();
    public void onDisconnect();
  }
  
}