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
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/device/PairedDeviceItemclickListener.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class PairedDeviceItemClickListener implements RecyclerView.OnItemTouchListener {

  private GestureDetector gesture_detector;
  private OnItemClickListener click_listener;

  /*
   * Interface for click listener.
   */
  public interface OnItemClickListener {
    public void onItemClick(View view, int position);
  }

  /*
   * Constructor.
   */
  public PairedDeviceItemClickListener (Context context, OnItemClickListener listener) {
    click_listener = listener;
    gesture_detector = new GestureDetector(context, new GestureDetector.OnGestureListener () {
      @Override
      public boolean onSingleTapUp (MotionEvent e) {
        return true;
      }
      @Override public void onShowPress (MotionEvent e) {}
      @Override public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
      @Override public void onLongPress (MotionEvent e) {}
      @Override public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false; }
      @Override public boolean onDown (MotionEvent e) { return false; }
    });
  }

  /*
   * Detects touch/motion event and fires click listener if onSingleTapUp return true.
   */
  @Override
  public boolean onInterceptTouchEvent (RecyclerView view, MotionEvent event) {
    View item = view.findChildViewUnder(event.getX(), event.getY());
    if (item != null && gesture_detector.onTouchEvent(event)) {
      click_listener.onItemClick(item, view.getChildPosition(item));
    }
    return false;
  }

  /*
   * Nothing to do here.
   */
  @Override public void onTouchEvent (RecyclerView arg0, MotionEvent arg1) {}

}