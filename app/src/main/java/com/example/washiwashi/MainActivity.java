package com.example.washiwashi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.graphics.drawable.AnimationDrawable;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView img;
    private EditText userF;
    private Button main_btn;
    private TextView ResultW;
    private TextView ResultC;
    private RelativeLayout gradient;
    private GifImageView GifA;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gradient = findViewById(R.id.gradient);
        AnimationDrawable animationDrawable = (AnimationDrawable) gradient.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        img = (ImageView)findViewById(R.id.imageView1);
        userF = findViewById(R.id.userF);
        main_btn = findViewById(R.id.main_btn);
        ResultW = findViewById(R.id.ResultW);
        ResultC = findViewById(R.id.ResultC);
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userF.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_user, Toast.LENGTH_LONG).show();
                else {
                    String city = userF.getText().toString();
                    String key = "47f1e72f28121dcd6bab4e5788b83324";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid="+ key +"&units=metric&lang=ru";

                    new UrlData().execute(url);

                }
                //main_btn.setOnClickListener((View.OnClickListener) GifA);
            }
        });
    }

    private class UrlData extends AsyncTask < String, String, String > {

        protected void onPreExecute() {
            super.onPreExecute();
            ResultW.setText("Жди...");

            super.onPreExecute();
            ResultC.setText("Жди...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "0";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("/n");

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
                ResultW.setText("Температура : " + obj.getJSONObject("main").getDouble("temp") + " °C");
                ResultC.setText("Погода: " + obj.getJSONArray("weather").getJSONObject(0).getString("description"));
                image(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        public void image(JSONObject obj) {

                        try {
                            if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("небольшой снег")) {
                                img.setImageResource(R.drawable.snowy);
                            } else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("солнечно")) {
                                img.setImageResource(R.drawable.sunny);
                            }else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("ясно")) {
                                img.setImageResource(R.drawable.sunny);
                            } else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("облачно")) {
                                img.setImageResource(R.drawable.cloudy);
                            } else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("пасмурно")) {
                                img.setImageResource(R.drawable.cloudy);
                            } else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("переменная облачность")) {
                                img.setImageResource(R.drawable.cloudy);
                            } else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("облачно с проясненияи")) {
                                img.setImageResource(R.drawable.cloudy);
                            } else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("дождь")) {
                                img.setImageResource(R.drawable.showers);
                            }else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("небольшой дождь")) {
                                img.setImageResource(R.drawable.showers);
                            } else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("снег")) {
                                img.setImageResource(R.drawable.snowy);
                            } else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("туман")) {
                                img.setImageResource(R.drawable.fog);
                            }else if (obj.getJSONArray("weather").getJSONObject(0).getString("description").equals("дымка")) {
                                img.setImageResource(R.drawable.fog);
                            } else {
                                System.out.println("Неверная оценка");
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


        }
    }


}
