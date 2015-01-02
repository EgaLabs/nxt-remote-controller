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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                          *
 *                                                                                                                                                               *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                           *
 *                                                                                                                                                               *
 *  And the corresponding file at:                                                                                                                               *
 *                                                                                                                                                               *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/views/BaseProgressDialog.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.views;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;

import git.egatuts.nxtremotecontroller.activity.BaseActivity;

/*
 *  Base progress dialog class.
 */
public abstract class BaseProgressDialog extends ProgressDialog {

  protected BaseActivity activity;
  protected boolean isCancelled;

  public BaseProgressDialog (BaseActivity activity) {
    super(activity);
    this.setCancelable(false);
    this.activity = activity;
  }

  /*
   *  Method executed after the show method has been called. Used to style the progress bar.
   */
  public abstract void onPostShow ();

  /*
   *  Abstract method to get the View that saves the text (TextView).
   */
  public abstract View getTextView ();

  /*
   *  Abstract method to get the progress bar view.
   */
  public abstract View getProgressBar ();

  /*
   *  Overridden show method that automatically sets the content view and styles the color of the progress bar.
   */
  @Override
  public void show () {
    super.show();
    this.onPostShow();
  }

  /*
   *  Sets the text of the progress dialog.
   */
  public void setText (int resId) {
    ((TextView) this.getTextView()).setText(this.activity.getString(resId));
  }

  public void setText (String msg) {
    ((TextView) this.getTextView()).setText(msg);
  }

}
