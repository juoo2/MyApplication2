package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import android.app.ProgressDialog;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    static Handler han;
    static Handler han2;
    private boolean positionFlag;
    LatLng w;
    public static double xandyx=0;
    public static double xandyy=0;



    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        final TextView textView4 = (TextView) findViewById(R.id.textview3);



    }



        public void thrun(){


            Thread thread= new Thread(new Runnable() {
                private boolean positionFlag;
                private void getPoint(String... addr) {
                    geo geo = new geo(MainActivity.this, listener);
                    geo.execute(addr);
                }

                private geo.OnGeoPointListener listener = new geo.OnGeoPointListener() {
                    @Override
                    public void onPoint(geo.Point[] p) {
                        int sCnt = 0;
                        for (geo.Point point : p) {

                            if (point.havePoint) sCnt++;
                            final String a= point.toString();
                            double k=  point.x;
                            double d=  point.y;
                            xandyx=point.x;
                            xandyy=point.y;

                            Message msg=han.obtainMessage();
                            Message msg2=han2.obtainMessage();
                            msg.obj=Double.toString(k);
                            msg2.obj=Double.toString(d);
                            han.sendMessage(msg);
                            han2.sendMessage(msg2);





                            final TextView textView=(TextView)findViewById(R.id.textview);
                            textView.setText(a);




                        }

                    }

                    @Override
                    public void onProgress(int progress, int max) {

                    }
                };
                @Override
                public void run() {
                    final EditText editText=(EditText)findViewById(R.id.edittext);
                    final String c=editText.getText().toString();
                    getPoint(
                            c

                    );


                }


            });

            thread.start();



        }
    @UiThread
    @Override
    public void onMapReady ( @NonNull final NaverMap naverMap){
       final  Marker marker1 = new Marker();
        findViewById(R.id.run1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thrun();
                final TextView textView2 = (TextView) findViewById(R.id.textview1);
                final TextView textView3 = (TextView) findViewById(R.id.textview2);

                han = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        textView2.setText(msg.obj + "");
                    }
                };
                han2 = new Handler() {
                    @Override
                    public void handleMessage(Message msg2) {
                        super.handleMessage(msg2);
                        textView3.setText(msg2.obj + "");
                    }
                };

                LatLng k=  new LatLng((double)xandyy, (double)xandyx);

                marker1.setPosition(k);
                marker1.setMap(naverMap);
                naverMap.moveCamera(CameraUpdate.scrollTo(k));



            }



        });



    }
        }
















