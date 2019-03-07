package com.example.donggyukim.teamseach;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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



public class updateInfo extends AppCompatActivity {

    static String loginIdt;
    static String loginPwt;
    static String phonet;
    static String emailt;
    static String ocassiont;
    static String namet;

    static String conPwt;

    EditText loginId ;
    EditText loginPw ;
    EditText phone ;
    EditText email ;
    EditText name ;
    TextView t1;
    EditText pwConfirm;
    static String uid;
    static String upw;
    static String uphone;
    static String uname;
    static String uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        Intent intent = getIntent();

        uid = intent.getStringExtra("loginId");
        upw = intent.getStringExtra("loginPw");
        uphone = intent.getStringExtra("phone");
        uname = intent.getStringExtra("name");
        uemail = intent.getStringExtra("email");




        Button sendInfo = (Button)findViewById(R.id.sendInfo);
        Button back = (Button)findViewById(R.id.back);
        TextView t1 = (TextView)findViewById(R.id.textView8);

        //t1.setText(uphone);

        name = (EditText) findViewById(R.id.name);
        name.setText(uname);

        phone = (EditText) findViewById(R.id.phone);
        phone.setText(uphone);

        email = (EditText) findViewById(R.id.up_company);
        email.setText(uemail);

        loginPw = (EditText) findViewById(R.id.loginPw);
        loginPw.setText(upw);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),select.class);
                startActivity(intent);
            }
        });

        sendInfo.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                loginPw = (EditText) findViewById(R.id.loginPw);
                loginPwt = loginPw.getText().toString();

                pwConfirm = (EditText) findViewById(R.id.e);
                conPwt = pwConfirm.getText().toString();

                namet = name.getText().toString();
                phonet = phone.getText().toString();
                emailt = email.getText().toString();

                if(!loginPwt.equals("")) {
                    new JSONTask().execute("http://192.168.177.213:33018/user");//AsyncTask 시작시킴

                }
                else {
                    Toast.makeText(getApplicationContext(), "변경할 비밀 번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("login_id", uid);
                jsonObject.accumulate("login_pw", loginPwt);
                jsonObject.accumulate("phone", phonet);

                jsonObject.accumulate("email", emailt);
                jsonObject.accumulate("name", namet);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("PUT");//POST방식으로 보냄
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
            //t1.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }



    }
}
