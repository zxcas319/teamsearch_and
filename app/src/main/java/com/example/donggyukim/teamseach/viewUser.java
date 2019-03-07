package com.example.donggyukim.teamseach;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class viewUser extends AppCompatActivity {
    static String userinfo;
    ListView listview;
    ArrayAdapter adapter1;
    List<String> users;
    List<String> infoList;
    List<String> ocassionList;
    static String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_user);
        Intent intent = getIntent();
        userinfo = intent.getStringExtra("userinfo");
        super.onCreate(savedInstanceState);

        new JSONTaskas().execute("http://192.168.177.213:33018/selectCompany");//AsyncTask 시작시킴

        users = new ArrayList<>();
        infoList = new ArrayList<>();
        ocassionList = new ArrayList<>();
        listview = (ListView) findViewById(R.id.listView1) ;
        JSONArray array = null;   // JSONArray 생성
        try {
            array = new JSONArray(userinfo);
            for(int i=0; i < array.length(); i++){
                JSONObject jObject1 = null;  // JSONObject 추출
                jObject1 = array.getJSONObject(i);
                String name = jObject1.getString("name");

                String ocassion = jObject1.getString("ocassion");

                String nextName  =  jObject1.getString("name");
                String nextEmail =  jObject1.getString("email");
                String nextPhone =  jObject1.getString("phone");
                String company =jObject1.getString("company");
                String up_company = jObject1.getString("up_company");

                String id = String.valueOf(jObject1.getInt("id"));

                //띄어쓰기 조심
                users.add("이름:"+name+"\t"+"사번:"+id+"부서:"+company);
                infoList.add("^"+nextName+"&"+nextEmail+"*"+nextPhone+"!"+ocassion);
                //
                ocassionList.add(ocassion);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users) ;

        listview.setAdapter(adapter1);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s= (String) listview.getSelectedItem();
                //클릭한 리스트 뷰의 텍스트 가져오는 방
                String vo = (String) parent.getAdapter().getItem(position);

                int idx=users.indexOf(users.get(position));


                String target = "사번";
                int target_num = vo.indexOf(target);
                String targetCompany = "부서";
                int target_Cnum = vo.indexOf(targetCompany);
                String von; von= vo.substring(target_num+3,target_Cnum);

                Intent intent =new Intent(getApplicationContext(),detailUser.class);

                intent.putExtra("id",von); /*송신*/

                String fullList= infoList.get(idx);
                String targetn = "^";
                String targete = "&";
                String targetp = "*";
                String targeto = "!";



                int n_num = fullList.indexOf(targetn);
                int e_num = fullList.indexOf(targete);
                int p_num = fullList.indexOf(targetp);
                int o_num = fullList.indexOf(targeto);
                String subListn; subListn= fullList.substring(n_num+1,e_num);
                String subListp; subListp= fullList.substring(e_num+1,p_num);
                String subListe; subListe= fullList.substring(p_num+1,o_num);
                String subListo; subListo= fullList.substring(o_num+1,fullList.length());
                intent.putExtra("name",subListn); /*송신*/
                intent.putExtra("email",subListp); /*송신*/
                intent.putExtra("phone",subListe); /*송신*/
                intent.putExtra("ocassion",subListo); /*송신*/
                intent.putExtra("companyinfo",a); /*송신*/


                startActivity(intent);
            }
        });

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
