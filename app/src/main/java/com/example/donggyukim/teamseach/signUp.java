package com.example.donggyukim.teamseach;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signUp extends AppCompatActivity {

    DatabaseHelper myDb;
    DatabaseHelper1 myDb1;
    EditText editName,editSurname,editMarks ,editTextId,editText_phone;
    Button btnAddData;
    Button btnAddCompany;
    Button btnviewAll;
    Button btnviewAll1;
    Button btnDelete;
    Button btnDelete1;
    Button btn_result;
    Button btnviewUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        myDb = new DatabaseHelper(this);
        myDb1 = new DatabaseHelper1(this);

        editName = (EditText)findViewById(R.id.editText_name);
        editSurname = (EditText)findViewById(R.id.editText_surname);
        editMarks = (EditText)findViewById(R.id.editText_Marks);
        editTextId = (EditText)findViewById(R.id.editText_id);
        editText_phone= (EditText)findViewById(R.id.editText_name2);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnAddCompany = (Button)findViewById(R.id.btnAddCompany);

        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        btnviewAll1 = (Button)findViewById(R.id.btnviewAll1);
        btnviewUpdate= (Button)findViewById(R.id.button_update);
        btnDelete= (Button)findViewById(R.id.button_delete);
        btnDelete1= (Button)findViewById(R.id.button_delete1);
        btn_result=(Button)findViewById(R.id.btn_result);
        AddData();
        AddCompany();
        viewAll();
        viewAll1();
        UpdateData();
        DeleteData();
        DeleteData1();

        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),teamInfo.class);
                startActivity(intent);
            }
        });

    }
    public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                        if(deletedRows > 0)
                            Toast.makeText(signUp.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(signUp.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void DeleteData1() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb1.deleteData(editTextId.getText().toString());

                        if(deletedRows > 0)
                            Toast.makeText(signUp.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(signUp.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void UpdateData() {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
                                editName.getText().toString(),
                                editSurname.getText().toString(),editMarks.getText().toString());
                        if(isUpdate == true)
                            Toast.makeText(signUp.this,"Data Update",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(signUp.this,"Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public  void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(editName.getText().toString(),
                                editSurname.getText().toString() );
                        if(isInserted == true)
                            Toast.makeText(signUp.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(signUp.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public  void AddCompany() {
        btnAddCompany.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb1.insertData(

                                editMarks.getText().toString(),
                                editText_phone.getText().toString()
                                );
                        if(isInserted == true)
                            Toast.makeText(signUp.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(signUp.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :"+ res.getString(1)+"\n");
                            buffer.append("pw :"+ res.getString(2)+"\n");
                        }

                        // Show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }

    public void viewAll1() {
        btnviewAll1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb1.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("COMPANY :"+ res.getString(1)+"\n");
                            buffer.append("PHONE :"+ res.getString(2)+"\n");

                        }

                        // Show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
