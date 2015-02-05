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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                    *
 *                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                     *
 *                                                                                                                                                         *
 *  And the corresponding file at:                                                                                                                         *
 *                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/views/JoystickView.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import git.egatuts.nxtremotecontroller.R;

/*
 *  Extended View that adds a customizable Joystick.
 */
public class JoystickView extends View {

  private int WIDTH;
  private int HEIGHT;
  private float RADIUS;
  private float JOYSTICK_RADIUS;
  private int centerX;
  private int centerY;
  private int posX = -1;
  private int posY = -1;
  private float lastPosX;
  private float lastPosY;
  private double angle;
  private double module;
  private int joystickColor;
  private int joystickStroke;
  private Paint paint;
  private Path path;
  private OnChangePosition listener;

  /*
   *  Represents a changing position event of the joystick.
   */
  public interface OnChangePosition {
    public void onChange (float x, float y);
  }

  /*
   *  Constructor.
   */
  public JoystickView (Context context) {
    super(context);
  }

  public JoystickView (Context context, AttributeSet attrs) {
    super(context, attrs);
    this.init(context, attrs);
  }

  public JoystickView (Context context, AttributeSet attrs, int defaultStyle) {
    super(context, attrs, defaultStyle);
    this.init(context, attrs);
  }

  /*
   *  Executed in all the constructors.
   */
  private void init (Context context, AttributeSet attrs) {
    if (isInEditMode()) return;
    final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JoystickView);
    this.joystickColor = typedArray.getInt(R.styleable.JoystickView_joystickColor, 0xBB333333);
    this.joystickStroke = typedArray.getInt(R.styleable.JoystickView_joystickStroke, 0xBB222222);
    this.resize(this.getWidth(), this.getHeight(), false);
    this.paint = new Paint();
    this.paint.setAntiAlias(true);
    this.path = new Path();
  }

  /*
   *  Private method to resize view with the specified bounds.
   *  It will fit to the minimum value because it must be a perfectly shaped circle.
   */
  private void resize (int w, int h, boolean ready) {
    this.WIDTH = w;
    this.HEIGHT = h;
    if (this.WIDTH < this.HEIGHT) {
      this.centerX = (this.WIDTH / 2) + this.getLeft();
      this.centerY = (this.WIDTH / 2) + this.getTop() + ((this.HEIGHT - this.WIDTH) / 2);
      this.HEIGHT = this.WIDTH;
    } else {
      this.centerX = (this.HEIGHT / 2) + this.getLeft() + ((this.WIDTH - this.HEIGHT) / 2);
      this.centerY = (this.HEIGHT / 2) + this.getTop();
      this.WIDTH = this.HEIGHT;
    }
    this.RADIUS = (float) ((double) (this.WIDTH / 2) * (1 - 0.2));
    this.JOYSTICK_RADIUS = (float) ((double) (this.WIDTH / 2) * 0.2);
    /*if (ready) {
      ViewGroup.LayoutParams params = this.getLayoutParams();
      params.width = this.WIDTH;
      params.height = this.HEIGHT;
      this.setLayoutParams(params);
    }*/
  }

  /*
   *  Getter and setter for the change position listener of the joystick.
   */
  public void setOnChangePositionListener (OnChangePosition listener) {
    this.listener = listener;
  }

  public OnChangePosition getOnChangePositionListener () {
    return this.listener;
  }

  /*
   *  Getters for the X and Y value, the angle, the module and the maximum module to
   *  do the calculations and return a value between -1 and 1.
   */
  public float getXValue () {
    return (float) (this.posX - this.centerX) / this.RADIUS;
  }

  public float getYValue () {
    return (float) (this.centerY - this.posY) / this.RADIUS;
  }

  public double getAngle () {
    return this.angle;
  }

  public double getModule () {
    return this.module;
  }

  public double getMaximumModule () {
    return this.RADIUS;
  }

  /*
   *  When the size changed we resize it fitting to only one bound.
   */
  @Override
  public void onSizeChanged (int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    this.resize(w, h, true);
  }

  /*
   *  We handle here all the stuff to calculate the point that is touched and draw it only without exiting the bounds
   *  and applying paddings (internal margins) to avoid clipping of the joystick and do the correct drawing.
   *  Also restores its state to the geometric center when the screen touch has been released and executes the corresponding listeners.
   */
  @Override
  public boolean onTouchEvent (MotionEvent event) {
    float x = event.getX(0);
    float y = event.getY(0);
    int action = event.getAction();
    int down = MotionEvent.ACTION_DOWN;
    int up = MotionEvent.ACTION_UP;
    int move = MotionEvent.ACTION_MOVE;
    int cancel = MotionEvent.ACTION_CANCEL;
    if (action == down || action == move) {
      double offsetX = x - this.centerX;
      double offsetY = y - this.centerY;
      this.angle = Math.atan2(offsetY, offsetX);
      this.posX = (int) x;
      this.posY = (int) y;
      double moduleTouch = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
      if (moduleTouch > this.RADIUS) {
        this.posX = (int) (this.centerX + this.RADIUS * Math.cos(this.angle));
        this.posY = (int) (this.centerY + this.RADIUS * Math.sin(this.angle));
      }
      this.module = Math.sqrt(Math.pow(this.posX - this.centerX, 2) + Math.pow(this.posY - this.centerY, 2));
      if (this.listener != null) this.listener.onChange(this.getXValue(), this.getYValue());
      invalidate();
    } else if (action == up || action == cancel) {
      if (this.posX == this.lastPosX || this.posY == this.lastPosY) {
        this.posX = this.centerX;
        this.posY = this.centerY;
      }
      this.module = Math.sqrt(Math.pow(this.posX - this.centerX, 2) + Math.pow(this.posY - this.centerY, 2));
      if (this.listener != null) this.listener.onChange(this.getXValue(), this.getYValue());
      invalidate();
    }
    this.lastPosX = this.posX;
    this.lastPosY = this.posY;
    return true;
  }

  /*
   *  Draws the joystick based on the position.
   */
  @Override
  public void onDraw (Canvas canvas) {
    super.onDraw(canvas);
    this.path.reset();
    int joystickX = this.posX < 0 ? this.centerX : this.posX;
    int joystickY = this.posY < 0 ? this.centerY : this.posY;
    this.path.addCircle(joystickX, joystickY, this.JOYSTICK_RADIUS, Path.Direction.CW);
    this.paint.setStyle(Paint.Style.FILL);
    this.paint.setColor(this.joystickColor);
    canvas.drawPath(path, this.paint);
    this.paint.setStyle(Paint.Style.STROKE);
    this.paint.setStrokeWidth(3.0f);
    this.paint.setColor(this.joystickStroke);
    this.paint.setAlpha(255);
    canvas.drawPath(path, this.paint);
  }

}