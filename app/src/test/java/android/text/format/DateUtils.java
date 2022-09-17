package android.text.format;

public class DateUtils {

    public static CharSequence getRelativeTimeSpanString(long time, long now, long minResolution) {
        return "1 hour ago";
    }

 /*   public static final int FORMAT_SHOW_TIME = 0x00001;
    public static final int FORMAT_SHOW_WEEKDAY = 0x00002;
    public static final int FORMAT_SHOW_YEAR = 0x00004;
    public static final int FORMAT_NO_YEAR = 0x00008;
    public static final int FORMAT_SHOW_DATE = 0x00010;
    public static final int FORMAT_NO_MONTH_DAY = 0x00020;
    public static final int FORMAT_12HOUR = 0x00040;
    public static final int FORMAT_24HOUR = 0x00080;

    public static final int FORMAT_ABBREV_ALL = 0x80000;
    public static final int FORMAT_ABBREV_TIME = 0x04000;
    public static final int FORMAT_ABBREV_WEEKDAY = 0x08000;
    public static final int FORMAT_ABBREV_MONTH = 0x10000;
    public static final int FORMAT_NUMERIC_DATE = 0x20000;
    public static final int FORMAT_ABBREV_RELATIVE = 0x40000;

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;

    private static final int DAY_IN_MS = 24 * 60 * 60 * 1000;
    private static final int EPOCH_JULIAN_DAY = 2440588;

    private static final FormatterCache CACHED_FORMATTERS = new FormatterCache();

    static class FormatterCache
            extends LruCache<String, android.icu.text.RelativeDateTimeFormatter> {
        FormatterCache() {
            super(8);
        }
    }

    private static final FormatterCache_ CACHED_FORMATTERS_ = new FormatterCache_();

    static class FormatterCache_ extends LruCache<String, DateFormat> {
        FormatterCache_() {
            super(8);
        }
    }

    public static CharSequence getRelativeTimeSpanString(long time, long now, long minResolution) {
        int flags = FORMAT_SHOW_DATE | FORMAT_SHOW_YEAR | FORMAT_ABBREV_MONTH;
        final DisplayContext displayContext =
                DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE;
        return getRelativeTimeSpanString(Locale.getDefault(), TimeZone.getDefault(), time, now, minResolution, flags,
                displayContext);
    }

    public static String getRelativeTimeSpanString(Locale locale, java.util.TimeZone tz, long time,
                                                   long now, long minResolution, int flags, DisplayContext displayContext) {
        if (locale == null) {
            throw new NullPointerException("locale == null");
        }
        if (tz == null) {
            throw new NullPointerException("tz == null");
        }
        ULocale icuLocale = ULocale.forLocale(locale);
        android.icu.util.TimeZone icuTimeZone = icuTimeZone(tz);
        return getRelativeTimeSpanString(icuLocale, icuTimeZone, time, now, minResolution, flags,
                displayContext);
    }

    private static String getRelativeTimeSpanString(ULocale icuLocale,
                                                    android.icu.util.TimeZone icuTimeZone, long time, long now, long minResolution,
                                                    int flags,
                                                    DisplayContext displayContext) {

        long duration = Math.abs(now - time);
        boolean past = (now >= time);

        android.icu.text.RelativeDateTimeFormatter.Style style;
        if ((flags & (FORMAT_ABBREV_RELATIVE | FORMAT_ABBREV_ALL)) != 0) {
            style = android.icu.text.RelativeDateTimeFormatter.Style.SHORT;
        } else {
            style = android.icu.text.RelativeDateTimeFormatter.Style.LONG;
        }

        android.icu.text.RelativeDateTimeFormatter.Direction direction;
        if (past) {
            direction = android.icu.text.RelativeDateTimeFormatter.Direction.LAST;
        } else {
            direction = android.icu.text.RelativeDateTimeFormatter.Direction.NEXT;
        }

        // 'relative' defaults to true as we are generating relative time span
        // string. It will be set to false when we try to display strings without
        // a quantity, such as 'Yesterday', etc.
        boolean relative = true;
        int count;
        android.icu.text.RelativeDateTimeFormatter.RelativeUnit unit;
        android.icu.text.RelativeDateTimeFormatter.AbsoluteUnit aunit = null;

        if (duration < MINUTE_IN_MILLIS && minResolution < MINUTE_IN_MILLIS) {
            count = (int) (duration / SECOND_IN_MILLIS);
            unit = android.icu.text.RelativeDateTimeFormatter.RelativeUnit.SECONDS;
        } else if (duration < HOUR_IN_MILLIS && minResolution < HOUR_IN_MILLIS) {
            count = (int) (duration / MINUTE_IN_MILLIS);
            unit = android.icu.text.RelativeDateTimeFormatter.RelativeUnit.MINUTES;
        } else if (duration < DAY_IN_MILLIS && minResolution < DAY_IN_MILLIS) {
            // Even if 'time' actually happened yesterday, we don't format it as
            // "Yesterday" in this case. Unless the duration is longer than a day,
            // or minResolution is specified as DAY_IN_MILLIS by user.
            count = (int) (duration / HOUR_IN_MILLIS);
            unit = android.icu.text.RelativeDateTimeFormatter.RelativeUnit.HOURS;
        } else if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
            count = Math.abs(dayDistance(icuTimeZone, time, now));
            unit = android.icu.text.RelativeDateTimeFormatter.RelativeUnit.DAYS;

            if (count == 2) {
                // Some locales have special terms for "2 days ago". Return them if
                // available. Note that we cannot set up direction and unit here and
                // make it fall through to use the call near the end of the function,
                // because for locales that don't have special terms for "2 days ago",
                // icu4j returns an empty string instead of falling back to strings
                // like "2 days ago".
                String str;
                if (past) {
                    synchronized (CACHED_FORMATTERS) {
                        str = getFormatter(icuLocale, style, displayContext).format(
                                android.icu.text.RelativeDateTimeFormatter.Direction.LAST_2,
                                android.icu.text.RelativeDateTimeFormatter.AbsoluteUnit.DAY);
                    }
                } else {
                    synchronized (CACHED_FORMATTERS) {
                        str = getFormatter(icuLocale, style, displayContext).format(
                                android.icu.text.RelativeDateTimeFormatter.Direction.NEXT_2,
                                android.icu.text.RelativeDateTimeFormatter.AbsoluteUnit.DAY);
                    }
                }
                if (str != null && !str.isEmpty()) {
                    return str;
                }
                // Fall back to show something like "2 days ago".
            } else if (count == 1) {
                // Show "Yesterday / Tomorrow" instead of "1 day ago / In 1 day".
                aunit = android.icu.text.RelativeDateTimeFormatter.AbsoluteUnit.DAY;
                relative = false;
            } else if (count == 0) {
                // Show "Today" if time and now are on the same day.
                aunit = android.icu.text.RelativeDateTimeFormatter.AbsoluteUnit.DAY;
                direction = android.icu.text.RelativeDateTimeFormatter.Direction.THIS;
                relative = false;
            }
        } else if (minResolution == WEEK_IN_MILLIS) {
            count = (int) (duration / WEEK_IN_MILLIS);
            unit = android.icu.text.RelativeDateTimeFormatter.RelativeUnit.WEEKS;
        } else {
            Calendar timeCalendar = createIcuCalendar(icuTimeZone, icuLocale, time);
            // The duration is longer than a week and minResolution is not
            // WEEK_IN_MILLIS. Return the absolute date instead of relative time.

            // Bug 19822016:
            // If user doesn't supply the year display flag, we need to explicitly
            // set that to show / hide the year based on time and now. Otherwise
            // formatDateRange() would determine that based on the current system
            // time and may give wrong results.
            if ((flags & (FORMAT_NO_YEAR | FORMAT_SHOW_YEAR)) == 0) {
                Calendar nowCalendar = createIcuCalendar(icuTimeZone, icuLocale,
                        now);

                if (timeCalendar.get(Calendar.YEAR) != nowCalendar.get(Calendar.YEAR)) {
                    flags |= FORMAT_SHOW_YEAR;
                } else {
                    flags |= FORMAT_NO_YEAR;
                }
            }
            return format(icuLocale, timeCalendar, flags, displayContext);
        }

        synchronized (CACHED_FORMATTERS) {
            android.icu.text.RelativeDateTimeFormatter formatter =
                    getFormatter(icuLocale, style, displayContext);
            if (relative) {
                return formatter.format(count, direction, unit);
            } else {
                return formatter.format(direction, aunit);
            }
        }
    }

    private static android.icu.util.TimeZone icuTimeZone(java.util.TimeZone tz) {
        android.icu.util.TimeZone icuTimeZone = android.icu.util.TimeZone.getTimeZone(tz.getID());
        icuTimeZone.freeze(); // Optimization - allows the timezone to be copied cheaply.
        return icuTimeZone;
    }

    private static android.icu.text.RelativeDateTimeFormatter getFormatter(
            ULocale locale, android.icu.text.RelativeDateTimeFormatter.Style style,
            DisplayContext displayContext) {
        String key = locale + "\t" + style + "\t" + displayContext;
        android.icu.text.RelativeDateTimeFormatter formatter = CACHED_FORMATTERS.get(key);
        if (formatter == null) {
            formatter = android.icu.text.RelativeDateTimeFormatter.getInstance(
                    locale, null, style, displayContext);
            CACHED_FORMATTERS.put(key, formatter);
        }
        return formatter;
    }

    public static Calendar createIcuCalendar(android.icu.util.TimeZone icuTimeZone, ULocale icuLocale,
                                             long timeInMillis) {
        Calendar calendar = new GregorianCalendar(icuTimeZone, icuLocale);
        calendar.setTimeInMillis(timeInMillis);
        return calendar;
    }

    public static String format(ULocale icuLocale, Calendar time, int flags,
                                DisplayContext displayContext) {
        String skeleton = toSkeleton(time, time, flags);
        String key = skeleton + "\t" + icuLocale + "\t" + time.getTimeZone();
        synchronized (CACHED_FORMATTERS_) {
            DateFormat formatter = CACHED_FORMATTERS_.get(key);
            if (formatter == null) {
                DateTimePatternGenerator generator = DateTimePatternGenerator.getInstance(
                        icuLocale);
                formatter = new SimpleDateFormat(generator.getBestPattern(skeleton), icuLocale);
                CACHED_FORMATTERS_.put(key, formatter);
            }
            formatter.setContext(displayContext);
            return formatter.format(time);
        }
    }

    public static String toSkeleton(Calendar startCalendar, Calendar endCalendar, int flags) {
        if ((flags & FORMAT_ABBREV_ALL) != 0) {
            flags |= FORMAT_ABBREV_MONTH | FORMAT_ABBREV_TIME | FORMAT_ABBREV_WEEKDAY;
        }

        String monthPart = "MMMM";
        if ((flags & FORMAT_NUMERIC_DATE) != 0) {
            monthPart = "M";
        } else if ((flags & FORMAT_ABBREV_MONTH) != 0) {
            monthPart = "MMM";
        }

        String weekPart = "EEEE";
        if ((flags & FORMAT_ABBREV_WEEKDAY) != 0) {
            weekPart = "EEE";
        }

        String timePart = "j"; // "j" means choose 12 or 24 hour based on current locale.
        if ((flags & FORMAT_24HOUR) != 0) {
            timePart = "H";
        } else if ((flags & FORMAT_12HOUR) != 0) {
            timePart = "h";
        }

        // If we've not been asked to abbreviate times, or we're using the 24-hour clock (where it
        // never makes sense to leave out the minutes), include minutes. This gets us times like
        // "4 PM" while avoiding times like "16" (for "16:00").
        if ((flags & FORMAT_ABBREV_TIME) == 0 || (flags & FORMAT_24HOUR) != 0) {
            timePart += "m";
        } else {
            // Otherwise, we're abbreviating a 12-hour time, and should only show the minutes
            // if they're not both "00".
            if (!(onTheHour(startCalendar) && onTheHour(endCalendar))) {
                timePart = timePart + "m";
            }
        }

        if (fallOnDifferentDates(startCalendar, endCalendar)) {
            flags |= FORMAT_SHOW_DATE;
        }

        if (fallInSameMonth(startCalendar, endCalendar) && (flags & FORMAT_NO_MONTH_DAY) != 0) {
            flags &= (~FORMAT_SHOW_WEEKDAY);
            flags &= (~FORMAT_SHOW_TIME);
        }

        if ((flags & (FORMAT_SHOW_DATE | FORMAT_SHOW_TIME | FORMAT_SHOW_WEEKDAY)) == 0) {
            flags |= FORMAT_SHOW_DATE;
        }

        // If we've been asked to show the date, work out whether we think we should show the year.
        if ((flags & FORMAT_SHOW_DATE) != 0) {
            if ((flags & FORMAT_SHOW_YEAR) != 0) {
                // The caller explicitly wants us to show the year.
            } else if ((flags & FORMAT_NO_YEAR) != 0) {
                // The caller explicitly doesn't want us to show the year, even if we otherwise
                // would.
            } else if (!fallInSameYear(startCalendar, endCalendar) || !isThisYear(startCalendar)) {
                flags |= FORMAT_SHOW_YEAR;
            }
        }

        StringBuilder builder = new StringBuilder();
        if ((flags & (FORMAT_SHOW_DATE | FORMAT_NO_MONTH_DAY)) != 0) {
            if ((flags & FORMAT_SHOW_YEAR) != 0) {
                builder.append("y");
            }
            builder.append(monthPart);
            if ((flags & FORMAT_NO_MONTH_DAY) == 0) {
                builder.append("d");
            }
        }
        if ((flags & FORMAT_SHOW_WEEKDAY) != 0) {
            builder.append(weekPart);
        }
        if ((flags & FORMAT_SHOW_TIME) != 0) {
            builder.append(timePart);
        }
        return builder.toString();
    }

    private static boolean fallOnDifferentDates(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)
                || c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)
                || c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH);
    }

    private static boolean onTheHour(Calendar c) {
        return c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0;
    }

    private static boolean fallInSameMonth(Calendar c1, Calendar c2) {
        return c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    private static boolean fallInSameYear(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    private static boolean isThisYear(Calendar c) {
        Calendar now = (Calendar) c.clone();
        now.setTimeInMillis(System.currentTimeMillis());
        return c.get(Calendar.YEAR) == now.get(Calendar.YEAR);
    }

    // Return the date difference for the two times in a given timezone.
    private static int dayDistance(android.icu.util.TimeZone icuTimeZone, long startTime,
                                   long endTime) {
        return julianDay(icuTimeZone, endTime) - julianDay(icuTimeZone, startTime);
    }

    private static int julianDay(android.icu.util.TimeZone icuTimeZone, long time) {
        long utcMs = time + icuTimeZone.getOffset(time);
        return (int) (utcMs / DAY_IN_MS) + EPOCH_JULIAN_DAY;
    }*/
}
