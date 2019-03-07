package com.example.donggyukim.teamseach;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class companyDelete extends AppCompatActivity {
    String companyinfo;
    TextView t1;
    static String company;
    static String up_company;
    List<String> companyList;
    List<String> compList;
    ListView listview;
    static String von;
    ArrayAdapter adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_delete);
        listview = (ListView) findViewById(R.id.listView) ;

        Intent intent = getIntent();
        companyinfo = intent.getStringExtra("companyinfo");

        companyList = new ArrayList<>();
        compList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(companyinfo);// JSONArray 생성
            for (int i = 0; i < array.length(); i++) {
                JSONObject jObject1 = null;  // JSONObject 추출
                jObject1 = array.getJSONObject(i);

                String viewCompany = jObject1.getString("company");
                String viewUp_company = jObject1.getString("up_company");


                int a= compList.size();

                compList.add(viewCompany);
                compList.add(viewUp_company);

                for(int j=0;j<a;j++){

                    if(viewUp_company.equals(compList.get(j))){
                        companyList.remove("*상위: "+viewUp_company);
                        break;
                    }

                }
                companyList.add("*상위: "+viewUp_company);
                companyList.add("부서: "+viewCompany);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, companyList) ;

        listview.setAdapter(adapter1);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s= (String) listview.getSelectedItem();
                //클릭한 리스트 뷰의 텍스트 가져오는 방

                String vo = (String) parent.getAdapter().getItem(position);
                int idx=companyList.indexOf(companyList.get(position));
                adapter1.remove(idx);
                adapter1.notifyDataSetChanged();
                listview.setAdapter(adapter1);
                // 스트링 타겟
                String target = "부서"; //+3
                String targetu = "*"; //+5
                String targetuu = "상위"; //+3

                //부서정보
                if(vo.contains(target)){
                    int target_num = vo.indexOf(target);
                     von= vo.substring(target_num+4,vo.length());
                     new JSONTask().execute("http://192.168.177.213:33018/companyDelete");//AsyncTask 시작시킴

                }
                //상위부서정보
                else if(vo.contains(targetu)){
                    int target_num = vo.indexOf(targetuu);
                     von= vo.substring(target_num+4,vo.length());
                     new JSONTask().execute("http://192.168.177.213:33018/upCompanyDelete");//AsyncTask 시작시킴


                }
                Intent intent =new Intent(getApplicationContext(),companyAdmin.class);
                startActivity(intent);

            }
        });


    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("delInfo", von);

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
