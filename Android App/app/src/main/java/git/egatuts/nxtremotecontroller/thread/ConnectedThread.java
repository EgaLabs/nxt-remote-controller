/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  Copyright (c) 2014 EgaTuts & Esaú García - All Rights Reserved                 *
 *                                                                                 *
 *  Open-source code licensed under the MIT License (the "License").               *
 *                                                                                 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy   *
 *  of this software and associated documentation files (the "Software"), to deal  *
 *  in the Software without restriction, including without limitation the rights   *
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell      *
 *  copies of the Software, and to permit persons to whom the Software is          *
 *  furnished to do so, subject to the following conditions:                       *
 *                                                                                 *
 *  The above copyright notice and this permission notice shall be included in     *
 *  all copies or substantial portions of the Software.                            *
 *                                                                                 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR     *
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,       *
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE    *
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      *
 *  THE SOFTWARE.                                                                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                        *
 *                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                         *
 *                                                                                                                                                             *
 *  And the corresponding file at:                                                                                                                             *
 *                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/thread/ConnectedThread.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;

/*
 *  Thread used to read and write data through the BluetoothSocket with the connected device.
 */
public class ConnectedThread extends BaseThread {

  private InputStream input;
  private OutputStream output;

  /*
   *  Executed in all the constructors.
   */
  private void init () {
    InputStream in = null;
    OutputStream out = null;
    try {
      in = this.connector.getSocket().getInputStream();
      out = this.connector.getSocket().getOutputStream();
    } catch (IOException e) {
      //e.printStackTrace();
    }
    this.input = in;
    this.output = out;
  }

  /*
   *  Constructors.
   */
  public ConnectedThread () {
    super();
    this.init();
  }

  public ConnectedThread (NXTConnector connector) {
    super(connector);
    this.init();
  }

  /*
   *  Sends a byte[] of data to the robot.
   */
  public void write (byte[] buffer) {
    if (!this.isRunning()) {
      this.connector.setConnectionClosed();
      return;
    }
    try {
      this.output.write(buffer);
    } catch (IOException e) {
      //e.printStackTrace();
    }
  }

  /*
   *  Stops the thread and deletes the socket.
   */
  @Override
  public void stopThread () {
    super.stopThread();
    if (this.connector.getSocket() != null) this.connector.setConnectionClosed();
  }

  @Override
  public void run () {
    if (!this.isRunning()) return;
    byte[] buffer = new byte[1024];
    int bytes;
    while (this.isRunning()) {
      try {
        bytes = this.input.read(buffer);
      } catch (IOException e) {
        //e.printStackTrace();
        if (this.isRunning()) this.connector.setConnectionLost();
        break;
      }
    }
  }

}
