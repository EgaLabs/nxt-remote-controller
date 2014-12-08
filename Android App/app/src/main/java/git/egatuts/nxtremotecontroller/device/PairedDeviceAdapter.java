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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                     *
 *                                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                      *
 *                                                                                                                                         *
 * And the corresponding file at:                                                                                                          *
 *                                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/device/PairedDeviceAdapter.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.device;

import git.egatuts.nxtremotecontroller.R;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceViewHolder> {

  private ArrayList<PairedDevice> paired_devices;
  private ArrayList<PairedDevice> differences;
  private PairedDevice device;
  private boolean not_exists;
  private int i;

  /*
   * Constructor.
   */
  public PairedDeviceAdapter (ArrayList<PairedDevice> devices) {
    this.paired_devices = devices;
  }

  /*
   * Returns size of the data.
   */
  @Override
  public int getItemCount () {
    return paired_devices != null ? paired_devices.size() : 0;
  }

  /*
   * Returns the ViewHolder (used by the adapter to pass as argument to onBindViewHolder).
   */
  @Override
  public PairedDeviceViewHolder onCreateViewHolder (ViewGroup parent, int position) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paired_device, parent, false);
    return new PairedDeviceViewHolder(view);
  }

  /*
   * Modifies the binded ViewHolder created in onCreateViewHolder.
   */
  @Override
  public void onBindViewHolder (PairedDeviceViewHolder viewHolder, int index) {
    device = paired_devices.get(index);
    viewHolder.connection.setText(Integer.toString(device.getConnectivity()));
    viewHolder.name.setText(device.getName());
    viewHolder.address.setText(device.getAddress());
  }

  /*
   * Adds a new item to the adapter and triggers the changes.
   */
  public void add (PairedDevice device) {
    this.paired_devices.add(device);
    this.notifyDataSetChanged();
  }

  /*
   * Returns the PairedDevice based on the given index/position.
   */
  public PairedDevice get (int position) {
    return paired_devices.get(position);
  }

  /*
   * Triggers item update.
   */
  public void update (int position) {
    this.notifyItemChanged(position);
  }

  /*
   * Removes item based on index/position and triggers changes.
   */
  public void remove (int position) {
    this.paired_devices.remove(position);
    this.notifyItemRemoved(position);
  }

  /*
   * Returns an ArrayList of PairedDevice containing those devices which doesn't contain the given ArrayList.
   */
  public ArrayList<PairedDevice> diff (ArrayList<PairedDevice> devices) {
    differences = new ArrayList<PairedDevice>();
    not_exists = true;
    for (PairedDevice difference: paired_devices) {
      not_exists = true;
      for (PairedDevice device: devices) {
        if (device.getAddress().equalsIgnoreCase(difference.getAddress())) not_exists = false;
      }
      if (not_exists) differences.add(difference);
    }
    return differences;
  }

  /*
   * Same functionality as indexOf. Returns -1 if doesn't exists or the index/position if it does (based on bluetooth device MAC address).
   */
  public int exists (String address) {
    for (i = 0; i < paired_devices.size(); i++) {
      if (paired_devices.get(i).getAddress().equalsIgnoreCase(address)) return i;
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