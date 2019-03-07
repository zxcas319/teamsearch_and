package com.example.donggyukim.teamseach;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


public class detailUser extends AppCompatActivity {

    static String loginIdt;
    static String loginPwt;
    static String phonet;
    static String emailt;
    static String namet;
    static String ocassiont;

    static String conPwt;
    static String a;
    EditText loginId ;
    EditText loginPw ;
    EditText phone ;
    EditText email ;
    EditText name ;
    EditText ocassion;
    TextView t1;
    EditText pwConfirm;

    static String upw;
    static String uphone;
    static String uid;
    static String uname;
    static String uemail;
    static String uocassion;
    static String companyinfo;
    List<String> company;
    List<String> upCompany;
    List<String> tempUpCompany;
    List<String> company_id;

    static String cSelctText;
    static String upselctText;
    static String conselctText;

    static int idx;
    static String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        Intent intent = getIntent();




        uid = intent.getStringExtra("id");
        uphone = intent.getStringExtra("phone");
        uname = intent.getStringExtra("name");
        uemail = intent.getStringExtra("email");
        uocassion = intent.getStringExtra("ocassion");
        companyinfo = intent.getStringExtra("companyinfo");
        company = new ArrayList<>();
        upCompany = new ArrayList<>();
        tempUpCompany = new ArrayList<>();
        company_id = new ArrayList<>();
        company.add("부서를 등록하세요");
        upCompany.add("상위 부서를 등록하세요");
        JSONArray array = null;   // JSONArray 생성
        try {
            array = new JSONArray(companyinfo);
            for(int i=0; i < array.length(); i++){
                JSONObject jObject1 = null;  // JSONObject 추출
                jObject1 = array.getJSONObject(i);
                String companyv = jObject1.getString("company");

                String up_companyv =  jObject1.getString("up_company");


                String id = jObject1.getString("id");


                tempUpCompany.add(up_companyv);
                int a= tempUpCompany.size();
                for(int j=0;j<a;j++){

                    if(up_companyv.equals(tempUpCompany.get(j))){
                        upCompany.remove(up_companyv);
                        break;
                    }

                }

                //띄어쓰기 조심
                company.add(companyv);
                company_id.add(id);
                upCompany.add(up_companyv);
                //
                // companyList.add();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button sendInfo = (Button) findViewById(R.id.sendInfo);
        Button back = (Button) findViewById(R.id.back);
        TextView t1 = (TextView) findViewById(R.id.textView8);

        //t1.setText(uphone);
        name = (EditText) findViewById(R.id.name);
        name.setText(uname);
        phone = (EditText) findViewById(R.id.phone);
        phone.setText(uphone);
        email = (EditText) findViewById(R.id.up_company);
        email.setText(uemail);
        ocassion = (EditText) findViewById(R.id.ocassion);
        if(uocassion!=null && !uocassion.equals("null")&&!uocassion.equals("NULL") ){
        ocassion.setText(uocassion);}
        else
            ocassion.setText("");
        JSONTaskD jt=new JSONTaskD();
        jt.execute("http://192.168.177.213:33018/detailUser");//AsyncTask 시작시킴

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), admin_select.class);
                startActivity(intent);
            }
        });

        sendInfo.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {



                namet = name.getText().toString();
                phonet = phone.getText().toString();
                emailt = email.getText().toString();
                ocassiont = ocassion.getText().toString();
                    if (!ocassiont.equals("")) {
                        //update 정보의 jsonTaskU 로 명명 하기
                        new JSONTaskU().execute("http://192.168.177.213:33018/UpdateDetailUser");//AsyncTask 시작시킴
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "직위를 입력하세요!.", Toast.LENGTH_LONG).show();
                    }

            }
        });
        //company spiner


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, company);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner company_spinner;
        company_spinner = (Spinner) findViewById(R.id.company_spinner);
        company_spinner.setAdapter(adapter1);

        company_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cSelctText = company_spinner.getSelectedItem().toString();
                idx=company.indexOf(company.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //upcompany spiner

        /*
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, upCompany);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner up_spinner;
        up_spinner = (Spinner) findViewById(R.id.up_spinner);
        up_spinner.setAdapter(adapter2);

        up_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                upselctText = up_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
*/
        //confirm spinner
        final Spinner con_spinner;
        con_spinner = (Spinner) findViewById(R.id.con_spinner);

        final String[] ar = new String[]{"사용자 승인 : 1, 관리자 임명 : 2", "0", "1", "2"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ar);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        con_spinner.setAdapter(adapter3);
        con_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                conselctText = con_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }





    public class JSONTaskD extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", uid);


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
                            uphone = jObject1.getString("phone");
                            uemail = jObject1.getString("email");
                            uname = jObject1.getString("name");



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
            //t1.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }



    }

    //업데이트 정보
    public class JSONTaskU extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", namet);
                jsonObject.accumulate("phone", phonet);
                jsonObject.accumulate("email", emailt);
                jsonObject.accumulate("ocassion", ocassiont);
                jsonObject.accumulate("id", uid);
                jsonObject.accumulate("company", cSelctText);
                jsonObject.accumulate("up_company", upselctText);
                jsonObject.accumulate("confirm", conselctText);
                cid=company_id.get(idx-1);
                jsonObject.accumulate("company_id", cid);
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
            //t1.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }



    }
}
