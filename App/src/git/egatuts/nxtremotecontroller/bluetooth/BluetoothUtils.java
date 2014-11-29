package git.egatuts.nxtremotecontroller.bluetooth;

import git.egatuts.nxtremotecontroller.device.PairedDevice;

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class BluetoothUtils
{
  
  private BluetoothAdapter bluetooth_adapter;
  
  private void initAdapter ()
  {
    bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
  }
  
  public BluetoothUtils ()
  {
    initAdapter();
  }
  
  public BluetoothAdapter getAdapter ()
  {
    if (bluetooth_adapter == null) initAdapter();
    return bluetooth_adapter;
  }
  
  public boolean isEnabled ()
  {
    return getAdapter().isEnabled();
  }
  
  public boolean isDiscovering ()
  {
    return getAdapter().isDiscovering();
  }
  
  public boolean startDiscovery ()
  {
    return this.getAdapter().startDiscovery();
  }
  
  public boolean cancelDiscovery ()
  {
    return this.getAdapter().startDiscovery();
  }
  
  public boolean enable ()
  {
    return this.getAdapter().enable();
  }
  
  public boolean disable ()
  {
    return this.getAdapter().disable();
  }
  
  public ArrayList<PairedDevice> getDevices ()
  {
    Set<BluetoothDevice> devices = bluetooth_adapter.getBondedDevices();
    ArrayList<PairedDevice> devices_set = new ArrayList<PairedDevice>();
    PairedDevice new_device;
    for (BluetoothDevice old_device : devices)
    {
      new_device = PairedDevice.from(old_device);
      devices_set.add(new_device);
    }
    return devices_set;
  }
  
}