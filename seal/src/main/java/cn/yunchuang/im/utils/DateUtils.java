package cn.yunchuang.im.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * Created by bob on 2015/2/28.
 */
public class DateUtils {

    /**
     * 根据时间戳得到年份
     *
     * @param timestamp
     * @return
     */
    public static String getYear(long timestamp) {
        return String.valueOf(new SimpleDateFormat("yyyy", Locale.CHINA)
                .format(new Date(timestamp * 1000)));
    }

    public static String getMonth(long timestamp) {
        return String.valueOf(new SimpleDateFormat("MM", Locale.CHINA)
                .format(new Date(timestamp * 1000)));
    }

    public static String getDay(long timestamp) {
        return String.valueOf(new SimpleDateFormat("dd", Locale.CHINA)
                .format(new Date(timestamp * 1000)));
    }

    public static String getHour(long timestamp) {
        return String.valueOf(new SimpleDateFormat("HH", Locale.CHINA)
                .format(new Date(timestamp * 1000)));
    }

    public static String getMinute(long timestamp) {
        return String.valueOf(new SimpleDateFormat("mm", Locale.CHINA)
                .format(new Date(timestamp * 1000)));
    }

    public static String getHMS(long timestamp) {
        return String.valueOf(new SimpleDateFormat("HH:mm:ss", Locale.CHINA)
                .format(new Date(timestamp * 1000)));
    }

    public static String getYMDHM(long timestamp) {
        return String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                .format(new Date(timestamp * 1000)));
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }


    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期，如：1992-02-16
     * @param format   如：yyyy-MM-dd
     * @return
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据出生日期时间戳得到年龄，显示年龄时用到
     *
     * @param timestamp 出生日期的时间戳
     * @return
     */
    public static int getAgeFromTimeStamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int birthYear = Integer.valueOf(getYear(timestamp));
        int birthMonth = Integer.valueOf(getMonth(timestamp));
        int birthDay = Integer.valueOf(getDay(timestamp));
        int age = currentYear - birthYear - 1;
        if (birthMonth > currentMonth) {
            return age;
        } else if (birthMonth == currentMonth && birthDay > currentDay) {
            return age;
        } else {
            return age + 1;
        }
    }

    /**
     * 根据年龄得到出生日期时间戳，根据年龄筛选时用到，
     * 用不到了，年龄直接存到数据库里，直接根据年龄来筛选
     *
     * @param age 年龄
     * @return
     */
    public static long getTimeStampFromAge(int age) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - age;
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //最少从当前日期后一天，年份减去对应年龄
        String houYiTian = getSpecifiedDayAfter(year + "-" + currentMonth + "-" + currentDay);

        return date2TimeStamp(houYiTian, "yyyy-MM-dd");

    }

    /**
     * 得到当前时间
     *
     * @param dateFormat 时间格式
     * @return 转换后的时间格式
     */
    public static String getStringToday(String dateFormat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将字符串型日期转换成日期
     *
     * @param dateStr    字符串型日期
     * @param dateFormat 日期格式
     * @return
     */
    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }

    /**
     * 两个时间点的间隔时长（分钟）
     *
     * @param before 开始时间
     * @param after  结束时间
     * @return 两个时间点的间隔时长（分钟）
     */
    public static long compareMin(Date before, Date after) {
        if (before == null || after == null) {
            return 0l;
        }
        long dif = 0;
        if (after.getTime() >= before.getTime()) {
            dif = after.getTime() - before.getTime();
        } else if (after.getTime() < before.getTime()) {
            dif = after.getTime() + 86400000 - before.getTime();
        }
        dif = Math.abs(dif);
        return dif / 60000;
    }

    /**
     * 获取指定时间间隔分钟后的时间
     *
     * @param date 指定的时间
     * @param min  间隔分钟数
     * @return 间隔分钟数后的时间
     */
    public static Date addMinutes(Date date, int min) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    /**
     * 根据时间返回指定术语，自娱自乐，可自行调整
     *
     * @param hourday 小时
     * @return
     */
    public static String showTimeView(int hourday) {
        if (hourday >= 22 && hourday <= 24) {
            return "晚上";
        } else if (hourday >= 0 && hourday <= 6) {
            return "凌晨";
        } else if (hourday > 6 && hourday <= 12) {
            return "上午";
        } else if (hourday > 12 && hourday < 22) {
            return "下午";
        }
        return null;
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {
//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /**
     * 如果不满足两位数，则补充0
     *
     * @param number
     * @return
     */
    @NonNull
    public static String fillZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    /**
     * 指定时间的，n小时之后的小时是多少
     *
     * @param timestamp  单位是秒
     * @param hoursLater
     * @return 返回n小时之后是几小时
     */
    public static String getHourHoursLater(long timestamp, int hoursLater) {
        long newTs = timestamp + hoursLater * 60 * 60;
        return getHour(newTs);
    }


    /**
     * 指定时间的，n小时之后的日期是多少
     *
     * @param timestamp  单位是秒
     * @param hoursLater
     * @return
     */
    public static String getDateHoursLater(long timestamp, int hoursLater) {
        long newTs = timestamp + hoursLater * 60 * 60;
        return getDay(newTs);
    }

    /**
     * 指定时间的，n小时之后的月份是多少
     *
     * @param timestamp  单位是秒
     * @param hoursLater
     * @return
     */
    public static String getMonthHoursLater(long timestamp, int hoursLater) {
        long newTs = timestamp + hoursLater * 60 * 60;
        return getMonth(newTs);
    }

    /**
     * 指定时间的，n小时之后的年是多少
     *
     * @param timestamp  单位是秒
     * @param hoursLater
     * @return
     */
    public static String getYearHoursLater(long timestamp, int hoursLater) {
        long newTs = timestamp + hoursLater * 60 * 60;
        return getYear(newTs);
    }

    /**
     * 指定时间的，n小时之后的日期是多少
     *
     * @param timestamp  单位是秒
     * @param hoursLater
     * @return
     */
    public static String getYMDHoursLater(long timestamp, int hoursLater) {
        long newTs = timestamp + hoursLater * 60 * 60;
        return getDay(newTs);
    }
}
