package com.example.projectprogresstracker;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalcHelper {

    /**
     * method to calculate daysleft
     */
    public String getDaysLeft(String dateTill) {
        SimpleDateFormat sdf, inputFormat;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar c = Calendar.getInstance();
        int mStartYear = c.get(Calendar.YEAR);
        int mStartMonth = c.get(Calendar.MONTH) + 1;
        int mStartDay = c.get(Calendar.DAY_OF_MONTH);


        String mDate = mStartYear + "-" + mStartMonth + "-" + mStartDay;
        Date mdate = null;
        try {
            mdate = inputFormat.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        try {
            date = inputFormat.parse(dateTill);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int diffInDays = (int) ((date.getTime() - mdate.getTime()) / (1000 * 60 * 60 * 24));

        return String.valueOf(diffInDays);
    }

    /**
     * method to calculate daysleftBetnFromAndToDate
     */
    public String getDaysLeftFromAndTo(String dateFrom,String dateTill) {
        SimpleDateFormat inputFormat;
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFr = null;
        try {
            dateFr = inputFormat.parse(dateFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateTo = null;
        try {
            dateTo = inputFormat.parse(dateTill);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int diffInDays = (int) ((dateTo.getTime() - dateFr.getTime()) / (1000 * 60 * 60 * 24));

        return String.valueOf(diffInDays);
    }

    /**
     * method to calculate target
     */
    public String getTarget(String startDate, String endDate) {
        SimpleDateFormat sdf, inputFormat;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date mdate = null;
        try {
            mdate = inputFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        try {
            date = inputFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int diffInDays = (int) ((date.getTime() - mdate.getTime()) / (1000 * 60 * 60 * 24));

        double target = 100.00 / (Double.valueOf(diffInDays));
        DecimalFormat df = new DecimalFormat("0.00");


        return df.format(target);


    }
}
