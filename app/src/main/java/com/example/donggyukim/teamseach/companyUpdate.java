package com.example.donggyukim.teamseach;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class companyUpdate extends AppCompatActivity {
    String companyinfo;
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
        companyinfo = intent.getStringExtra("companyinfo");
        companyList = new ArrayList<>();
        upCompanyList = new ArrayList<>();
        compList = new ArrayList<>();
        tempCompList = new ArrayList<>();
        spinnercompany = new ArrayList<>();


        upCompanyList.add("상위 부서를 선택하세요");
        try {
            JSONArray array = new JSONArray(companyinfo);// JSONArray 생성
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

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String s = (String) listview.getSelectedItem();
                            //클릭한 리스트 뷰의 텍스트 가져오는 방

                            String vo = (String) parent.getAdapter().getItem(position);

                            Intent intent = new Intent(getApplicationContext(), companyUpdateDetail.class);
                            intent.putExtra("up_company", cSelctText); /*송신*/
                            intent.putExtra("company", vo); /*송신*/
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });





    }


}
