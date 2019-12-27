package com.example.inclass09;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder>  {
    ArrayList<email> emails;
    Context activity;
    int position;
    RecyclerView mrecyler;
    public EmailAdapter(ArrayList<email> emails, Context activity, RecyclerView mrecyler) {
        this.emails=emails;
        this.activity=activity;
        this.mrecyler=mrecyler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_mail, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mrecyler.getChildLayoutPosition(view);
                email item = emails.get(itemPosition);
                Intent intent = new Intent(activity, DisplayMail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("emailData",item);
                intent.putExtra("emailBundle", bundle);
                activity.startActivity(intent);
            }
        });
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        this.position=position;
        final email fc = emails.get(position);
        holder.tv_CreatedDate.setText(fc.created_at);
        holder.tv_subject.setText(fc.subject);
        holder.activity = activity;
        holder.email=fc;

    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public static  class  ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_subject;
        TextView tv_CreatedDate;
        ImageView deleteIcon;
        email email;
        Context activity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subject = (TextView) itemView.findViewById(R.id.subjectList);
            tv_CreatedDate = (TextView) itemView.findViewById(R.id.tv_createdDate);
            deleteIcon= (ImageView) itemView.findViewById(R.id.imageView3);

        }
    }
}
