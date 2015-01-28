package git.egatuts.nxtremotecontroller.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.andexert.library.RippleView;

import java.util.ArrayList;

import git.egatuts.nxtremotecontroller.GlobalUtils;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.BaseActivity;
import git.egatuts.nxtremotecontroller.fragment.BaseFragment;
import git.egatuts.nxtremotecontroller.listener.AnimationEndListener;
import git.egatuts.nxtremotecontroller.utils.ImageDownloader;

public class ClientAdapter extends RecyclerView.Adapter<ClientViewHolder> {

  private ArrayList<Client> clients;
  private GlobalUtils utils;
  private ArrayList<Client> differences;
  private int rippleColor;
  private int rippleDuration;
  private boolean exists;
  private int i;

  public ClientAdapter (GlobalUtils utils, ArrayList<Client> devices) {
    this.clients = devices;
    this.utils = utils;
    this.rippleColor = this.utils.getAttribute(R.attr.colorPrimaryDark);
    this.rippleDuration = this.utils.getAttribute(R.attr.paired_device_ripple_duration);
  }

  public ClientAdapter (BaseFragment context, ArrayList<Client> clients) {
    this(context.getGlobalUtils(), clients);
  }

  public ClientAdapter (BaseActivity context, ArrayList<Client> devices) {
    this(context.getGlobalUtils(), devices);
  }

  public ClientAdapter (GlobalUtils utils) {
    this.utils = utils;
  }

  public ClientAdapter (BaseFragment context) {
    this(context.getGlobalUtils());
  }

  public ClientAdapter (BaseActivity context) {
    this(context.getGlobalUtils());
  }

  public Client[] getAll () {
    return (Client[]) this.clients.toArray();
  }

  private void setupRipple (RippleView ripple) {
    ripple.setRippleColor(this.rippleColor);
    ripple.setRippleDuration(this.rippleDuration);
  }

  private AlphaAnimation getShowAnimation (final View view0) {
    AlphaAnimation show = new AlphaAnimation(0.0f, 1.0f);
    show.setDuration(300);
    show.setStartOffset(300);
    show.setFillAfter(true);
    show.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart (Animation animation) {
        view0.setVisibility(View.VISIBLE);
      }
      @Override public void onAnimationEnd (Animation animation) {}
      @Override public void onAnimationRepeat (Animation animation) {}
    });
    return show;
  }

  private AlphaAnimation getHideAnimation (final View view0, final View view1) {
    AlphaAnimation hide = new AlphaAnimation(1.0f, 0.0f);
    hide.setDuration(300);
    hide.setFillAfter(true);
    hide.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart (Animation animation) {
        view0.setVisibility(View.VISIBLE);
      }
      @Override public void onAnimationEnd (Animation animation) {
        view0.setVisibility(View.GONE);
        view1.startAnimation(getShowAnimation(view1));
      }
      @Override public void onAnimationRepeat (Animation animation) {}
    });
    return hide;
  }

  private void toggleRipple (RippleView ripple, final View view0, final View view1) {
    this.setupRipple(ripple);
    ripple.setAnimationFinishListener(new RippleView.AnimationFinishListener() {
      @Override
      public void onFinish () {
        if (view0.getVisibility() == View.GONE) {
          view1.startAnimation(getHideAnimation(view1, view0));
        } else if (view1.getVisibility() == View.GONE) {
          view0.startAnimation(getHideAnimation(view0, view1));
        }
      }
    });
  }

  @Override
  public int getItemCount () {
    return this.clients != null ? this.clients.size() : 0;
  }

  @Override
  public ClientViewHolder onCreateViewHolder (ViewGroup parent, int position) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_cardview, parent, false);
    return new ClientViewHolder(view);
  }

  @Override
  public void onBindViewHolder (ClientViewHolder view, int index) {
    Client client = this.clients.get(index);
    this.toggleRipple(view.nameRipple, view.name, view.email);
    this.toggleRipple(view.coordsRipple, view.location, view.coords);
    this.setupRipple(view.imageRipple);
    view.name.setText(client.getName());
    view.email.setText(client.getEmail());
    view.coords.setText(client.getLongitude() + ", " + client.getLatitude());
    view.location.setText(client.getShortLocation());
    view.profileImage.setTag(client.getProfileImageURL());
    new ImageDownloader().execute(view.profileImage);
  }

  public void add (Client client, boolean notify) {
    this.clients.add(client);
    if (notify) this.notifyDataSetChanged();
  }

  public void add (Client client) {
    this.add(client, true);
  }

  public Client get (int position) {
    return this.clients.get(position);
  }

  public void update (int position) {
    this.notifyItemChanged(position);
  }

  public void remove (int position, boolean notify) {
    this.clients.remove(position);
    if (notify) this.notifyItemRemoved(position);
  }

  public void remove (int position) {
    this.remove(position, true);
  }

  public int exists (Client client) {
    String name = client.getName();
    String email = client.getEmail();
    double latt = client.getLatitude();
    double longg = client.getLongitude();
    String shortLocation = client.getShortLocation();
    String longLocation = client.getLongLocation();
    for (i = 0; i < this.clients.size(); i++) {
      Client clientLoop = this.clients.get(i);
      String mName = clientLoop.getName();
      String mEmail = clientLoop.getEmail();
      double mLatt = clientLoop.getLatitude();
      double mLongg = clientLoop.getLongitude();
      String mShortLocation = clientLoop.getShortLocation();
      String mLongLocation = clientLoop.getLongLocation();
      if (name == mName && email == mEmail && latt == mLatt && longg == mLongg && shortLocation.equalsIgnoreCase(mShortLocation) && longLocation.equalsIgnoreCase(mLongLocation)) {
        return i;
      }
    }
    return -1;
  }

}
