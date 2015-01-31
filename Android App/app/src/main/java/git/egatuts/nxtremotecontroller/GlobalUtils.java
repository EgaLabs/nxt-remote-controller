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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                              *
 *                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                               *
 *                                                                                                                                                   *
 *  And the corresponding file at:                                                                                                                   *
 *                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/GlobalUtils.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

/*
 *  Utility-belt class used almost in all the parts of the Android Application with a few static
 *  methods in order to not need to create an instance of the class. Also available as non-static
 *  methods without needing to pass the context as argument every time.
 */
public class GlobalUtils {

  protected Context context;

  /*
   *  Constructors.
   */
  public GlobalUtils (Context context) {
    this.context = context;
  }

  public GlobalUtils () {
  }

  /*
   *  Getter and setter for the context.
   */
  public void setContext (Context context) {
    this.context = context;
  }

  public Context getContext () {
    return this.context;
  }


  /*
   *  Returns resource constant value by name.
   */
  public int getResourceId (String name, String type, String packageName) {
    return this.context.getResources().getIdentifier(name, type, packageName);
  }

  /*
   *  Returns Attribute resource id.
   */
  public int getAttributeId (String name, String packageName) {
    return this.getResourceId(name, "attr", packageName);
  }

  public int getAttributeId (String name) {
    return this.getAttributeId(name, this.context.getPackageName());
  }

  /*
   *  Returns String resource id.
   */
  public int getStringId (String name, String packageName) {
    return this.getResourceId(name, "string", packageName);
  }

  public int getStringId (String name) {
    return this.getStringId(name, this.context.getPackageName());
  }

  /*
   *  Returns Array resource id.
   */
  public int getArrayId (String name, String packageName) {
    return this.getResourceId(name, "array", packageName);
  }

  public int getArrayId (String name) {
    return this.getArrayId(name, this.context.getPackageName());
  }

  /*
   *  Returns Plural resource id.
   */
  public int getPluralId (String name, String packageName) {
    return this.getResourceId(name, "plurals", packageName);
  }

  public int getPluralId (String name) {
    return this.getPluralId(name, this.context.getPackageName());
  }

  /*
   *  Returns Color resource id.
   */
  public int getColorId (String name, String packageName) {
    return this.getResourceId(name, "color", packageName);
  }

  public int getColorId (String name) {
    return this.getColorId(name, this.context.getPackageName());
  }

  /*
   *  Returns Drawable resource id.
   */
  public int getDrawableId (String name, String packageName) {
    return this.getResourceId(name, "drawable", packageName);
  }

  public int getDrawableId (String name) {
    return this.getDrawableId(name, this.context.getPackageName());
  }

  /*
   *  Returns Layout resource id.
   */
  public int getLayoutId (String name, String packageName) {
    return this.getResourceId(name, "layout", packageName);
  }

  public int getLayoutId (String name) {
    return this.getLayoutId(name, this.context.getPackageName());
  }

  /*
   *  Returns Menu resource id.
   */
  public int getMenuId (String name, String packageName) {
    return this.getResourceId(name, "menu", packageName);
  }

  public int getMenuId (String name) {
    return this.getMenuId(name, this.context.getPackageName());
  }

  /*
   *  Returns Style resource id.
   */
  public int getStyleId (String name, String packageName) {
    return this.getResourceId(name, "style", packageName);
  }

  public int getStyleId (String name) {
    return this.getStyleId(name, this.context.getPackageName());
  }

  /*
   *  Returns Property Animation resource id.
   */
  public int getPropertyAnimationId (String name, String packageName) {
    return this.getResourceId(name, "animator", packageName);
  }

  public int getPropertyAnimationId (String name) {
    return this.getPropertyAnimationId(name, this.context.getPackageName());
  }

  /*
   *  Returns View Animation resource id.
   */
  public int getViewAnimationId (String name, String packageName) {
    return this.getResourceId(name, "anim", packageName);
  }

  public int getViewAnimationId (String name) {
    return this.getViewAnimationId(name, this.context.getPackageName());
  }

  /*
   *  Returns Boolean resource id.
   */
  public int getBooleanId (String name, String packageName) {
    return this.getResourceId(name, "bool", packageName);
  }

  public int getBooleanId (String name) {
    return this.getBooleanId(name, this.context.getPackageName());
  }

  /*
   *  Returns Dimension resource id.
   */
  public int getDimensionId (String name, String packageName) {
    return this.getResourceId(name, "dimen", packageName);
  }

  public int getDimensionId (String name) {
    return this.getDimensionId(name, this.context.getPackageName());
  }

