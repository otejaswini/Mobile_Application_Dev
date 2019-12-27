package com.example.inclassassignment07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MusicAdapter extends ArrayAdapter<musix> {
    private static class ViewHolder {
        TextView track;
        TextView album;
        TextView artist;
        TextView date;
    }
    public MusicAdapter(Context context, int resource, ArrayList<musix> music) {
        super(context, resource, music);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        musix music = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.musix_list, parent, false);
            viewHolder.track = (TextView) convertView.findViewById(R.id.track);
            viewHolder.album = (TextView) convertView.findViewById(R.id.album);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (music.track_name != "null")
            viewHolder.track.setText(music.track_name);
        viewHolder.artist.setText(music.artist_name);
        viewHolder.album.setText(music.album_name);
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = inputFormat.parse(music.updated_time.substring(0,10));
            String outputDateStr = outputFormat.format(date);
            viewHolder.date.setText(outputDateStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
