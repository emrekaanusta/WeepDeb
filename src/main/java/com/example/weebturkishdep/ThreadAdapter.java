package com.example.weebturkishdep;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadViewHolder>{

    Context ctx;
    List<Thread> data;

    interface ThreadListListener {
        void personClicked(Thread thread);
        void onRowClick(Thread thread);
    }

    ThreadListListener listener;

    public ThreadAdapter(Context ctx, List<Thread> data) {
        this.ctx = ctx;
        this.data = data;
    }

    public void setListener(ThreadListListener listener) {
        this.listener = listener;
    }

    public void setThreads(List<Thread> threads) {
        this.data = threads;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(ctx).inflate(R.layout.row_layout,parent,false);

        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {

        holder.txtContent.setText(data.get(position).getContent());

        holder.txtId.setText(data.get(position).getId());

        holder.txtTitle.setText(data.get(position).getTitle());



        holder.row.setOnClickListener(v -> {
            int clickedPosition = holder.getBindingAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {
                Thread clickedThread = data.get(clickedPosition);

                if (listener != null) {
                    listener.onRowClick(clickedThread);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class ThreadViewHolder extends RecyclerView.ViewHolder{

        TextView txtContent;
        TextView txtTitle;

        TextView txtId;

        ConstraintLayout row;

        public ThreadViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId =  itemView.findViewById(R.id.textView3);
            txtTitle = itemView.findViewById(R.id.textView2);
            txtContent = itemView.findViewById(R.id.textView4);
            row = itemView.findViewById(R.id.threadcontainer);
        }
    }




}
