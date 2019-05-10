package tk.evidenceprotector.evidenceprotector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectOption extends AppCompatActivity implements View.OnClickListener {
    private Button buttonForVideo, buttonForImage;
    private String mImageFileLocation = "";
    private String gotFirsname, gotLastname;
    Uri uriOfFile;

    private String selectedPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option);
        buttonForVideo = (Button) findViewById(R.id.buttonTakeVideo);
        buttonForImage = (Button) findViewById(R.id.buttonCaptureImage);

        buttonForVideo.setOnClickListener(this);
        buttonForImage.setOnClickListener(this);

        Bundle userdata = getIntent().getExtras();
        gotFirsname = userdata.getString("sentFirstname");
        gotLastname = userdata.getString("sentLastname");

    }


    //IMAGE INITIALIZATION
    String mCurrentFilePath;

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = gotFirsname + "_" + gotLastname + "_" + "BMP_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".bmp", storageDir);
        mCurrentFilePath = image.getAbsolutePath();
        return image;
    }


    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // To ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex)
            {
                //Error occurred while creating the File
                ex.printStackTrace();

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "tk.evidenceprotector.evidenceprotector.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String mCurrentVideoPath;

    private File createVideoFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = gotFirsname + "_" + gotLastname + "_" + "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(videoFileName, ".mp4", storageDir);
        mCurrentFilePath = video.getAbsolutePath();
        return video;
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the video should go
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex)

            {
                //Error occurred while creating the File
                ex.printStackTrace();
            }

            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        "tk.evidenceprotector.evidenceprotector.android.fileprovider", videoFile);
                uriOfFile=videoURI;
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

            }
        }
    } @Override

      public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            uploadFile();


        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            uploadFile();
        }

    }

    private void uploadFile()
    {
        Log.d("IN IT","BEFORE ASYNC");
        class UploadFile extends AsyncTask<Void, Void, String>
        {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                uploading = ProgressDialog.show(SelectOption.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                uploading.dismiss();
                Toast.makeText(SelectOption.this,s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... params)
            {
                Upload u = new Upload();
                String msg = u.uploadVideo(mCurrentFilePath);
                return msg;
            }
        }
        UploadFile uf = new UploadFile();
        uf.execute();
    }


    public void onClick(View v)
    {
        if (v == buttonForVideo)
        {
        dispatchTakeVideoIntent();
        }
        else if (v == buttonForImage)
        {
            dispatchTakePictureIntent();
        }
    }
}

