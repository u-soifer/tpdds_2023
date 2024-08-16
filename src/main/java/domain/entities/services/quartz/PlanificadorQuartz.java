package domain.entities.services.quartz;

import domain.entities.config.Config;
import domain.entities.scheduler.AdapterPlanificador;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

public class PlanificadorQuartz implements AdapterPlanificador {
    private static Scheduler scheduler;
    private String expresionCron;
    private CronTrigger trigger;
    private JobDetail notificar;


    public PlanificadorQuartz() throws SchedulerException, IOException {
        expresionCron = Config.getProperty("CRON_EXPRESSION");
        SchedulerFactory sf = new StdSchedulerFactory();
        this.scheduler = sf.getScheduler();
    }

    @Override
    public void inicializarCron() throws SchedulerException {
        this.notificar= JobBuilder.newJob(NotificarContactos.class)
                .withIdentity("job1","group1")
                .build();
        this.trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")) // a las 15 del dia 5 de cada mes
                .build();
        scheduler.scheduleJob(notificar,trigger);
    }

    @Override
    public void comenzarCron() throws SchedulerException {
        scheduler.start();
    }


}
