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
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE    *
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      *
 *  THE SOFTWARE.                                                                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                        *
 *                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                         *
 *                                                                                                                                                             *
 *  And the corresponding file at:                                                                                                                             *
 *                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/ScanFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.andexert.library.RippleView;

import java.util.ArrayList;
import java.util.Arrays;

import git.egatuts.nxtremotecontroller.utils.GlobalUtils;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.MainActivity;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.device.PairedDeviceAdapter;
import git.egatuts.nxtremotecontroller.device.PairedDeviceItemClickListener;
import git.egatuts.nxtremotecontroller.listener.AnimationEndListener;
import git.egatuts.nxtremotecontroller.listener.BluetoothDiscoveryListener;
import git.egatuts.nxtremotecontroller.listener.BluetoothPairingListener;
import git.egatuts.nxtremotecontroller.preference.PreferencesUtils;
import git.egatuts.nxtremotecontroller.receiver.BluetoothDiscoveryReceiver;
import git.egatuts.nxtremotecontroller.receiver.BluetoothPairingReceiver;
import git.egatuts.nxtremotecontroller.views.BaseIndeterminateProgressDialog;

/*
 *  Fragment shown when selected scan option in drawer menu.
 */
public class ScanFragment extends ActivityBaseFragment implements Animation.AnimationListener {

  public static final String PREFERENCE_MAX_SIGNAL_KEY = "preference_maximum_signal";
  public static final String PREFERENCE_MAX_SIGNAL_VALUE = "0";
  public static final String PREFERENCE_MIN_SIGNAL_KEY = "preference_minimum_signal";
  public static final String PREFERENCE_MIN_SIGNAL_VALUE = "-100";

  public static final String PREFERENCE_SHOW_TOAST_KEY = "preference_show_toast";
  public static final boolean PREFERENCE_SHOW_TOAST_VALUE = false;
  public static final String PREFERENCE_GO_BACK_HOME_KEY = "preference_back_home";
  public static final boolean PREFERENCE_GO_BACK_HOME_VALUE = true;

  private ArrayList<PairedDevice> devicesFound;
  private ArrayList<PairedDevice> devicesLost;
  private PairedDeviceAdapter devicesAdapter;
  private LinearLayoutManager linearLayoutManager;
  private RecyclerView recyclerView;
  private RippleView buttonFloat;
  private ImageView buttonFloatImage;
  private BluetoothDiscoveryReceiver bluetoothDiscoveryReceiver;
  private BluetoothDiscoveryListener bluetoothDiscoveryListener;
  private BluetoothPairingReceiver bluetoothPairingReceiver;
  private BluetoothPairingListener bluetoothPairingListener;
  private boolean isPairing;
  private boolean autoStart;

  private AlphaAnimation fadeOutAnim;
  private AlphaAnimation fadeInAnim;
  private AnimationSet hideAnimSet;
  private ScaleAnimation scaleUpAnim;
  private ScaleAnimation scaleDownAnim;
  private AnimationSet showAnimSet;
  private RotateAnimation spinAnim;

  /*
   *  Empty constructor.
   */
  public ScanFragment () {}

  /*
   *  Changes the icon of the button float.
   */
  public void changeButtonIcon (final Drawable drawable) {
    final ScanFragment self = this;
    this.hideAnimSet.setAnimationListener(new AnimationEndListener() {
      @Override
      public void onAnimationEnd (Animation animation) {
        self.buttonFloatImage.setImageDrawable(drawable);
        self.buttonFloatImage.startAnimation(self.showAnimSet);
      }
    });
    this.showAnimSet.setAnimationListener(new AnimationEndListener() {
      @Override
      public void onAnimationEnd (Animation animation) {
        if (self.getBluetoothUtils().isDiscovering()) {
          self.buttonFloatImage.startAnimation(self.spinAnim);
        }
      }
    });
    this.buttonFloatImage.startAnimation(this.hideAnimSet);
  }

  public void changeButtonIcon (int resId) {
    this.changeButtonIcon(this.getGlobalUtils().getDrawableResource(resId));
  }

