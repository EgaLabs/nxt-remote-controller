package git.egatuts.nxtremotecontroller.device;

import git.egatuts.nxtremotecontroller.R;

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

}