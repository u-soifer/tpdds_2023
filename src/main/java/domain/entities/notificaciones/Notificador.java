package domain.entities.notificaciones;

import domain.entities.notificaciones.mail.NotificadorMail;
import domain.entities.notificaciones.whatsapp.NotificadorWpp;

import java.io.IOException;

public class Notificador {
    private NotificadorMail notificadorMail;
    private NotificadorWpp notificadorWpp;

    public Notificador(NotificadorMail notificadorMail, NotificadorWpp notificadorWpp) {
        this.notificadorMail = notificadorMail;
        this.notificadorWpp  = notificadorWpp;
    }

    public void notificarPorWpp(String cel, String msg) throws IOException {
        notificadorWpp.notificarPorWpp(cel, msg);
    }

    public void notificarPorMail(String mail, String asunto, String cuerpo) throws IOException {
        notificadorMail.notificarPorMail(mail, asunto, cuerpo);
    }
}
