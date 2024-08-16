package domain.entities.notificaciones.whatsapp;

import java.io.IOException;

public interface AdapterNotificadorViaWpp {
    public String notificarPorWpp(String cel, String msg) throws IOException;
}
