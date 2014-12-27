package git.egatuts.nxtremotecontroller.bluetooth.threads;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.bluetooth.exception.ConnectionFailedException;
import git.egatuts.nxtremotecontroller.device.PairedDevice;

public class ConnectedThread extends BaseThread {

  private InputStream input_stream;
  private OutputStream output_stream;

  /*
   * Constructors.
   */
  private void init () {
    InputStream in = null;
    OutputStream out = null;
    try {
      in = this.socket.getInputStream();
      out = this.socket.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.input_stream = in;
    this.output_stream = out;
  }

  public ConnectedThread (PairedDevice device, NXTConnector connector) throws IOException, ConnectionFailedException {
    super(device, connector);
    this.init();
  }

  public ConnectedThread (BluetoothDevice device, NXTConnector connector) throws IOException, ConnectionFailedException {
    super(device, connector);
    this.init();
  }

  public ConnectedThread (BluetoothSocket socket, NXTConnector connector) {
    super(socket, connector);
    this.init();
  }

  /*
   * Main thread.
   */
  public void run () {
    byte[] buffer = new byte[1024];
    int bytes;
    while (true) {
      try {
        bytes = this.input_stream.read(buffer);
      } catch (IOException e) {
        e.printStackTrace();
        break;
      }
    }
  }

  public void write (byte[] buffer) {
    try {
      this.output_stream.write(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
