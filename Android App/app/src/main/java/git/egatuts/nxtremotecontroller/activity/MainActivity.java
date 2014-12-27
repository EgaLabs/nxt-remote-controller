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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                        *
 *                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                         *
 *                                                                                                                                                             *
 *  And the corresponding file at:                                                                                                                             *
 *                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/activity/MainActivity.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.device.PairedDeviceAdapter;
import git.egatuts.nxtremotecontroller.fragment.ActivityBaseFragment;
import git.egatuts.nxtremotecontroller.fragment.BluetoothFragment;
import git.egatuts.nxtremotecontroller.fragment.FragmentPendingTransition;
import git.egatuts.nxtremotecontroller.fragment.HomeFragment;
import git.egatuts.nxtremotecontroller.fragment.ScanFragment;
import git.egatuts.nxtremotecontroller.fragment.UltraBaseFragment;
import git.egatuts.nxtremotecontroller.listener.AppKillerListener;
import git.egatuts.nxtremotecontroller.listener.BluetoothEnableListener;
import git.egatuts.nxtremotecontroller.navigation.NavigationDrawerCallback;
import git.egatuts.nxtremotecontroller.navigation.NavigationDrawerFragment;
import git.egatuts.nxtremotecontroller.receiver.AppKillerReceiver;
import git.egatuts.nxtremotecontroller.receiver.BluetoothEnableReceiver;
import git.egatuts.nxtremotecontroller.views.BaseIndeterminateProgressDialog;
import git.egatuts.nxtremotecontroller.views.LongIndeterminateProgressDialog;
import git.egatuts.nxtremotecontroller.views.ShortIndeterminateProgressDialog;

/*
 *  Main activity created when the app is called from the android launcher.
 *  It loads a fragment based on the clicked option from the drawer menu.
 *  Keep in mind that it doesn't load by default a fragment, it relays
 *  on the NavigationDrawerCallback#onNavigationItemSelected.
 */
public class MainActivity extends UltraBaseActivity implements NavigationDrawerCallback, ActivityPendingTransition {

  public static final String ACTION_RESTART_APP = "restart_app";
  public static final String URL_HELP = "https://github.com/Egatuts/nxt-remote-controller";
  public static final String URL_DONATE = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=9VPAAPMYC2HEJ";

  public static final String PREFERENCE_BLUETOOTH_DISABLE_KEY = "preference_disable_bluetooth";
  public static final boolean PREFERENCE_BLUETOOTH_DISABLE_VALUE = false;
  public static final String PREFERENCE_START_DISCOVERY_KEY = "preference_auto_discovery";
  public static final boolean PREFERENCE_START_DISCOVERY_VALUE = true;

  private static final int NAVIGATION_HOME = 0;
  private static final int NAVIGATION_SCAN = 1;
  private static final int NAVIGATION_SETTINGS = 2;
  private static final int NAVIGATION_HELP = 3;
  private static final int NAVIGATION_DONATE = 4;

  private NavigationDrawerFragment drawerFragment;
  private PairedDeviceAdapter devicesAdapter;
  private ActivityBaseFragment viewFragment;
  private ActivityBaseFragment activeFragment;
  private ActivityBaseFragment lastFragment;
  private Intent intent;
  private AppKillerReceiver appKillerReceiver;
  private boolean selfDestroyed = false;
  private ProgressDialog.OnDismissListener progressDialogOnDismiss;
  private BluetoothEnableReceiver bluetoothEnableReceiver;
  private long showingTime;

  /*
   *  Getter and setter for the lastFragment property.
   */
  public void setLastFragment (ActivityBaseFragment fragment) {
    this.lastFragment = fragment;
  }

  public ActivityBaseFragment getLastFragment () {
    return this.lastFragment;
  }

  public BaseIndeterminateProgressDialog getLongProgressDialog () {
    if (!(this.progressDialog instanceof LongIndeterminateProgressDialog)) {
      this.progressDialog = new LongIndeterminateProgressDialog(this);
    }
    return this.progressDialog;
  }

  public BaseIndeterminateProgressDialog getShortProgressDialog () {
    if (!(this.progressDialog instanceof  ShortIndeterminateProgressDialog)) {
      this.progressDialog = new ShortIndeterminateProgressDialog(this);
    }
    return this.progressDialog;
  }

  /*
   *  Saves the adapter.
   */
  public void saveAdapter (PairedDeviceAdapter adapter) {
    this.devicesAdapter = adapter;
  }

