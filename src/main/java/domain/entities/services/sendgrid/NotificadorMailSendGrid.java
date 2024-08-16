package domain.entities.services.sendgrid;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import domain.entities.config.Config;
import domain.entities.notificaciones.mail.AdapterNotificadorViaMail;

import java.io.IOException;

public class NotificadorMailSendGrid implements AdapterNotificadorViaMail {

    @Override
    public String notificarPorMail(String mailTo, String asunto, String cuerpo) throws IOException {
        Email from = new Email(Config.getProperty("MAIL_FROM"));
        String subject = asunto;
        Email to = new Email(mailTo);
        Content content = new Content("text/plain", cuerpo);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(Config.getProperty("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            return String.valueOf(response.getStatusCode());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
