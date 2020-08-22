package com.example.suredone;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HotlistCustomArrayAdapter extends ArrayAdapter<HotlistTask> {

    private ArrayList<HotlistTask> mTasks;

    public HotlistCustomArrayAdapter(@NonNull Context context, ArrayList<HotlistTask> tasks) {
        super(context, android.R.layout.simple_list_item_1, tasks);
        mTasks = tasks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView txt = v.findViewById(android.R.id.text1);
        int flags = txt.getPaintFlags();
        if (mTasks.get(position).getDoneTask()){
            flags = flags | Paint.STRIKE_THRU_TEXT_FLAG;
        }else{
            flags = flags & ~Paint.STRIKE_THRU_TEXT_FLAG;
        }
        txt.setPaintFlags(flags);
        return v;
    }
}
