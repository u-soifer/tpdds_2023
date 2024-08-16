package domain.entities.notificaciones.mail;

import java.io.IOException;

public interface AdapterNotificadorViaMail {
    public String notificarPorMail(String mailTo, String asunto, String cuerpo) throws IOException;
}
