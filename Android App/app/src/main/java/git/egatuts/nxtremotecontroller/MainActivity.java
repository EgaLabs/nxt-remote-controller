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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                               *
 *                                                                                                                                                   *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                *
 *                                                                                                                                                   *
 * And the corresponding file at:                                                                                                                    *
 *                                                                                                                                                   *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/MainActivity.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import java.util.List;

import git.egatuts.nxtremotecontroller.activity.BaseActivity;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.fragment.BaseFragment;
import git.egatuts.nxtremotecontroller.fragment.BluetoothFragment;
import git.egatuts.nxtremotecontroller.fragment.HomeFragment;
import git.egatuts.nxtremotecontroller.fragment.ScanFragment;
import git.egatuts.nxtremotecontroller.navigation.NavigationDrawerCallback;
import git.egatuts.nxtremotecontroller.navigation.NavigationDrawerFragment;

public class MainActivity extends BaseActivity implements NavigationDrawerCallback {

  NavigationDrawerFragment drawer_fragment;
  BaseFragment fragmented_view;
  BaseFragment fragment_active = null;

  /*
   * When the activity is first created.
   */
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.main_layout);
    toolbar = (Toolbar) super.findViewById(R.id.toolbar_element);
    this.setSupportToolbar();
    drawer_fragment = (NavigationDrawerFragment) fragment_manager.findFragmentById(R.id.drawer_fragment);
    drawer_fragment.setup(R.id.drawer_fragment, (DrawerLayout) super.findViewById(R.id.drawer_element), toolbar);

  }

  /*
   * Returns the active/shown fragment.
   */
  public Fragment getActiveFragment () {
    List<Fragment> fragments = getSupportFragmentManager().getFragments();
    for (Fragment fragment : fragments) {
      if (fragment != null && fragment.isVisible()) {
        return fragment;
      }
    }
    return null;
  }

  /*
   * When an item is selected on the navigation drawer.
   */
  @Override
  public void onNavigationDrawerItemSelected (int position) {
    BaseFragment fragmented_view = null;
    Intent intent = null;

    switch (position) {
      case 0:
        fragmented_view = new HomeFragment();
      break;
      case 1:
        fragmented_view = new ScanFragment();
      break;
      case 2:
        intent = new Intent(this, SettingsActivity.class);
      break;
      case 3:
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/Egatuts/nxt-remote-controller"));
      break;
      case 4:
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=9VPAAPMYC2HEJ"));
      break;
      default:
      break;
    }
    if (fragment_active != null && fragmented_view!= null && !bluetooth_utils.isEnabled()) return;
    if (fragmented_view != null) {
      if (fragment_active != null) {
        fragment_active.changeFragmentTo(fragmented_view, true);
      } else {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmented_view).commit();
      }
      fragment_active = fragmented_view;
    } else if (intent != null) {
      drawer_fragment.getActionBarDrawerToggle().onDrawerSlide(null, 0.0f);
      super.startActivity(intent);
    }
  }

  /*
   * When the back button is pressed.
   */
  @Override
  public void onBackPressed () {
    if (drawer_fragment.isDrawerOpened()) {
      drawer_fragment.closeDrawer();
    } else {
      super.onBackPressed();
    }
  }

  /*
   * When the onCreate event has finished. Pretty useless but recommended on Google tutorial. It could be placed at the final of the onCreate.
   */
  @Override
  public void onPostCreate (Bundle savedInstanceState) {
    drawer_fragment.getActionBarDrawerToggle().syncState();
    super.onPostCreate(savedInstanceState);
  }

}