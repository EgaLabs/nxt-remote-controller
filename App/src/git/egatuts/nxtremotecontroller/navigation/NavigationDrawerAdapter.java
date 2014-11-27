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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                 *
 *                                                                                                                                     *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                  *
 *                                                                                                                                     *
 * And the corresponding file at:                                                                                                      *
 *                                                                                                                                     *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/NavigationDrawerAdapter.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import git.egatuts.nxtremotecontroller.R;

import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder>
{

  private List<DrawerItem> data;
  private NavigationDrawerCallback drawer_callback;
  private View drawer_menu;

  private int selected_position;
  private int touched_position;
  private int last_position;

  public static class ViewHolder extends RecyclerView.ViewHolder
  {
    public TextView text_view;

    public ViewHolder (View item)
    {
      super(item);
      text_view = (TextView) item.findViewById(R.id.item_name);
    }
  }

  /*
   * Constructor.
   */
  public NavigationDrawerAdapter (List<DrawerItem> custom_data)
  {
    data = custom_data;
  }

  /*
   * Getter and setter for NavigationDrawerCallback.
   */
  public void setNavigationDrawerCallback (NavigationDrawerCallback custom_callback)
  {
    drawer_callback = custom_callback;
  }

  public NavigationDrawerCallback getNavigationDrawerCallback ()
  {
    return drawer_callback;
  }

  /*
   * Item counter.
   */
  @Override
  public int getItemCount() {
    return data != null ? data.size() : 0;
  }

  /*
   * Records the last touched position and the last selected position.
   */
  private void touchPosition (int current_position)
  {
    last_position = touched_position;
    touched_position = current_position;
    if (last_position >= 0) {
      notifyItemChanged(last_position);
    }
    if (current_position >= 0) {
      notifyItemChanged(current_position);
    }
  }

  public void selectedPosition (int current_position)
  {
    last_position = selected_position;
    selected_position = current_position;
    notifyItemChanged(last_position);
    notifyItemChanged(current_position);
  }

  /*
   * Binder and creator for view holders.
   */
  @Override
  public NavigationDrawerAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int index)
  {
    drawer_menu = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item_row, parent, false);
    return new ViewHolder(drawer_menu);
  }

  @Override
  public void onBindViewHolder(NavigationDrawerAdapter.ViewHolder view_holder, final int index)
  {
    TextView temp_view = view_holder.text_view;
    temp_view.setText( data.get(index).getText() );
    temp_view.setCompoundDrawablesWithIntrinsicBounds(data.get(index).getDrawable(), null, null, null);

    temp_view.setOnTouchListener(new View.OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            touchPosition(index);
            return false;

          case MotionEvent.ACTION_CANCEL:
            touchPosition(-1);
            return false;

          case MotionEvent.ACTION_MOVE:
            return false;

          case MotionEvent.ACTION_UP:
            touchPosition(-1);
            return false;
        }
        return true;
      }

    });

    temp_view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (drawer_callback != null) drawer_callback.onNavigationDrawerItemSelected(index);
      }
    });

  }

}