package com.xionces.Youtube_To_Mp3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.infxios.HttpRequest.HttpRequestFactory;
import com.infxios.HttpRequest.RIO;
import com.infxios.HttpRequest.RequestType;
import com.xionces.EasyTool.ImageView;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyActivity extends Activity {


    Button btnShowProgress;

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    EditText url;
    ImageView img;
    String video_id;
    Button convert;
    String video_title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        url = (EditText) findViewById(R.id.editText);
        img = (ImageView) findViewById(R.id.imageView);
        convert = (Button) findViewById(R.id.button);
        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);


        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!url.getText().toString().equals(""))
                {
                    video_id = extractYTId(url.getText().toString());
                    img.L("http://img.youtube.com/vi/"+video_id+"/0.jpg");
                }
            }
        });


        btnShowProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                HttpRequestFactory factory = new HttpRequestFactory(getApplicationContext()) {
                    @Override
                    public void Happens(RIO rio) {
                        JSONObject object = rio.response_json;
                        try {
                            video_title = object.getString("title");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!url.getText().toString().equals("")) {
                            new DownloadFileFromURL().execute("http://www.youtubeinmp3.com/fetch/?video=https://www.youtube.com/watch?v="+video_id);
                        }


                    }
                };
                factory.executeRequest(RequestType.GET,"http://www.youtubeinmp3.com/fetch/?format=JSON&video=https://www.youtube.com/watch?v="+video_id,null);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;

            default:
                return null;
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                File folder = new File(Environment.getExternalStorageDirectory() + "/YoutubeToMp3");
                boolean success = true;
                OutputStream output;
                if (!folder.exists()) {
                    success = folder.mkdir();
                }
                if (success) {
                     output = new FileOutputStream("/sdcard/YoutubeToMp3/"+video_title+".mp3");
                } else {
                    output = new FileOutputStream("/sdcard/YoutubeToMp3/"+video_title+".mp3");
                }
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }
        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
            String filepath = Environment.getExternalStorageDirectory() + "/YoutubeToMp3/"+ video_title+".mp3";
            Toast.makeText(getApplicationContext(),"Downloaded path is "+filepath,Toast.LENGTH_LONG).show();
        }

    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }



}
