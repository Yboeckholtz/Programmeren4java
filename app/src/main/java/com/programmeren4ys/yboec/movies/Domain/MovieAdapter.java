package com.programmeren4ys.yboec.movies.Domain;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.programmeren4ys.yboec.movies.R;

import java.util.ArrayList;

/**
 * Created by yboec on 3-7-2017.
 */

public class MovieAdapter extends BaseAdapter {
    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private LayoutInflater mInflator;
    private ArrayList<Movie> bolMovieArrayList;

    public MovieAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Movie> bolMovieArrayList) {
        this.mContext = context;
        this.mInflator = layoutInflater;
        this.bolMovieArrayList = bolMovieArrayList;
    }


    public int getCount() {
        int size = bolMovieArrayList.size();
        Log.i(TAG, "getCount() = " + size);
        return size;
    }


    public Object getItem(int position) {
        Log.i(TAG, "getItem() at " + position);
        return bolMovieArrayList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView at " + position);

        ViewHolder viewHolder;

        if(convertView == null){

            Log.i(TAG, "convertView is NULL - nieuwe maken");

            // Koppel de convertView aan de layout van onze eigen row
            convertView = mInflator.inflate(R.layout.movie_list_row, null);

            // Maak een ViewHolder en koppel de schermvelden aan de velden uit onze eigen row.
            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.rowMovieTitle);
            viewHolder.textViewYear = (TextView) convertView.findViewById(R.id.rowMovieYear);
            // viewHolder.textViewContents = (TextView) convertView.findViewById(R.id.rowToDoDate);

            // Sla de viewholder op in de convertView
            convertView.setTag(viewHolder);
        } else {
            Log.i(TAG, "convertView BESTOND AL - hergebruik");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie bolMovie = bolMovieArrayList.get(position);
        viewHolder.textViewTitle.setText(bolMovie.getMovietitle());
        viewHolder.textViewYear.setText(bolMovie.getReleaseYear());

        return convertView;
    }

    private static class ViewHolder {
        public TextView textViewTitle;
        public TextView textViewYear;
        // public TextView textViewContents;
    }
}
