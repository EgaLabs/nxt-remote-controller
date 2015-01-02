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
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/listener/BaseListener.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.listener;

import android.content.Context;
import android.content.Intent;

/*
 *  Most base listener which defines all the possible events.
 */
public abstract class BaseListener {

  /*
   *  Methods for Broadcast Actions from BluetoothAdapter.
   */
  public abstract void onConnectionChange (Context context, Intent intent);

  public abstract void onDiscoveryFinish (Context context, Intent intent);

  public abstract void onDiscoveryStart (Context context, Intent intent);

  public abstract void onLocalNameChange (Context context, Intent intent);

  public abstract void onScanModeChange (Context context, Intent intent);

  public abstract void onStateChange (Context context, Intent intent);

  /*
   *  Methods for Broadcast Actions from BluetoothDevice.
   */
  public abstract void onLowLevelConnect (Context context, Intent intent);

  public abstract void onLowLevelDisconnect (Context context, Intent intent);

  public abstract void onLowLevelDisconnectRequest (Context context, Intent intent);

  public abstract void onBondStateChange (Context context, Intent intent);

  public abstract void onDeviceClassChange (Context context, Intent intent);

  public abstract void onDeviceFound (Context context, Intent intent);

  public abstract void onRemoteNameChange (Context context, Intent intent);

  /*
   *  Our unique intent.
   */
  public abstract void onAppNeedsRestart (Context context, Intent intent);

}
