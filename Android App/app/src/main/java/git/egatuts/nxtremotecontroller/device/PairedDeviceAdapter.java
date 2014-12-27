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
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/device/PairedDeviceAdapter.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.device;

import android.bluetooth.BluetoothDevice;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import git.egatuts.nxtremotecontroller.GlobalUtils;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.UltraBaseActivity;
import git.egatuts.nxtremotecontroller.fragment.UltraBaseFragment;

/*
 *  Extended class of the RecyclerView.Adapter to show the paired/found devices.
 */
public class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceViewHolder> {

  private ArrayList<PairedDevice> pairedDevices;
  private GlobalUtils utils;
  private ArrayList<PairedDevice> differences;
  private PairedDevice device;
  private int rippleColor;
  private int rippleDuration;
  private GradientDrawable signalDrawable;
  private boolean exists;
  private int i;

  /*
   *  Constructor.
   */
  public PairedDeviceAdapter (GlobalUtils utils, ArrayList<PairedDevice> devices) {
    this.pairedDevices = devices;
    this.utils = utils;
    this.rippleColor = this.utils.getAttribute(R.attr.colorPrimaryDark);
    this.rippleDuration = this.utils.getAttribute(R.attr.paired_device_ripple_duration);
    GradientDrawable tmpDrawable = new GradientDrawable();
    tmpDrawable.setCornerRadius(this.utils.getAttribute(R.attr.paired_device_height));
    tmpDrawable.setColor(this.rippleColor);
    this.signalDrawable = tmpDrawable;
  }

  public PairedDeviceAdapter (UltraBaseFragment context, ArrayList<PairedDevice> devices) {
    this(context.getGlobalUtils(), devices);
  }

  public PairedDeviceAdapter (UltraBaseActivity context, ArrayList<PairedDevice> devices) {
    this(context.getGlobalUtils(), devices);
  }

  public PairedDeviceAdapter (GlobalUtils utils) {
    this.utils = utils;
  }

  public PairedDeviceAdapter (UltraBaseFragment context) {
    this(context.getGlobalUtils());
  }

  public PairedDeviceAdapter (UltraBaseActivity context) {
    this(context.getGlobalUtils());
  }

  /*
   *  Getter and setter the global utils.
   */
  public void setGlobalUtils (GlobalUtils utils) {
    this.utils = utils;
  }

  public GlobalUtils getGlobalUtils () {
    return this.utils;
  }

  /*
   *  Returns all the devices stored in the adapter. Remember they are Parcelable :)
   */
  public PairedDevice[] getAll () {
    return (PairedDevice[]) this.pairedDevices.toArray();
  }

  /*
   *  Returns size of the added data.
   */
  @Override
  public int getItemCount () {
    return this.pairedDevices != null ? this.pairedDevices.size() : 0;
  }

  /*
   *  Returns the extended version of RecyclerView.Viewholder (PairedDeviceViewHolder).
   */
  @Override
  public PairedDeviceViewHolder onCreateViewHolder (ViewGroup parent, int position) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paired_device, parent, false);
    return new PairedDeviceViewHolder(view);
  }

  /*
   *  Modifies and binds the ViewHolder created in onCreateViewHolder.
   *  Sets the name, address, theme color and the signal strength.
   */
  @Override
  public void onBindViewHolder (PairedDeviceViewHolder viewHolder, int index) {
    device = this.pairedDevices.get(index);
    viewHolder.rippleView.setRippleColor(this.rippleColor);
    viewHolder.rippleView.setRippleDuration(this.rippleDuration);
    viewHolder.connection.setText(Integer.toString(device.getConnectivity()));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      viewHolder.connection.setBackground(this.signalDrawable);
    } else {
      viewHolder.connection.setBackgroundDrawable(this.signalDrawable);
    }
    viewHolder.name.setText(device.getName());
    viewHolder.address.setText(device.getAddress());
  }

  /*
   *  Adds a new item to the adapter and optionally notifies the changes.
   */
  public void add (PairedDevice device, boolean notify) {
    this.pairedDevices.add(device);
    if (notify) this.notifyDataSetChanged();
  }

  public void add (PairedDevice device) {
    this.add(device, true);
  }

  /*
   *  Returns the PairedDevice based on the given index/position.
   */
  public PairedDevice get (int position) {
    return this.pairedDevices.get(position);
  }

  /*
   *  Notifies item update.
   */
  public void update (int position) {
    this.notifyItemChanged(position);
  }

  /*
   *  Removes item based on index/position and optionally notifies changes.
   */
  public void remove (int position, boolean notify) {
    this.pairedDevices.remove(position);
    if (notify) this.notifyItemRemoved(position);
  }

  public void remove (int position) {
    this.remove(position, true);
  }

  /*
   *  Returns the PairedDevice(s) contained in the Adapter which doesn't exist in the given Array.
   *  If passed an empty Array it will return all the devices contained in the Adapter.
   *  If passed an Array containing all the devices in the Adapter it will return an empty Array.
   *  If passed an Array with a few devices from the Adapter it will return an Array containing the rest.
   */
  public ArrayList<PairedDevice> diff (ArrayList<PairedDevice> devices) {
    differences = new ArrayList<PairedDevice>();
    exists = false;
    for (PairedDevice difference : this.pairedDevices) {
      exists = false;
      for (PairedDevice device : devices) {
        if (device.getAddress().equalsIgnoreCase(difference.getAddress())) {
          exists = true;
        }
      }
      if (!exists) differences.add(difference);
    }
    return differences;
  }

  /*
   *  Returns the position of the PairedDevice by the given address.
   *  Returns -1 if doesn't exist.
   */
  public int exists (String address) {
    for (i = 0; i < this.pairedDevices.size(); i++) {
      if (this.pairedDevices.get(i).getAddress().equalsIgnoreCase(address)) {
        return i;
      }
    }
    return -1;
  }

  public int exists (PairedDevice device) {
    return this.exists(device.getAddress());
  }

  public int exists (BluetoothDevice device) {
    return this.exists(device.getAddress());
  }

}