package domain.entities.notificaciones.whatsapp;

import domain.entities.services.twilio.NotificadorTwilioWpp;

public class FactoryNotificadorWpp {
    public static AdapterNotificadorViaWpp iniciarNotificadorWpp(MetodoNotificarPorWpp metodo){
        AdapterNotificadorViaWpp instancia = null;

        switch (metodo){
            case TWILIO: instancia = new NotificadorTwilioWpp();
        }

        return instancia;
    }
}
