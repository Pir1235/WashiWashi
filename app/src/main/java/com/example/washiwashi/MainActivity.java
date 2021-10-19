package com.example.washiwashi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText userF;
    private Button main_btn;
    private TextView ResultW;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userF = findViewById(R.id.userF);
        main_btn = findViewById(R.id.main_btn);
        ResultW = findViewById(R.id.ResultW);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userF.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_user, Toast.LENGTH_LONG).show();
                else {
                    String city = userF.getText().toString();
                    String key = "47f1e72f28121dcd6bab4e5788b83324";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid="+ key +"&units=metric&lang=ru";

                    new UrlData().execute();
                }
            }
        });
    }

    private class UrlData extends AsyncTask < String, String, String > {

        protected void onPreExecute() {
            super.onPreExecute();
            ResultW.setText("Жди...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection =(HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "0";

                while((line = reader.readLine()) != null)
                    buffer.append(line).append("/n");

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
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
        protected void onPostExecute(String result){
           super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
                ResultW.setText("Температура: " + obj.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}