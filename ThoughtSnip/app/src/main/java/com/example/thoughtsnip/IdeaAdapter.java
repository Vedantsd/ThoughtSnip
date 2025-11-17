package com.example.thoughtsnip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class IdeaAdapter extends ArrayAdapter<Idea> {

    Context context;
    List<Idea> ideaList;

    public IdeaAdapter(Context context, List<Idea> ideas) {
        super(context, 0, ideas);
        this.context = context;
        this.ideaList = ideas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_idea, parent, false);
        }

        Idea idea = ideaList.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvDate = convertView.findViewById(R.id.tvDate);

        tvTitle.setText(idea.getTitle());
        tvDate.setText("Added: " + idea.getDateTime());

        return convertView;
    }
}
