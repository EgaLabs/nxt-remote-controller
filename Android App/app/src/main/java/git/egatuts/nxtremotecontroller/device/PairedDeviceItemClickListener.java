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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                      *
 *                                                                                                                                                                           *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                       *
 *                                                                                                                                                                           *
 *  And the corresponding file at:                                                                                                                                           *
 *                                                                                                                                                                           *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/device/PairedDeviceItemClickListener.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/*
 *  PairedDevice item click listener used to detect tap up events using GestureDetector class.
 */
public class PairedDeviceItemClickListener implements RecyclerView.OnItemTouchListener {

  public static final int GESTURE_NONE       = 0;
  public static final int GESTURE_TAP_UP     = 1;
  public static final int GESTURE_PRESS      = 2;
  public static final int GESTURE_SCROLL     = 3;
  public static final int GESTURE_LONG_PRESS = 4;
  public static final int GESTURE_FLING      = 5;
  public static final int GESTURE_DOWN       = 6;

  private GestureDetector gestureDetector;
  private OnItemClickListener listener;
  private int result;

  /*
   *  Interface for click listener.
   */
  public interface OnItemClickListener {
    public void onItemClick (View view, int position);
    public void onItemLongClick (View view, int position);
  }

  /*
   *  Constructor.
   */
  public PairedDeviceItemClickListener (Context context, OnItemClickListener listener) {
    final PairedDeviceItemClickListener self = this;
    this.listener = listener;
    this.gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
      @Override
      public boolean onSingleTapUp (MotionEvent e) {
        self.result = PairedDeviceItemClickListener.GESTURE_TAP_UP;
        return false;
      }

      @Override
      public void onShowPress (MotionEvent e) {
        self.result = PairedDeviceItemClickListener.GESTURE_NONE;
      }

      @Override
      public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        self.result = PairedDeviceItemClickListener.GESTURE_NONE;
        return false;
      }

      @Override
      public void onLongPress (MotionEvent e) {
        self.result = PairedDeviceItemClickListener.GESTURE_LONG_PRESS;
      }

      @Override
      public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        self.result = PairedDeviceItemClickListener.GESTURE_NONE;
        return false;
      }

      @Override
      public boolean onDown (MotionEvent e) {
        self.result = PairedDeviceItemClickListener.GESTURE_NONE;
        return false;
      }
    });
  }

  /*
   *  Returns the result after processing the touch event.
   */
  public int getResult (MotionEvent event) {
    this.gestureDetector.onTouchEvent(event);
    return this.result;
  }

  public int getResult () {
    return this.result;
  }

  /*
   *  Detects touch/motion event and fires click listener if onSingleTapUp return true.
   */
  @Override
  public boolean onInterceptTouchEvent (RecyclerView view, MotionEvent event) {

    /*
     *  We get the recycler item view on that position because we can't get the view by the index position
     *  due to the logic of the recycler view... because it recycles views...
     */
    View item = view.findChildViewUnder(event.getX(), event.getY());
    int res = this.getResult(event);
    if (item != null && res != PairedDeviceItemClickListener.GESTURE_NONE) {

      /*
       *  We handle each event.
       */
      if (res == PairedDeviceItemClickListener.GESTURE_TAP_UP) {
        this.listener.onItemClick(item, view.getChildPosition(item));
      } else if (res == PairedDeviceItemClickListener.GESTURE_LONG_PRESS) {
        this.listener.onItemLongClick(item, view.getChildPosition(item));
      }
    }
    return false;
  }

  /*
   *  Nothing to do here.
   */
  @Override public void onTouchEvent (RecyclerView arg0, MotionEvent arg1) {}

}