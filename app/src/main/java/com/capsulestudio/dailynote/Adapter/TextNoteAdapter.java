package com.capsulestudio.dailynote.Adapter;

/**
 * Created by Juman on 1/21/2018.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.capsulestudio.dailynote.Model.TextNoteModel;
import com.capsulestudio.dailynote.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shuvo on 12/13/2017.
 */

public class TextNoteAdapter extends ArrayAdapter<TextNoteModel> {

    Context mCtx;
    List<TextNoteModel> imageNoteList;
    List<TextNoteModel> searchList;
    int listLayoutRes;

    private int lastPosition = -1;

    public TextNoteAdapter(Context mCtx, int listLayoutRes, List<TextNoteModel> imageNoteList) {
        super(mCtx, listLayoutRes, imageNoteList);
        this.mCtx = mCtx;
        this.imageNoteList = imageNoteList;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(imageNoteList);
        this.listLayoutRes = listLayoutRes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //getting students of the specified position
        TextNoteModel students = imageNoteList.get(position);

        //getting views
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvDateTime = (TextView) view.findViewById(R.id.tvdate_time);


        /**
         *  Animation Part
         */

        Animation animation = AnimationUtils.loadAnimation(mCtx,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.bottom_from_up);
        view.startAnimation(animation);
        lastPosition = position;


        //adding data to views
        tvTitle.setText(students.getTitle());
        tvDateTime.setText(students.getDate_time());

        return view;
    }

    //Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        imageNoteList.clear();
        if (charText.length() == 0) {
            imageNoteList.addAll(searchList);
        } else {
            for (TextNoteModel s : searchList) {
                if (s.getTitle().toLowerCase(Locale.getDefault()).contains(charText) ||
                        s.getDate_time().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    imageNoteList.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }
}