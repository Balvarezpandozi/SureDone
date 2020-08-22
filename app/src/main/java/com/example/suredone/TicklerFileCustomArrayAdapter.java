package com.example.suredone;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TicklerFileCustomArrayAdapter extends ArrayAdapter {
    private ArrayList<TicklerFileTask> mTasks;

    public TicklerFileCustomArrayAdapter(@NonNull Context context, ArrayList<TicklerFileTask> tasks) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text2, tasks);
        mTasks = tasks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView title = v.findViewById(android.R.id.text1);
        TextView subTitle = v.findViewById(android.R.id.text2);
        Log.i("Task Info", mTasks.get(position).getTitle() + mTasks.get(position).getActiveDate());
        title.setText(mTasks.get(position).getTitle());
        subTitle.setText(mTasks.get(position).getActiveDate());
        return v;
    }
}
