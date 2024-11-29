package com.example.cscb07_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private List<Activity> activityList;
    private OnActivityClickListener listener;

    public interface OnActivityClickListener {
        void onEditClick(Activity activity, int position);

        void onDeleteClick(Activity activity, int position);
    }

    public ActivityAdapter(List<Activity> activityList, OnActivityClickListener listener) {
        this.activityList = activityList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity activity = activityList.get(position);
        holder.activityDescriptionTextView.setText(activity.getDescription());
        holder.editButton.setOnClickListener(v -> listener.onEditClick(activity, position));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(activity, position));
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        ImageButton editButton, deleteButton;
        TextView activityDescriptionTextView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityDescriptionTextView = itemView.findViewById(R.id.activityDescriptionTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
