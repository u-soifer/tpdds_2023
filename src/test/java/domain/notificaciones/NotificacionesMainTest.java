package domain.notificaciones;

import domain.entities.notificaciones.Contacto;
import domain.entities.notificaciones.Notificador;
import domain.entities.notificaciones.mail.FactoryNotificadorMail;
import domain.entities.notificaciones.mail.MetodoNotificarPorMail;
import domain.entities.notificaciones.mail.NotificadorMail;
import domain.entities.notificaciones.whatsapp.FactoryNotificadorWpp;
import domain.entities.notificaciones.whatsapp.MetodoNotificarPorWpp;
import domain.entities.notificaciones.whatsapp.NotificadorWpp;
import domain.entities.organizacion.Organizacion;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.ubicacion.Ubicacion;

import java.io.IOException;

import static domain.entities.organizacion.ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO;
import static domain.entities.organizacion.TipoOrganizacion.EMPRESA;

public class NotificacionesMainTest {

    public static void main(String[] args) throws IOException {

        NotificadorMail notificadorMail = new NotificadorMail(FactoryNotificadorMail.iniciarNotificadorMail(MetodoNotificarPorMail.SEND_GRID));
        NotificadorWpp notificadorWpp   = new NotificadorWpp(FactoryNotificadorWpp.iniciarNotificadorWpp(MetodoNotificarPorWpp.TWILIO));
        Notificador notificador = new Notificador(notificadorMail, notificadorWpp);

        Contacto juan  = new Contacto("Juan", "urisoifer@gmail.com", "+5491124638391", notificador);
        Contacto pedro = new Contacto("Pedro", "a@gmail.com", "+5491124638391", notificador);
        Contacto pablo = new Contacto("Pablo", "b@gmail.com", "+5491124638391", notificador);

        Pais argentina = new Pais(1, "Argentina");
        Provincia caba = new Provincia(174, "CABA", argentina);
        Municipio m_caba = new Municipio(241, "CABA", caba);
        Localidad palermo = new Localidad(5354, "Palermo", 2659, m_caba);

        Ubicacion ubicacion = new Ubicacion(argentina, caba, m_caba, palermo, "A", 20);

        Organizacion organizacion = new Organizacion("Google", EMPRESA, ubicacion, EMPRESA_SECTOR_PRIMARIO);

        organizacion.agregarContacto(juan, pedro, pablo);

        organizacion.enviarRecomendaciones("www.google.com");

    }

}
