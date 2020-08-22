package com.example.suredone;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class hotlistCustomAdapter extends ArrayAdapter<HotlistTask> {
    private ArrayList<HotlistTask> hotlistTaskList;


    public hotlistCustomAdapter(@NonNull Context context, int resource, ArrayList<HotlistTask> hotlistTaskList) {
        super(context, resource);
        this.hotlistTaskList = hotlistTaskList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView text = convertView.findViewById(android.R.id.text1);
        HotlistTask hotlistTask = hotlistTaskList.get(position);
        String title = hotlistTask.getTitle();
        text.setText(title);


        return convertView;
    }
}