  /*
   *  Guess the state of the bonding process.
   */
  private boolean isBonding (int[] states) {
    if (states[0] == BluetoothDevice.BOND_BONDING && states[1] == BluetoothDevice.BOND_NONE) {
      return true;
    }
    return false;
  }

  private boolean isBonded (int[] states) {
    if (states[0] == BluetoothDevice.BOND_BONDED && states[1] == BluetoothDevice.BOND_BONDING) {
      return true;
    }
    return false;
  }

  private boolean isBondedFailed (int[] states) {
    if (states[0] == BluetoothDevice.BOND_NONE && states[1] == BluetoothDevice.BOND_BONDING) {
      return true;
    }
    return false;
  }

  private boolean isBondedRemoved (int[] states) {
    if (states[0] == BluetoothDevice.BOND_NONE && states[1] == BluetoothDevice.BOND_BONDED) {
      return true;
    }
    return false;
  }

  /*
   *  Saves the adapter passing it to the activity.
   */
  public void saveAdapter (PairedDeviceAdapter adapter) {
    ((MainActivity) this.getBaseActivity()).saveAdapter(adapter);
  }

  /*
   *  Getter and setter for device adapter.
   */
  public void setDevicesAdapter (PairedDeviceAdapter adapter) {
    this.devicesAdapter = adapter;
  }

  public void setDevicesAdapter (ArrayList<PairedDevice> devices) {
    this.devicesAdapter = new PairedDeviceAdapter(this, devices);
  }

  public void setDevicesAdapter (PairedDevice[] devices) {
    this.setDevicesAdapter(new ArrayList<PairedDevice>(Arrays.asList(devices)));
  }

  public PairedDeviceAdapter getDevicesAdapter () {
    return this.devicesAdapter;
  }

  /*
   *  Getter and setter for auto start.
   */
  public void setAutoStart (boolean autoStart) {
    this.autoStart = autoStart;
  }

  public boolean getAutoStart (boolean autoStart) {
    return this.autoStart;
  }

  /*
   *  Static method used to create a new instance of the fragment.
   */
  public static ScanFragment newInstance (PairedDeviceAdapter adapter, boolean autoStart) {
    ScanFragment fragment = new ScanFragment();
    fragment.setDevicesAdapter(adapter);
    fragment.setAutoStart(autoStart);
    return fragment;
  }

  /*
   *  When the fragment is destroyed we save the adapter.
   */
  @Override
  public void onDestroy () {
    this.saveAdapter(this.devicesAdapter);
    super.onDestroy();
  }

