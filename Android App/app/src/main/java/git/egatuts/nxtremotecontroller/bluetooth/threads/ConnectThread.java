package git.egatuts.nxtremotecontroller.bluetooth.threads;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

import git.egatuts.nxtremotecontroller.bluetooth.BluetoothConstants;
import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.bluetooth.exception.ConnectionFailedException;
import git.egatuts.nxtremotecontroller.device.PairedDevice;

public class ConnectThread extends BaseThread {

  /*
   * Constructors.
   */
  public ConnectThread (PairedDevice device, NXTConnector connector) throws IOException, ConnectionFailedException {
    super(device, connector);
  }

  public ConnectThread (BluetoothDevice device, NXTConnector connector) throws IOException, ConnectionFailedException {
    super(device, connector);
  }

  public ConnectThread (BluetoothSocket socket, NXTConnector connector) {
    super(socket, connector);
  }

  /*
   * Main thread.
   */
  public void run () {
    this.setName("ConnectThread");
    this.bluetooth_utils.cancelDiscovery();
    this.connector.setState(BluetoothConstants.STATE_CONNECTING);
    try {
      this.socket.connect();
    } catch (IOException e) {
      e.printStackTrace();
      try {
        this.socket.close();
      } catch (IOException e2) {
        e2.printStackTrace();
      }
      this.connector.setError(BluetoothConstants.ERROR_CONNECTION_REQUEST_FAILED);
      return;
    }

    this.connector.setState(BluetoothConstants.STATE_CONNECTED);
    this.connector.stopConnectedThread();
    this.connector.setSocket(this.socket);
  }

  @Override
  public void cancel () {
    super.cancel();
    //this.connector.setConnectionAsFailed();
  }

}