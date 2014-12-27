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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                        *
 *                                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                         *
 *                                                                                                                                                                             *
 *  And the corresponding file at:                                                                                                                                             *
 *                                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/views/BaseIndeterminateProgressDialog.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.views;

import android.view.View;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.UltraBaseActivity;

/*
 *  Extended class of ProgressDialog used in all the application only
 *  styles the color of the progress bar and overrides some abstract methods.
 */
public class BaseIndeterminateProgressDialog extends BaseProgressDialog {

  /*
   *  Constructor.
   */
  public BaseIndeterminateProgressDialog (UltraBaseActivity activity) {
    super(activity);
  }

  /*
   *  Getter and setter to set if the first animation should be
   */
  public void setDoFirstAnimation (boolean value) {
    ((ProgressBarCircularIndeterminate) this.getProgressBar()).setDoFirstAnimation(value);
  }

  public boolean getDoFirstAnimation () {
    return ((ProgressBarCircularIndeterminate) this.getProgressBar()).getDoFirstAnimation();
  }

  public void setColor (int color) {
    ((ProgressBarCircularIndeterminate) this.getProgressBar()).setBackgroundColor(color);
  }

  /*
   *  Returns the view of the shown text.
   */
  @Override
  public View getTextView () {
    return this.findViewById(R.id.text_view);
  }

  /*
   *  Returns the circular progress bar of the progress dialog.
   */
  @Override
  public View getProgressBar () {
    return this.findViewById(R.id.progress_bar);
  }

  /*
   *  When the view is shown we style it's layout (progress bar).
   */
  @Override
  public void onPostShow () {
    this.setContentView(R.layout.progress_dialog);
    this.setColor(this.activity.getGlobalUtils().getAttribute(R.attr.progress_circular_color));
  }

}