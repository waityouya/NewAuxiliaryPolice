package com.auxiliary.myapplication.util;

import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class DateUtil {


    /**
     * 时间的各种格式器
     */
    public static final String DATE_STYLE = "yyyy-MM-dd";
    public static final String DATE_STYLE2 = "yyyyMMdd";
    public static final String DATE_YEAR_MONTH = "yyyyMM";
    public static final String DATE_YM = "yyyy-MM";
    public static final String DATE_TIME_STYLE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_STYLE3 = "yyyyMMddHHmmss";
    public static final String DATE_TIME_STYLE4 = "yyyyMMddHHmmssSSS";
    public static final String DATE_YEAR = "yyyy";
    public static final String DATE_TIME = "HH:mm:ss";

   /* private static final SimpleDateFormat SDF1 = new SimpleDateFormat(DATE_STYLE);
    private static final SimpleDateFormat SDF2 = new SimpleDateFormat(DATE_TIME_STYLE);
    private static final SimpleDateFormat SDF3 = new SimpleDateFormat(DATE_TIME_STYLE3);
    private static final SimpleDateFormat SDF4 = new SimpleDateFormat(DATE_TIME_STYLE4);
    private static final SimpleDateFormat SDF5 = new SimpleDateFormat(DATE_TIME);
    private static final SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);*/

    public static synchronized String formatDate(Date date) {
        SimpleDateFormat SDF1 = new SimpleDateFormat(DATE_STYLE);
        String dateStr = SDF1.format(date);
        return dateStr;
    }

    public static synchronized String formatDate(Date date,String sdf) {
        SimpleDateFormat SDF1 = new SimpleDateFormat(sdf);
        String dateStr = SDF1.format(date);
        return dateStr;
    }

    public static synchronized String formatDate2(Date date) {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        String dateStr = SDF6.format(date);
        return dateStr;
    }

    public static synchronized String formatDateTime(Date date) {
        SimpleDateFormat SDF2 = new SimpleDateFormat(DATE_TIME_STYLE);
        String dateStr = SDF2.format(date);
        return dateStr;
    }

    public static synchronized String formatTime(Date date) {
        SimpleDateFormat SDF5 = new SimpleDateFormat(DATE_TIME);
        String dateStr = SDF5.format(date);
        return dateStr;
    }

    public static synchronized String formatDateTime2(Date date) {
        SimpleDateFormat SDF3 = new SimpleDateFormat(DATE_TIME_STYLE3);
        String dateStr = SDF3.format(date);
        return dateStr;
    }

    public static synchronized Date parseToDate(String dateStr) {
        try {
            SimpleDateFormat SDF1 = new SimpleDateFormat(DATE_STYLE);
            Date date = SDF1.parse(dateStr); // sdf1.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized Date parseToDateTime(String dateStr) {
        try {
            SimpleDateFormat SDF2 = new SimpleDateFormat(DATE_TIME_STYLE);
            Date date = SDF2.parse(dateStr); // sdf1.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized Date parseToDateTimeEmpty(String dateStr) {
        try {
            SimpleDateFormat SDF3 = new SimpleDateFormat(DATE_TIME_STYLE3);
            Date date = SDF3.parse(dateStr);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 生成17位编码
    public static synchronized String formatDateToLongChar(Date date) {
        SimpleDateFormat SDF4 = new SimpleDateFormat(DATE_TIME_STYLE4);
        String dateStr = SDF4.format(date);
        return dateStr;
    }

    /**
     * 转换时间的格式yyyy-MM-dd HH:mm:ss到yyyyMMddHHmmss
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String convertToDateStr(String date) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(date.substring(0, 4));
        sbr.append(date.substring(5, 7));
        sbr.append(date.substring(8, 10));
        sbr.append(date.substring(11, 13));
        sbr.append(date.substring(14, 16));
        sbr.append(date.substring(17, 19));
        return sbr.toString();

    }

    /**
     *
     * 功能描述: <br>
     * 当前日期加减n天后的日期
     *
     * @param n
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String nDaysAftertoday(String str, int n) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_STYLE);
        try {
            Date myDate = df.parse(str);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(myDate);
            rightNow.add(Calendar.DAY_OF_MONTH, +n);
            return df.format(rightNow.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * 功能描述: <br>
     * 当前日期加减n天后的日期
     *
     * @param n
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String nHoursAftertoday(Date date, int n) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_STYLE3);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.HOUR_OF_DAY, +n);
        return df.format(rightNow.getTime());
    }

    /**
     *
     * 功能描述: <br>
     * 当前日期加减n天后的日期
     *
     * @param n
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String nDaysAftertoday(Date date, int n) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_STYLE2);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return df.format(rightNow.getTime());
    }



    public static String nDaysAftertoday(int n,String sdf) {
        SimpleDateFormat df = new SimpleDateFormat(sdf);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return df.format(rightNow.getTime());
    }

    public static String nYearsAftertoday(Date date, int n) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_YEAR);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.YEAR, +n);
        return df.format(rightNow.getTime());
    }

    public static int compareDate(Date date1, Date date2) {

        try {

            if (date1.getTime() > date2.getTime()) {
                return 1;
            } else if (date1.getTime() < date2.getTime()) {
                return -1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return 0;
    }

    public static boolean validateTime(String date) {
        try {
            new SimpleDateFormat(DATE_TIME_STYLE).parse(date);
        } catch (ParseException e) {
            return false;
        }
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(datePattern1);
        Matcher match = pattern.matcher(date);
        return match.matches();
    }

    public static String getHours() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 小时
        if (hour == 0) {
            return "00";
        } else if (hour < 11) {
            return "0" + (hour - 1);
        } else {
            return String.valueOf(hour - 1);
        }
    }

    /**
     *
     * 获取当前时间相隔i小时的时间<br>
     * 格式是：yyyyMMddHHmm
     *
     * @param i
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String getTimeToMinute(int i) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + i);
        String time = sdf.format(cal.getTime());
        return time.substring(0, 12);
    }

    public static String getFirstDayOfMonth() {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = SDF6.format(c.getTime());
        return first;
    }

    public static String getFirstDayOfMonth(String dateStr) {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        Date date = parseToDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = SDF6.format(c.getTime());
        return first;
    }

    public static String getLastDayOfFrontMonth() {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 0);
        String first = SDF6.format(c.getTime());
        return first;
    }

    public static String getLastDayOfFrontMonth(String dateStr) {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        Date date = parseToDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 0);
        String first = SDF6.format(c.getTime());
        return first;
    }

    /**
     *
     * 功能描述: h获取上一个季度的最后一天<br>
     * 〈功能详细描述〉
     *
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String getLastDayOfFrontQuarter() {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);// 获取当前月份0-11
        int n = month / 3;
        month = n * 3;
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 0);
        String first = SDF6.format(c.getTime());
        return first;
    }

    public static String getLastDayOfFrontQuarter(String dateStr) {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        Date date = parseToDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);// 获取当前月份0-11
        int n = month / 3;
        month = n * 3;
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 0);
        String first = SDF6.format(c.getTime());
        return first;
    }

    public static String getFirstDayOfYear() {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 1);
        String first = SDF6.format(c.getTime());
        return first;
    }

    public static String getFirstDayOfYear(String dateStr) {
        SimpleDateFormat SDF6 = new SimpleDateFormat(DATE_STYLE2);
        Date date = parseToDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 1);
        String first = SDF6.format(c.getTime());
        return first;
    }

    /**
     *
     * 功能描述: 获取日期的星期<br>
     * 〈功能详细描述〉
     *
     * @param
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String getWeek(String dateStr) {
        StringBuilder sb = new StringBuilder("");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date1 = null;
        try {
            date1 = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("获取当前月自然周，日期格式转换错误！11");
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date1);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int count = 0;
        for (int i = 1; i <= days; i++) {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = null;
            try {
                date2 = dateFormat1.parse(dateStr + "-" + i);
            } catch (ParseException e) {
                System.out.println("获取当前月自然周，日期格式转换错误！22");
                e.printStackTrace();
            }
            calendar.clear();
            calendar.setTime(date2);
            int k = new Integer(calendar.get(Calendar.DAY_OF_WEEK));
            int startDay = 0;
            int endDay = 0;
            // 若当天是周日
            if (k == 1) {
                count++;
                if (i - 6 <= 1) {
                    startDay = 1;
                } else {
                    startDay = i - 6;
                }
                endDay = i;
            }
            // 若是本月最好一天，且不是周日
            if (k != 1 && i == days) {
                count++;
                startDay = i - k + 2;
                endDay = i;
            }
            if(startDay != 0 && endDay != 0){
                if(sb.toString() == null || "".equals(sb.toString())){
                    sb.append(startDay + "-" + endDay);
                }else{
                    sb.append("," + startDay + "-" + endDay);
                }
            }
        }

        return sb.toString();

    }


    /**
     *
     * 功能描述: <br>
     * 当前日期加减n天后的日期
     *
     * @param n
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String nMonthsAftertoday(Date date, int n) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_YEAR_MONTH);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, +n);
        return df.format(rightNow.getTime());
    }

    /**
     *
     * 功能描述: <br>
     * 当前日期加减n天后的日期
     *
     * @param n
     * @return
     * @throws ParseException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String nMonthsAftertoday(String date, int n) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_YEAR_MONTH);
        Calendar rightNow = Calendar.getInstance();
        try {
            rightNow.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rightNow.add(Calendar.MONTH, +n);
        return df.format(rightNow.getTime());
    }


    /**
     *
     * 功能描述: <br>
     * 当前日期加减n天后的日期
     *
     * @param
     * @return
     * @throws ParseException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String getDateByFormat(String fmt, Date date){
        SimpleDateFormat df = new SimpleDateFormat(fmt);
        return df.format(date);
    }

}
