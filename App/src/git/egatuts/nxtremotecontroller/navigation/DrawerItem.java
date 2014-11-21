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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                     *
 *                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                      *
 *                                                                                                                         *
 * And the corresponding file at:                                                                                          *
 *                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/DrawerItem.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.navigation;

import android.graphics.drawable.Drawable;

public class DrawerItem
{
  
  private String text;
  private Drawable drawable;
  
  /*
   * Setters and getters for text.
   */
  public void setText (String custom_text)
  {
    text = custom_text;
  }
  
  public String getText ()
  {
    return text;
  }
  
  /*
   * Setters and getters for drawable.
   */
  public void setDrawable (Drawable custom_drawable)
  {
    drawable = custom_drawable;
  }
  
  public Drawable getDrawable ()
  {
    return drawable;
  }
  
  /*
   * Constructors.
   */
  private void init (String custom_text, Drawable custom_drawable)
  {
    text = custom_text;
    drawable = custom_drawable;
  }
  
  public DrawerItem (String custom_text, Drawable custom_drawable)
  {
    init(custom_text, custom_drawable);
  }
  
  public DrawerItem (String custom_text)
  {
    init(custom_text, null);
  }
  
  public DrawerItem (Drawable custom_drawable)
  {
    init(null, custom_drawable);
  }
  
}