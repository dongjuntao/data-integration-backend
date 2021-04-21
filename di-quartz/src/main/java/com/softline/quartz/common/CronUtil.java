package com.softline.quartz.common;

import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class CronUtil {

    public static long getPeriodByCron(String cronExpression) throws ParseException{
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setCronExpression(cronExpression);
        List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 2);
        if (dates == null || dates.size() == 0) {
            return 1000*60*60*12L;
        }
        return dates.get(1).getTime() - dates.get(0).getTime();
    }
}
