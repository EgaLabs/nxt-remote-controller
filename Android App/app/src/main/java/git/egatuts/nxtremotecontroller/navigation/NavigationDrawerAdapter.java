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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                    *
 *                                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                     *
 *                                                                                                                                                                         *
 *  And the corresponding file at:                                                                                                                                         *
 *                                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/navigation/NavigationDrawerAdapter.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.navigation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import git.egatuts.nxtremotecontroller.R;

/*
 *  Recycler view adapter used in the navigation drawer menu.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<DrawerItemViewHolder> {

  private NavigationDrawerCallback drawerCallback;
  private List<DrawerItem> data;
  View drawerMenu;

  /*
   *  Constructor.
   */
  public NavigationDrawerAdapter (List<DrawerItem> custom_data) {
    data = custom_data;
  }

  /*
   *  Getter and setter for NavigationDrawerCallback.
   */
  public void setNavigationDrawerCallback (NavigationDrawerCallback custom_callback) {
    drawerCallback = custom_callback;
  }

  public NavigationDrawerCallback getNavigationDrawerCallback () {
    return drawerCallback;
  }

  /*
   *  Item counter.
   */
  @Override
  public int getItemCount () {
    return data != null ? data.size() : 0;
  }

  /*
   *  Creates and returns the view holder.
   */
  @Override
  public DrawerItemViewHolder onCreateViewHolder (ViewGroup parent, int index) {
    drawerMenu = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item_row, parent, false);
    return new DrawerItemViewHolder(drawerMenu);
  }

  /*
   *  Binds the holder to the adapter.
   */
  @Override
  public void onBindViewHolder (DrawerItemViewHolder viewHolder, final int index) {
    TextView tmpView = viewHolder.textView;
    tmpView.setText(data.get(index).getText());
    tmpView.setCompoundDrawablesWithIntrinsicBounds(data.get(index).getDrawable(), null, null, null);
    tmpView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        if (drawerCallback != null) drawerCallback.onNavigationDrawerItemSelected(index);
      }
    });
  }

}