  /*
   *  Returns Identificator resource id.
   */
  public int getIdentificatorId (String name, String packageName) {
    return this.getResourceId(name, "id", packageName);
  }

  public int getIdentificatorId (String name) {
    return this.getIdentificatorId(name, this.context.getPackageName());
  }

  /*
   *  Returns Integer resource id.
   */
  public int getIntegerId (String name, String packageName) {
    return this.getResourceId(name, "integer", packageName);
  }

  public int getIntegerId (String name) {
    return this.getIntegerId(name, this.context.getPackageName());
  }


  /*
   *  Returns the string resource by it's constant value or it's name.
   */
  public String getStringResource (int resId) {
    return this.context.getResources().getString(resId);
  }

  public String getStringResource (String name, String packageName) {
    return this.getStringResource(this.getStringId(name, packageName));
  }

  public String getStringResource (String name) {
    return this.getStringResource(name, this.context.getPackageName());
  }

  /*
   *  Returns the Array String resource (String[]) by it's constant value or it's name.
   */
  public String[] getStringArrayResource (int resId) {
    return this.context.getResources().getStringArray(resId);
  }

  public String[] getStringArrayResource (String name, String packageName) {
    return this.getStringArrayResource(this.getArrayId(name, packageName));
  }

  public String[] getStringArrayResource (String name) {
    return this.getStringArrayResource(name, this.context.getPackageName());
  }

  /*
   *  Returns the Color resource (int) by it's constant value or it's name.
   */
  public int getColorResource (int resId) {
    return this.context.getResources().getColor(resId);
  }

  public int getColorResource (String name, String packageName) {
    return this.getColorResource(this.getColorId(name, packageName));
  }

  public int getColorResource (String name) {
    return this.getColorResource(name, this.context.getPackageName());
  }

  /*
   *  Returns the Boolean resource by it's constant value or it's name.
   */
  public boolean getBooleanResource (int resId) {
    return this.context.getResources().getBoolean(resId);
  }

  public boolean getBooleanResource (String name, String packageName) {
    return this.getBooleanResource(this.getBooleanId(name, packageName));
  }

  public boolean getBooleanResource (String name) {
    return this.getBooleanResource(name, this.context.getPackageName());
  }

  /*
   *  Returns the Dimension resource (float) by it's constant value or it's name.
   */
  public float getDimensionResource (int resId) {
    return this.context.getResources().getDimension(resId);
  }

  public float getDimensionResource (String name, String packageName) {
    return this.getDimensionResource(this.getDimensionId(name, packageName));
  }

  public float getDimensionResource (String name) {
    return this.getDimensionResource(name, this.context.getPackageName());
  }

  /*
   *  Returns the Drawable resource (Drawable) by it's constant value or it's name.
   */
  public Drawable getDrawableResource (int resId) {
    return this.context.getResources().getDrawable(resId);
  }

  public Drawable getDrawableResource (String name, String packageName) {
    return this.getDrawableResource(this.getDrawableId(name, packageName));
  }

  public Drawable getDrawableResource (String name) {
    return this.getDrawableResource(name, this.context.getPackageName());
  }

  /*
   *  Returns the Integer resource (int) by it's constant value or it's name.
   */
  public int getIntegerResource (int resId) {
    return this.context.getResources().getInteger(resId);
  }

  public int getIntegerResource (String name, String packageName) {
    return this.getIntegerResource(this.getIntegerId(name, packageName));
  }

  public int getIntegerResource (String name) {
    return this.getIntegerResource(name, this.context.getPackageName());
  }

  /*
   *  Returns the Array Integer resource (int[]) by it's constant value or it's name.
   */
  public int[] getIntArrayResource (int resId) {
    return this.context.getResources().getIntArray(resId);
  }

  public int[] getIntArrayResource (String name, String packageName) {
    return this.getIntArrayResource(this.getArrayId(name, packageName));
  }

  public int[] getIntArrayResource (String name) {
    return this.getIntArrayResource(name, this.context.getPackageName());
  }

  /*
   *  Returns the Animation resource (XMLResourceParser) by it's constant value or it's name.
   */
  public XmlResourceParser getAnimationResource (int resId) {
    return this.context.getResources().getAnimation(resId);
  }

  /*
   *  Returns the Color list resource (colorStateList) by it's constant value or it's name.
   */
  public ColorStateList getColorStateListResource (int resId) {
    return this.context.getResources().getColorStateList(resId);
  }

  public ColorStateList getColorStateListResource (String name, String packageName) {
    return this.getColorStateListResource(this.getColorId(name, packageName));
  }

  public ColorStateList getColorStateListResource (String name) {
    return this.getColorStateListResource(name, this.context.getPackageName());
  }


