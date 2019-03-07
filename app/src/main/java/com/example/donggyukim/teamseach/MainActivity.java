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

public class MainActivity extends AppCompatActivity {

    TextView tvData;
    static EditText id;
    static EditText pw;
    static String occasion;
    static String confirm;
    static String a;
    public class JSONTaskM extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                // 리스트 뷰에 넣을 정보
                jsonObject.accumulate("id", id.getText().toString());
                jsonObject.accumulate("pw", pw.getText().toString());

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
            a=result;

            if(!a.equals("") && a.equals("1")) {
                //서버에서 confirm = 1 이고 해당 id의 pw가 일치할 때 가져오게 처리

                Intent intent = new Intent(getApplicationContext(), select.class);
                intent.putExtra("id", id.getText().toString()); /*송신*/
                intent.putExtra("pw", pw.getText().toString()); /*송신*/
                intent.putExtra("confirm", a); /*송신*/
                startActivity(intent);
            }
            else if (!a.equals("") && a.equals("2")){
                Intent intent = new Intent(getApplicationContext(), admin_select.class);
                intent.putExtra("id", id.getText().toString()); /*송신*/
                intent.putExtra("pw", pw.getText().toString()); /*송신*/
                intent.putExtra("confirm", a); /*송신*/
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "등록되지 않은 아이디이거나 비밀번호가 틀립니다.", Toast.LENGTH_LONG).show();
            }
            tvData.setText(a);//서버로 부터 받은 값을 출력해주는 부분

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        id=(EditText)findViewById(R.id.idInput);
        pw=(EditText)findViewById(R.id.pwInput);
        TextView enroll =(TextView) findViewById(R.id.enroll);
        tvData = (TextView)findViewById(R.id.textView4);
        Button loginButton=(Button)findViewById(R.id.loginBtn);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JSONTaskM().execute("http://192.168.177.213:33018/main");//AsyncTask 시작시킴

            }
        });

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),enroll.class);
                intent.putExtra("id",id.getText().toString()); /*송신*/
                intent.putExtra("pw",pw.getText().toString()); /*송신*/
                startActivity(intent);
            }
        });

    }



}
