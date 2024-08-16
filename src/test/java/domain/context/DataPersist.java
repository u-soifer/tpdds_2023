package domain.context;

import com.google.common.hash.Hashing;
import domain.entities.actividades.Medicion;
import domain.entities.agentessectoriales.AgenteSectorial;
import domain.entities.agentessectoriales.SectorTerritorial;
import db.EntityManagerHelper;
import domain.entities.mediosDeTransporte.ServicioContratado;
import domain.entities.mediosDeTransporte.TipoDeServicioContratado;
import domain.entities.mediosDeTransporte.TipoTransportePublico;
import domain.entities.mediosDeTransporte.TransportePublico;
import domain.entities.notificaciones.Contacto;
import domain.entities.organizacion.*;
import domain.entities.services.geodds.entities.*;
import domain.entities.ubicacion.Parada;
import domain.entities.ubicacion.Ubicacion;
import domain.entities.usuario.TipoUsuario;
import domain.entities.usuario.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataPersist {

    public static void main(String[] args) throws IOException {


        List<Organizacion> organizaciones = new ArrayList<>();
        List<Area> areas = new ArrayList<>();
        List<Ubicacion> ubicaciones = new ArrayList<>();
        List<Empleado> empleados = new ArrayList<>();
        List<SectorTerritorial> sectoresTerritoriales = new ArrayList<>();
        List<AgenteSectorial> agentesSectoriales = new ArrayList<>();
        List<Usuario> usuarios = new ArrayList<>();
        List<Medicion> mediciones = new ArrayList<>();
        List<TipoDeServicioContratado> tipoServiciosContratados = new ArrayList<>();
        List<TransportePublico> transportesPublicos = new ArrayList<>();
        List<Parada> paradas = new ArrayList<>();

        List<Object> oPersistentes = new ArrayList<>();

        //Usuarios
        Usuario empleado = new Usuario(TipoUsuario.EMPLEADO,
                                "empleado",
                                       Hashing.sha256().hashString("grupo4", StandardCharsets.UTF_8).toString(),
                                       "grupo4ddsmama@gmail.com"
                                      );

        Usuario organizacion = new Usuario(TipoUsuario.ORGANIZACION,
                "organizacion",
                Hashing.sha256().hashString("grupo4", StandardCharsets.UTF_8).toString(),
                "grupo4ddsmama@gmail.com"
        );

        Usuario agente = new Usuario(TipoUsuario.AGENTE_SECTORIAL,
                "agente",
                Hashing.sha256().hashString("grupo4", StandardCharsets.UTF_8).toString(),
                "grupo4ddsmama@gmail.com"
        );

        Usuario agenteIgnacio = new Usuario(TipoUsuario.AGENTE_SECTORIAL,
                "agenteIgnacio",
                Hashing.sha256().hashString("grupo4", StandardCharsets.UTF_8).toString(),
                "grupo4ddsmama@gmail.com"
        );

        Usuario agenteEnrique = new Usuario(TipoUsuario.AGENTE_SECTORIAL,
                "agenteEnrique",
                Hashing.sha256().hashString("grupo4", StandardCharsets.UTF_8).toString(),
                "grupo4ddsmama@gmail.com"
        );

        Usuario admin = new Usuario(TipoUsuario.ADMIN,
                "admin",
                Hashing.sha256().hashString("grupo4", StandardCharsets.UTF_8).toString(),
                "grupo4ddsmama@gmail.com"
        );

        usuarios.add(empleado);
        usuarios.add(organizacion);
        usuarios.add(agente);
        usuarios.add(agenteEnrique);
        usuarios.add(agenteIgnacio);
        usuarios.add(admin);

        //Ubicaciones
        Pais argentina = (Pais) EntityManagerHelper.createQuery("from Pais where id = 9").getSingleResult();
        Provincia caba = (Provincia) EntityManagerHelper.createQuery("from Provincia where id = 174").getSingleResult();
        Municipio m_caba = (Municipio) EntityManagerHelper.createQuery("from Municipio where id = 241").getSingleResult();
        Localidad palermo = (Localidad) EntityManagerHelper.createQuery("from Localidad where id = 5354").getSingleResult();
        Localidad retiro = (Localidad) EntityManagerHelper.createQuery("from Localidad where id = 5361").getSingleResult();
        Localidad nuniez = (Localidad) EntityManagerHelper.createQuery("from Localidad where id = 5353").getSingleResult();
        Localidad caballito = (Localidad) EntityManagerHelper.createQuery("from Localidad where id = 5339").getSingleResult();
        Localidad v_urquiza = (Localidad) EntityManagerHelper.createQuery("from Localidad where id = 5380").getSingleResult();

        Provincia rio_negro = (Provincia) EntityManagerHelper.createQuery("from Provincia where id = 182").getSingleResult();
        Municipio alumine = (Municipio) EntityManagerHelper.createQuery("from Municipio where id = 106").getSingleResult();
        Municipio lacar = (Municipio) EntityManagerHelper.createQuery("from Municipio where id = 113").getSingleResult();

        Provincia chaco = (Provincia) EntityManagerHelper.createQuery("from Provincia where id = 170").getSingleResult();
        Municipio general_guemes = (Municipio) EntityManagerHelper.createQuery("from Municipio where id = 489").getSingleResult();

        Ubicacion u_nestle = new Ubicacion("Trabajo - NESTLE", argentina, caba, m_caba, palermo, "A", 1234);
        Ubicacion u_telefonica = new Ubicacion("Trabajo - TELEFONICA", argentina, rio_negro, alumine, retiro, "B", 1234);
        Ubicacion u_shell = new Ubicacion("Trabajo - SHELL", argentina, caba, m_caba, nuniez, "C", 1234);
        Ubicacion u_sony = new Ubicacion("Trabajo - SONY", argentina, rio_negro, lacar, caballito, "D", 1234);
        Ubicacion u_samsung = new Ubicacion("Trabajo - SAMSUNG", argentina, chaco, general_guemes, v_urquiza, "E", 1234);

        ubicaciones.add(u_nestle);
        ubicaciones.add(u_telefonica);
        ubicaciones.add(u_shell);
        ubicaciones.add(u_sony);
        ubicaciones.add(u_samsung);

        //Organizaciones
        Organizacion nestle = new Organizacion("Nestle", TipoOrganizacion.EMPRESA, u_nestle, ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO);
        Organizacion telefonica = new Organizacion("Telefonica", TipoOrganizacion.EMPRESA, u_telefonica, ClasificacionOrganizacion.EMPRESA_SECTOR_SECUNDARIO);
        Organizacion shell = new Organizacion("Shell", TipoOrganizacion.GUBERNAMENTAL, u_shell, ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO);
        Organizacion sony = new Organizacion("Sony", TipoOrganizacion.INSTITUCION, u_sony, ClasificacionOrganizacion.EMPRESA_SECTOR_SECUNDARIO);
        Organizacion samsung = new Organizacion("Samsung", TipoOrganizacion.GUBERNAMENTAL, u_samsung, ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO);

        nestle.setUsuario(organizacion);

        nestle.setHC(101.32);
        telefonica.setHC(56.76);
        shell.setHC(83.26);
        sony.setHC(132.88);
        samsung.setHC(432.43);

        organizaciones.add(nestle);
        organizaciones.add(telefonica);
        organizaciones.add(shell);
        organizaciones.add(sony);
        organizaciones.add(samsung);

        //NESTLE
        Area a_dir_nestle = new Area("Direccion", nestle);
        Area a_ven_nestle = new Area("Ventas", nestle);
        Area a_cont_nestle = new Area("Contabilidad", nestle);

        areas.add(a_dir_nestle);
        areas.add(a_ven_nestle);
        areas.add(a_cont_nestle);

        nestle.agregarAreas(a_dir_nestle, a_ven_nestle, a_cont_nestle);

        //TELEFONICA
        Area a_dir_telefonica = new Area("Direccion", telefonica);
        Area a_prod_telefonica = new Area("Produccion", telefonica);
        Area a_rrhh_telefonica = new Area("RRHH", telefonica);

        areas.add(a_dir_telefonica);
        areas.add(a_prod_telefonica);
        areas.add(a_rrhh_telefonica);

        telefonica.agregarAreas(a_dir_telefonica, a_prod_telefonica, a_rrhh_telefonica);

        //SHELL
        Area a_mkt_shell = new Area("Marketing", shell);
        Area a_sac_shell = new Area("Servicio al cliente", shell);
        Area a_inn_shell = new Area("Innovacion", shell);

        areas.add(a_mkt_shell);
        areas.add(a_sac_shell);
        areas.add(a_inn_shell);

        shell.agregarAreas(a_mkt_shell, a_sac_shell, a_inn_shell);

        //SONY
        Area a_it_sony = new Area("IT", sony);
        Area a_mkt_sony = new Area("Marketing", sony);
        Area a_rrhh_sony = new Area("RRHH", sony);

        areas.add(a_it_sony);
        areas.add(a_mkt_sony);
        areas.add(a_rrhh_sony);

        sony.agregarAreas(a_it_sony, a_mkt_sony, a_rrhh_sony);

        //SAMSUNG
        Area a_dir_samsung = new Area("Direccion", samsung);
        Area a_sac_samsung = new Area("Servicio al cliente", samsung);
        Area a_ven_samsung = new Area("Ventas", samsung);

        areas.add(a_dir_samsung);
        areas.add(a_sac_samsung);
        areas.add(a_ven_samsung);

        samsung.agregarAreas(a_dir_samsung, a_sac_samsung, a_ven_samsung);

        //Empleados
        Empleado a = new Empleado("Santiago", "A", TipoDocumento.DNI, 23452645);
        Empleado b = new Empleado("Tomas", "B", TipoDocumento.DNI, 67385721);
        Empleado c = new Empleado("Abril", "C", TipoDocumento.DNI, 53859283);
        Empleado d = new Empleado("Daniel", "D", TipoDocumento.DNI, 35748345);
        Empleado e = new Empleado("Camila", "E", TipoDocumento.DNI, 42837599);
        Empleado f = new Empleado("Juan", "F", TipoDocumento.DNI, 562545843);
        Empleado g = new Empleado("Lucia", "G", TipoDocumento.DNI, 34572845);
        Empleado h = new Empleado("Sofia", "H", TipoDocumento.DNI, 35947385);
        Empleado i = new Empleado("Adrian", "I", TipoDocumento.DNI, 24495824);
        Empleado j = new Empleado("Martina", "J", TipoDocumento.DNI, 34471472);
        Empleado k = new Empleado("Javier", "K", TipoDocumento.DNI, 20494738);
        Empleado l = new Empleado("Mario", "L", TipoDocumento.DNI, 43829483);
        Empleado m = new Empleado("Paula", "M", TipoDocumento.DNI, 34274618);
        Empleado n = new Empleado("Valeria", "N", TipoDocumento.DNI, 49583273);
        Empleado o = new Empleado("Emma", "O", TipoDocumento.DNI, 51238475);
        Empleado p = new Empleado("Julieta", "P", TipoDocumento.DNI, 32904823);
        Empleado q = new Empleado("Jesus", "Q", TipoDocumento.DNI, 39913749);
        Empleado r = new Empleado("Fernando", "R", TipoDocumento.DNI, 5384938);
        Empleado s = new Empleado("Horacio", "S", TipoDocumento.DNI, 58492834);
        Empleado t = new Empleado("Alberto", "T", TipoDocumento.DNI, 54782343);

        a.setUsuario(empleado);

        Ubicacion a_casa = new Ubicacion("Casa", argentina, caba, m_caba, retiro, "Paraguay", 500);
        Ubicacion b_casa = new Ubicacion("Casa", argentina, caba, m_caba, retiro, "San Martin", 500);
        Ubicacion c_casa = new Ubicacion("Casa", argentina, caba, m_caba, retiro, "Paraguay", 500);
        Ubicacion d_casa = new Ubicacion("Casa", argentina, caba, m_caba, palermo, "Thames", 500);
        Ubicacion e_casa = new Ubicacion("Casa", argentina, caba, m_caba, v_urquiza, "Av Congreso", 500);

        a.agregarUbicaciones(a_casa);
        b.agregarUbicaciones(b_casa);
        c.agregarUbicaciones(c_casa);
        d.agregarUbicaciones(d_casa);
        e.agregarUbicaciones(e_casa);

        Collections.addAll(empleados, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);

        //Trabajos
        a.solicitarVinculacion(nestle);
        b.solicitarVinculacion(nestle);
        c.solicitarVinculacion(nestle);
        d.solicitarVinculacion(nestle);
        e.solicitarVinculacion(nestle);

        nestle.ingresarEmpleado(a, a_dir_nestle);
        nestle.ingresarEmpleado(b, a_ven_nestle);
        nestle.ingresarEmpleado(c, a_cont_nestle);



        f.solicitarVinculacion(telefonica);
        g.solicitarVinculacion(telefonica);
        h.solicitarVinculacion(telefonica);
        i.solicitarVinculacion(telefonica);
        j.solicitarVinculacion(telefonica);

        telefonica.ingresarEmpleado(f, a_dir_telefonica);
        telefonica.ingresarEmpleado(g, a_prod_telefonica);
        telefonica.ingresarEmpleado(h, a_rrhh_telefonica);



        k.solicitarVinculacion(shell);
        l.solicitarVinculacion(shell);
        m.solicitarVinculacion(shell);
        n.solicitarVinculacion(shell);
        o.solicitarVinculacion(shell);

        shell.ingresarEmpleado(k, a_inn_shell);
        shell.ingresarEmpleado(l, a_sac_shell);
        shell.ingresarEmpleado(m, a_mkt_shell);



        p.solicitarVinculacion(sony);
        q.solicitarVinculacion(sony);
        r.solicitarVinculacion(sony);
        s.solicitarVinculacion(sony);
        t.solicitarVinculacion(sony);

        sony.ingresarEmpleado(p, a_it_sony);
        sony.ingresarEmpleado(q, a_mkt_sony);
        sony.ingresarEmpleado(r, a_rrhh_sony);



        a.solicitarVinculacion(samsung);
        g.solicitarVinculacion(samsung);
        m.solicitarVinculacion(samsung);
        s.solicitarVinculacion(samsung);
        t.solicitarVinculacion(samsung);

        samsung.ingresarEmpleado(a, a_dir_samsung);
        samsung.ingresarEmpleado(g, a_sac_samsung);
        samsung.ingresarEmpleado(m, a_ven_samsung);

        //Contactos
        Contacto uriel = new Contacto("Uriel", "urisoifer@gmail.com", "+541124638391");
        Contacto carlos = new Contacto("Carlos", "", "");
        Contacto tomas = new Contacto("Tomas", "fake@gmail.com", "fake");
        Contacto camila = new Contacto("Camila", "fake@gmail.com", "fake");
        Contacto julieta = new Contacto("Julieta", "fake@gmail.com", "fake");
        Contacto martina = new Contacto("Martina", "fake@gmail.com", "fake");

        nestle.agregarContacto(uriel, carlos);
        telefonica.agregarContacto(tomas);
        shell.agregarContacto(martina);
        sony.agregarContacto(camila);
        samsung.agregarContacto(julieta);

        //Sector territorial
        SectorTerritorial s1 = new SectorTerritorial(alumine);
        SectorTerritorial s2 = new SectorTerritorial(rio_negro);
        SectorTerritorial s3 = new SectorTerritorial(general_guemes);
        SectorTerritorial s4 = new SectorTerritorial(chaco);
        SectorTerritorial s5 = new SectorTerritorial(caba);
        SectorTerritorial s6 = new SectorTerritorial(m_caba);

        Collections.addAll(sectoresTerritoriales, s1, s2, s3, s4, s5, s6);

        //Agentes sectoriales
        AgenteSectorial as1 = new AgenteSectorial(s1);
        AgenteSectorial as2 = new AgenteSectorial(s2);
        AgenteSectorial as3 = new AgenteSectorial(s3);
        AgenteSectorial as4 = new AgenteSectorial();

        as1.setUsuario(agente);
        as1.setNombre("Martin");

        as2.setUsuario(agenteEnrique);
        as2.setNombre("Enrique");

        as3.setNombre("Pedro");

        as4.setNombre("Ignacio");
        as4.setUsuario(agenteIgnacio);

        Collections.addAll(agentesSectoriales, as1, as2, as3, as4);

        //Tipo de servicios contratados
        tipoServiciosContratados.add(new TipoDeServicioContratado("Taxi"));
        tipoServiciosContratados.add(new TipoDeServicioContratado("UBER"));
        tipoServiciosContratados.add(new TipoDeServicioContratado("Cabify"));

        //Transportes publicos
        TransportePublico colectivoA = new TransportePublico(TipoTransportePublico.COLECTIVO, "151");
        TransportePublico colectivoB = new TransportePublico(TipoTransportePublico.COLECTIVO, "19");
        TransportePublico colectivoC = new TransportePublico(TipoTransportePublico.COLECTIVO, "130");

        TransportePublico subteA = new TransportePublico(TipoTransportePublico.SUBTE, "D");
        TransportePublico subteB = new TransportePublico(TipoTransportePublico.SUBTE, "B");
        TransportePublico subteC = new TransportePublico(TipoTransportePublico.SUBTE, "E");

        TransportePublico trenA = new TransportePublico(TipoTransportePublico.TREN, "Roca");
        TransportePublico trenB = new TransportePublico(TipoTransportePublico.TREN, "Mitre");
        TransportePublico trenC = new TransportePublico(TipoTransportePublico.TREN, "San Martin");

        //Paradas
        Parada A_1 = new Parada(argentina, caba, m_caba, palermo, "A", 10, new Distancia(150.0, "KM"), new Distancia(200.0, "KM"));
        Parada A_2 = new Parada(argentina, caba, m_caba, palermo, "B", 20, new Distancia(100.0, "KM"), new Distancia(75.0, "KM"));
        Parada A_3 = new Parada(argentina, caba, m_caba, retiro, "C", 30, new Distancia(75.0, "KM"), new Distancia(300.0, "KM"));

        Parada B_1 = new Parada(argentina, caba, m_caba, retiro, "A", 10, new Distancia(150.0, "KM"), new Distancia(200.0, "KM"));
        Parada B_2 = new Parada(argentina, caba, m_caba, caballito, "B", 20, new Distancia(100.0, "KM"), new Distancia(75.0, "KM"));
        Parada B_3 = new Parada(argentina, caba, m_caba, caballito, "C", 30, new Distancia(75.0, "KM"), new Distancia(300.0, "KM"));

        Parada C_1 = new Parada(argentina, caba, m_caba, caballito, "A", 10, new Distancia(150.0, "KM"), new Distancia(200.0, "KM"));
        Parada C_2 = new Parada(argentina, caba, m_caba, v_urquiza, "B", 20, new Distancia(100.0, "KM"), new Distancia(75.0, "KM"));
        Parada C_3 = new Parada(argentina, caba, m_caba, v_urquiza, "C", 30, new Distancia(75.0, "KM"), new Distancia(300.0, "KM"));

        Collections.addAll(paradas, A_1, A_2, A_3, B_1, B_2, B_3, C_1, C_2, C_3);

        colectivoA.agregarParadas(A_1, A_2, A_3);
        colectivoB.agregarParadas(B_1, B_2, B_3);
        colectivoC.agregarParadas(C_1, C_2, C_3);

        subteA.agregarParadas(A_1, A_2, A_3);
        subteB.agregarParadas(B_1, B_2, B_3);
        subteC.agregarParadas(C_1, C_2, C_3);

        trenA.agregarParadas(A_1, A_2, A_3);
        trenB.agregarParadas(B_1, B_2, B_3);
        trenC.agregarParadas(C_1, C_2, C_3);

        Collections.addAll(transportesPublicos, colectivoA, colectivoB, colectivoC, subteA, subteB, subteC, trenA, trenB, trenC);

        //Se cargan los excels
        nestle.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        telefonica.cargarExcel("src/main/resources/TestDA10Mediciones.xlsx");
        shell.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        sony.cargarExcel("src/main/resources/TestDA10Mediciones.xlsx");
        samsung.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");

        mediciones.addAll(nestle.getMediciones());
        mediciones.addAll(telefonica.getMediciones());
        mediciones.addAll(shell.getMediciones());
        mediciones.addAll(sony.getMediciones());
        mediciones.addAll(samsung.getMediciones());

        //Persisto los objetos
        oPersistentes.addAll(ubicaciones);
        oPersistentes.addAll(empleados);
        oPersistentes.addAll(organizaciones);
        oPersistentes.addAll(areas);
        oPersistentes.addAll(sectoresTerritoriales);
        oPersistentes.addAll(agentesSectoriales);
        oPersistentes.addAll(usuarios);
        oPersistentes.addAll(tipoServiciosContratados);
        oPersistentes.addAll(mediciones);
        oPersistentes.addAll(transportesPublicos);
        oPersistentes.addAll(paradas);

        EntityManagerHelper.beginTransaction();
        for (Object obj : oPersistentes){
            System.out.println(obj.toString());
            EntityManagerHelper.persist(obj);
        }
        EntityManagerHelper.commit();

        EntityManagerHelper.closeEntityManager();
        //EntityManagerHelper.closeEntityManagerFactory();
    }
}
