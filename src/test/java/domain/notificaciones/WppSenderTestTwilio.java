package domain.notificaciones;

import domain.entities.services.twilio.NotificadorTwilioWpp;

import java.io.IOException;

public class WppSenderTestTwilio {

    public static void main(String[] args) throws IOException {
        NotificadorTwilioWpp notificadorTwilioWpp;

        notificadorTwilioWpp = new NotificadorTwilioWpp();

        String sid = notificadorTwilioWpp.notificarPorWpp("+5491124638391", "Probando");
        System.out.println(sid);
    }
}
