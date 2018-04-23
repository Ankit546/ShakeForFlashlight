package com.example.kumarankit.shakeforflashlight;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorManager;//this import statement for sensors
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;


public class MainActivity extends AppCompatActivity {

    private SensorManager sm;

    private float acelVal; //CURRENT ACCELARATIONA ND GRAVITY VALUE
    private float acelLast; //LAST ACCELARATION AND GRAVITY
    private float shake; //ACCELARATION VALUE diff FROM GRAVITY

    //this is for the flashlight, THE VARIABLES
    Camera camera;
    Camera.Parameters parameters;
    boolean isFLash=false;
    boolean isOn=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal=SensorManager.GRAVITY_EARTH;
        acelLast=SensorManager.GRAVITY_EARTH;
        shake=0.00f;

        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
        {
            camera=Camera.open();
            parameters=camera.getParameters();
            isFLash=true;


        }

    }

    private final SensorEventListener sensorListener= new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x=event.values[0];
            float y=event.values[1];
            float z=event.values[2];

            acelLast=acelVal;
            acelVal=(float)Math.sqrt((double) (x*x +y*y +z*z));
            float delta=acelVal-acelLast;
            shake=shake*0.9f+delta;

            if(shake>12) {

                //here the event to TURN ON FLASHLIGHT OR ANYHTING ELSE!
                //Toast toast = Toast.makeText(getApplicationContext(),"DO NOT SHAKE ME", Toast.LENGTH_LONG);
                //toast.show();

                    if (isFLash) {
                        //it has flash

                            if (!isOn) {
                                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                camera.setParameters(parameters);
                                camera.startPreview();
                                isOn = true;
                            } else {
                                parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                                camera.setParameters(parameters);
                                camera.stopPreview();
                                isOn = false;

                        }

                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    protected void onStop(){
        super.onStop();
        if(camera!=null){
            camera.release();
            camera=null;
        }
    }

    }

