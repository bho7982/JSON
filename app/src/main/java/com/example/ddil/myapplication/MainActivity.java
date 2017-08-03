package com.example.ddil.myapplication;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView txt_ID, txt_pwd, txt_name, txt_birthday;
    Button btn_id_check, btn_submit;
    EditText edt_ID, edt_pwd1, edt_pwd2, edt_frist_name, edt_last_name, edt_birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textview define
        txt_ID = (TextView) findViewById(R.id.txt_ID);
        txt_pwd = (TextView) findViewById(R.id.txt_pwd);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_birthday = (TextView) findViewById(R.id.txt_birthday);

        //button define
        btn_submit = (Button) findViewById(R.id.btn_submit);

        //edittext define
        edt_ID = (EditText) findViewById(R.id.edt_ID);
        edt_pwd1 = (EditText) findViewById(R.id.edt_pwd1);
        edt_pwd2 = (EditText) findViewById(R.id.edt_pwd2);
        edt_frist_name = (EditText) findViewById(R.id.edt_frist_name);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_birthday = (EditText) findViewById(R.id.edt_birthday);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //JsonTransfer는 Json데이터를 전송하는것. (아래에서 JsonTransfer라는 함수를 정의한다.)
                JsonTransfer data_transfer = new JsonTransfer();

                //JSONObject는 JSON을 만들기 위함.
                JSONObject json_dataTransfer = new JSONObject();
                try
                {
                    //json_dataTransfer에 ("키값" : "보낼데이터") 형식으로 저장한다.
                    json_dataTransfer.put("user_id", edt_ID.getText().toString());
                    json_dataTransfer.put("user_password", edt_pwd1.getText().toString());
                    json_dataTransfer.put("first_name", edt_frist_name.getText().toString());
                    json_dataTransfer.put("last_name", edt_last_name.getText().toString());
                    json_dataTransfer.put("user_birthday", edt_birthday.getText().toString());

                    //json_dataTransfer의 데이터들을 하나의 json_string으로 묶는다.
                    String json_string = json_dataTransfer.toString();

                    //보내야 할 곳 주소 정의
                    String url = "보내야할곳 주소!";

                    //보내기 전에 json_string 양 쪽 끝에 대괄호를 붙인다. (Object로 처리하기 때문이다. 만약 Array로 처리한다면, 대괄호는 필요없다고 한다.)
                    data_transfer.execute(url,"["+json_string+"]");

                    //Toast.makeText(getApplicationContext(), json_string, Toast.LENGTH_LONG);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public class JsonTransfer extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection;
            String data = params[1];
            String result = null;
            try {
                //Connect
                urlConnection = (HttpURLConnection) ((new URL(params[0]).openConnection()));
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(10000 /*milliseconds*/);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                //urlConnection.setRequestProperty("Content-Type", "application/json");
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                // urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
                //uid,mac,filename,time

                //Write
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(data);
                writer.close();
                outputStream.close();

                //Read
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();


                while ((line = bufferedReader.readLine()) != null)
                {
                    sb.append(line);
                }

                bufferedReader.close();
                result = sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}