package domain.planificador;

import domain.entities.scheduler.PlanificadorDeNotificaciones;
import domain.entities.services.quartz.PlanificadorQuartz;
import org.quartz.SchedulerException;

import java.io.IOException;

public class PlanificadorMainTest {

    public static void main(String args[]) throws SchedulerException, IOException {
        PlanificadorQuartz planificadorQuartz = new PlanificadorQuartz();
        PlanificadorDeNotificaciones planificadorDeNotificaciones = new PlanificadorDeNotificaciones(planificadorQuartz);
        PlanificadorDeNotificaciones.setRecomendaciones("SUGERENCIAS");
        planificadorDeNotificaciones.inicializarCron();
        planificadorDeNotificaciones.comenzarCron();
    }
}
