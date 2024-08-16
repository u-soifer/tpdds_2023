package domain.entities.scheduler;

import org.quartz.SchedulerException;

public class PlanificadorDeNotificaciones {
    private AdapterPlanificador adapterPlanificador;
    private static String recomendaciones;

    public PlanificadorDeNotificaciones(AdapterPlanificador adapterPlanificador) {
        this.adapterPlanificador = adapterPlanificador;
    }

    public void inicializarCron() throws SchedulerException {
        adapterPlanificador.inicializarCron();
    }

    public void comenzarCron() throws SchedulerException {
        adapterPlanificador.comenzarCron();
    }

    public static String getRecomendaciones() {
        return recomendaciones;
    }

    public static void setRecomendaciones(String recomendaciones) {
        PlanificadorDeNotificaciones.recomendaciones = recomendaciones;
    }
}
