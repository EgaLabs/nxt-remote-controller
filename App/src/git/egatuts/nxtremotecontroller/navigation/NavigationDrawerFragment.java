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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                             *
 *                                                                                                                                                 *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                              *
 *                                                                                                                                                 *
 * And the corresponding file at:                                                                                                                  *
 *                                                                                                                                                 *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/navigation/NavigationDrawerFragment.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.navigation;

import git.egatuts.nxtremotecontroller.R;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallback {

  private static final String CURRENT_POSITION = "drawer_current_position";

  private View fragment_container_view;
  private DrawerLayout drawer_layout;

  private ActionBarDrawerToggle drawer_toggle;

  private int current_position;
  private NavigationDrawerCallback custom_callback;

  private View inflated_view;
  private LinearLayoutManager layout_manager;
  private RecyclerView drawer_list;
  private NavigationDrawerAdapter navigation_drawer_adapter;

  /*
   * Getter and setter for drawer layout.
   */
  public void setDrawerLayout (DrawerLayout layout) {
    drawer_layout = layout;
  }

  public DrawerLayout getDrawerLayout () {
    return drawer_layout;
  }

  /*
   * Getter and setter for action bar drawer toggle.
   */
  public void setActionBarDrawerToggle (ActionBarDrawerToggle custom_drawer_toggle) {
    drawer_toggle = custom_drawer_toggle;
  }

  public ActionBarDrawerToggle getActionBarDrawerToggle () {
    return drawer_toggle;
  }

  /*
   * onDetach and onAttach methods.
   */
  @Override
  public void onDetach () {
    super.onDetach();
    custom_callback = null;
  }

  @Override
  public void onAttach (Activity custom_activity) {
    super.onAttach(custom_activity);
    try {
      custom_callback = (NavigationDrawerCallback) custom_activity;
    } catch (ClassCastException e) {
      throw new ClassCastException("Activity MUST implement git.egatuts.nxtremotecontroller.NavigationDrawerCallback interface.");
    }
  }

  /*
   * Drawer opener and closer.
   */
  public void openDrawer () {
    drawer_layout.openDrawer(fragment_container_view);
  }

  public void closeDrawer () {
    drawer_layout.closeDrawer(fragment_container_view);
  }

  public boolean isDrawerOpened () {
    return drawer_layout != null && drawer_layout.isDrawerOpen(fragment_container_view);
  }

  /*
   * Select item and executes custom callback.
   */
  public void selectItem (int position) {
    current_position = position;
    if (drawer_layout != null) {
      drawer_layout.closeDrawer(fragment_container_view);
    }
    if (custom_callback != null) {
      custom_callback.onNavigationDrawerItemSelected(position);
    }
  }

  @Override
  public void onNavigationDrawerItemSelected (int position) {
    selectItem(position);
  }

  /*
   * onConfigurationChanged and onSaveInstanceState methods.
   * Both save the actual state or configuration when the Activity is destroyed or paused.
   */
  @Override
  public void onConfigurationChanged (Configuration config) {
    super.onConfigurationChanged(config);
    drawer_toggle.onConfigurationChanged(config);
  }

  @Override
  public void onSaveInstanceState (Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(CURRENT_POSITION, current_position);
  }

  /*
   * onCreate and onCreateView methods.
   */
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      current_position = savedInstanceState.getInt(CURRENT_POSITION);
    }
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    inflated_view = inflater.inflate(R.layout.navigation_drawer_fragment, container, false);
    drawer_list = (RecyclerView) inflated_view.findViewById(R.id.drawer_list);
    layout_manager = new LinearLayoutManager(getActivity());

    layout_manager.setOrientation(LinearLayoutManager.VERTICAL);
    drawer_list.setLayoutManager(layout_manager);
    drawer_list.setHasFixedSize(true);

    final List<DrawerItem> drawer_menu = getMenu();
    navigation_drawer_adapter = new NavigationDrawerAdapter(drawer_menu);
    navigation_drawer_adapter.setNavigationDrawerCallback(this);
    drawer_list.setAdapter(navigation_drawer_adapter);
    selectItem(current_position);
    return inflated_view;
  }

  public void setup (int fragment_id, DrawerLayout custom_drawer_layout, Toolbar toolbar) {
    fragment_container_view = getActivity().findViewById(fragment_id);
    drawer_layout = custom_drawer_layout;

    drawer_toggle = new ActionBarDrawerToggle(getActivity(), drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close) {
      @Override
      public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if (!isAdded()) return;
        getActivity().invalidateOptionsMenu();
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if (!isAdded()) return;
        getActivity().invalidateOptionsMenu();
      }
    };

    drawer_layout.post(new Runnable() {
      @Override
      public void run() {
        drawer_toggle.syncState();
      }
    });

    drawer_layout.setDrawerListener(drawer_toggle);
  }

  /*
   * Strings, drawables and menu getters.
   */
  private String getStr (int id) {
    return getResources().getString(id);
  }

  private Drawable getDrw (int id) {
    return getResources().getDrawable(id);
  }

  public List<DrawerItem> getMenu() {
    List<DrawerItem> items = new ArrayList<DrawerItem>();
    items.add(new DrawerItem(getStr(R.string.home_label), getDrw(R.drawable.ic_home)));
    items.add(new DrawerItem(getStr(R.string.scan_label), getDrw(R.drawable.ic_scan)));
    items.add(new DrawerItem(getStr(R.string.settings_label), getDrw(R.drawable.ic_preferences)));
    items.add(new DrawerItem(getStr(R.string.help_label), getDrw(R.drawable.ic_help)));
    items.add(new DrawerItem(getStr(R.string.donations_label), getDrw(R.drawable.ic_paypal)));
    return items;
  }

}