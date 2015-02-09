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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                              *
 *                                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                               *
 *                                                                                                                                                                   *
 *  And the corresponding file at:                                                                                                                                   *
 *                                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/preference/PreferencesUtils.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;

/*
 *  PreferenceUtils class used to update and get the SharedPreferences.
 */
public class PreferencesUtils {

  private Context context;
  private SharedPreferences sharedPreferences;

  /*
   *  Constructor.
   */
  public PreferencesUtils (Context context) {
    this.context = context;
  }

  /*
   *  Getter for SharedPreferences.
   */
  public SharedPreferences getSharedPreferences () {
    return this.sharedPreferences;
  }

  /*
   *  Returns a new instance of PreferenceUtils.Editor.
   */
  public Editor getEditor () {
    return new Editor(this.sharedPreferences);
  }

  /*
   *  Sets SharedPreferences mode to Context.MODE_PRIVATE with the given name.
   */
  public void privateMode (String name) {
    this.sharedPreferences = this.context.getSharedPreferences(name, Context.MODE_PRIVATE);
  }

  /*
   *  Returns the default SharedPreferences mode.
   */
  public void privateMode () {
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
  }

  /*
   *  Editor subclass.
   */
  public class Editor {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /*
     *  Constructor.
     */
    public Editor (SharedPreferences preferences) {
      this.sharedPreferences = preferences;
    }

    /*
     *  Returns SharedPreferences instance.
     */
    public SharedPreferences getSharedPreferences () {
      return this.sharedPreferences;
    }

    /*
     *  Returns SharedPrefences.Editor instance.
     */
    public SharedPreferences.Editor getEditor () {
      return this.editor;
    }

    /*
     *  Initializes SharedPreferences edition.
     */
    public void edit () {
      this.editor = this.sharedPreferences.edit();
    }

    /*
     *  Saves changes using AsyncTask if SharedPreferences.Editor.edit() is not available (API level < 10).
     */
    public void save () {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
        this.editor.apply();
      } else {
        new AsyncTask<Void, Void, Void>() {
          @Override
          public Void doInBackground (Void... params) {
            PreferencesUtils.Editor.this.editor.commit();
            return null;
          }
        }.execute();
      }
    }

    /*
     *  Saves a string value. Can trigger this.save() method.
     */
    public void saveString (String key, String value, boolean autosave) {
      this.editor.putString(key, value);
      if (autosave) this.save();
    }

    /*
     *  Saves a string value with (boolean) autosave always as true.
     */
    public void saveString (String key, String value) {
      this.saveString(key, value, true);
    }

    /*
     *  Returns a shared preference string value using a predefined value if not available.
     */
    public String getString (String key, String default_value) {
      return this.sharedPreferences.getString(key, default_value);
    }

    /*
     *  Returns a shared preference string value using empty string ("") as predefined value.
     */
    public String getString (String key) {
      return this.sharedPreferences.getString(key, "");
    }

    /*
     *  Saves an int value. Can trigger this.save() method.
     */
    public void saveInt (String key, int value, boolean autosave) {
      this.editor.putInt(key, value);
      if (autosave) this.save();
    }

    /*
     *  Saves an int value with (boolean) autosave always as true.
     */
    public void saveInt (String key, int value) {
      this.saveInt(key, value, true);
    }

    /*
     *  Returns a shared preference int value using a predefined value if not available.
     */
    public int getInt (String key, int default_value) {
      return this.sharedPreferences.getInt(key, default_value);
    }

    /*
     *  Returns a shared preference int value using 0 as predefined value.
     */
    public int getInt (String key) {
      return this.getInt(key, 0);
    }

    /*
     *  Saves a boolean value. Can trigger this.save() method.
     */
    public void saveBoolean (String key, boolean value, boolean autosave) {
      this.editor.putBoolean(key, value);
      if (autosave) this.save();
    }

    /*
     *  Saves a boolean value with (boolean) autosave always as true.
     */
    public void saveBoolean (String key, boolean value) {
      this.saveBoolean(key, value, true);
    }

    /*
     *  Returns a shared preference boolean value using a predefined value if not available.
     */
    public boolean getBoolean (String key, boolean default_value) {
      return this.sharedPreferences.getBoolean(key, default_value);
    }

    /*
     *  Returns a shared preference boolean value using false as predefined value.
     */
    public boolean getBoolean (String key) {
      return this.getBoolean(key, false);
    }

  }

}