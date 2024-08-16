package domain.entities.services.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import domain.entities.config.Config;
import domain.entities.notificaciones.whatsapp.AdapterNotificadorViaWpp;

import java.io.IOException;

public class NotificadorTwilioWpp implements AdapterNotificadorViaWpp {

    @Override
    public String notificarPorWpp(String cel, String msg) throws IOException {

        String ACCOUNT_SID;
        String AUTH_TOKEN;
        String PHONE_FROM;

        ACCOUNT_SID = Config.getProperty("ACCOUNT_SID");
        AUTH_TOKEN = Config.getProperty("AUTH_TOKEN");
        PHONE_FROM = Config.getProperty("PHONE_NUMBER");


        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:"+cel),
                        new com.twilio.type.PhoneNumber("whatsapp:"+PHONE_FROM), msg)
                .create();

        return message.getSid();
    }
}
