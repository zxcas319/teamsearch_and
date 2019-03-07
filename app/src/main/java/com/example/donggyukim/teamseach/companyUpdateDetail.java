package com.example.donggyukim.teamseach;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class companyUpdateDetail extends AppCompatActivity {



    static String phonet;
    static String up_companyt;
    static String companyt;



    static String a;


    EditText up_company ;
    EditText company ;

    static String cup_company;
    static String ccompany;

    static String tempcompany;
    static String tempup_company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_update_detail);

        Intent intent = getIntent();

        ccompany = intent.getStringExtra("company");
        cup_company = intent.getStringExtra("up_company");

        Button sendInfo = (Button) findViewById(R.id.sendInfo);
        Button back = (Button) findViewById(R.id.back);


        //t1.setText(uphone);
        company = (EditText) findViewById(R.id.company);
        company.setText(ccompany);
        up_company = (EditText) findViewById(R.id.up_company);
        up_company.setText(cup_company);

        tempcompany=ccompany;
        tempup_company=cup_company;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), companyAdmin.class);
                startActivity(intent);
            }
        });

        sendInfo.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                JSONTaskD jt=new JSONTaskD();
                jt.execute("http://192.168.177.213:33018/UpdateDetailCompany");//AsyncTask 시작시킴
            }
        });

    }





    public class JSONTaskD extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("up_company", up_company.getText().toString());
                jsonObject.accumulate("company", company.getText().toString());
                jsonObject.accumulate("standard", tempcompany);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    a=buffer.toString();

                    try {
                        JSONArray array = new JSONArray(a);// JSONArray 생성
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jObject1 = null;  // JSONObject 추출
                            jObject1 = array.getJSONObject(i);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }



    }
}
