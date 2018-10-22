package com.example.note;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Main2Activity extends Activity {
    Uri uri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button btnBack=(Button)findViewById(R.id.back);
        Button btnSave=(Button)findViewById(R.id.save);
        Button btnPicture=(Button)findViewById(R.id.button);
        final EditText text1=(EditText)findViewById(R.id.editText1);
        final EditText text2=(EditText)findViewById(R.id.editText4);
        //打开或创建test.db数据库
        final SQLiteDatabase db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
//        db.execSQL("DROP TABLE IF EXISTS note");
        //创建note表
//        db.execSQL("CREATE TABLE note (_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, text VARCHAR ,date VARCHAR,picture VARCHAR,video VARCHAR,music VARCHAR )");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this ,MainActivity.class);
                startActivity(i);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=text1.getText().toString();
                String detail=text2.getText().toString();
                Log.e("myTag",title);
                operate(db,title,detail,uri);
                Intent i = new Intent(Main2Activity.this ,MainActivity.class);
                startActivity(i);
            }
        });
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建File对象，用于存储选择的照片
                Log.e("mytag","111111111");
                System.out.println("1111111");
                File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                System.out.println("1111111");
                startActivityForResult(intent, 2);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                ContentResolver resolver = getContentResolver();
                uri = data.getData();
            }

        }
    }

    public void operate(SQLiteDatabase db,String title,String text,Uri uri){
        Note person = new Note();
        person.title = title;
        person.text = text;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        person.date=df.format(new Date());// new Date()为获取当前系统时间
        //插入数据
        if(uri==null)
            db.execSQL("INSERT INTO note VALUES (NULL, ?, ?,?,NULL,NULL,NULL)", new Object[]{person.title, person.text,person.date});
        else
            db.execSQL("INSERT INTO note VALUES (NULL, ?, ?,?,?,NULL,NULL)", new Object[]{person.title, person.text,person.date,uri.toString()});
//        person.title = "david";
//        person.text = "hijklmn";
//        //ContentValues以键值对的形式存放数据
//        ContentValues cv = new ContentValues();
//        cv.put("title", person.title);
//        cv.put("text", person.text);
//        //插入ContentValues中的数据
//        db.insert("note", null, cv);
//
//        cv = new ContentValues();
//        cv.put("text", "opqrst");
//        //更新数据
//        db.update("note", cv, "text = ?", new String[]{"john"});
        Cursor c;
        c = db.rawQuery("SELECT * FROM note",null);
        while (c.moveToNext()) {
            int _id = c.getInt(c.getColumnIndex("_id"));
            String title1 = c.getString(c.getColumnIndex("title"));
            String text1 = c.getString(c.getColumnIndex("text"));
            String date1=c.getString(c.getColumnIndex("date"));
            String picture1=c.getString(c.getColumnIndex("picture"));
            Log.i("db", "_id=>" + _id + ", title=>" + title1 + ", text=>" + text1 + ", date=>" + date1+",picture=>"+picture1);
        }
        c.close();

        //关闭当前数据库
//        db.close();

        //删除test.db数据库
        //      deleteDatabase("test.db");
    }

}
