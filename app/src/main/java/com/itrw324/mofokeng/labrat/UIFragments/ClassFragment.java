package com.itrw324.mofokeng.labrat.UIFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.itrw324.mofokeng.labrat.NonActivityClasses.Class;
import com.itrw324.mofokeng.labrat.NonActivityClasses.DatabaseHandler;
import com.itrw324.mofokeng.labrat.NonActivityClasses.LabRatConstants;
import com.itrw324.mofokeng.labrat.NonActivityClasses.LabRatDialogs;
import com.itrw324.mofokeng.labrat.NonActivityClasses.Schedule;
import com.itrw324.mofokeng.labrat.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassFragment newInstance(String param1, String param2) {
        ClassFragment fragment = new ClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        FloatingActionButton classFab = (FloatingActionButton)view.findViewById(R.id.classFab);
        classFab.setOnClickListener(new FabHandler());

        DatabaseHandler handler = new DatabaseHandler(getActivity());

        Class[] classes = handler.getClassList();

        LinearLayout classLayout =(LinearLayout) view.findViewById(R.id.classLayout);

        for(Class campusClass:classes)
        {
            classLayout.addView(createCardView(campusClass,inflater));
            Space nothing = new Space(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
            nothing.setLayoutParams(params);
            classLayout.addView(nothing);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public CardView createCardView(final Class uniClass, LayoutInflater inflater) {
        CardView card = (CardView) inflater.inflate(R.layout.card, null);
        ((TextView) card.findViewById(R.id.txtDay)).setText(uniClass.getDay());
        ((TextView) card.findViewById(R.id.txtModcode)).setText(uniClass.getModule_Code());
        ((TextView) card.findViewById(R.id.txtVenue)).setText(uniClass.getVenueID());
        ((TextView) card.findViewById(R.id.txtPeriod)).setText(uniClass.getClassTime());

        card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setPositiveButton("OK",null);
                builder.setMessage("Added To Schedule, Please Refresh");
                AlertDialog dialog = builder.create();
                dialog.show();

                DatabaseHandler handler = new DatabaseHandler(getActivity());
                handler.addToSchedule(uniClass, LabRatConstants.LOGGED_IN);
                Schedule a[] =handler.getMySchedule(LabRatConstants.LOGGED_IN);

                for(Schedule s:a)
                {
                    Log.println(Log.DEBUG,"Work",s.getUserEmail()+s.getClassID());
                }
            }
        });

        return card;
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

    class FabHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            LabRatDialogs dialogs = new LabRatDialogs(getActivity());
            AlertDialog dialog = dialogs.addClassDialog();
            dialog.show();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
