package config;

import domain.entities.actividades.Consumo;
import domain.entities.actividades.TiposConsumo;
import domain.entities.mediosDeTransporte.BiciPie;
import domain.entities.mediosDeTransporte.ServicioContratado;
import domain.entities.mediosDeTransporte.TransportePublico;
import domain.entities.mediosDeTransporte.VehiculoParticular;
import domain.entities.notificaciones.Notificador;
import domain.entities.notificaciones.mail.FactoryNotificadorMail;
import domain.entities.notificaciones.mail.MetodoNotificarPorMail;
import domain.entities.notificaciones.mail.NotificadorMail;
import domain.entities.notificaciones.whatsapp.FactoryNotificadorWpp;
import domain.entities.notificaciones.whatsapp.MetodoNotificarPorWpp;
import domain.entities.notificaciones.whatsapp.NotificadorWpp;
import domain.entities.services.geodds.distancia.ServicioDistancia;
import domain.entities.services.geodds.factory.FactoryServicioDistancia;
import domain.entities.services.geodds.factory.MetodoCalculoDistancia;
import domain.entities.validador.*;
import org.apache.poi.ss.formula.functions.Vlookup;

import java.io.IOException;

public class Config {
    private static ServicioDistancia servicioDistancia = null;
    private static Notificador notificador = null;

    private static Validador validador = null;

    private Consumo consumoBP;
    private Consumo consumoVP;
    private Consumo consumoSC;
    private Consumo consumoTP;

    public Config() throws IOException {
        servicioDistancia = new ServicioDistancia(FactoryServicioDistancia.iniciarServicio(MetodoCalculoDistancia.RETROFIT));
        NotificadorMail notificadorMail = new NotificadorMail(FactoryNotificadorMail.iniciarNotificadorMail(MetodoNotificarPorMail.SEND_GRID));
        NotificadorWpp notificadorWpp   = new NotificadorWpp(FactoryNotificadorWpp.iniciarNotificadorWpp(MetodoNotificarPorWpp.TWILIO));
        notificador = new Notificador(notificadorMail, notificadorWpp);

        validador = new Validador();
        validador.agregarValidacion(new ContieneUnaMayuscula(), new ContieneUnaMinuscula(), new ContieneUnNumero(), new EsContraseniaSencilla(), new TieneLongitudCorrecta(8));

        consumoBP = new Consumo(TiposConsumo.BICI_PIE, 0.0);
        consumoVP = new Consumo(TiposConsumo.VEHICULO_PARTICULAR, 3.0);
        consumoSC = new Consumo(TiposConsumo.SERVICIO_CONTRATADO, 2.0);
        consumoTP = new Consumo(TiposConsumo.TRANSPORTE_PUBLICO, 1.5);

        BiciPie.setConsumo(consumoBP);
        VehiculoParticular.setConsumo(consumoVP);
        ServicioContratado.setConsumo(consumoSC);
        TransportePublico.setConsumo(consumoTP);
    }

    public static ServicioDistancia getServicioDistancia() {
        return servicioDistancia;
    }

    public static Notificador getNotificador(){
        return notificador;
    }

    public static Validador getValidador(){
        return validador;
    }
}