  /*
   *  Getter and setter for the active theme.
   */
  public Theme getActiveTheme () {
    return this.context.getTheme();
  }

  public void setActiveTheme (String name, String packageName) {
    this.context.setTheme(this.getStyleId(name, packageName));
  }

  public void setActiveTheme (String name) {
    this.setActiveTheme(name, this.context.getPackageName());
  }

  /*
   *  Returns the value of an attribute defined in the theme by it's id or it's name.
   */
  public int getAttribute (int attr) {
    TypedValue value = new TypedValue();
    this.getActiveTheme().resolveAttribute(attr, value, true);
    return value.data;
  }

  public int getAttribute (String name, String packageName) {
    return this.getAttribute(this.getAttributeId(name, packageName));
  }

  public int getAttribute (String name) {
    return this.getAttribute(name, this.context.getPackageName());
  }

  /*
   *  Mixes two colors.
   */
  public static int mixColors (int color1, int color2, float amount) {
    final byte ALPHA_CHANNEL = 24;
    final byte RED_CHANNEL = 16;
    final byte GREEN_CHANNEL = 8;
    final byte BLUE_CHANNEL = 0;
    final float inverseAmount = 1.0f - amount;
    int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount) +
            ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount))) & 0xff;
    int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) +
            ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
    int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) +
            ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
    int b = ((int) (((float) (color1 & 0xff) * amount) +
            ((float) (color2 & 0xff) * inverseAmount))) & 0xff;
    return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
  }

  public static int mixColors (int color1, int color2) {
    return GlobalUtils.mixColors(color1, color2, 0.5f);
  }

  /*
   *  Makes a color darker.
   */
  public static int getDarkerColor (int color, float factor) {
    int a = Color.alpha(color);
    int r = (int) (Color.red(color) * factor);
    int g = (int) (Color.green(color) * factor);
    int b = (int) (Color.blue(color) * factor);
    return Color.argb(a, r, g, b);
  }

  /*
   *  Formats a string.
   */
  public String format (int resId, String data) {
    return String.format(this.getStringResource(resId), data);
  }

  /*
   *  Shows a toast.
   */
  public static void showToast (final Context context, final String message, final boolean longLength) {
    ((Activity) context).runOnUiThread(new Runnable() {
      @Override
      public void run () {
        Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void showToast (String message, boolean longLength) {
    GlobalUtils.showToast(this.context, message, longLength);
  }

  public void showToast (String message) {
    this.showToast(message, false);
  }

  public void showToast (int resId, boolean longLength) {
    this.showToast(this.getStringResource(resId), longLength);
  }

  public void showToast (int resId) {
    this.showToast(resId, false);
  }

  public void showToast (int resId, String message, boolean longLength) {
    this.showToast(this.format(resId, message), longLength);
  }

  public void showToast (int resId, String message) {
    this.showToast(resId, message, false);
  }

  /*
   *  Creates an AlertDialog returning it's Builder.
   */
  public static AlertDialog.Builder createAlertDialog (Context context, String title, String message) {
    return new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, null);
  }

  public AlertDialog.Builder createAlertDialog (String title, String message) {
    return new AlertDialog.Builder(this.context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, null);
  }

  public AlertDialog.Builder createAlertDialog (int resIdTitle, int resIdMessage) {
    return new AlertDialog.Builder(this.context)
            .setTitle(this.getStringResource(resIdTitle))
            .setMessage(this.getStringResource(resIdMessage));
  }

  /*
   *  Joins multiple arrays.
   */
  public static byte[] concatBytes (byte[]... arrays) {
    int length = 0;
    for (byte[] array : arrays) {
      length += array.length;
    }
    byte[] result = new byte[length];
    int index = 0;
    for (byte[] array : arrays) {
      for (byte data : array) {
        result[index] = data;
        index++;
      }
    }
    return result;
  }

  /*
   *  Gets MD5 of String without omitting trailing zeros.
   */
  public static String md5 (byte[] bytes) {
    byte[] digest = null;
    try {
      digest = MessageDigest.getInstance("MD5").digest(bytes);
    } catch (NoSuchAlgorithmException e) {
      //e.printStackTrace();
    }
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < digest.length; i++) {
      int number = (digest[i] & 0xFF) | 0x100;
      buffer.append(Integer.toHexString(number).substring(1, 3));
    }
    return buffer.toString();
  }

  public static String md5 (String data) {
    return GlobalUtils.md5(data.getBytes());
  }

  public static Bitmap downloadDrawable (String url) throws IOException {
    HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
    connection.connect();
    return BitmapFactory.decodeStream(connection.getInputStream());
  }

}
