package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.itrw324.mofokeng.labrat.R;

/**
 * Created by Mofokeng on 19-Nov-16.
 */

public class LabRatDialogs {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Context context;

    public LabRatDialogs(Context context) {

        this.context = context;
        builder = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.AppTheme));
    }

    private Spinner modSpinner;
    private Spinner venueSpinner;
    private Spinner daySpinner;
    private Spinner periodSpinner;

    public AlertDialog addClassDialog()
    {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.content_add_class_dialog,null);

        builder.setView(v);
        builder.setTitle(R.string.moreDetails);
        builder.setPositiveButton(R.string.btnProceed,new CreateClass());
        builder.setCancelable(false);

        dialog = builder.create();

        dialog.show();

        modSpinner =(Spinner) v.findViewById(R.id.moduleSpinner);
        venueSpinner = (Spinner)v.findViewById(R.id.venueSpinner);
        periodSpinner = (Spinner)v.findViewById(R.id.periodSpinner);
        daySpinner = (Spinner)v.findViewById(R.id.daySpinner);
        DatabaseHandler handler = new DatabaseHandler(context);
        String [] modules = handler.getModuleList();
        String [] venues = handler.getVenueList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item);
        for(String module:modules)
                adapter.add(module);

        ArrayAdapter<String> venueAdapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item);
        for(String venue:venues)
                venueAdapter.add(venue);



        modSpinner.setAdapter(adapter);
        venueSpinner.setAdapter(venueAdapter);


        return dialog;
    }

    class CreateClass implements DialogInterface.OnClickListener
    {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            DatabaseHandler handler = new DatabaseHandler(context);

            String module = modSpinner.getSelectedItem().toString();
            String venue = venueSpinner.getSelectedItem().toString();
            int period = periodSpinner.getSelectedItemPosition();
            String day = daySpinner.getSelectedItem().toString();

            Class uniClass = new Class(period,venue,module,day);

            handler.insertClass(uniClass);

        }
    }
}
