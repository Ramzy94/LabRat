package com.itrw324.mofokeng.labrat.UIFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.itrw324.mofokeng.labrat.NonActivityClasses.Class;
import com.itrw324.mofokeng.labrat.NonActivityClasses.DatabaseHandler;
import com.itrw324.mofokeng.labrat.NonActivityClasses.LabRatConstants;
import com.itrw324.mofokeng.labrat.R;


public class ClassFragment extends Fragment {

    private LinearLayout linearLayout;
    private AlertDialog.Builder builder;
    private OnFragmentInteractionListener mListener;

    public ClassFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Spinner modSpinner;
    private Spinner venueSpinner;
    private Spinner daySpinner;
    private Spinner periodSpinner;

    public void initDialog()
    {
        Context context = getActivity();
        builder= new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);

        LayoutInflater inflater = (getActivity().getLayoutInflater());
        View v = inflater.inflate(R.layout.content_add_class_dialog,null);
        builder.setView(v);

        modSpinner =(Spinner) v.findViewById(R.id.moduleSpinner);
        venueSpinner = (Spinner)v.findViewById(R.id.venueSpinner);
        periodSpinner = (Spinner)v.findViewById(R.id.periodSpinner);
        daySpinner = (Spinner)v.findViewById(R.id.daySpinner);

        DatabaseHandler handler = new DatabaseHandler(context);
        String [] modules = handler.getModuleList();
        String [] venues = handler.getVenueList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item);
        for(String module:modules)
            adapter.add(module);

        ArrayAdapter<String> venueAdapter = new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item);
        for(String venue:venues)
            venueAdapter.add(venue);

        modSpinner.setAdapter(adapter);
        venueSpinner.setAdapter(venueAdapter);
    }

    public AlertDialog addClassDialog()
    {
        builder.setTitle(R.string.add_class);
        builder.setPositiveButton(R.string.btnProceed,new CreateClass());

        return builder.create();
    }

    private final int EDIT_DIALOG = 0;
    private final int ADD_DIALOG = 1;

    private void updateUI()
    {
        linearLayout.removeAllViews();

        DatabaseHandler handler = new DatabaseHandler(getActivity());
        Class[] classes = handler.getClassList();

        for(Class campusClass:classes)
        {
            linearLayout.addView(createCardView(campusClass,getActivity().getLayoutInflater()));

            Space nothing = new Space(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
            nothing.setLayoutParams(params);
            linearLayout.addView(nothing);
            linearLayout.refreshDrawableState();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        FloatingActionButton classFab = (FloatingActionButton)view.findViewById(R.id.classFab);
        classFab.setOnClickListener(new FabHandler());

        linearLayout =(LinearLayout) view.findViewById(R.id.classLayout);
        updateUI();
        initDialog();

        return view;
    }

    public CardView createCardView(final Class uniClass, LayoutInflater inflater) {
        final CardView card = (CardView) inflater.inflate(R.layout.card, null);
        ((TextView) card.findViewById(R.id.txtDay)).setText(uniClass.getDay());
        ((TextView) card.findViewById(R.id.txtModcode)).setText(uniClass.getModule_Code());
        ((TextView) card.findViewById(R.id.txtVenue)).setText(uniClass.getVenueID());
        ((TextView) card.findViewById(R.id.txtPeriod)).setText(uniClass.getClassTime());

        card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(linearLayout,R.string.class_added,Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                snackbar.show();

                DatabaseHandler handler = new DatabaseHandler(getActivity());
                handler.addToSchedule(uniClass, LabRatConstants.LOGGED_IN);
            }
        });

        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                builder.setTitle(R.string.edit_class);
                modSpinner.setEnabled(false);
                modSpinner.setSelection(uniClass.getDaySpinnerValue());
                periodSpinner.setSelection(uniClass.getClass_Period());
                daySpinner.setSelection(Integer.parseInt(new DatabaseHandler(getActivity()).getVenueID(uniClass))+1);

                builder.setPositiveButton(R.string.btnProceed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String venue = venueSpinner.getSelectedItem().toString();
                        int period = periodSpinner.getSelectedItemPosition();
                        String day = daySpinner.getSelectedItem().toString();

                        uniClass.setDay(day);
                        uniClass.setClassTime(period);
                        uniClass.setVenueID(venue);

                        DatabaseHandler handler = new DatabaseHandler(getActivity());
                        handler.updateClass(uniClass);

                        updateUI();
                    }
                });

                //builder.setNegativeButton(R.string.delete_class,new )

                builder.create().show();
                return true;
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
            AlertDialog dialog = addClassDialog();
            dialog.show();
        }
    }

    class CreateClass implements DialogInterface.OnClickListener
    {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            DatabaseHandler handler = new DatabaseHandler(getActivity());

            String module = modSpinner.getSelectedItem().toString();
            String venue = venueSpinner.getSelectedItem().toString();
            int period = periodSpinner.getSelectedItemPosition();
            String day = daySpinner.getSelectedItem().toString();

            Class uniClass = new Class(period,venue,module,day);

            handler.insertClass(uniClass);
            updateUI();
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
