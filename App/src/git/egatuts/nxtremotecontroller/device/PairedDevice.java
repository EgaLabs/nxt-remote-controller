package git.egatuts.nxtremotecontroller.device;

import android.bluetooth.BluetoothDevice;

public class PairedDevice
{
  
  private String _name;
  private String _mac_address;
  private byte _signal;
  private BluetoothDevice _bluetooth_device;
  
  public PairedDevice (String name, String mac_address, byte signal, BluetoothDevice device)
  {
    this._name = name;
    this._mac_address = mac_address;
    this._signal = signal;
    this._bluetooth_device = device;
  }
  
  public String getName ()
  {
    return this._name;
  }
  
  public void setName (String name)
  {
    this._name = name;
  }
  
  public String getAddress ()
  {
    return this._mac_address;
  }
  
  public void setAddress (String address)
  {
    this._mac_address = address;
  }
  
  public byte getSignal ()
  {
    return this._signal;
  }
  
  public void setSignal (byte signal)
  {
    this._signal = signal;
  }
  
  public int getConnectivity ()
  {
    return (int) ((float) (this._signal & 0xff) / 0xff * 100);
  }
  
  public void setConnectivity (int connectivity)
  {
    int sanitized = Math.min(100, Math.max(0, connectivity));
    byte binary = (byte) ((float) sanitized / 100 * 0xff);
    this.setSignal(binary);
  }
  
  public BluetoothDevice getBluetoothDevice ()
  {
    return _bluetooth_device;
  }
  
  public void setBluetoothDevice (BluetoothDevice device)
  {
    _bluetooth_device = device;
  }
  
  public static PairedDevice from (BluetoothDevice device)
  {
    return new PairedDevice(device.getName(), device.getAddress(), (byte) 0, device);
  }

}