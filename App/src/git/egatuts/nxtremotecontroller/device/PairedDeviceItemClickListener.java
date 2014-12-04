package git.egatuts.nxtremotecontroller.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class PairedDeviceItemClickListener implements RecyclerView.OnItemTouchListener
{
  
  private GestureDetector gesture_detector;
  private OnItemClickListener click_listener;
  
  public interface OnItemClickListener
  {
    public void onItemClick (View view, int position);
  }
  
  public PairedDeviceItemClickListener (Context context, OnItemClickListener listener)
  {
    click_listener = listener;
    gesture_detector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
      @Override
      public boolean onSingleTapUp(MotionEvent e)
      {
        return true;
      }
      @Override public void onShowPress(MotionEvent e) {}
      @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
      @Override public void onLongPress(MotionEvent e) {}
      @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false; }
      @Override public boolean onDown(MotionEvent e) { return false; }
    });
  }
  
  @Override
  public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent event) {
    View item = view.findChildViewUnder(event.getX(), event.getY());
    if (item != null && gesture_detector.onTouchEvent(event)) {
      click_listener.onItemClick(item, view.getChildPosition(item));
    }
    return false;
  }

  @Override
  public void onTouchEvent(RecyclerView arg0, MotionEvent arg1) {}
}