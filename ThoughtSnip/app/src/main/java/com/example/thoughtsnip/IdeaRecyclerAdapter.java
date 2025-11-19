package com.example.thoughtsnip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IdeaRecyclerAdapter extends RecyclerView.Adapter<IdeaRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Idea> list;

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onClick(Idea idea);
    }

    public IdeaRecyclerAdapter(Context context,
                               ArrayList<Idea> list,
                               OnItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_idea_clean, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {

        Idea idea = list.get(pos);

        holder.tvTitle.setText(idea.getTitle());
        holder.tvDate.setText(idea.getDateTime());

        holder.itemView.setOnClickListener(v ->
                clickListener.onClick(idea)
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate;

        public ViewHolder(@NonNull View v) {
            super(v);

            tvTitle = v.findViewById(R.id.tvTitle);
            tvDate = v.findViewById(R.id.tvDate);
        }
    }
}
