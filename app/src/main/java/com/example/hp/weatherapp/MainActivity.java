package com.example.hp.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultTextView;
    JSONArray arr;
    JSONArray temp_arr;

    public void checkWeather(View view){
        DownloadTask DT=new DownloadTask();
        DT.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
        Log.i("lets-check","https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }
            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"we couldn't find the weather -do in background",Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                JSONObject TempMax=jsonObject.getJSONObject("main");
                String temp=TempMax.getString("temp_min");


                 arr = new JSONArray(weatherInfo);

                String message="";


                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main");


                    String description = jsonPart.getString("description");

                    if (!main.equals("") && !description.equals("")) {

                        message = main + " : " + description +"\r\n";


                    }
                }

                if(!message.equals("")){

                    resultTextView.setText(message+"\r\n"+"Temp : "+temp);

                }
                else{

                    Toast.makeText(getApplicationContext(),"we couldn't find the weather-on post execute-else-wala",Toast.LENGTH_SHORT).show();

                }



            }
            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"we couldn't find the weather-on post execute-catch",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        resultTextView=(TextView)findViewById(R.id.resultTextView);


    }
}
