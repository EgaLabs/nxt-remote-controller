package git.egatuts.nxtremotecontroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment
{
  
  public HomeFragment () {}
  
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent_container, Bundle savedInstanceState) {
    View home_view = inflater.inflate(R.layout.home_fragment, parent_container, false);
    return home_view;
  }
  
}