package com.alokomkar.mashup.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Alok on 19/12/17.
 */

class DownloadFileAsync extends AsyncTask<String, String, String> {
    private ProgressDialog mProgressDialog;
    private Context context;

    public DownloadFileAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Downloading file..");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;
        try {


                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream is = url.openStream();
                File testDirectory = new File(Environment.getExternalStorageDirectory() + "/Folder");
                if (!testDirectory.exists()) {
                    testDirectory.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(testDirectory+ "/"+aurl[0]+".mp3");
                byte data[] = new byte[1024];
                long total = 0;
                int progress = 0;
                while ((count = is.read(data)) != -1) {
                    total += count;
                    int progress_temp = (int) total * 100 / lenghtOfFile;
                    publishProgress(""+ progress_temp);
                    if (progress_temp % 10 == 0 && progress != progress_temp) {
                        progress = progress_temp;
                    }
                    fos.write(data, 0, count);
                }
                is.close();
                fos.close();

        } catch (Exception e) {}
        return null;

    }
    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC",progress[0]);
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String unused) {
        mProgressDialog.dismiss();
    }
}
