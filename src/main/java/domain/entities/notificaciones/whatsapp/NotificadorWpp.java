package domain.entities.notificaciones.whatsapp;

import java.io.IOException;

public class NotificadorWpp {
    private AdapterNotificadorViaWpp adapterNotificadorViaWpp;

    public NotificadorWpp(AdapterNotificadorViaWpp adapterNotificadorViaWpp) {
        this.adapterNotificadorViaWpp = adapterNotificadorViaWpp;
    }

    public void notificarPorWpp(String cel, String msg) throws IOException {
        adapterNotificadorViaWpp.notificarPorWpp(cel, msg);
    }
}
