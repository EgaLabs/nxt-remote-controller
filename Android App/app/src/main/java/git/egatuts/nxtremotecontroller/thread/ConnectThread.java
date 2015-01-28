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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                      *
 *                                                                                                                                                           *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                       *
 *                                                                                                                                                           *
 *  And the corresponding file at:                                                                                                                           *
 *                                                                                                                                                           *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/thread/ConnectThread.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.thread;

import android.util.Log;

import java.io.IOException;

import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.exception.SocketCreationException;

/*
 *  Thread used to establish the connection with the robot.
 */
public class ConnectThread extends BaseThread {

  /*
   *  Constructor.
   */
  public ConnectThread () {
    super();
  }

  public ConnectThread (NXTConnector connector) {
    super(connector);
  }

  @Override
  public void run () {
    if (!this.isRunning()) return;
    this.setName("ConnectThread");

    /*
     *  Here we are starting the connecting process but still not tried to.
     */
      this.connector.setStatePreparing();
      try {

      /*
       *  Here we create the socket and then connect to it.
       */
        this.connector.setStateCreatingSocket();
        this.connector.createSocket();
        this.connector.setStateConnecting();
        this.connector.getSocket().connect();
      } catch (SocketCreationException e) {

      /*
       *  If there was a SocketCreationException this means the socket failed at it's creation.
       */
      //e.printStackTrace();
      this.connector.setConnectionSocketFailed();
      return;
    } catch (IOException e2) {

      /*
       *  If there was an IOException that means there was an error connecting.
       *  So we close the socket.
       */
      //e2.printStackTrace();
      try {
        this.connector.getSocket().close();
      } catch (IOException e3) {

        /*
         *  If there was another IOException that means there was an unexpected error closing the socket.
         */
        //e3.printStackTrace();
        this.connector.setConnectionUnexpectedError();
        return;
      }

      /*
       *  If no exception was thrown during the socket closing, it's just a SocketCreationException.
       */
      this.connector.setConnectionRequestFailed();
      return;
    }

    /*
     *  Here all went OK. so we send the success state and stop the thread.
     */
    this.connector.setStateConnected();
  }

}
