package com.example.donggyukim.teamseach;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class admin_select extends AppCompatActivity {

    static String confirm;
    static String a;
    static String loginId;

    static String phone;
    static String email;
    static String name;
    static String loginPw;

    List<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select);
        Intent intent = getIntent();
        //클릭 이벤트 전에 먼저 실행을 해버리면 된다.
        new JSONTaskas().execute("http://192.168.177.213:33018/viewUser");//AsyncTask 시작시킴

        Button selectInfo =(Button)findViewById(R.id.selectInfo);
        selectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //부서관리
                Intent intent =new Intent(getApplicationContext(),companyAdmin.class);
                startActivity(intent);
            }
        });

        users = new ArrayList<>();

        Button updateInfo =(Button)findViewById(R.id.updateInfo);
        updateInfo.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), viewUser.class);
                intent.putExtra("userinfo", a); /*송신*/
                startActivity(intent);


            }
        });


    }
    void doJSONParser() {


        try {
            JSONArray array = new JSONArray(a);// JSONArray 생성
            for (int i = 0; i < array.length(); i++) {
                JSONObject jObject1 = null;  // JSONObject 추출
                jObject1 = array.getJSONObject(i);
                phone = jObject1.getString("phone");
                loginPw = jObject1.getString("login_pw");
                email = jObject1.getString("email");
                name = jObject1.getString("name");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class JSONTaskas extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                // 리스트 뷰에 넣을 정보





                HttpURLConnection con = null;
                BufferedReader reader = null;

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



                return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //a=result;


            //textView8.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }



    }//jsontask end
}
