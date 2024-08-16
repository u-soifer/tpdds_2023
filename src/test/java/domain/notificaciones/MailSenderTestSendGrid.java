package domain.notificaciones;

import domain.entities.services.sendgrid.NotificadorMailSendGrid;

import java.io.IOException;

public class MailSenderTestSendGrid {

    public static void main(String[] args) throws IOException {
        NotificadorMailSendGrid notificadorMailSendGrid = new NotificadorMailSendGrid();
        notificadorMailSendGrid.notificarPorMail("urisoifer@gmail.com", "A", "B");
    }
}
