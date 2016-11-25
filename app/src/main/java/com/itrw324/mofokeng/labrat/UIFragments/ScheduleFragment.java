package com.itrw324.mofokeng.labrat.UIFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itrw324.mofokeng.labrat.NonActivityClasses.Class;
import com.itrw324.mofokeng.labrat.NonActivityClasses.DatabaseHandler;
import com.itrw324.mofokeng.labrat.NonActivityClasses.LabRatConstants;
import com.itrw324.mofokeng.labrat.NonActivityClasses.Schedule;
import com.itrw324.mofokeng.labrat.R;


public class ScheduleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayout scheduleLayout;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private void updateUI()
    {
        scheduleLayout.removeAllViews();
        DatabaseHandler handler = new DatabaseHandler(getActivity());
        Schedule[]schedules = handler.getMySchedule(LabRatConstants.LOGGED_IN.getAccount());

        for (int i=0;i<schedules.length;i++)
        {
            Class nwuClass = handler.getOneClass(schedules[i].getClassID());

            scheduleLayout.addView(createCardView(nwuClass,getActivity().getLayoutInflater(),schedules[i]));
        }
    }

    public CardView createCardView(final Class uniClass, LayoutInflater inflater, final Schedule theSchedule)
    {
        CardView card = (CardView) inflater.inflate(R.layout.card, null);
        ((TextView) card.findViewById(R.id.txtDay)).setText(uniClass.getDay());
        ((TextView) card.findViewById(R.id.txtModcode)).setText(uniClass.getModule_Code());
        ((TextView) card.findViewById(R.id.txtVenue)).setText(uniClass.getVenueID());
        ((TextView) card.findViewById(R.id.txtPeriod)).setText(uniClass.getClass_Time());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler handler = new DatabaseHandler(getActivity());
                handler.deleteSchedule(theSchedule);
                Toast.makeText(getActivity(), R.string.class_removed, Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });

        return card;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        scheduleLayout = (LinearLayout)view.findViewById(R.id.scheduleLayout);
        updateUI();
        return view;
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