  /*
   *  When the Fragment is attached to the root activity we create all the animations,
   *  animations sets, receivers, listeners, etc.
   */
  @Override
  public void onAttach (Activity activity) {
    super.onAttach(activity);
    final ScanFragment self = this;

    /*
     *  FadeOut and FadeIn animations.
     */
    if (this.fadeOutAnim == null) {
      this.fadeOutAnim = new AlphaAnimation(
              1.0f,  /* Start point (100%) */
              0.0f   /*   End point (0%)   */
      );
      this.fadeOutAnim.setDuration(200);
    }
    if (this.fadeInAnim == null) {
      this.fadeInAnim = new AlphaAnimation(
              0.0f,  /* Start point (0%) */
              1.0f   /* End point (100%) */
      );
      this.fadeInAnim.setDuration(200);
    }

    /*
     *  ScaleDown, ScaleUp animations and it's animation set.
     */
    if (this.scaleDownAnim == null) {
      this.scaleDownAnim = new ScaleAnimation(
              1.0f, 0.0f,  /* Start and end point of X axis (from 100% to 0%) */
              1.0f, 0.0f,  /* Start and end point of Y axis (from 100% to 0%) */
              Animation.RELATIVE_TO_SELF, 0.5f,  /* Relative to self pivot point in the center (X to 50%) */
              Animation.RELATIVE_TO_SELF, 0.5f   /* Relative to self pivot point in the middle (Y to 50%) */
      );
      this.scaleDownAnim.setDuration(200);
    }
    if (this.scaleUpAnim == null) {
      this.scaleUpAnim = new ScaleAnimation(
              0.0f, 1.0f,  /* Start and end point of X axis (from 0% to 100%) */
              0.0f, 1.0f,  /* Start and end point of Y axis (from 0% to 100%) */
              Animation.RELATIVE_TO_SELF, 0.5f,  /* Relative to self pivot point in the center (X to 50%) */
              Animation.RELATIVE_TO_SELF, 0.5f   /* Relative to self pivot point in the middle (Y to 50%) */
      );
      this.scaleUpAnim.setDuration(200);
    }

    /*
     *  Hide and Show animation sets.
     */
    if (this.hideAnimSet == null) {
      this.hideAnimSet = new AnimationSet(true);
      this.hideAnimSet.addAnimation(this.fadeOutAnim);
      this.hideAnimSet.addAnimation(this.scaleDownAnim);
    }
    if (this.showAnimSet == null) {
      this.showAnimSet = new AnimationSet(true);
      this.showAnimSet.addAnimation(this.fadeInAnim);
      this.showAnimSet.addAnimation(this.scaleUpAnim);
    }

    /*
     *  SpinAnim.
     */
    if (this.spinAnim == null) {
      this.spinAnim = new RotateAnimation(
              0f, 360f,  /* Start and end degrees (from 0º to 360º) */
              Animation.RELATIVE_TO_SELF, 0.5f,  /* Relative to self pivot point in the center (X to 50%) */
              Animation.RELATIVE_TO_SELF, 0.5f   /* Relative to self pivot point in the middle (Y to 50%) */
      );
      this.spinAnim.setDuration(1000);
      this.spinAnim.setRepeatCount(Animation.INFINITE);
      this.spinAnim.setRepeatMode(Animation.REVERSE);
    }

    /*
     *  We create the receivers previously defined.
     */
    if (this.bluetoothDiscoveryReceiver == null) {
      this.bluetoothDiscoveryReceiver = new BluetoothDiscoveryReceiver(this.getBaseActivity());
    }
    if (this.bluetoothPairingReceiver == null) {
      this.bluetoothPairingReceiver = new BluetoothPairingReceiver(this.getBaseActivity());
    }

    /*
     *  We create some variables we will use in the listeners.
     */
    this.devicesLost = new ArrayList<PairedDevice>();
    this.devicesFound = new ArrayList<PairedDevice>();

    /*
     *  And now the discovery listener and the pairing listener.
     */
    if (this.bluetoothDiscoveryListener == null) {
      this.bluetoothDiscoveryListener = new BluetoothDiscoveryListener() {
        @Override
        public void onDiscoveryStart (Context context, Intent intent) {
          /*
           *  We start the animation when the device is starting discovery.
           */
          self.changeButtonIcon(R.drawable.ic_sync);
        }

        @Override
        public void onDiscoveryFinish (Context context, Intent intent) {
          /*
           *  When the discovery has finished we no longer want to listen for
           *  new devices found because it's impossible to happen. We get the devices that
           *  were discovered but this time not, so this means they are no longer available
           *  and we remove them from the recycler view adapter.
           */
          self.bluetoothDiscoveryReceiver.unregisterReceiver();
          self.devicesLost = self.devicesAdapter.diff(self.devicesFound);
          int index;
          for (PairedDevice deviceFound : self.devicesLost) {
            index = self.devicesAdapter.exists(deviceFound);
            if (index != -1) {
              self.devicesAdapter.remove(index);
            }
          }

          /*
           *  We reset the arrays that let us know which devices are new
           *  and which are now out of range and reset the button float icon.
           */
          self.devicesFound.clear();
          self.devicesLost.clear();
          self.changeButtonIcon(R.drawable.ic_discover);
        }

        @Override
        public void onDeviceFound (Context context, Intent intent) {
          /*
           *  We update the data if the device was previously found
           *  or we add it if it didn't existed before.
           */
          PairedDevice device = (PairedDevice) self.bluetoothDiscoveryReceiver.getIntentData(intent);
          PreferencesUtils.Editor editor = self.getPreferencesEditor();
          int min = Integer.parseInt(editor.getString(ScanFragment.PREFERENCE_MIN_SIGNAL_KEY, ScanFragment.PREFERENCE_MIN_SIGNAL_VALUE));
          int max = Integer.parseInt(editor.getString(ScanFragment.PREFERENCE_MAX_SIGNAL_KEY, ScanFragment.PREFERENCE_MAX_SIGNAL_VALUE));
          device.setConnectivity(PairedDevice.calculateConnectivity(min, max, intent, editor));
          int index = self.devicesAdapter.exists(device);
          if (index != -1) {
            self.devicesAdapter.get(index).setSignal(device.getSignal());
            self.devicesAdapter.update(index);
          } else {
            self.devicesAdapter.add(device);
          }

          /*
           *  We add the discovered device to the discovered devices array to guess
           *  which devices are now out of range or simply not available.
           */
          self.devicesFound.add(device);
        }
      };
    }
    if (this.bluetoothPairingListener == null) {
      this.bluetoothPairingListener = new BluetoothPairingListener() {
        @Override
        public void onBondStateChange (Context context, Intent intent) {
          if (!self.getBluetoothUtils().isEnabled()) return;
          int[] states = (int[]) self.bluetoothPairingReceiver.getIntentData(intent);
          BaseIndeterminateProgressDialog progressDialog = self.getLongProgressDialog();
          progressDialog.setCancelable(false);
          boolean showToast = self.getPreferencesEditor()
                  .getBoolean(ScanFragment.PREFERENCE_SHOW_TOAST_KEY, ScanFragment.PREFERENCE_SHOW_TOAST_VALUE);
          int msg = 0;

          /*
           *  If the state changed and now it's pairing.
           */
          if (self.isBonding(states)) {
            self.isPairing = true;
            progressDialog.show();
            progressDialog.setText(R.string.bluetooth_pairing);

          /*
           *  If the state changed and now it's paired.
           */
          } else if (self.isBonded(states)) {
            self.isPairing = false;
            progressDialog.dismiss();
            msg = R.string.bluetooth_paired;
            boolean goBack = self.getPreferencesEditor()
                    .getBoolean(ScanFragment.PREFERENCE_GO_BACK_HOME_KEY, ScanFragment.PREFERENCE_GO_BACK_HOME_VALUE);
            if (goBack) {
              self.replaceFragmentWith(new HomeFragment(), self);
            }

          /*
           *  If the state changed and the pairing process failed.
           */
          } else if (self.isBondedFailed(states)) {
            self.isPairing = false;
            msg = R.string.bluetooth_failed_pairing;
          }

          /*
           *  If there is a message it means that the process ended.
           *  Correctly or not.
           */
          if (msg != 0) {
            self.bluetoothPairingReceiver.unregisterReceiver();
            if (!showToast) return;
            progressDialog.dismiss();
            String name = ((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)).getName();
            self.getGlobalUtils().showToast(msg, name);
          }
        }
      };
    }

    /*
     *  Now we link the listeners to the receivers.
     */
    this.bluetoothDiscoveryReceiver.setListener(this.bluetoothDiscoveryListener);
    this.bluetoothPairingReceiver.setListener(this.bluetoothPairingListener);
  }

