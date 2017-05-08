package com.example.saleckope.piszkosanyagiak.ui.statistics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.saleckope.piszkosanyagiak.R;
import com.example.saleckope.piszkosanyagiak.model.MoneyItem;

public class StatisticsDetailsFragment extends Fragment {
    private TextView tvAmount;
    private TextView tvType;
    private TextView tvCategory;
    private TextView tvDate;
    private TextView tvNote;
    private ImageView ivIcon;
    private MoneyItem item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get extra data from intent
        item = (MoneyItem) getActivity().getIntent().getExtras().getSerializable("DATA");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate  details layout
        View view = inflater.inflate(R.layout.fragment_statistics_details, container, false);
        //Initialize elements
        tvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvType = (TextView) view.findViewById(R.id.tvType);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvNote = (TextView) view.findViewById(R.id.tvNote);
        ivIcon = (ImageView) view.findViewById(R.id.surpriseTrex);

        //Set elements with item's data
        tvType.setText(item.type.name());
        tvAmount.setText(Integer.toString(item.amount));
        tvDate.setText(Integer.toString(item.year) + "." + Integer.toString(item.month) + "." + Integer.toString(item.day) + ".");
        tvNote.setText(item.note);

        switch (item.type){
            case INCOME:
                //Set color to green
                tvAmount.setTextColor(Color.rgb(20, 150, 30));
                //set image to almost happy trex because user got money
                ivIcon.setImageResource(R.drawable.trex_hug);
                //Income has no outlay category -> category is income
                tvCategory.setText("income");
                break;
            case OUTLAY:
                //Set color to red
                tvAmount.setTextColor(Color.RED);
                //Set image to sad trex because user spent money
                ivIcon.setImageResource(R.drawable.sad_trex);
                //Set outlay category
                tvCategory.setText(item.category.name());
                break;
            default: break;
        }
        return view;
    }
}