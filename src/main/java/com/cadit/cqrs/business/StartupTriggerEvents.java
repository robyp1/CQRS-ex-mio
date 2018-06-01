package com.cadit.cqrs.business;


import com.cadit.cqrs.cdi.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * *************************************************************************************************
 * ********************************************BACK-END*********************************************
 * *************************************************************************************************
 */
@Singleton
@Startup
@AccessTimeout(value = 120000)
public class StartupTriggerEvents {

    private static final String CONVERTER_TIMER_ENABLED = "CONVERTER.TIMER.ENABLED";
    private static final String CONVERTER_TIMER_ENABLED_DEFAULT = "false";
    private static final String CONVERTER_TIMER_NAME = "CONVERTER.TIMER.NAME";
    private static final String CONVERTER_TIMER_NAME_DEFAULT = "TestConverterTimer";
    private static final String CONVERTER_TIMER_SCHEDULE_HOUR = "CONVERTER.TIMER.SCHEDULE.HOUR";
    private static final String CONVERTER_TIMER_SCHEDULE_HOUR_DEFAULT = "*";
    private static final String CONVERTER_TIMER_SCHEDULE_MINUTE = "CONVERTER.TIMER.SCHEDULE.MINUTE";
    private static final String CONVERTER_TIMER_SCHEDULE_MINUTE_DEFAULT = "*/2";
    private static final String CONVERTER_TIMER_SCHEDULE_SECOND = "CONVERTER.TIMER.SCHEDULE.SECOND";
    private static final String CONVERTER_TIMER_SCHEDULE_SECOND_DEFAULT = "0";

    @Inject
    Configuration configuration;

    @EJB
    DocumentCreatorBean documentCreatorBean;

    @Resource
    private TimerService timerService;

    private Logger log = Logger.getLogger(this.getClass().getSimpleName());

    public StartupTriggerEvents() {
    }

    @PostConstruct
    public void init() {

        Boolean timerEnabled = Boolean.valueOf(configuration.getConf().getProperty(CONVERTER_TIMER_ENABLED, CONVERTER_TIMER_ENABLED_DEFAULT));
        if (timerEnabled) {
            String converterScheduleHour = configuration.getConf().getProperty(CONVERTER_TIMER_SCHEDULE_HOUR, CONVERTER_TIMER_SCHEDULE_HOUR_DEFAULT);
            String converterScheduleMinute = configuration.getConf().getProperty(CONVERTER_TIMER_SCHEDULE_MINUTE, CONVERTER_TIMER_SCHEDULE_MINUTE_DEFAULT);
            String converterScheduleSeconds = configuration.getConf().getProperty(CONVERTER_TIMER_SCHEDULE_SECOND, CONVERTER_TIMER_SCHEDULE_SECOND_DEFAULT);
            ScheduleExpression exp = new ScheduleExpression()
                    .hour(converterScheduleHour)
                    .minute(converterScheduleMinute)
                    .second(converterScheduleSeconds);
            String converterTimerName = configuration.getConf().getProperty(CONVERTER_TIMER_NAME, CONVERTER_TIMER_NAME_DEFAULT);
            Timer timer = timerService.createCalendarTimer(exp, new TimerConfig(converterTimerName, false));
            Logger.getLogger(StartupTriggerEvents.class.getName()).info("Trigger initialized");
        }
    }


    @Timeout
    @Lock(LockType.WRITE)
    public void callService(Timer timer){
        //call to produce pdf document
        log.log(Level.INFO, "Trigger document creation");
        documentCreatorBean.pdfCreator();
    }

}

