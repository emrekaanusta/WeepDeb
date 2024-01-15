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

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private Context ctx;
    private List<Comments> data;

    interface CommentListListener {
        void onCommentClick(Comments comment);
    }

    CommentListListener listener;

    public CommentsAdapter(Context ctx, List<Comments> data) {
        this.ctx = ctx;
        this.data = data;
    }

    public void setListener(CommentListListener listener) {
        this.listener = listener;
    }

    public void setComments(List<Comments> comments) {
        this.data = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.comment_row_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.txtComment.setText(data.get(position).getComment());
        holder.txtUsername.setText(data.get(position).getUsername());

        // Additional logic for handling anonymous comments
        if (data.get(position).getAnon()) {
            holder.txtUsername.setText(data.get(position).getAnonName());
        }

        holder.row.setOnClickListener(v -> {
            int clickedPosition = holder.getBindingAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {
                Comments clickedComment = data.get(clickedPosition);

                if (listener != null) {
                    listener.onCommentClick(clickedComment);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView txtComment;
        TextView txtUsername;
        ConstraintLayout row;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            txtComment = itemView.findViewById(R.id.textView5);
            txtUsername = itemView.findViewById(R.id.username);
            row = itemView.findViewById(R.id.commentrow);
        }
    }
}