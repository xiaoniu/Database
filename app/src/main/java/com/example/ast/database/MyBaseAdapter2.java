package com.example.ast.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xiaoniu on 2017/6/30.
 */

public class MyBaseAdapter2 extends BaseAdapter {

    private Context context;
    private List<User2> list;

    public MyBaseAdapter2(Context context, List<User2> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        TextView id = view.findViewById(R.id.id);
        TextView name = view.findViewById(R.id.name);
        TextView age = view.findViewById(R.id.age);
        id.setText(list.get(i).getId().toString());
        name.setText(list.get(i).getName());
        age.setText(list.get(i).getAge()+"");
        id.setTextColor(context.getResources().getColor(R.color.holo_red_light));
        name.setTextColor(context.getResources().getColor(R.color.holo_red_light));
        age.setTextColor(context.getResources().getColor(R.color.holo_red_light));
        return view;
    }
}
