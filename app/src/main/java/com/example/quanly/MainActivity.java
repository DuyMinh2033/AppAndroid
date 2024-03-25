package com.example.quanly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtmasp, edttensp, edtsoluong;
    Button btninsert, btndelete, btnupdate, btnquery;

    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtmasp = findViewById(R.id.edtmasp);
        edttensp = findViewById(R.id.edttensp);
        edtsoluong = findViewById(R.id.edtsoluong);
        btninsert = findViewById(R.id.btninsert);
        btndelete = findViewById(R.id.btndelete);
        btnupdate = findViewById(R.id.btnupdate);
        btnquery = findViewById(R.id.btnquery);
// Tạo ListView
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);
// Tạo và mở Cơ sở dữ liệu Sqlite
        mydatabase = openOrCreateDatabase("qlsinhvien.db", MODE_PRIVATE, null);
// Tạo Table để chứa dữ liệu
        try {
            String sql = "CREATE TABLE tbllop(masp TEXT primary key,tensp TEXT, soluong INTEGER)";
            mydatabase.execSQL(sql);
        } catch (Exception e) {
            Log.e("Error", "Table đã tồn tại");
        }
        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masp = edtmasp.getText().toString();
                String tensp = edttensp.getText().toString();
                int soluong = Integer.parseInt(edtsoluong.getText().toString());
                ContentValues myvalue = new ContentValues();
                myvalue.put("masp", masp);
                myvalue.put("tensp", tensp);
                myvalue.put("soluong", soluong);
                String msg = "";
                if (mydatabase.insert("tbllop", null, myvalue) == -1) {
                    msg = "Fail to Insert Record!";
                } else {
                    msg = "Insert record Sucessfully";
                }
                Toast.makeText(MainActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masp = edtmasp.getText().toString(); // Sử dụng biến masp thay vì malop
                int n = mydatabase.delete("tbllop", "masp = ?", new String[]{masp}); // Sử dụng masp làm điều kiện
                String msg = "";
                if (n == 0) {
                    msg = "No record to Delete";
                } else {
                    msg = n + " record is deleted";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masp = edtmasp.getText().toString(); // Sử dụng biến masp thay vì malop
                String tensp = edttensp.getText().toString(); // Cập nhật tên sản phẩm
                int soluong = Integer.parseInt(edtsoluong.getText().toString()); // Cập nhật số lượng
                ContentValues myvalue = new ContentValues();
                myvalue.put("tensp", tensp);
                myvalue.put("soluong", soluong);
                int n = mydatabase.update("tbllop", myvalue, "masp = ?", new String[]{masp}); // Sử dụng masp làm điều kiện
                String msg = "";
                if (n == 0) {
                    msg = "No record to Update";
                } else {
                    msg = n + " record is updated";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylist.clear();
                Cursor c =
                        mydatabase.query("tbllop", null, null, null, null, null, null);
                c.moveToNext();
                String data = "";
                while (c.isAfterLast() == false) {
                    data = c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2);
                    c.moveToNext();
                    mylist.add(data);
                }
                c.close();
                myadapter.notifyDataSetChanged();
            }
        });
    }}