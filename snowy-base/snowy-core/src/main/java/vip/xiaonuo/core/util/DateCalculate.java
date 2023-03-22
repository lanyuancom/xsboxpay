package vip.xiaonuo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @since 2016-09-18  & JDK 1.8.0_91
 */
public class DateCalculate {
    static Logger logger = LoggerFactory.getLogger(DateCalculate.class);

    /**
     * 切割时间段
     * 支持每月/每天/每小时/每分钟交易金额(可分应用平台统计)
     * @param dateType 日期类型 M(每月)/D(每天)/H(每小时)/N(每分钟)
     * M：日期段应为当年月份以内 且 日期必须是01 时分秒必须是 00:00:00  例如：2016-06-01 00:00:00 2016-10-01 00:00:00
     * D: 日期段应为一月内 且 日期应当是01或31  时分秒必须是 00:00:00   例如：2016-10-01 00:00:00 2016-10-31 00:00:00
     * H：日期段应为一天内  且 时分秒必须是 00:00:00   例如：2016-10-01 00:00:00 2016-10-02 00:00:00
     * N：日期段应为一小时内  日期应相同 且 分秒必须是 xx:00:00   例如：2016-10-02 22:00:00 2016-10-02 23:00:00

     * @param dateType 交易类型 M/D/H/T -->每月/每天/每小时/每分钟
     * @param start    yyyy-MM-dd HH:mm:ss
     * @param end      yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static List<Date> cutDate(String dateType, String start, String end) {
        try {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dBegin = sdf.parse(start);
            Date dEnd = sdf.parse(end);
            return cutDate(dateType, dBegin, dEnd);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static List<Date> cutDate(String dateType, Date dBegin, Date dEnd) throws Exception {
        List<Date> listDate = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        listDate.add(calBegin.getTime());
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (calEnd.after(calBegin)) {
            switch (dateType) {
                case "M":
                    calBegin.add(Calendar.MONTH, 1);
                    break;
                case "D":
                    calBegin.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                case "H":
                    calBegin.add(Calendar.HOUR, 1);
                    break;
                case "T":
                    calBegin.add(Calendar.MINUTE, 1);
                    break;
                default:
                    return null;
            }
            if (calEnd.after(calBegin))
                listDate.add(calBegin.getTime());
            else {
                listDate.add(calEnd.getTime());
                break;
            }
        }
        return listDate;
    }


    public static void main(String[] args) throws Exception {
        String start = "2021-10-02 22:00:00";
        String end = "2021-10-02 23:00:00";
        List<Date> list = cutDate("T", start, end);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("-------------------");
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(list.get(i)));
//            System.out.println(list.get(i).getMonth()+1 +"月");
//            System.out.println(list.get(i).getDate()+"日");
//            System.out.println(list.get(i).getHours());
 //           System.out.println(list.get(i).getMinutes());
//            System.out.println(list.get(i));
        }

//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(findEndDate("M", start, end)));
    }
}