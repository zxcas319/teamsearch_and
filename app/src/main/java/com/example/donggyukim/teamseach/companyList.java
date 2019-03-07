package com.example.donggyukim.teamseach;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;

public class companyList extends AppCompatActivity {

    List<String> companys;
    ListView listview;
    ListView listview2;

    ArrayAdapter adapter2;
    String cId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        Intent intent = getIntent();
        cId = intent.getStringExtra("id");



        companys = new ArrayList<>();

        adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, companys) ;
        new JSONTask2().execute("http://192.168.177.213:33018/company");//AsyncTask 시작시킴
        Button reform = (Button)findViewById(R.id.reform);
        reform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),reform.class);
                intent.putExtra("id",cId); /*송신*/
                startActivity(intent);
            }
        });
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),teamInfo.class);

                startActivity(intent);
            }
        });

    }

    public class JSONTask2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                // 두 번째 리스트 뷰에 넣을 정보
                jsonObject.accumulate("cid", cId);



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

                    String a = buffer.toString();

                    StringBuffer sb = new StringBuffer();
                    //a = buffer.toString();

                    //sb.append(a);
                    //String address = jObject.getInt("id");
                    JSONArray array = null;   // JSONArray 생성
                    try {
                        array = new JSONArray(a);
                        for(int i=0; i < array.length(); i++){
                            JSONObject jObject1 = null;  // JSONObject 추출
                            jObject1 = array.getJSONObject(i);
                            String login_id = jObject1.getString("login_id");
                            String phone = jObject1.getString("phone");
                            String ocassion = jObject1.getString("ocassion");
                            String email = jObject1.getString("email");

                            companys.add("아이디:"+login_id+"\n");
                            companys.add("폰번호:"+phone+"\n");
                            companys.add("직위:"+ocassion+"\n");
                            companys.add("email:"+email+"\n");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    //tmp="1";

                    //team1.setText(tmp);


                    /*
                    String str = buffer.toString();
                    JSONArray jarray = new JSONArray(str);
                    for(int i = 0 ; i<str.length(); i++){
                        JsonObj = jarray.getJSONObject(i);
                        String color = JsonObj.getString("id");
                        String number = JsonObj.getString("company");
                        String string = JsonObj.getString("up_company");

                    }*/

                    sb.append("안녕");
                    String test1=String.valueOf(array.length());
                    sb.append(test1);
                    String strtset=sb.toString();
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

            listview = (ListView) findViewById(R.id.listView2) ;
            listview.setAdapter(adapter2);


            //textView8.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }



    }

}
