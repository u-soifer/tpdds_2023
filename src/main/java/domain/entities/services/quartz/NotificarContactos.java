package domain.entities.services.quartz;

import domain.entities.notificaciones.Contacto;
import domain.entities.notificaciones.Notificador;
import domain.entities.notificaciones.mail.FactoryNotificadorMail;
import domain.entities.notificaciones.mail.MetodoNotificarPorMail;
import domain.entities.notificaciones.mail.NotificadorMail;
import domain.entities.notificaciones.whatsapp.FactoryNotificadorWpp;
import domain.entities.notificaciones.whatsapp.MetodoNotificarPorWpp;
import domain.entities.notificaciones.whatsapp.NotificadorWpp;
import domain.entities.organizacion.Organizacion;
import domain.entities.scheduler.PlanificadorDeNotificaciones;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.ubicacion.Ubicacion;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static domain.entities.organizacion.ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO;
import static domain.entities.organizacion.TipoOrganizacion.EMPRESA;

public class NotificarContactos implements Job {

    private List<Organizacion> organizaciones;

    public NotificarContactos() throws IOException {
        this.organizaciones = new ArrayList<>();

        NotificadorMail notificadorMail = new NotificadorMail(FactoryNotificadorMail.iniciarNotificadorMail(MetodoNotificarPorMail.SEND_GRID));
        NotificadorWpp notificadorWpp   = new NotificadorWpp(FactoryNotificadorWpp.iniciarNotificadorWpp(MetodoNotificarPorWpp.TWILIO));
        Notificador notificador = new Notificador(notificadorMail, notificadorWpp);

        Pais argentina = new Pais(1, "Argentina");
        Provincia caba = new Provincia(174, "CABA", argentina);
        Municipio m_caba = new Municipio(241, "CABA", caba);
        Localidad palermo = new Localidad(5354, "Palermo", 2659, m_caba);
        Ubicacion ubicacion = new Ubicacion(argentina, caba, m_caba, palermo, "A", 20);

        Organizacion google   = new Organizacion("Google",   EMPRESA, ubicacion, EMPRESA_SECTOR_PRIMARIO);
        Organizacion apple    = new Organizacion("Apple",    EMPRESA, ubicacion, EMPRESA_SECTOR_PRIMARIO);
        Organizacion ibm      = new Organizacion("IBM",      EMPRESA, ubicacion, EMPRESA_SECTOR_PRIMARIO);
        Organizacion facebook = new Organizacion("Facebook", EMPRESA, ubicacion, EMPRESA_SECTOR_PRIMARIO);

        Contacto juan  = new Contacto("Juan", "urisoifer@gmail.com", "+5491124638391", notificador);
        Contacto pedro = new Contacto("Pedro", "a@gmail.com", "+5491100000000", notificador);
        Contacto pablo = new Contacto("Pablo", "b@gmail.com", "+5491100000000", notificador);

        google.agregarContacto(juan, pedro, pablo);
        apple.agregarContacto(juan, pedro, pablo);
        ibm.agregarContacto(juan, pedro, pablo);
        facebook.agregarContacto(juan, pedro, pablo);

        this.organizaciones.add(google);
        this.organizaciones.add(apple);
        this.organizaciones.add(ibm);
        this.organizaciones.add(facebook);

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.notificarAContactos();
    }

    public void notificarAContactos(){
        this.organizaciones.forEach(a->a.enviarRecomendaciones(PlanificadorDeNotificaciones.getRecomendaciones()));
        System.out.println("Se enviaron las recomendaciones: "+PlanificadorDeNotificaciones.getRecomendaciones());
    }
}
