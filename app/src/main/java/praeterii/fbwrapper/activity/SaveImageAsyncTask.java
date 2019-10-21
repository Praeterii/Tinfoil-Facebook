package praeterii.fbwrapper.activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import praeterii.fbwrapper.R;

class SaveImageAsyncTask extends AsyncTask<Bitmap, Void, Boolean> {
    private final Application application;

    SaveImageAsyncTask(Application application) {
        this.application = application;
    }

    @Override
    protected Boolean doInBackground(Bitmap... images) {
        Bitmap bitmap = images[0];
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File imageFile = new File(directory, System.currentTimeMillis() + ".jpg");
        try {
            if (imageFile.createNewFile()) {
                FileOutputStream ostream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                ostream.close();

                // Ping the media scanner
                application.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(application, R.string.txt_save_image_success, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(application, R.string.txt_save_image_failed, Toast.LENGTH_LONG).show();
        }
    }
}
