package domain.entities.notificaciones.mail;

import domain.entities.services.sendgrid.NotificadorMailSendGrid;

public class FactoryNotificadorMail {

    public static AdapterNotificadorViaMail iniciarNotificadorMail(MetodoNotificarPorMail metodo){
        AdapterNotificadorViaMail instancia = null;

        switch (metodo){
            case SEND_GRID: instancia = new NotificadorMailSendGrid();
        }

        return instancia;
    }
}
