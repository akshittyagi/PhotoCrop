package com.example.akty7.photocrop;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class  MainActivity extends ActionBarActivity implements OnClickListener{

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;
    //keep track of cropping intent
    final int PIC_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //retrieve a reference to the UI button
        Button captureBtn = (Button)findViewById(R.id.capture_btn);
//handle button clicks
        captureBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.capture_btn) {
            try {
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }
            catch(ActivityNotFoundException e)
            {
                String errorMessage = "Your device doesn't support capturing images";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();

            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){
                //user is returning from capturing an image using the camera
                    picUri = data.getData();
                    //carry out the crop operation
                    performCrop();
            }
            else if(requestCode == PIC_CROP)
            {
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                //retrieve a reference to the ImageView
                ImageView picView = (ImageView)findViewById(R.id.picture);
                //display the returned cropped image
                picView.setImageBitmap(thePic);
            }
        }
    }

    private void performCrop(){
        try {

            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException e){
            //display an error message
            String errorMessage = "Your device doesn't support the crop action";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}