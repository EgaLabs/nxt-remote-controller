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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                            *
 *                                                                                                                                                                 *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                             *
 *                                                                                                                                                                 *
 *  And the corresponding file at:                                                                                                                                 *
 *                                                                                                                                                                 *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/receiver/UltraBaseReceiver.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import git.egatuts.nxtremotecontroller.activity.MainActivity;
import git.egatuts.nxtremotecontroller.activity.UltraBaseActivity;
import git.egatuts.nxtremotecontroller.fragment.UltraBaseFragment;
import git.egatuts.nxtremotecontroller.listener.UltraBaseListener;

/*
 *  Base class to extend BroadcastReceiver using UltraBaseListener.
 */
public abstract class UltraBaseReceiver extends BroadcastReceiver {

  protected UltraBaseActivity activity;
  protected UltraBaseListener listener;

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
      put("ON_ACTIVITY_RESTART",             false);
    }
  };

  public static final HashMap<String, String> BROADCAST_CALLBACKS_METHODS = new HashMap<String, String>() {
    {
      put("ON_CONNECTION",                   "onConnectionChange");
      put("ON_DISCOVER_FINISH",              "onDiscoveryFinish");
      put("ON_DISCOVER_START",               "onDiscoveryStart");
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
      put("ON_ACTIVITY_RESTART",             "onAppNeedsRestart");
    }
  };

  public static final HashMap<String, String> BROADCAST_ACTIONS = new HashMap<String, String>() {
    {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        put("ON_CONNECTION", BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
      }
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
      put("ON_ACTIVITY_RESTART",             MainActivity.ACTION_RESTART_APP);
    }
  };

  /*
   *  Constructors. Saves the Context and the listener.
   */
  public UltraBaseReceiver (UltraBaseActivity activity) {
    this.activity = activity;
  }

  public UltraBaseReceiver (UltraBaseFragment fragment) {
    this.activity = fragment.getBaseActivity();
  }

  public UltraBaseReceiver (Context context) {
    this.activity = (UltraBaseActivity) context;
  }

  public UltraBaseReceiver (UltraBaseActivity activity, UltraBaseListener listener) {
    this.activity = activity;
    this.listener = listener;
  }

  public UltraBaseReceiver (UltraBaseFragment fragment, UltraBaseListener listener) {
    this.activity = fragment.getBaseActivity();
    this.listener = listener;
  }

  public UltraBaseReceiver (Context context, UltraBaseListener listener) {
    this.activity = (UltraBaseActivity) context;
    this.listener = listener;
  }

  /*
   *  Registers the BroadcastReceiver.
   */
  public void registerReceiver () {
    this.activity.registerReceiver(this, this.getIntentFilter());
  }

  /*
   *  Unregisters the BroadcastReceiver.
   */
  public void unregisterReceiver () {
    this.activity.unregisterReceiver(this);
  }

  /*
   *  Getter and setter for the listener.
   */
  public void setListener (UltraBaseListener listener) {
    this.listener = listener;
  }

  public UltraBaseListener getListener () {
    return this.listener;
  }

  /*
   *  Inherited abstract method from BroadcastReceiver class.
   *  Whenever it receives a new Intent it does a loop for each UltraBaseReceiver#BROADCAST_ACTIONS.
   *  When the received action equals a registered action from the previous loop checks if it's enabled in UltraBaseReceiver#BROADCAST_CALLBACK_STATES.
   *  If that's true it uses Java Reflection to invoke the method of the listener corresponding to the received action declared in UltraBaseReceiver#BROADCAST_CALLBACK_METHODS.
   */
  @Override
  public void onReceive (Context context, Intent intent) {

    /*
     *  We create some variables.
     */
    String action = intent.getAction();
    Iterator< Map.Entry<String, String> > actionsIterator = UltraBaseReceiver.BROADCAST_ACTIONS.entrySet().iterator();
    Map.Entry<String, String> pair;
    String realAction;
    String idAction;
    String listenerMethod;
    Class<? extends UltraBaseListener> defClass;
    Method method;

    /*
     *  We do a loop for each possible action and compare to the received one.
     */
    while (actionsIterator.hasNext()) {
      pair = actionsIterator.next();
      realAction = pair.getValue();
      idAction = pair.getKey();

      /*
       *  If the received action corresponds to the onw of the loop and the one of the loop
       *  is enabled in BROADCAST_CALLBACK_STATES we process it and execute the
       *  corresponding listener method.
       */
      if (realAction.equals(action) && this.BROADCAST_CALLBACKS_STATES.get(idAction)) {
        listenerMethod = BROADCAST_CALLBACKS_METHODS.get(idAction);

        /*
         *  Using Java Reflection we try to execute that listener method.
         */
        try {

          /*
           *  Remember that the listener must extends our UltraBaseListener.
           */
          defClass = this.listener.getClass();
          method = defClass.getMethod(listenerMethod, new Class[] { Context.class, Intent.class });
          method.invoke(this.listener, context, intent);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /*
   *  Returns the IntentFilter used when registering the receiver.
   */
  public abstract IntentFilter getIntentFilter ();

  /*
   *  Returns the most relevant data of the intent (e.g. the bluetooth device when found a new one, etc).
   */
  public abstract Object getIntentData (Intent intent);

}