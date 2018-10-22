package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Main3Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Button btnBack = (Button) findViewById(R.id.back);
        Button btnLook = (Button) findViewById(R.id.look);
        final EditText lookText=(EditText)findViewById(R.id.lookText);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        //打开或创建test.db数据库
        final ArrayList<Note> notes = query();
        NoteAdapter adapter = new NoteAdapter(Main3Activity.this, R.layout.outline, notes);
        listView.setAdapter(adapter);
//        db.execSQL("DROP TABLE IF EXISTS note");
        //创建note表
//        db.execSQL("CREATE TABLE note (_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, text VARCHAR ,picture VARCHAR,video VARCHAR,music VARCHAR )");
        query();
//        delete(db,13);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main3Activity.this, MainActivity.class);
                startActivity(i);
            }
        });
        btnLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = lookText.getText().toString();
                final ArrayList<Note> Looknotes=look(text);
                NoteAdapter adapter = new NoteAdapter(Main3Activity.this, R.layout.outline, Looknotes);
                listView.setAdapter(adapter);
            }
        });
        //listview的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Note info;
                String text = lookText.getText().toString();
                final ArrayList<Note> Looknotes=look(text);
                if(Looknotes!=null) {
                    info = Looknotes.get(position);
                }else {
                    info = notes.get(position);
                }
                Bundle bundle = new Bundle();

                bundle.putInt("id", info.getId());
                bundle.putString("text", info.getText());
                bundle.putString("title",info.getTitle());
                bundle.putString("date", info.getDate());
                bundle.putString("picture", info.getPicture());
                bundle.putString("video", info.getVideo());
                bundle.putString("music", info.getMusic());

                Intent intent = new Intent(Main3Activity.this,Main4Activity.class);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }
        });
        // 添加长按点击弹出选择菜单
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener()
        {
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 1, 0, "删除");
            }
        });

    }

    //给菜单项添加事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.id得到listview中选择的条目绑定的id
        String _id = String.valueOf(info.position);
        int id1=Integer.parseInt(_id);
        ArrayList<Note> notes = query();
        Note note = notes.get(id1);
        int id=note.id;
        switch (item.getItemId()) {
            case 1:
                System.out.println("删除" + id);
                delete(id);
                Intent i = new Intent(Main3Activity.this, Main3Activity.class);
                startActivity(i);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public ArrayList<Note> query(){
        SQLiteDatabase db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
        ArrayList<Note> notes = new ArrayList<Note>();
        Cursor c;
        c = db.rawQuery("SELECT * FROM note",null);
        while (c.moveToNext()) {
            int _id = c.getInt(c.getColumnIndex("_id"));
            String title1 = c.getString(c.getColumnIndex("title"));
            String text1 = c.getString(c.getColumnIndex("text"));
            String date1=c.getString(c.getColumnIndex("date"));
            String picture1=c.getString(c.getColumnIndex("picture"));
            Note note = new Note();
            note.id=_id;
            note.date=date1;
            note.text=text1;
            note.title=title1;
            note.picture=picture1;
            notes.add(note);
            Log.i("db", "_id=>" + _id + ", title=>" + title1 + ", text=>" + text1 + ", date=>" + date1+",picture=>"+picture1);
        }
        c.close();
        return notes;
    }

    public ArrayList<Note> look(String text){
        SQLiteDatabase db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
        if(text==null){
            return null;
        }
        ArrayList<Note> notes = new ArrayList<Note>();
        Cursor c;
        c = db.rawQuery("SELECT * FROM note",null);
        while (c.moveToNext()) {
            int _id = c.getInt(c.getColumnIndex("_id"));
            String title1 = c.getString(c.getColumnIndex("title"));
            String text1 = c.getString(c.getColumnIndex("text"));
            String date1=c.getString(c.getColumnIndex("date"));
            String picture1=c.getString(c.getColumnIndex("picture"));
            if(title1.indexOf(text)!=-1){
                Note note = new Note();
                note.id=_id;
                note.date=date1;
                note.text=text1;
                note.title=title1;
                note.picture=picture1;
                notes.add(note);
                Log.i("db", "_id=>" + _id + ", title=>" + title1 + ", text=>" + text1 + ", date=>" + date1+",picture=>"+picture1);
            }
        }
        c.close();
        return notes;
    }

    public void delete(int id){
        //删除数据
        SQLiteDatabase db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
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