  /*
   *  When the View is first created.
   */
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    final ScanFragment self = this;

    /*
     *  Creates the adapter and configures the linear layout manager.
     *  We use the previous devices if available.
     */

    ArrayList<PairedDevice> devices;
    View view = inflater.inflate(R.layout.scan_fragment, parent, false);

    if (this.devicesAdapter == null) this.devicesAdapter = new PairedDeviceAdapter(this, new ArrayList<PairedDevice>(0));
    this.linearLayoutManager = new LinearLayoutManager(this.getBaseActivity());
    this.linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    this.linearLayoutManager.scrollToPosition(0);

    /*
     *  Defines the elements Views.
     */
    this.recyclerView = (RecyclerView) view.findViewById(R.id.discovered_devices);
    this.buttonFloat = (RippleView) view.findViewById(R.id.button_float);
    this.buttonFloatImage = (ImageView) view.findViewById(R.id.button_float_image);

    /*
     *  Creates the background drawable that let us shape the float button.
     */
    GradientDrawable background = new GradientDrawable();
    GlobalUtils utils = this.getGlobalUtils();
    int radius = utils.getAttribute(R.attr.button_float_radius);
    int color = utils.getAttribute(R.attr.button_float_background);
    background.setCornerRadius(radius);
    background.setColor(color);

