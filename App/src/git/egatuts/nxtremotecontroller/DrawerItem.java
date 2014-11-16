package git.egatuts.nxtremotecontroller;

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