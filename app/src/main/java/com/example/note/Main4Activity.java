package com.example.note;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main4Activity extends Activity {
    Uri uri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Button btnBack=(Button)findViewById(R.id.back);
        Button btnSave=(Button)findViewById(R.id.save);
        Button btnPicture=(Button)findViewById(R.id.button);
        final EditText text1=(EditText)findViewById(R.id.editText1);
        final EditText text2=(EditText)findViewById(R.id.editText4);
        TextView textView = (TextView)findViewById(R.id.textView7);
        ImageView imView = (ImageView) findViewById(R.id.view);

        //打开或创建test.db数据库
        final SQLiteDatabase db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
//        db.execSQL("DROP TABLE IF EXISTS note");
        //创建note表
//        db.execSQL("CREATE TABLE note (_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, text VARCHAR ,date VARCHAR,picture VARCHAR,video VARCHAR,music VARCHAR )");

        Bundle b=getIntent().getExtras();
        //获取Bundle的信息
        String title=b.getString("title");
        String article=b.getString("text");
        final String str=b.getString("picture");
        System.out.println("str="+str);
        if(str!=null) {
            Uri urI = Uri.parse(str);
            imView.setImageURI(urI);
        }else{
            textView.setText("");
        }
//        Bitmap bitmap = BitmapFactory.decodeFile(str);
//        imView.setImageBitmap(bitmap);
        final int id=b.getInt("id");
        text1.setText(title);
        text2.setText(article);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main4Activity.this ,Main3Activity.class);
                startActivity(i);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=text1.getText().toString();
                String detail=text2.getText().toString();
                System.out.println("title="+title);
                System.out.println("detail="+detail);
                Log.e("mytag",detail);
                Log.e("myTag",title);
                update(db,id,title,detail,uri);
                Intent i = new Intent(Main4Activity.this ,Main3Activity.class);
                startActivity(i);
            }
        });
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void update(SQLiteDatabase db,int id,String title,String text,Uri uri){
        if(uri!=null) {
            String sql = "update note set title=?,text=?,picture=? where _id=?";
            db.execSQL(sql, new Object[]{title, text, uri.toString(), id});
        }else{
            String sql = "update note set title=?,text=? where _id=?";
            db.execSQL(sql, new Object[]{title, text,id});
        }
        Cursor c;
        c = db.rawQuery("SELECT * FROM note",null);
        while (c.moveToNext()) {
            int _id = c.getInt(c.getColumnIndex("_id"));
            String title1 = c.getString(c.getColumnIndex("title"));
            String text1 = c.getString(c.getColumnIndex("text"));
            String date1=c.getString(c.getColumnIndex("date"));
            String picture1=c.getString(c.getColumnIndex("picture"));
            Log.i("db", "_id=>" + _id + ", title=>" + title1 + ", text=>" + text1 + ", date=>" + date1+ ", picture=>" + picture1);
        }
        c.close();

    }
    public void delete(SQLiteDatabase db,int id){
        //删除数据
        String sql = "delete from note where _id = ?";
        db.execSQL(sql, new Integer[]{id});
        Cursor c;
        c = db.rawQuery("SELECT * FROM note",null);
        while (c.moveToNext()) {
            int _id = c.getInt(c.getColumnIndex("_id"));
            String title1 = c.getString(c.getColumnIndex("title"));
            String text1 = c.getString(c.getColumnIndex("text"));
            String date1=c.getString(c.getColumnIndex("date"));
            Log.i("db", "_id=>" + _id + ", title=>" + title1 + ", text=>" + text1 + ", date=>" + date1);
        }
        c.close();
    }
}
