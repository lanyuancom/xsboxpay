package vip.xiaonuo.pay.modular.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vip.xiaonuo.pay.modular.mchpiecesinfo.entity.MchPiecesInfo;
import vip.xiaonuo.pay.modular.mchpiecesinfo.service.MchPiecesInfoService;
import vip.xiaonuo.pay.modular.ordernotifyinfo.entity.OrderNotifyInfo;
import vip.xiaonuo.pay.modular.ordernotifyinfo.service.OrderNotifyInfoService;
import vip.xiaonuo.pay.modular.payorder.entity.PayOrder;
import vip.xiaonuo.pay.modular.payorder.service.PayOrderService;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @date 2021-10-17
 */
@Component
@EnableScheduling
public class FixedPrintTask {
    private Log log = Log.get();

    @Resource
    PayOrderService payOrderService;
    @Resource
    OrderNotifyInfoService orderNotifyInfoService;
    @Resource
    MchPiecesInfoService mchPiecesInfoService;

    //@Scheduled(cron = "0 0 2 * * ?")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void execute() throws FileNotFoundException {
            QueryWrapper<PayOrder> order = new QueryWrapper();
            order.select("GROUP_CONCAT(pay_order_id) pay_order_id");
            order.lambda().eq(PayOrder::getNotifyState,2);
            order.lambda().eq(PayOrder::getState,2);
            PayOrder orders = payOrderService.getOne(order);
            if(orders != null) {
                QueryWrapper queryWrapper = new QueryWrapper();
                //SELECT COUNT(pay_order_id) COUNT ,create_time,pay_order_id,mch_no,notify_url FROM xs_order_notify_info GROUP BY pay_order_id  HAVING COUNT(pay_order_id) <6 ORDER BY create_time DESC
                //统计所有通知失败的订单，通知次数少于5次，按 1  2  5  15分钟再通知一次
                queryWrapper.select("COUNT(pay_order_id) count " +
                        ",create_time,pay_order_id,mch_no,notify_url,app_id,way_code");
                queryWrapper.groupBy("pay_order_id");
                queryWrapper.having("COUNT(pay_order_id) < 6");
                queryWrapper.orderByDesc("create_time");
                queryWrapper.in("pay_order_id", Arrays.asList(orders.getPayOrderId().split(",")));
                List<Map<String, Object>> maps = orderNotifyInfoService.list(queryWrapper);
                if (CollUtil.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                        Thread thread=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OrderNotifyInfo notifyInfo = new OrderNotifyInfo();
                                try {
                                    notifyInfo.setPayOrderId(map.get("pay_order_id") + "");
                                    notifyInfo.setNotifyUrl(map.get("notify_url") + "");
                                    notifyInfo.setAppId(map.get("app_id") + "");
                                    notifyInfo.setWayCode(map.get("way_code") + "");
                                    notifyInfo.setMchNo(map.get("mch_no") + "");
                                    HttpResponse responseBody = HttpRequest.get(notifyInfo.getNotifyUrl())
                                            .charset(Charset.forName("UTF-8"))
                                            .timeout(6000)//超时，毫秒
                                            .execute();
                                    String body = responseBody.body();
                                    notifyInfo.setHttpStatus(responseBody.getStatus() + "");
                                    if ("SUCCESS".equals(responseBody)) {
                                        notifyInfo.setNotifyStatus(1);
                                        LambdaUpdateWrapper<PayOrder> orderUpdate = new LambdaUpdateWrapper();
                                        orderUpdate.eq(PayOrder::getPayOrderId,notifyInfo.getPayOrderId());
                                        orderUpdate.set(PayOrder::getNotifyState,1);
                                        payOrderService.update(orderUpdate);
                                    } else {
                                        notifyInfo.setNotifyStatus(0);
                                    }
                                    notifyInfo.setReturnMsg(body);

                                } catch (Exception e) {
                                    notifyInfo.setNotifyStatus(0);
                                    notifyInfo.setReturnMsg(e.getMessage());
                                }
                                orderNotifyInfoService.save(notifyInfo);
                            }
                        });
                        thread.start();
                    }
                }
            }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void executeDivision() throws FileNotFoundException {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateTime yesToday = DateUtil.offsetDay(today,-1);

        QueryWrapper<PayOrder> order = new QueryWrapper();
        order.select("division_sub_no," +
                "IFNULL(SUM(CASE WHEN division_state = 3 AND DATE_FORMAT(create_time, '%Y-%m-%d') = '"+format.format(yesToday)+"' THEN division_amount END ),0) yesToday_amount ," +
                "IFNULL(SUM(CASE WHEN division_state = 3 AND DATE_FORMAT(create_time, '%Y-%m-%d') = '"+format.format(today)+"' THEN division_amount END ),0) today_amount ," +
                "IFNULL(SUM(CASE WHEN division_state = 3 THEN division_amount END ),0) count_amount ");
        order.groupBy("division_sub_no");
        order.lambda().eq(PayOrder::getDivisionState,3);
        order.lambda().eq(PayOrder::getState,2);
        List<Map<String,Object>> orders = payOrderService.listMaps(order);
        if(CollUtil.isNotEmpty(orders)){
            orders.forEach(v -> {
                if(null != v.get("division_sub_no") && !"null".equals(v.get("division_sub_no"))){
                    LambdaUpdateWrapper<MchPiecesInfo> updateWrapper = new LambdaUpdateWrapper();
                    updateWrapper.set(!"null".equals(v.get("count_amount")),MchPiecesInfo::getCountAmount,v.get("count_amount"));
                    updateWrapper.set(!"null".equals(v.get("today_amount")),MchPiecesInfo::getToDayAmount,v.get("today_amount"));
                    updateWrapper.set(!"null".equals(v.get("yesToday_amount")),MchPiecesInfo::getToDayAmount,v.get("yesToday_amount"));
                    updateWrapper.eq(MchPiecesInfo::getSubUserNo,v.get("division_sub_no"));
                    updateWrapper.isNotNull(MchPiecesInfo::getSubUserNo);
                    mchPiecesInfoService.update(updateWrapper);
                }

            });
        }
    }
}