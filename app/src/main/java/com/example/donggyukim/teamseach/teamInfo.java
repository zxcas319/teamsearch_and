package com.example.donggyukim.teamseach;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class teamInfo extends AppCompatActivity {
    String companyinfos;
    TextView t1;
    List<String> companyList;
    List<String> upCompanyList;
    List<String> tempCompList;
    List<String> compList;
    List<String> spinnercompany;
    ListView listview;
    ArrayAdapter adapter1;
    ArrayAdapter adapter2;

    static String cSelctText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_update);
        Intent intent = getIntent();
        JSONTaskI jt=new JSONTaskI();
        jt.execute("http://192.168.177.213:33018/selectCompany");
        companyList = new ArrayList<>();
        upCompanyList = new ArrayList<>();
        compList = new ArrayList<>();
        tempCompList = new ArrayList<>();
        spinnercompany = new ArrayList<>();


        upCompanyList.add("상위 부서를 선택하세요");


        //스피너 코드
        final Spinner spinner;
        spinner = (Spinner)findViewById(R.id.spinner);



        //스피너

        adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, upCompanyList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        //리스트뷰
        adapter2= new ArrayAdapter(this, android.R.layout.simple_list_item_1, compList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cSelctText = spinner.getSelectedItem().toString();

                int a= companyList.size();
                compList.clear();

                for(int j=0;j<a;j++){

                    if(companyList.get(j).contains(cSelctText)){

                        compList.add(spinnercompany.get(j));
                    }
                }


                if(compList.size()!=0)
                    if(!cSelctText.equals("상위 부서를 선택하세요"))
                    {
                        listview = (ListView) findViewById(R.id.listview);
                        listview.setAdapter(adapter2);


                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });





    }

    public class JSONTaskI extends AsyncTask<String, String, String> {

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
                companyinfos=buffer.toString();



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
            try {
                JSONArray array = new JSONArray(companyinfos);// JSONArray 생성
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jObject1 = null;  // JSONObject 추출
                    jObject1 = array.getJSONObject(i);

                    String viewCompany = jObject1.getString("company");
                    String viewUp_company = jObject1.getString("up_company");

                    tempCompList.add(viewUp_company);
                    for(int j=0;j<tempCompList.size();j++){
                        if(viewUp_company.equals(tempCompList.get(j))){
                            upCompanyList.remove(viewUp_company);
                        }
                    }

                    companyList.add(viewCompany+"*"+viewUp_company);
                    upCompanyList.add(viewUp_company);
                    spinnercompany.add(viewCompany);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //textView8.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }



    }//jsontask end

}
