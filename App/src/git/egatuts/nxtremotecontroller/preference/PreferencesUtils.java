package git.egatuts.nxtremotecontroller.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;

public class PreferencesUtils
{
  
  private Context _context;
  private SharedPreferences _shared_preferences;
  
  public PreferencesUtils (Context context)
  {
    _context = context;
  }
  
  public SharedPreferences getsharedPreferences ()
  {
    return _shared_preferences;
  }
  
  public Editor getEditor ()
  {
    return new Editor(_shared_preferences);
  }
  
  public void privateMode (String name)
  {
    _shared_preferences = _context.getSharedPreferences(name, Context.MODE_PRIVATE);
  }
  
  public class Editor
  {
    
    private SharedPreferences _shared_preferences;
    private SharedPreferences.Editor _editor;
    
    public Editor (SharedPreferences preferences)
    {
      _shared_preferences = preferences;
    }
    
    public SharedPreferences getSharedPreferences ()
    {
      return _shared_preferences;
    }
    
    public SharedPreferences.Editor getEditor ()
    {
      return _editor;
    }
    
    public void edit ()
    {
      _editor = _shared_preferences.edit();
    }
    
    public void save ()
    {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
        _editor.apply();
      } else {
        new AsyncTask<Void, Void, Void>() {
          @Override
          public Void doInBackground (Void... params) {
            _editor.commit();
            return null;
          }
        }.execute();
      }
    }
    
    public void saveString (String key, String value, boolean autosave)
    {
      _editor.putString(key, value);
      if (autosave == true) this.save();
    }
    
    public void saveString (String key, String value)
    {
      this.saveString(key, value, true);
    }
    
    public String getString (String key, String default_value)
    {
      return _shared_preferences.getString(key, default_value);
    }
    
    public String getString (String key)
    {
      return _shared_preferences.getString(key, "");
    }
    
    public void saveInt (String key, int value, boolean autosave)
    {
      _editor.putInt(key, value);
      if (autosave == true) this.save();
    }
    
    public void saveInt (String key, int value)
    {
      this.saveInt(key, value, true);
    }
    
    public int getInt (String key, int default_value)
    {
      return _shared_preferences.getInt(key, default_value);
    }
    
    public int getInt(String key)
    {
      return this.getInt(key, 0);
    }
    
  }
  
}