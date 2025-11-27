package com.example.thoughtsnip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IdeaRecyclerAdapter extends RecyclerView.Adapter<IdeaRecyclerAdapter.ViewHolder>
        implements Filterable {
    private Context context;
    private ArrayList<Idea> list;
    private ArrayList<Idea> fullList;

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onClick(Idea idea);
    }

    public IdeaRecyclerAdapter(Context context,
                               ArrayList<Idea> list,
                               OnItemClickListener clickListener) {

        this.context = context;
        this.list = list;
        this.fullList = new ArrayList<>(list);
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

    @Override
    public Filter getFilter() {
        return ideaFilter;
    }

    private final Filter ideaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Idea> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String searchText = constraint.toString().toLowerCase().trim();

                for (Idea idea : fullList) {

                    boolean matchesTitle = idea.getTitle().toLowerCase().contains(searchText);

                    boolean matchesDate = idea.getDateTime().toLowerCase().contains(searchText);

                    if (matchesTitle || matchesDate) {
                        filteredList.add(idea);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((ArrayList<Idea>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate;

        public ViewHolder(@NonNull View v) {
            super(v);

            tvTitle = v.findViewById(R.id.tvTitle);
            tvDate = v.findViewById(R.id.tvDate);
        }
    }
}
