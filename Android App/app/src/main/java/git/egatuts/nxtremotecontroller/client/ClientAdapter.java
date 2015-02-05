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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                                       *
 *                                                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                        *
 *                                                                                                                                                           *
 * And the corresponding file at:                                                                                                                            *
 *                                                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/client/ClientAdapter.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.client;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.andexert.library.RippleView;

import java.util.ArrayList;

import git.egatuts.nxtremotecontroller.utils.GlobalUtils;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.BaseActivity;
import git.egatuts.nxtremotecontroller.fragment.BaseFragment;
import git.egatuts.nxtremotecontroller.utils.ImageDownloader;

/*
 *  RecyclerView adapter to add, remove, modify and store all the Clients (and render).
 */
public class ClientAdapter extends RecyclerView.Adapter<ClientViewHolder> {

  private ArrayList<Client> clients;
  private GlobalUtils utils;
  private ArrayList<Client> differences;
  private int rippleColor;
  private int rippleDuration;
  private boolean exists;
  private int i;
  private OnClickListener clickListener;

  /*
   *  Interface used to detect click only in the image profile to start a new connection.
   */
  public interface OnClickListener {
    public void onClick (ClientViewHolder view, int index);
  }

  /*
   *  Constructors.
   */
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

  /*
   *  Getter and setter for the click adapter.
   */
  public void setOnClickListener (OnClickListener clickListener) {
    this.clickListener = clickListener;
  }

  public OnClickListener getClickListener () {
    return this.clickListener;
  }

  /*
   *  Returns all the Clients in a primitive array.
   */
  public Client[] getAll () {
    Client[] clientsCopy = new Client[this.clients.size()];
    this.clients.toArray(clientsCopy);
    return clientsCopy;
  }

  /*
   *  Setups a ripple with the theme colors.
   */
  private void setupRipple (RippleView ripple) {
    ripple.setRippleColor(this.rippleColor);
    ripple.setRippleDuration(this.rippleDuration);
  }

  /*
   *  Returns the show animation used to show info about the client.
   */
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

  /*
   *  Returns the hide animation used to hide info about the client.
   */
  private AlphaAnimation getHideAnimation (final View view0, final View view1) {
    AlphaAnimation hide = new AlphaAnimation(1.0f, 0.0f);
    hide.setDuration(300);
    hide.setFillAfter(true);
    hide.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart (Animation animation) {
        view0.setVisibility(View.VISIBLE);
      }
      @Override
      public void onAnimationEnd (Animation animation) {
        view0.setVisibility(View.GONE);
        view1.startAnimation(getShowAnimation(view1));
      }
      @Override public void onAnimationRepeat (Animation animation) {}
    });
    return hide;
  }

  /*
   *  Sets up a view with a click listener to change views visibility.
   */
  private void toggleRipple (RippleView ripple, final View view0, final View view1) {
    this.setupRipple(ripple);
    ripple.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        if (view0.getVisibility() == View.GONE) {
          view1.startAnimation(getHideAnimation(view1, view0));
        } else if (view1.getVisibility() == View.GONE) {
          view0.startAnimation(getHideAnimation(view0, view1));
        }
      }
    });
  }

  /*
   *  Returns item count.
   */
  @Override
  public int getItemCount () {
    return this.clients != null ? this.clients.size() : 0;
  }

  /*
   *  Creates the ViewHolder passing the view as parameter.
   */
  @Override
  public ClientViewHolder onCreateViewHolder (ViewGroup parent, int position) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_cardview, parent, false);
    return new ClientViewHolder(view);
  }

  /*
   *  Binds the view holder to the recycler view and modifies it with the data.
   */
  @Override
  public void onBindViewHolder (final ClientViewHolder view, final int index) {
    final ClientAdapter self = this;
    Client client = this.clients.get(index);
    this.toggleRipple(view.nameRipple, view.name, view.email);
    this.toggleRipple(view.coordsRipple, view.location, view.coords);
    this.setupRipple(view.imageRipple);
    view.imageRipple.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        if (self.clickListener != null) self.clickListener.onClick(view, index);
      }
    });
    view.name.setText(client.getName());
    view.email.setText(client.getEmail());
    view.coords.setText(client.getLatitude() + ", " + client.getLongitude());
    view.location.setText(client.getShortLocation());
    view.profileImage.setTag(client.getProfileImageURL());
    new ImageDownloader().execute(view.profileImage);
  }

  /*
   *  Method to add new client to the adapter.
   */
  public void add (Client client, boolean notify) {
    this.clients.add(client);
    if (notify) this.notifyDataSetChanged();
  }

  public void add (Client client) {
    this.add(client, true);
  }

  /*
   *  Method to get a client from a custom position/index.
   */
  public Client get (int position) {
    return this.clients.get(position);
  }

  /*
   *  Method to send a notification to the adapter that an item was updated.
   */
  public void update (int position) {
    this.notifyItemChanged(position);
  }

  /*
   *  Method to remove an item from a custom position.
   */
  public void remove (int position, boolean notify) {
    this.clients.remove(position);
    if (notify) this.notifyItemRemoved(position);
  }

  public void remove (int position) {
    this.remove(position, true);
  }

  /*
   *  Returns the position of the client in the adapter by passing a Client instance. Returns -1 if it doesn't exists.
   */
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
      if (name.equals(mName) && email.equals(mEmail) && latt == mLatt && longg == mLongg && shortLocation.equalsIgnoreCase(mShortLocation) && longLocation.equalsIgnoreCase(mLongLocation)) {
        return i;
      }
    }
    return -1;
  }

  /*
   *  Returns the client object inside the adapter by passing the peer id.
   */
  public Client getByPeer (String peer) {
    for (int i = 0; i < this.clients.size(); i++) {
      if (peer.equals(this.clients.get(i).getPeerId())) return this.clients.get(i);
    }
    return null;
  }

}
