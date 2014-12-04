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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                     *
 *                                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                      *
 *                                                                                                                                         *
 * And the corresponding file at:                                                                                                          *
 *                                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/preference/PreferencesUtils.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;

public class PreferencesUtils {

  private Context _context;
  private SharedPreferences _shared_preferences;

  public PreferencesUtils (Context context) {
    _context = context;
  }

  public SharedPreferences getsharedPreferences () {
    return _shared_preferences;
  }

  public Editor getEditor () {
    return new Editor(_shared_preferences);
  }

  public void privateMode (String name) {
    _shared_preferences = _context.getSharedPreferences(name, Context.MODE_PRIVATE);
  }

  public void privateMode () {
    _shared_preferences = PreferenceManager.getDefaultSharedPreferences(_context);
  }

  public class Editor {

    private SharedPreferences _shared_preferences;
    private SharedPreferences.Editor _editor;

    public Editor (SharedPreferences preferences) {
      _shared_preferences = preferences;
    }

    public SharedPreferences getSharedPreferences () {
      return _shared_preferences;
    }

    public SharedPreferences.Editor getEditor () {
      return _editor;
    }

    public void edit () {
      _editor = _shared_preferences.edit();
    }

    public void save () {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
        _editor.apply();
      } else {
        new AsyncTask<Void, Void, Void> () {
          @Override
          public Void doInBackground (Void...params) {
            _editor.commit();
            return null;
          }
        }.execute();
      }
    }

    public void saveString (String key, String value, boolean autosave) {
      _editor.putString(key, value);
      if (autosave == true) this.save();
    }

    public void saveString (String key, String value) {
      this.saveString(key, value, true);
    }

    public String getString (String key, String default_value) {
      return _shared_preferences.getString(key, default_value);
    }

    public String getString (String key) {
      return _shared_preferences.getString(key, "");
    }

    public void saveInt (String key, int value, boolean autosave) {
      _editor.putInt(key, value);
      if (autosave == true) this.save();
    }

    public void saveInt (String key, int value) {
      this.saveInt(key, value, true);
    }

    public int getInt (String key, int default_value) {
      return _shared_preferences.getInt(key, default_value);
    }

    public int getInt (String key) {
      return this.getInt(key, 0);
    }

  }

}