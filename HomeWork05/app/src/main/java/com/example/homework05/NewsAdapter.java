package com.example.homework05;


import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<newsApi> {
    private static class ViewHolder {
        TextView author;
        TextView title;
        TextView publishedAt;
        ImageView newsImage;
    }

    public NewsAdapter(Context context,int resource, ArrayList<newsApi> news) {
        super(context, resource, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        newsApi news = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.news_items, parent, false);
            viewHolder.newsImage = (ImageView) convertView.findViewById(R.id.newsImage);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.publishedAt = (TextView) convertView.findViewById(R.id.publishedAt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (news.author == "null")
            viewHolder.author.setText("No Author Found");
        else {
            viewHolder.author.setText(news.author);
        }
        viewHolder.title.setText(news.title);
        viewHolder.publishedAt.setText(news.publishedAt);
        if (!news.urlToImage.isEmpty())
        Picasso.get().load(news.urlToImage).into(viewHolder.newsImage);

        return convertView;
    }
}
