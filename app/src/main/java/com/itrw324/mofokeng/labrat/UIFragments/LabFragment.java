package com.itrw324.mofokeng.labrat.UIFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itrw324.mofokeng.labrat.NonActivityClasses.Class;
import com.itrw324.mofokeng.labrat.NonActivityClasses.DatabaseHandler;
import com.itrw324.mofokeng.labrat.NonActivityClasses.ImageAdapter;
import com.itrw324.mofokeng.labrat.R;

import java.util.ArrayList;

public class LabFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Spinner venueSpinner;
    private ImageAdapter imageAdapter;
    private GridView gridview;
    private OnFragmentInteractionListener mListener;

    public LabFragment() {
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
        View view=inflater.inflate(R.layout.fragment_lab, container, false);

        String venues[] = new DatabaseHandler(getActivity()).getVenueList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item);

        for(String venue:venues)
        {
            adapter.add(venue);
        }

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.classDialogFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClassesDialog();
            }
        });

        venueSpinner = (Spinner)view.findViewById(R.id.labSpinner);
        venueSpinner.setAdapter(adapter);

        gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        venueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseHandler handler = new DatabaseHandler(getActivity());
                if (handler.venueHasClass(venueSpinner.getSelectedItem().toString()))
                    imageAdapter = new ImageAdapter(getActivity(),ImageAdapter.VEUNUE_IS_OCCUPIED);
                else
                    imageAdapter = new ImageAdapter(getActivity());

                gridview.setAdapter(imageAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    private void showClassesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.content_lab_dialog,null);

        DatabaseHandler handler = new DatabaseHandler(getActivity());

        ArrayList<Class> classList = handler.getClassesInVenue(venueSpinner.getSelectedItem().toString());

        for(Class theClass:classList){

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.labLayout);
            TextView tv = new TextView(getActivity());
            LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,5);
            tv.setLayoutParams(param);

            tv.setText(theClass.getClass_Time()+" :\t\t"+theClass.getModule_Code());
            Space nothing = new Space(getActivity());
            nothing.setLayoutParams(params);

            layout.addView(tv);
            layout.addView(nothing);
        }

        builder.setTitle(R.string.classes_in_venue);
        builder.setPositiveButton(R.string.btnOkay,null);

        builder.setView(view);
        builder.create().show();
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
