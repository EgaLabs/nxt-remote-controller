package git.egatuts.nxtremotecontroller.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;

import git.egatuts.nxtremotecontroller.GlobalUtils;

public class ImageDownloader extends AsyncTask<ImageView, Void, Bitmap> {

  ImageView image;

  @Override
  protected Bitmap doInBackground (ImageView... images) {
    this.image = images[0];
    try {
      return GlobalUtils.downloadDrawable((String) images[0].getTag());
    } catch (IOException e) {
      //e.printStackTrace();
    }
    return null;
  }

  @Override
  protected void onPostExecute (Bitmap result) {
    this.image.setImageBitmap(result);
  }
}