      /*
     *  Now we set the color of the background, the ripple color and the default image of the button.
     */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      this.buttonFloat.setBackground(background);
    } else {
      this.buttonFloat.setBackgroundDrawable(background);
    }

    /*
     *  If the user was stupid and exited the fragment when it was discovering
     *  and now enters it again it will still be searching so we start the animation
     *  and re-register the BroadcastReceiver.
     */
    if (this.getBluetoothUtils().isDiscovering()) {
      this.bluetoothDiscoveryReceiver.registerReceiver();
      this.buttonFloatImage.setImageDrawable(utils.getDrawableResource(R.drawable.ic_sync));
      this.buttonFloatImage.startAnimation(this.spinAnim);
    } else {
      this.buttonFloatImage.setImageDrawable(utils.getDrawableResource(R.drawable.ic_discover));
    }
    this.buttonFloat.setRippleColor(GlobalUtils.getDarkerColor(color, 0.65f));
    this.buttonFloat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        self.bluetoothDiscoveryReceiver.registerReceiver();
        self.getBluetoothUtils().startDiscovery();
      }
    });

    /*
     *  We configure the recycler view.
     */
    this.recyclerView.setAdapter(this.devicesAdapter);
    this.recyclerView.setLayoutManager(this.linearLayoutManager);
    this.recyclerView.setItemAnimator(new DefaultItemAnimator());

    /*
     *  When an item is clicked.
     */
    this.recyclerView.addOnItemTouchListener(new PairedDeviceItemClickListener(this.getBaseActivity(), new PairedDeviceItemClickListener.OnItemClickListener() {

      /*
       *  When we click a device we try to connect to it.
       */
      @Override
      public void onItemClick (View view, int position) {
        /*
         *  If it's already pairing we do nothing. If not we check if it's already paired.
         */
        if (self.isPairing || !self.getBluetoothUtils().isEnabled()) return;
        self.isPairing = true;
        self.bluetoothPairingReceiver.registerReceiver();
        final PairedDevice clickedDevice = self.devicesAdapter.get(position);
        for (PairedDevice device : self.getBluetoothUtils().getDevices()) {
          if (device.getAddress().equalsIgnoreCase(clickedDevice.getAddress())) {
            self.getGlobalUtils().showToast(R.string.bluetooth_already_paired, clickedDevice.getName());
            isPairing = false;
            return;
          }
        }
        self.getBluetoothUtils().pair(clickedDevice);
      }

      /*
       *  When we do a long click on the item we show the button to remove the paired the device.
       */
      @Override
      public void onItemLongClick (View view, int position) {
        //self.getGlobalUtils().showToast("LONG_CLICK");
      }
    }));

    return view;
  }

  /*
   *  When the fragment is replaced it does an animation so we check the animation has finished
   *  and then we perform the click to the button float if necessary.
   */
  @Override public void onAnimationStart (Animation animation) {}
  @Override public void onAnimationRepeat (Animation animation) {}

  @Override
  public void onAnimationEnd (Animation animation) {
    if (this.autoStart) {
      this.autoStart = false;
      this.buttonFloat.performClick();
    }
  }

  /*
   *  When the animation is created we add the listener defined above.
   */
  @Override
  public Animation onCreateAnimation (int transit, boolean enter, int nextAnim) {
    Animation anim = super.onCreateAnimation(transit, enter, nextAnim);
    if (anim == null && nextAnim != 0) {
      anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
    }
    if (anim != null) {
      anim.setAnimationListener(this);
    }
    return anim;
  }

}
