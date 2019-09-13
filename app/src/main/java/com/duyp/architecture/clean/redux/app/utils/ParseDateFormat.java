package com.duyp.architecture.clean.redux.app.utils;

import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duyp.architecture.clean.redux.data.api.ApiConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ParseDateFormat {

    private static final ParseDateFormat INSTANCE = new ParseDateFormat();

    private final Object lock = new Object();

    private final DateFormat dateFormat;

    private ParseDateFormat() {
        dateFormat = new SimpleDateFormat(ApiConstants.DATE_TIME_FORMAT, Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getDefault());
    }

    @NonNull
    public static CharSequence getTimeAgo(@Nullable final String toParse) {
        try {
            final Date parsedDate = getInstance().dateFormat.parse(toParse);
            final long now = System.currentTimeMillis();
            return DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), now, DateUtils.SECOND_IN_MILLIS);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    @NonNull
    public static CharSequence getTimeAgo(@Nullable final Date parsedDate) {
        if (parsedDate != null) {
            final long now = System.currentTimeMillis();
            return DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), now, DateUtils.SECOND_IN_MILLIS);
        }
        return "N/A";
    }

    @NonNull
    public static String toGithubDate(@NonNull final Date date) {
        return getInstance().format(date);
    }

    @NonNull
    public static String prettifyDate(final long timestamp) {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date(timestamp));
    }

    @Nullable
    public static Date getDateFromString(@NonNull final String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private static ParseDateFormat getInstance() {
        return INSTANCE;
    }

    private static String getDateByDays(final int days) {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat s = new SimpleDateFormat(ApiConstants.DATE_TIME_FORMAT, Locale.ENGLISH);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public static String getLastWeekDate() {
        return getDateByDays(-7);
    }

    @NonNull
    public String format(final Date date) {
        synchronized (lock) {
            return dateFormat.format(date);
        }
    }

}
