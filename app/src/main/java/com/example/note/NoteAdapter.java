package com.example.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    int resouceId;
    Context context;
    ArrayList<Note> note;
    public NoteAdapter(Context context,int resourceId,ArrayList<Note> objects) {
        super(context, resourceId, objects);
        this.context = context;
        objects = note;
        this.resouceId = resourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);  //获取当前项的Fruit实例
        //为子项动态加载布局
        View view = View.inflate(context, resouceId,null);
        TextView title = (TextView) view.findViewById(R.id.textView5);
        TextView text = (TextView) view.findViewById(R.id.textView6);
        TextView date=(TextView)view.findViewById(R.id.textView8);
        String datetime=note.date;
        String year=datetime.substring(0,4);
        String month=datetime.substring(5,7);
        String day=datetime.substring(8,10);
        date.setText(year+"年"+month+"月"+day+"日");
        title.setText(note.title);
        text.setText(note.text);
        return view;
    }

}