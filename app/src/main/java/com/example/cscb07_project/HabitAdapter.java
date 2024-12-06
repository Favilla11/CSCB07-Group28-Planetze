package com.example.cscb07_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder>{
    private List<Habit> habits;
    private OnHabitClickListner listner;

    public interface OnHabitClickListner {
        void OnHabitClickListner(String act);
    }
    public HabitAdapter(@NonNull List<Habit> habits) {
        this.habits = habits;
        this.listner = listner;
    }

    public void updateList(List<Habit> newHabits) {
        this.habits = newHabits;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HabitAdapter.HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_item,
                parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitAdapter.HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.action.setText(habit.getAct());
        holder.progress.setText( (int) habit.getProgress() +" days of " + habit.getAct());

    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder{
        TextView action;
        TextView progress;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            //textView = itemView.findViewById(R.id.adoptedListView);
            action = itemView.findViewById(R.id.action);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}

