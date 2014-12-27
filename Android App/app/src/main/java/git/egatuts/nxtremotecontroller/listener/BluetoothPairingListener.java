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
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE    *
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      *
 *  THE SOFTWARE.                                                                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                    *
 *                                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                     *
 *                                                                                                                                                                         *
 *  And the corresponding file at:                                                                                                                                         *
 *                                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/listener/BluetoothPairingListener.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.listener;

import android.content.Context;
import android.content.Intent;

/*
 *  Extended class of BaseListener which listens for bonding states changes.
 */
public abstract class BluetoothPairingListener extends BaseListener {

  @Override public void onConnectionChange (Context context, Intent intent) {}
  @Override public void onDiscoveryFinish  (Context context, Intent intent) {}
  @Override public void onDiscoveryStart   (Context context, Intent intent) {}
  @Override public void onLocalNameChange  (Context context, Intent intent) {}
  @Override public void onScanModeChange   (Context context, Intent intent) {}
  @Override public void onStateChange      (Context context, Intent intent) {}
  @Override public void onLowLevelConnect           (Context context, Intent intent) {}
  @Override public void onLowLevelDisconnect        (Context context, Intent intent) {}
  @Override public void onLowLevelDisconnectRequest (Context context, Intent intent) {}
  //@Override public void onBondStateChange         (Context context, Intent intent) {}
  @Override public void onDeviceClassChange         (Context context, Intent intent) {}
  @Override public void onDeviceFound               (Context context, Intent intent) {}
  @Override public void onRemoteNameChange          (Context context, Intent intent) {}
  @Override public void onAppNeedsRestart (Context context, Intent intent) {}

}
