package git.egatuts.nxtremotecontroller.client;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;

import git.egatuts.nxtremotecontroller.R;

public class ClientViewHolder extends RecyclerView.ViewHolder {

  public TextView name;
  public TextView email;
  public TextView coords;
  public TextView location;
  public ImageView profileImage;
  public RippleView nameRipple;
  public RippleView coordsRipple;
  public RippleView imageRipple;

  public ClientViewHolder (View item) {
    super(item);
    this.name         = (TextView)   item.findViewById(R.id.name);
    this.email        = (TextView)   item.findViewById(R.id.email);
    this.location     = (TextView)   item.findViewById(R.id.location);
    this.coords       = (TextView)   item.findViewById(R.id.coords);
    this.profileImage = (ImageView)  item.findViewById(R.id.profile_image);
    this.nameRipple   = (RippleView) item.findViewById(R.id.name_ripple) ;
    this.coordsRipple = (RippleView) item.findViewById(R.id.coords_ripple) ;
    this.imageRipple  = (RippleView) item.findViewById(R.id.image_ripple) ;
  }
}
