package com.example.jiteshyadav.vehicletracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {

    static final int CAM_REQUEST = 1;
    ImageView imageView;
    String nameOfPerson;
    String VehicleNo;
    EditText name;
    EditText vehicleNo;
    Button nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button CaptureButton = (Button) findViewById(R.id.myButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        name = (EditText) findViewById(R.id.editName);
        vehicleNo = (EditText) findViewById(R.id.editVehicleNo);
        nextButton = (Button) findViewById(R.id.button2);


        if (!hasCamera())
            CaptureButton.setEnabled(false);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }

    }

    public void launchCamera(View view)
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAM_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ( requestCode == CAM_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            imageView.setImageBitmap(photo);
        }
    }





    public void write (View view) {
        nameOfPerson = name.getText().toString();
        VehicleNo = vehicleNo.getText().toString();

        if (nameOfPerson.matches("") || VehicleNo.matches("") || imageView.getDrawable() == null )
        {
            Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
        }
        else
        {
            try {
                FileOutputStream fileOutputStream = openFileOutput("name.txt", MODE_PRIVATE);
                fileOutputStream.write(nameOfPerson.getBytes());
                fileOutputStream.close();
                //  Toast.makeText(getApplicationContext(), "Name saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                FileOutputStream fileOutputStream = openFileOutput("vehicleNo.txt", MODE_PRIVATE);
                fileOutputStream.write(VehicleNo.getBytes());
                fileOutputStream.close();
                //  Toast.makeText(getApplicationContext(), "Vehicle no. saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bm;

            View v = imageView;
            v.setDrawingCacheEnabled(true);
            bm = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);

            String fileName = "image.png";
            File file = new File(fileName);

            try {

                FileOutputStream fOut = openFileOutput(fileName, MODE_PRIVATE);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                // Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }


            Intent intent = new Intent(this, SecondActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }


}