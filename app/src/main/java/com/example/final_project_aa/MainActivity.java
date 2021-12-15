package com.example.final_project_aa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private int circleRadius = 30;
    private float circleX;
    private float circleY;
    private Timer timer,counter;
    private Handler handler;
    float sensorX,sensorY,sensorZ;
    long lastSensorUpdateTime = 0 ;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int screenWidth, screenHeight;
    private ImageView ball, hole;
    private FrameLayout frameLayout;
    AnimationDrawable callAnimation;
    private float score = 500;
    DBHelper dbHelper;
    private SharedPreference sharedPreference;
    Activity context = this;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String TAG = "Current Location";

    private float latitude,longitude;
    private int count = 0;
    private static int COUNTER = 1000;
    int begin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        ball=findViewById(R.id.ball);
        hole=findViewById(R.id.hole);
        frameLayout=findViewById(R.id.frame);
        sharedPreference = new SharedPreference();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
        sensorManager =(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        dbHelper = new DBHelper(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        circleX = screenWidth / 2 + 100;
        circleY = screenHeight / 2 + 200;

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(sensorX < 0 ){
                    circleX += 5;
                }else {
                    circleX -= 5;
                }if (sensorY > 0 ){
                    circleY += 5;
                }else {
                    circleY -= 5;
                }
                ball.setX(circleX);
                ball.setY(circleY);
                collision();
                handler.sendEmptyMessage(0);
            }
        },0,25);
        counter = new Timer();
        counter.schedule(new TimerTask() {
            @Override
            public void run() {
                count++;
            }
        },begin,COUNTER);
        getCurrentLocation();
    }




    public void collision() {
        //Ball and Border
        float borderX = screenWidth - 100;
        float borderY = screenHeight - 100;

        if (ball.getX() >= borderX || ball.getY() >= borderY) {
            System.out.println("Hit Border");
        }
        //Ball and Hole Collision
        Rect rect =new Rect();
        ball.getHitRect(rect);

        Rect rect1 = new Rect();
        hole.getHitRect(rect1);

        Rect rect2 = new Rect();
        frameLayout.getHitRect(rect2);

        if(Rect.intersects(rect, rect1)){
            //Toast.makeText(this, "hoho", Toast.LENGTH_SHORT).show();
            System.out.println("Hit Hole");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    frameLayout.removeView(ball);
                    hole.setBackgroundResource(R.drawable.animation);
                    callAnimation = (AnimationDrawable)hole.getBackground();
                    callAnimation.start();
                    String s = String.valueOf(score-count);
                    String lat= String.valueOf(latitude);
                    String lo= String.valueOf(longitude);
                    System.out.println("llllllllllllll"+lat+lo);
                    dbHelper.update(sharedPreference.getValue(context), s,lat,lo);
                    Intent i = new Intent(MainActivity.this,GameOverActivity.class);
                    startActivity(i);
                }
            });
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSensorUpdateTime > 100){
                lastSensorUpdateTime = currentTime;
                sensorX = event.values[0];
                sensorY = event.values[1];
                sensorZ = event.values[2];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(){
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                System.out.println("weeeeeeeeeeeeee");
                Location location =task.getResult();
                if(location != null){
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        Log.d(TAG,"Latitude" +addresses.get(0).getLatitude());
                        Log.d(TAG,"Longitude" +addresses.get(0).getLongitude());
                        Log.d(TAG,"Country" +addresses.get(0).getCountryName());
                        Log.d(TAG,"Locality" +addresses.get(0).getLocality());
                        Log.d(TAG,"Address" +addresses.get(0).getAddressLine(0));
                        latitude= (float) addresses.get(0).getLatitude();
                        longitude= (float) addresses.get(0).getLongitude();
                        //Toast.makeText(context, "LO5555555555"+latitude+longitude, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }
            }
        });
    }

}