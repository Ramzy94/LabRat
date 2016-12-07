package com.itrw324.mofokeng.labrat.UIFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;

import com.itrw324.mofokeng.labrat.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class DevBlogFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View devBlog;
    private View timeLine;
    private LayoutInflater inflater;
    private Button btnSwitch;
    private ScrollView scrollView;

    public DevBlogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_dev_blog, container, false);

        this.inflater = inflater;

        CardView cards[] = new CardView[9];
        cards[0] = (CardView)v.findViewById(R.id.card_android_dev);
        cards[1] = (CardView)v.findViewById(R.id.card_google_dev);
        cards[2] = (CardView)v.findViewById(R.id.card_android_hive);
        cards[3] = (CardView)v.findViewById(R.id.card_stack_overflow);
        cards[4] = (CardView)v.findViewById(R.id.card_android_studio);
        cards[5] = (CardView)v.findViewById(R.id.card_sqlite);
        cards[6] = (CardView)v.findViewById(R.id.card_git);
        cards[7] = (CardView)v.findViewById(R.id.card_github);
        cards[8] = (CardView)v.findViewById(R.id.card_icon8);

        for(CardView card:cards)
        {
            switch (card.getId())
            {
                case R.id.card_android_dev:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_android_developers));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;

                case R.id.card_google_dev:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_google_developers));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;

                case R.id.card_android_hive:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_android_hive));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;

                case R.id.card_stack_overflow:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_stack_overflow));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;

                case R.id.card_android_studio:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_android_studio));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;

                case R.id.card_sqlite:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_sqlite));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;

                case R.id.card_git:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_git));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;

                case R.id.card_github:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_github));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;

                case R.id.card_icon8:card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(getString(R.string.link_icons8));
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });break;
            }
        }

        btnSwitch = (Button)v.findViewById(R.id.btnDevSwitch);
        btnSwitch.setOnClickListener(new ButtonListener());

        scrollView = (ScrollView)v.findViewById(R.id.scrollViewLayout);
        devBlog = v.findViewById(R.id.blogLayout);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(btnSwitch.getText().toString().equalsIgnoreCase("View Timeline")) {

                btnSwitch.setText(R.string.btnSwitchViewResources);
                scrollView.removeAllViews();
                timeLine = inflater.inflate(R.layout.timeline_layout,null);
                scrollView.addView(timeLine);

            }
            else {
                btnSwitch.setText(R.string.btnSwitchViewTimeline);
                scrollView.removeAllViews();
                scrollView.addView(devBlog);

            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
