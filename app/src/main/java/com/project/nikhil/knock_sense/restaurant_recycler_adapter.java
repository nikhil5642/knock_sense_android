package com.project.nikhil.knock_sense;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class restaurant_recycler_adapter extends RecyclerView.Adapter<restaurant_recycler_adapter .ViewHolder> {
    public ArrayList<restaurant_object> mdatas;
    ViewGroup x;
    Context context;
Class activity;
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public restaurant_recycler_adapter(ArrayList<restaurant_object> myDataset, Class activity) {
        mdatas = myDataset;
    this.activity=activity;
    }
    public TextView c;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txt_title,txt_tag_line,txt_review_count;
        MaterialRatingBar ratingBar;
        ImageView imageView;
        CircularProgressButton button;
        private restaurant_recycler_adapter.MyClickListener myClickListener;

        public ViewHolder(View itemView) {

            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.restaurant_image);
            txt_title=(TextView)itemView.findViewById(R.id.restaurant_title);
            txt_tag_line=(TextView)itemView.findViewById(R.id.restaurant_tag_line);
            txt_review_count=(TextView)itemView.findViewById(R.id.restaurant_rating_count);
            ratingBar=(MaterialRatingBar) itemView.findViewById(R.id.restaurant_rating);

            itemView.setOnClickListener(this);
            context=itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getLayoutPosition(), view);
        }

        public void setOnItemClickListener(restaurant_recycler_adapter.MyClickListener myClickListener) {
            this.myClickListener = myClickListener;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        x=parent;
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_recycler_object,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final restaurant_object o1=mdatas.get(position);
        holder.txt_title.setText(o1.getName());
        holder.txt_tag_line.setText(o1.getTag_line());
        holder.txt_review_count.setText("("+o1.getReview_count()+")");
        holder.ratingBar.setRating(o1.getReview());
        Picasso picasso = Picasso.with(context);
        picasso.load(o1.getImage()).error(R.drawable.restaurant_sample_image).into(holder.imageView);
         holder.setOnItemClickListener(new restaurant_recycler_adapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i=new Intent(context,activity);
                i.putExtra("data2",o1);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mdatas==null){
            return 0;
        }
        return mdatas.size();
    }


}