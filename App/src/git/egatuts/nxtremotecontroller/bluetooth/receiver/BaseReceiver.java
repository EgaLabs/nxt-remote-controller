/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2014 EgaTuts & Esaú García - All Rights Reserved                *
 *                                                                               *
 * Open-source code licensed under the MIT License (the "License").              *
 *                                                                               *
 * Permission is hereby granted, free of charge, to any person obtaining a copy  *
 * of this software and associated documentation files (the "Software"), to deal *
 * in the Software without restriction, including without limitation the rights  *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell     *
 * copies of the Software, and to permit persons to whom the Software is         *
 * furnished to do so, subject to the following conditions:                      *
 *                                                                               *
 * The above copyright notice and this permission notice shall be included in    *
 * all copies or substantial portions of the Software.                           *
 *                                                                               *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR    *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,      *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                             *
 *                                                                                                                                 *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                              *
 *                                                                                                                                 *
 * And the corresponding file at:                                                                                                  *
 *                                                                                                                                 *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/bluetooth/receiver/BaseReceiver.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.bluetooth.receiver;

import git.egatuts.nxtremotecontroller.bluetooth.listener.BaseListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class BaseReceiver extends BroadcastReceiver {
  
  protected Context receiver_context;
  protected BaseListener receiver_listener;
  public HashMap<String, Boolean> BROADCAST_CALLBACKS_STATES = new HashMap<String, Boolean>() {
    {
      put("ON_CONNECTION",                   false);
      put("ON_DISCOVER_FINISH",              false);
      put("ON_DISCOVER_START",               false);
      put("ON_LOCAL_NAME_CHANGE",            false);
      put("ON_SCAN_MODE_CHANGE",             false);
      put("ON_STATE_CHANGE",                 false);
      put("ON_LOW_LEVEL_CONNECT",            false);
      put("ON_LOW_LEVEL_DISCONNECT",         false);
      put("ON_LOW_LEVEL_DISCONNECT_REQUEST", false);
      put("ON_BOND_STATE_CHANGE",            false);
      put("ON_DEVICE_CLASS_CHANGE",          false);
      put("ON_DEVICE_FOUND",                 false);
      put("ON_REMOTE_NAME_CHANGE",           false);
    }
  };
  public static final HashMap<String, String> BROADCAST_CALLBACKS_METHODS = new HashMap<String, String>() {
    {
      put("ON_CONNECTION",                   "onConnectionChange");
      put("ON_DISCOVER_FINISH",              "onDiscoverFinish");
      put("ON_DISCOVER_START",               "onDiscoverStart");
      put("ON_LOCAL_NAME_CHANGE",            "onLocalNameChange");
      put("ON_SCAN_MODE_CHANGE",             "onScanModeChange");
      put("ON_STATE_CHANGE",                 "onStateChange");
      put("ON_LOW_LEVEL_CONNECT",            "onLowLevelConnect");
      put("ON_LOW_LEVEL_DISCONNECT",         "onLowLevelDisconnect");
      put("ON_LOW_LEVEL_DISCONNECT_REQUEST", "onLowLevelDisconnectRequest");
      put("ON_BOND_STATE_CHANGE",            "onBondStateChange");
      put("ON_DEVICE_CLASS_CHANGE",          "onDeviceClassChange");
      put("ON_DEVICE_FOUND",                 "onDeviceFound");
      put("ON_REMOTE_NAME_CHANGE",           "onRemoteNameChange");
    }
  };
  public static final HashMap<String, String> BROADCAST_ACTIONS = new HashMap<String, String>() {
    {
      put("ON_CONNECTION",                   BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
      put("ON_DISCOVER_FINISH",              BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
      put("ON_DISCOVER_START",               BluetoothAdapter.ACTION_DISCOVERY_STARTED);
      put("ON_LOCAL_NAME_CHANGE",            BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
      put("ON_SCAN_MODE_CHANGE",             BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
      put("ON_STATE_CHANGE",                 BluetoothAdapter.ACTION_STATE_CHANGED);
      put("ON_LOW_LEVEL_CONNECT",            BluetoothDevice.ACTION_ACL_CONNECTED);
      put("ON_LOW_LEVEL_DISCONNECT",         BluetoothDevice.ACTION_ACL_DISCONNECTED);
      put("ON_LOW_LEVEL_DISCONNECT_REQUEST", BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
      put("ON_BOND_STATE_CHANGE",            BluetoothDevice.ACTION_BOND_STATE_CHANGED);
      put("ON_DEVICE_CLASS_CHANGE",          BluetoothDevice.ACTION_CLASS_CHANGED);
      put("ON_DEVICE_FOUND",                 BluetoothDevice.ACTION_FOUND);
      put("ON_REMOTE_NAME_CHANGE",           BluetoothDevice.ACTION_NAME_CHANGED);
    }
  };

  /*
   * Constructor. Saves the context and optionally a listener.
   */
  public BaseReceiver (Context context) {
    receiver_context = context;
  }

  public BaseReceiver (Context context, BaseListener listener) {
    receiver_context = context;
    receiver_listener = listener;
  }

  /*
   * Registers the receiver itself.
   */
  public boolean registerReceiver () {
    try {
      receiver_context.registerReceiver(this, this.getIntentFilter());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /*
   * Unregisters the receiver itself.
   */
  public boolean unregisterReceiver () {
    try {
      receiver_context.unregisterReceiver(this);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /*
   * Getter and setter for the listener for actions.
   */
  public void setListener (BaseListener listener) {
    receiver_listener = listener;
  }

  public BaseListener getListener () {
    return receiver_listener;
  }

  /*
   * Inherited abstract method from BroadcastReceiver.
   */
  @Override
  public void onReceive (Context context, Intent intent) {
    String action = intent.getAction();
    Iterator<Entry<String, String>> actions_iterator = BROADCAST_ACTIONS.entrySet().iterator();
    Entry<String, String> pair;
    while (actions_iterator.hasNext()) {
      pair = (Entry<String, String>) actions_iterator.next();
      if (pair.getValue().equals(action) && BROADCAST_CALLBACKS_STATES.get(pair.getKey()) == true) {
        String method_name = BROADCAST_CALLBACKS_METHODS.get(pair.getKey());
        try {
          Class<? extends BaseListener> clas = receiver_listener.getClass();
          Method method = clas.getMethod(method_name, new Class[] { Context.class, Intent.class });
          method.invoke(receiver_listener, new Object[] { context, intent} );
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /*
   * Returns the intent filter corresponding to the receiver.
   */
  public abstract IntentFilter getIntentFilter ();

}