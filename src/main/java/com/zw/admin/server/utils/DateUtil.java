package com.zw.admin.server.utils;

import com.zw.admin.server.dto.DateNumDto;
import com.zw.admin.server.dto.DateWeekly;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;

/**
 * 时间工具类
 *
 * @author Think
 */
public class DateUtil {

    /*
     *常用时间格式模板
     */
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /*
     *常用时间格式模板
     */
    public static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 根据生日获取年龄
     *
     * @param birthDay
     * @return
     * @throws ParseException
     */
    public static int getAgeByBirth(Date birthDay) throws ParseException {
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { // 出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR); // 当前年份
        int monthNow = cal.get(Calendar.MONTH); // 当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); // 当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth; // 计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;// 当前日期在生日之前，年龄减一
            } else {
                age--;// 当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    /**
     * 功能描述:格式化时间
     *
     * @param date
     * @param pattern
     * @return java.lang.String
     * @author larry
     * @Date 2020/10/21 16:43
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static long getDayPlus(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }

    /**
     * 功能描述:根据开始和结束时间返回中间时间集合
     *
     * @param beginDate
     * @param endDate
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/21 14:52
     */
    public static List<String> getBetweenDays(String beginDate, String endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(beginDate));
        for (long d = cal.getTimeInMillis(); d <= sdf.parse(endDate).getTime(); d = getDayPlus(cal)) {
            //System.out.println(sdf.format(d));
            dates.add(sdf.format(d));
        }
        return dates;
    }

    /**
     * 功能描述:根据开始和结束时间返回中间时间集合
     *
     * @param beginDate
     * @param endDate
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/21 14:52
     */
    public static List<String> getBetweenDays(Date beginDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        for (long d = cal.getTimeInMillis(); d <= endDate.getTime(); d = getDayPlus(cal)) {
            //System.out.println(sdf.format(d));
            dates.add(sdf.format(d));
        }
        return dates;
    }

    /**
     * 功能描述:获取开始时间和结束时间内的月集合
     *
     * @param minDate
     * @param maxDate
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/26 11:32
     */
    public static List<String> getMonthBetween(Date minDate, Date maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 功能描述:获取开始时间和结束时间内的月集合
     *
     * @param startDate
     * @param endDate
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/26 11:43
     */
    public static List<String> getMonthBetween(String startDate, String endDate) throws Exception {

        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Date minDate = sdf.parse(startDate);
        Date maxDate = sdf.parse(endDate);

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 获取下个月的最后一天
     *
     * @return
     */
    public static LocalDate getNextMonthLastDay() {
        return LocalDate.now()
                .plus(1, MONTHS)
                .with(temporal -> temporal.with(DAY_OF_MONTH, temporal.range(DAY_OF_MONTH).getMaximum()));
    }

    /**
     * 获取今年第一天
     * @return
     */
    public static LocalDate getYearFirstDay() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取今年最后一天
     * @return
     */
    public static LocalDate getYearLastDay() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 获取上个星期最后一天
     * @return
     */
    public static LocalDate getLastWeekLastDay() {
        return LocalDate.now()
                .plus(-1, WEEKS)
                .with(temporal -> temporal.with(DAY_OF_WEEK, temporal.range(DAY_OF_WEEK).getMaximum()));
    }

    /**
     * 获取当前星期第一天
     * @return
     */
    public static LocalDate getWeekFirstDay() {
        return LocalDate.now().with(temporal -> temporal.with(DAY_OF_WEEK, temporal.range(DAY_OF_WEEK).getMinimum()));
    }

    /**
     * 获取当前星期最后一天
     * @return
     */
    public static LocalDate getWeekLastDay() {
        return LocalDate.now().with(temporal -> temporal.with(DAY_OF_WEEK, temporal.range(DAY_OF_WEEK).getMaximum()));
    }

    /**
     * 获取本月第一天
     * @return
     */
    public static Date getMonthStartDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    /**
     * 获取本月最后一天
     * @return
     */
    public static Date getMonthEndDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 功能描述:本周一
     *
     * @param
     * @return java.util.Date
     * @author larry
     * @Date 2020/10/31 15:06
     */
    public static Date getWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    /**
     * 获取当年的第一天
     * @param
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     * @param
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 功能描述:获取本周每天
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/31 15:10
     */
    public static List<String> getThisWeek() {
        //获取本周第一天
        Date startDate = getWeekStartDate();
        Date endDate = new Date();
        //获取当前天
        return getBetweenDays(startDate, endDate);
    }

    /**
     * 功能描述:获取本月每天
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/31 15:26
     */
    public static List<String> getThisMonth() {
        Date startDate = getMonthStartDate();
        Date endDate = new Date();
        //获取当前天
        return getBetweenDays(startDate, endDate);
    }

    /**
     * 功能描述:获取本年的每个月
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/31 15:27
     */
    public static List<String> getThisYearMonth() {
        Date startDate = getCurrYearFirst();
        Date endDate = new Date();
        return getMonthBetween(startDate, endDate);
    }

    /**
     * 功能描述:获取本年的每个月
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/31 15:27
     */
    public static List<String> getThisYearMonthAll() {
        Date startDate = getCurrYearFirst();
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        Date yearLast = getYearLast(currentYear);
        return getMonthBetween(startDate, yearLast);
    }

    public static List<DateNumDto> formatDateNumDto(List<DateNumDto> list){
        if(!CollectionUtils.isEmpty(list)){
            for(DateNumDto dateNumDto : list){
                if(!StringUtils.isEmpty(dateNumDto.getDate())){
                    dateNumDto.setDate(dateNumDto.getDate().substring(5));
                }
            }
        }
        return list;
    }

    /**
     * 功能描述:根据时间类型获取当天是本月，本年，本周的第几天
     *
     * @param dateType
     * @return int
     * @author larry
     * @Date 2020/11/5 9:24
     */
    public static int getWeekMonthYear(String dateType) {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);// 此处可换为具体某一时间
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        int yearDay = calendar.get(Calendar.DAY_OF_YEAR);
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay = weekDay - 1;
        }
        if ("week".equals(dateType)) {
            return weekDay;
        } else if ("month".equals(dateType)) {
            return monthDay;
        } else if ("year".equals(dateType)) {
            return yearDay;
        } else {
            return 1;
        }
    }

    /**
     * 功能描述:获取最近n天
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/17 13:45
     */
    public static List<String> getSevenDate(int n) {
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        for (int i = 0; i < n; i++) {
            Date date = DateUtils.addDays(new Date(), -i);
            String formatDate = sdf.format(date);
            dateList.add(formatDate);
        }
        return dateList;
    }

    /**
     * 功能描述:获取最近n天
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/17 13:45
     */
    public static List<String> getDateByNum(int n) {
        //往前推n天
        Date min = DateUtils.addDays(new Date(), -n+1);
        //当天
        Date max = new Date();
        return getBetweenDays(min,max);
    }

    public static List<DateWeekly> getWeekly(String sDateTime, String eDateTime) {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(sDateTime);
            d2 = format.parse(eDateTime);
            //每周天数(7天)
            long week = 24 * 60 * 60 * 1000 * 7;
            long day = 24 * 60 * 60 * 1000;
            //毫秒ms
            long diff = d2.getTime() + 1 - d1.getTime() + 1;
            if (diff <= 0) {
                return null;
            }
            //毫秒转天
            long diffDays = diff / (24 * 60 * 60 * 1000);

            //创建所有周次及开始结束时间list
            List<DateWeekly> list = new ArrayList<>();
            //共多少周次
            if (diffDays > 0 & diffDays < 7) {

            }
            long endWeekly = diffDays / 7 == 0 ? (diffDays / 7) : (diffDays / 7) + 1;
            if (diffDays > 0 & diffDays < 7) {
                endWeekly = 1;
            }
            for (int i = 1; i <= endWeekly; i++) {
                DateWeekly dateWeekly = new DateWeekly();
                //当前周次
                dateWeekly.setWeekly(i + 1);
                //当前周次结束日期(当前周次开始日期为 开始日期 = 周次*周+ 开始日期)
                long s = i * week;
                long e = (i - 1) * week;
                long edt = s + d1.getTime() - day;
                //当前周次结束日期(结束日期为 =  开始日期 + 周次 * 周 )
                long sdt = e + d1.getTime();
                String sdate = format.format(sdt);
                String edate = format.format(edt);
                //将得到是数据放入list
                dateWeekly.setWeekly(i);
                dateWeekly.setSDate(sdate);
                dateWeekly.setEDate(edate);
                list.add(i - 1, dateWeekly);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 功能描述:时间转成时间戳
     *
     * @param date
     * @return java.lang.Long
     * @author larry
     * @Date 2020/11/21 14:08
     */
    public static Long dateToStamp(Date date) {
        long time = date.getTime();
        return time;
    }

    /**
     * 功能描述:根据时间戳转成时间字符串
     *
     * @param stamp
     * @return java.lang.String
     * @author larry
     * @Date 2020/11/21 14:13
     */
    public static Date stampToDate(String stamp) {
        Long timestap = Long.valueOf(stamp);
        Date date = new Date(timestap);
        return date;
    }

    public static Date getToday(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getYesterday(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE,-1);
        return calendar.getTime();
    }


}