  /*
   *  "Overridden" method replaceFragmentWith because we know the id of the frame layout.
   */
  public void replaceFragmentWith (UltraBaseFragment fragment, FragmentPendingTransition transitionInterface) {
    super.replaceFragmentWith(R.id.main_container, fragment, transitionInterface);
  }

  /*
   *  We set the forward transition depending of which activity we are going to start.
   */
  @Override
  public int[] onForward (Intent intent) {
    /*
     *  Here we check which activity is starting comparing the full class names (including package).
     *  If we are generating an intent adding a category the defClass statement
     *  will generate and propagate a NullPointerException. FUCK IT!
     */
    try {
      String defClass = intent.getComponent().getClassName();
      if (defClass.equals(SettingsActivity.class.getName())) {
        return new int[] { R.anim.settings_transition_in, R.anim.settings_transition_out };
      } else if (defClass.equals(ControllerActivity.class.getName())) {
        return new int[] { R.anim.controller_transition_in, R.anim.controller_transition_out };
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return new int[] {};
  }

  @Override
  public int[] onBackward () {
    return new int[] {};
  }

  /*
   *  When the configuration has changed (orientation) we
   *  should communicate that to the drawer fragment.
   */
  @Override
  public void onConfigurationChanged (Configuration configuration) {
    super.onConfigurationChanged(configuration);
    this.drawerFragment.getActionBarDrawerToggle().onConfigurationChanged(configuration);
  }

  /*
   *  We enable drawer opening/closing with the physical button available in some devices.
   */
  @Override
  public boolean onKeyDown (int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU) {
      if (this.drawerFragment.isDrawerOpened()) {
        this.drawerFragment.closeDrawer();
      } else {
        this.drawerFragment.openDrawer();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  /*
   *  We also enable closing drawer with the back button.
   */
  @Override
  public void onBackPressed () {
    if (this.drawerFragment.isDrawerOpened()) {
      this.drawerFragment.closeDrawer();
    }
    super.onBackPressed();
  }

  /*
   *  When the creation process has finished we sync the state of the drawer toggle.
   */
  @Override
  public void onPostCreate (Bundle savedInstanceState) {
    this.drawerFragment.getActionBarDrawerToggle().syncState();
    this.appKillerReceiver.registerReceiver();
    super.onPostCreate(savedInstanceState);
  }

  /*
   *  When the drawer is opened. Take in mind that this method
   *  will execute only when the opening animation has finished.
   */
  @Override public void onOpenDrawer () {}

  /*
   *  When the drawer is closed. Take in mind that this method
   *  will execute only when the closing animation has finished.
   */
  @Override
  public void onCloseDrawer () {
    if (this.intent == null) return;
    super.startActivity(this.intent, this);
    this.selfDestroyed = true;
    if (this.intent.getAction() == "") super.finish();
  }

  /*
   *  Executed when a new item has been selected from the navigation drawer.
   */
  @Override
  public void onNavigationDrawerItemSelected (int position) {
    this.viewFragment = null;
    this.intent = null;

    /*
     *  We try to know which option has been clicked. there is two possible actions.
     *  Change the fragment or change the activity.
     */
    switch (position) {
      case MainActivity.NAVIGATION_HOME:
        this.viewFragment = new HomeFragment();
        break;
      case MainActivity.NAVIGATION_SCAN:
        boolean autoStart = this.getPreferencesEditor()
                .getBoolean(MainActivity.PREFERENCE_START_DISCOVERY_KEY, MainActivity.PREFERENCE_START_DISCOVERY_VALUE);
        this.viewFragment = ScanFragment.newInstance(this.devicesAdapter, autoStart);
        break;
      case MainActivity.NAVIGATION_SETTINGS:
        this.intent = new Intent(this, SettingsActivity.class);
        break;
      case MainActivity.NAVIGATION_HELP:
        this.intent = new Intent(Intent.ACTION_VIEW);
        this.intent.setData(Uri.parse(MainActivity.URL_HELP));
        break;
      case MainActivity.NAVIGATION_DONATE:
        this.intent = new Intent(Intent.ACTION_VIEW);
        this.intent.setData(Uri.parse(MainActivity.URL_DONATE));
        break;
      default:
        break;
    }

    /*
     *  We check which of the two actions we should execute.
     */
    if (this.activeFragment != null && this.viewFragment != null && !this.bluetoothUtils.isEnabled()) {
      return;
    }
    if (this.viewFragment != null) {
      if (this.activeFragment != null) {
        this.activeFragment.replaceFragmentWith(this.viewFragment, this.viewFragment);
      } else {
        this.fragmentManager.beginTransaction()
                .replace(R.id.main_container, this.viewFragment)
                .commit();
      }
      this.activeFragment = this.viewFragment;
    }
  }

  /*
   *  We listen for changes when the activity is visible.
   */
  @Override
  public void onResume () {
    super.onResume();
    this.bluetoothEnableReceiver.registerReceiver();
  }

  /*
   *  We stop listening for bluetooth changes when the activity is no longer visible.
   */
  @Override
  public void onStop () {
    this.bluetoothEnableReceiver.unregisterReceiver();
    super.onStop();
  }

  /*
   *  When the activity is destroyed we unregister the receiver.
   */
  @Override
  public void onDestroy () {
    this.appKillerReceiver.unregisterReceiver();
    super.onDestroy();
    boolean autoDisable = this.getPreferencesEditor()
            .getBoolean(MainActivity.PREFERENCE_BLUETOOTH_DISABLE_KEY, MainActivity.PREFERENCE_BLUETOOTH_DISABLE_VALUE);
    if (!this.selfDestroyed && autoDisable) this.getBluetoothUtils().disable();
  }

  /*
   *  When the activity is created.
   */
  @Override
  public void onCreate (Bundle savedInstanceState) {
    final MainActivity self = this;
    super.onCreate(savedInstanceState);
    super.setActiveTheme(super.getPreferenceTheme());
    super.setContentView(R.layout.main_layout);
    toolbar = (Toolbar) super.findViewById(R.id.toolbar);
    this.setSupportToolbar();
    this.drawerFragment = (NavigationDrawerFragment) fragmentManager.findFragmentById(R.id.drawer_fragment);
    this.drawerFragment.setup(R.id.drawer_fragment, (DrawerLayout) super.findViewById(R.id.drawer_element), this.toolbar);
    this.appKillerReceiver = new AppKillerReceiver(this, new AppKillerListener() {
      @Override
      public void onAppNeedsRestart (Context context, Intent intent) {
        self.selfDestroyed = true;
        self.finish();
      }
    });
    this.progressDialogOnDismiss = new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss (DialogInterface dialog) {
        self.getShortProgressDialog().setOnDismissListener(null);
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run () {
            if (!(self.viewFragment instanceof BluetoothFragment)) {
              self.setLastFragment(self.viewFragment);
            }
            self.replaceFragmentWith(self.viewFragment, self.viewFragment);
          }
        }, 200);
      }
    };
    this.bluetoothEnableReceiver = new BluetoothEnableReceiver(this, new BluetoothEnableListener() {
      @Override
      public void onStateChange (Context context, Intent intent) {
        int state = (int) self.bluetoothEnableReceiver.getIntentData(intent);
        final BaseIndeterminateProgressDialog progress = self.getShortProgressDialog();
        Integer text = null;
        int hasToChange = 0;
        boolean hasToShow = false;

        /*
         *  We define if we have to change the fragment or we have to show a progress dialog.
         */
        switch (state) {
          case BluetoothAdapter.STATE_OFF:
            hasToChange = 2;
            break;
          case BluetoothAdapter.STATE_ON:
            hasToChange = 1;
            break;
          case BluetoothAdapter.STATE_TURNING_OFF:
            hasToShow = true;
            text = R.string.bluetooth_disabling;
            break;
          case BluetoothAdapter.STATE_TURNING_ON:
            hasToShow = true;
            text = R.string.bluetooth_enabling;
            break;
        }

        /*
         *  Here we have to show a progress dialog indicating that the bluetooth state is changing.
         */
        if (hasToShow && text != null) {
          self.showingTime = System.currentTimeMillis();
          progress.show();
          progress.setText(text);
        } else if (hasToChange != 0) {
          if (hasToChange == 1) self.viewFragment = (ActivityBaseFragment) self.getLastFragment();
          if (hasToChange == 2) self.viewFragment = new BluetoothFragment();
          if (!hasToShow && progress.isShowing()) {
            progress.setOnDismissListener(self.progressDialogOnDismiss);
            if (System.currentTimeMillis() - self.showingTime < 500) {
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run () {
                  progress.dismiss();
                }
              }, 500);
            } else {
              progress.dismiss();
            }
          }
        }
      }
    });
  }

}
