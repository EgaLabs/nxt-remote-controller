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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                       *
 *                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                        *
 *                                                                                                                           *
 * And the corresponding file at:                                                                                            *
 *                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/ScanFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller;

import java.util.ArrayList;

import com.gc.materialdesign.views.ButtonFloat;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothCallback;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothReceiver;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.device.PairedDeviceAdapter;
import git.egatuts.nxtremotecontroller.device.PairedDeviceItemClickListener;
import git.egatuts.nxtremotecontroller.preference.PreferencesUtils;

public class ScanFragment extends Fragment
{
  
  final private String MAXIMUM_SIGNAL = "preference_maximum_signal";
  final private String MINIMUM_SIGNAL = "preference_minimum_signal";
  
  private BluetoothUtils bluetooth_utils;
  private BluetoothReceiver bluetooth_receiver;
  private BluetoothCallback.OnDiscoveryListener discovery_callback;
  private PreferencesUtils preference_utils;
  private PreferencesUtils.Editor preference_editor;
  private View view;
  private PairedDeviceAdapter paired_devices_adapter;
  private PairedDevice discovered_device;
  private LinearLayoutManager linear_layout_manager;
  private RecyclerView recycler_view;
  private ButtonFloat button_float;
  private ArrayList<PairedDevice> discovered_devices;
  private ArrayList<PairedDevice> lost_devices;
  
  private AlphaAnimation fade_out;
  private AlphaAnimation fade_in;
  
  public ScanFragment () {}
  
  /*
   * Cancels existing discovery and starts a new one.
   */
  public void startDiscovery () {
    this.cancelDiscovery();
    bluetooth_utils.startDiscovery();
  }
  
  /*
   * Cancels existing discovery.
   */  
  public void cancelDiscovery () {
    if (bluetooth_utils.isDiscovering()) bluetooth_utils.cancelDiscovery();
  }
  
  /*
   * Registers BroadcastReceiver for Bluetooth discovery.
   */
  public void registerDiscoveryListener () {
    getActivity().registerReceiver(bluetooth_receiver, BluetoothReceiver.getIntentFilter());
  }
  
  /*
   * Unregisters BroadcastReceiver for Bluetooth discovery.
   */
  public void unregisterDiscoveryListener () {
    getActivity().unregisterReceiver(bluetooth_receiver);
  }
  
  /*
   * Initializes components used in the class.
   */
  public void initializeComponents (Activity activity) {
    bluetooth_utils = new BluetoothUtils();
    
    preference_utils = new PreferencesUtils(activity);
    preference_utils.privateMode();
    
    preference_editor = preference_utils.getEditor();
    preference_editor.edit();
    
    discovered_devices = new ArrayList<PairedDevice>();
    lost_devices = new ArrayList<PairedDevice>();
    
    bluetooth_receiver = new BluetoothReceiver();
    
    /*
     * Callbacks for Bluetooth events.
     */
    discovery_callback = new BluetoothCallback.OnDiscoveryListener () {
      
      /*
       * When the scanning process starts.
       */
      @Override
      public void onStart () {
        Toast.makeText(getActivity(), "inicioooo", Toast.LENGTH_SHORT).show();
      }
      
      /*
       * When the scan process triggers an event defined in the BluetoothReceiver.getIntentFilter().
       */
      @Override
      public void onDiscover(BluetoothDevice bluetooth_device, Intent intent) {
        
        /*
         * Get RSSI strength and maximum and minimum stored values.
         */
        int raw_signal = (int) intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
        int minimum_value = Integer.parseInt(preference_editor.getString(MINIMUM_SIGNAL, "0"));
        int maximum_value = Integer.parseInt(preference_editor.getString(MAXIMUM_SIGNAL, "-100"));
        
        /*
         * Save values if are less than minimum or more than maximum.
         */
        if (raw_signal > maximum_value) {
          maximum_value = raw_signal;
          preference_editor.saveString(MAXIMUM_SIGNAL, Integer.toString(maximum_value));
        }
        if (raw_signal < minimum_value) {
          minimum_value = raw_signal;
          preference_editor.saveString(MINIMUM_SIGNAL, Integer.toString(minimum_value));
        }
        
        /*
         * Calculate the percentage of the signal.
         */
        int total = maximum_value - minimum_value;
        int portion = raw_signal - minimum_value;
        float percentage = ((float) portion / (float) total) * 100;
        
        int position = paired_devices_adapter.exists(bluetooth_device);
        
        /*
         * If it as discovered before we update it's values (signal strength).
         * If not simply add it.
         */
        if (position != -1) {
          discovered_device = paired_devices_adapter.get(position);
          discovered_device.setConnectivity((int) percentage);
          paired_devices_adapter.update(position);
        } else {
          discovered_device = PairedDevice.from(bluetooth_device);
          discovered_device.setConnectivity((int) percentage);
          paired_devices_adapter.add(discovered_device);
        }
        
        /*
         * We add it to the discovered devices ArrayList to calculate which devices are too far from the range.
         */
        discovered_devices.add(discovered_device);
      }
      
      /*
       * When the scanning process finishes. 
       */
      @Override
      public void onFinish ()
      {
        lost_devices = paired_devices_adapter.diff(discovered_devices);
        int index;
        
        /*
         * Remove those devices from the View which were found before but not in this scan process.
         */
        for (PairedDevice discovered_device : lost_devices) {
          index = paired_devices_adapter.exists(discovered_device);
          if (index != -1) {
            paired_devices_adapter.remove(index);
          }
        }
        
        /*
         * Remove data sets and set float button icon.
         */
        discovered_devices.clear();
        lost_devices.clear();
        changeIconTo(R.drawable.ic_sync);
      }
    };
  }
  
