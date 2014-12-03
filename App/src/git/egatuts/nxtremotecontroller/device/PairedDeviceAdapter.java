package git.egatuts.nxtremotecontroller.device;

import git.egatuts.nxtremotecontroller.R;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceViewHolder>
{

  private ArrayList<PairedDevice> paired_devices;

  public PairedDeviceAdapter (ArrayList<PairedDevice> devices)
  {
    this.paired_devices = devices;
  }

  @Override
  public int getItemCount() {
    return paired_devices != null ? paired_devices.size() : 0;
  }

  @Override
  public PairedDeviceViewHolder onCreateViewHolder(ViewGroup parent, int position) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paired_device, parent, false);
    return new PairedDeviceViewHolder(view);
  }

  @Override
  public void onBindViewHolder(PairedDeviceViewHolder viewHolder, int index) {
    PairedDevice device = paired_devices.get(index);
    viewHolder.connection.setText(Integer.toString(device.getConnectivity()));
    viewHolder.name.setText(device.getName());
    viewHolder.address.setText(device.getAddress());
  }
  
  public void add (PairedDevice device)
  {
    this.paired_devices.add(device);
    this.notifyDataSetChanged();
  }
  
  public PairedDevice get (int position)
  {
    return paired_devices.get(position);
  }
  
  public void update (int position)
  {
    this.notifyItemChanged(position);
  }
  
  public void remove (int position)
  {
    this.paired_devices.remove(position);
    this.notifyItemRemoved(position);
  }
  
  public ArrayList<PairedDevice> diff (ArrayList<PairedDevice> devices)
  {
    ArrayList<PairedDevice> differences = new ArrayList<PairedDevice>();
    boolean not_exists = true;
    for (PairedDevice difference : paired_devices) {
      not_exists = true;
      for (PairedDevice device : devices) {
        if (device.getAddress().equalsIgnoreCase(difference.getAddress())) not_exists = false;
      }
      if (not_exists) differences.add(difference);
    }
    return differences;
  }
  
  public int exists (String address)
  {
    for (int i = 0; i < paired_devices.size(); i++) {
      if (paired_devices.get(i).getAddress().equalsIgnoreCase(address)) return i;
    }
    return -1;
  }
  
  public int exists (PairedDevice device)
  {
    return this.exists(device.getAddress());
  }
  
  public int exists (BluetoothDevice device)
  {
    return this.exists(device.getAddress());
  }

}