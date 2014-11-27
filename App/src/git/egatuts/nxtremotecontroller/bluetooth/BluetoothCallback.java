package git.egatuts.nxtremotecontroller.bluetooth;

public class BluetoothCallback
{
  
  public interface onDiscoveryListener
  {
    public void onDiscover ();
  }
  
  public interface onConnectionListener
  {
    public void onConnect ();
    public void onDisconnect();
  }
  
}