  /*
   * Creates the AlphaAnimations if doesn't exits.
   */
  public void initAnimators () {
    if (fade_out == null) {
      fade_out = new AlphaAnimation(1.0f, 0.0f);
      fade_out.setDuration(200);
    }
    if (fade_in == null) {
      fade_in = new AlphaAnimation(0.0f, 1.0f);
      fade_in.setDuration(200);
    }
  }
  
  /*
   * Changes the Drawable icon of the float button based on it's resource id or Drawable.
   */
  public void changeIconTo (int resId) {
    this.changeIconTo(getResources().getDrawable(resId));
  }
  
  public void changeIconTo (final Drawable drawable) {
    this.initAnimators();
    fade_out.setAnimationListener(new AnimationListener () {
      @Override
      public void onAnimationEnd (Animation animation) {
        button_float.setIconDrawable(drawable);
        button_float.getIcon().startAnimation(fade_in);
      }

      @Override public void onAnimationRepeat (Animation animation) {}
      @Override public void onAnimationStart (Animation animation) {}
    });
    button_float.getIcon().startAnimation(fade_out);
  }
  
  /*
   * Called when the fragment is detached from the parent Activity.
   */
  @Override
  public void onDetach ()
  {
    this.cancelDiscovery();
    this.unregisterDiscoveryListener();
    super.onDetach();
  }
  
  /*
   * 
   */
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent_container, Bundle savedInstanceState)
  {
    paired_devices_adapter = new PairedDeviceAdapter(new ArrayList<PairedDevice>(0));
    
    view = inflater.inflate(R.layout.scan_fragment, parent_container, false);
    linear_layout_manager = new LinearLayoutManager(parent_container.getContext());
    linear_layout_manager.setOrientation(LinearLayoutManager.VERTICAL);
    linear_layout_manager.scrollToPosition(0);
    
    recycler_view = (RecyclerView) view.findViewById(R.id.discovered_devices);
    button_float = (ButtonFloat) view.findViewById(R.id.sync_button);
    
    recycler_view.setAdapter(paired_devices_adapter);
    recycler_view.setLayoutManager(linear_layout_manager);
    recycler_view.setItemAnimator(new DefaultItemAnimator());
    recycler_view.addOnItemTouchListener(new PairedDeviceItemClickListener(getActivity(), new PairedDeviceItemClickListener.OnItemClickListener() {
      @Override
      public void onItemClick(View view, int position) {
        bluetooth_utils.pair(getActivity(), paired_devices_adapter.get(position));
      }
    }));
    
    button_float.setIconDrawable(getResources().getDrawable(R.drawable.ic_sync));
    button_float.setOnClickListener(new View.OnClickListener () {
      @Override
      public void onClick(View v) {
        changeIconTo(R.drawable.ic_cancel);
        startDiscovery();
      }
    });
    return view;
  }
  
  @Override
  public void onAttach (Activity activity)
  {
    super.onAttach(activity);
    this.initializeComponents(activity);
    
    if (bluetooth_utils.isEnabled() == false) {
      getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new BluetoothFragment(this, bluetooth_utils)).commit();
    }

    bluetooth_receiver.setOnDiscoveryListener(discovery_callback);
    this.registerDiscoveryListener();
  }

}