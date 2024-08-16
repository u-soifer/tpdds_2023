package domain.entities.notificaciones.mail;

import java.io.IOException;

public class NotificadorMail {

    private AdapterNotificadorViaMail adapterNotificadorViaMail;

    public NotificadorMail(AdapterNotificadorViaMail adapterNotificadorViaMail) {
        this.adapterNotificadorViaMail = adapterNotificadorViaMail;
    }

    public void notificarPorMail(String mail, String asunto, String cuerpo) throws IOException {
        this.adapterNotificadorViaMail.notificarPorMail(mail, asunto, cuerpo);
    }
}
