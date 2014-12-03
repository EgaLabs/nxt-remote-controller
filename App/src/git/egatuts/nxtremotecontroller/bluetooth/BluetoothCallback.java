package git.egatuts.nxtremotecontroller.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

public class BluetoothCallback
{
  
  public static interface OnDiscoveryListener
  {
    public void onDiscover (BluetoothDevice bluetooth_device, Intent intent);
    public void onFinish ();
  }
  
  public static interface OnConnectionListener
  {
    public void onConnect ();
    public void onDisconnect();
  }
  
}