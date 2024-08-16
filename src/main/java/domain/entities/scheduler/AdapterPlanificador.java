package domain.entities.scheduler;

import org.quartz.SchedulerException;

public interface AdapterPlanificador {
    public void inicializarCron() throws SchedulerException;
    public void comenzarCron() throws SchedulerException;
}
