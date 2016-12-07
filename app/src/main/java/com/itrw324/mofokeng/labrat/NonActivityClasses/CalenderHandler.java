package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;

import java.util.Calendar;

/**
 * Created by Mofokeng on 05-Dec-16.
 */

public class CalenderHandler {

    private Intent intent;
    private Calendar calendarBegin;
    private Calendar calendarEnd;
    private int year;

    public CalenderHandler() {

        calendarBegin = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();

        year = calendarBegin.get(Calendar.YEAR);

        intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(Events.CONTENT_URI);
    }

    public Intent addClassToCalendar(Class campusClass){

        calendarBegin.set(Calendar.YEAR,year);
        calendarEnd.set(Calendar.YEAR,year);

        calendarBegin.set(Calendar.DAY_OF_WEEK,campusClass.getCalenderDay());
        calendarEnd.set(Calendar.DAY_OF_WEEK,campusClass.getCalenderDay());

        calendarBegin.set(Calendar.HOUR_OF_DAY,campusClass.getStartHourMins()[0]);
        calendarBegin.set(Calendar.MINUTE,campusClass.getStartHourMins()[1]);

        calendarBegin.set(Calendar.HOUR_OF_DAY,campusClass.getStartHourMins()[0]);
        calendarBegin.set(Calendar.MINUTE,campusClass.getStartHourMins()[1]);

        calendarEnd.set(Calendar.HOUR_OF_DAY,campusClass.getEndHourMins()[0]);
        calendarEnd.set(Calendar.MINUTE,campusClass.getEndHourMins()[1]);

        intent.setData(Events.CONTENT_URI);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendarBegin.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendarEnd.getTimeInMillis());
        intent.putExtra(Events.TITLE, campusClass.getModule_Code()+" Class");
        intent.putExtra(Events.DESCRIPTION, "Group class");
        intent.putExtra(Events.RRULE,"FREQ=WEEKLY;COUNT=8");
        intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);

        return intent;
    }

}
