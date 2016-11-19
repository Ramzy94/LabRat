package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.content.Context;
import android.support.v7.app.AlertDialog;
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
        builder = new AlertDialog.Builder(context);
    }

    public AlertDialog addClassDialog()
    {
        builder.setView(R.layout.content_add_class_dialog);
        builder.setTitle(R.string.moreDetails);
        builder.setCancelable(false);

        dialog = builder.create();

        Spinner spinner =(Spinner) dialog.findViewById(R.id.daySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.daysArray,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        return dialog;
    }
}
