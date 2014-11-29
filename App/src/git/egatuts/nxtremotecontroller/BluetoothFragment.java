package git.egatuts.nxtremotecontroller;

import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BluetoothFragment extends Fragment
{
  
  private BluetoothUtils _bluetooth_utils;
  private Fragment _last_fragment;
  
  public BluetoothFragment (Fragment last_fragment, BluetoothUtils bluetooth_utils)
  {
    this._last_fragment = last_fragment;
    this._bluetooth_utils = bluetooth_utils;
  }
  
  public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.bluetooth_enable, parent, false);
    Button button = (Button) view.findViewById(R.id.enable_bluetooth);
    button.setOnClickListener(new View.OnClickListener ()
    {
      @Override
      public void onClick(View v) {
       if (!_bluetooth_utils.isEnabled())
       {
         _bluetooth_utils.enable();
         while (!_bluetooth_utils.isEnabled())
         {
           try {
             Thread.sleep(100L);
           } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
             return;
           }
         }
       }
       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, _last_fragment).commit();
      }
    });
    return view;
  }
  
}