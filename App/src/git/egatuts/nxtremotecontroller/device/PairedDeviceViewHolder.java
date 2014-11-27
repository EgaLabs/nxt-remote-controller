package git.egatuts.nxtremotecontroller.device;

import git.egatuts.nxtremotecontroller.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PairedDeviceViewHolder extends RecyclerView.ViewHolder
{
  
  public TextView connection;
  public TextView name;
  public TextView address;

  public PairedDeviceViewHolder (View itemView) {
    super(itemView);
    connection = (TextView) itemView.findViewById(R.id.paired_device_connection);
    name       = (TextView) itemView.findViewById(R.id.paired_device_name);
    address    = (TextView) itemView.findViewById(R.id.paired_device_address);
  }
  
}