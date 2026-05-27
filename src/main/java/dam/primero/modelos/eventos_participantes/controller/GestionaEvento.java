package dam.primero.modelos.eventos_participantes.controller;

import dam.primero.modelos.eventos_participantes.Modelo.*;
import dam.primero.repositorio.eventos_participantes.EstadoRepo;
import dam.primero.repositorio.eventos_participantes.PonenciaRepo;
import dam.primero.repositorio.eventos_participantes.RepoEventos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionaEvento {
    public static void main(String[] args) {
        // Prueba metodo listar evento
        RepoEventos repE = new RepoEventos();

        Evento evento1 = new Evento(
                1,
                "InnovaCode: Jornadas de Programación y BBDD",
                "Evento sobre programación y bases de datos",
                LocalDate.of(2026, 6, 20),
                LocalDate.of(2026, 6, 21),
                "Calle Leonardo Da Vinci, 18",
                "Sevilla",
                150,
                Estado.CERRADO,
                Modalidad.HIBRIDO,
                "TecnoIncubadora"
        );



        Evento evento2 = new Evento(
                2,
                "Frontend Lab Conference: UX y Desarrollo Moderno",
                "Conferencia sobre UX y desarrollo frontend moderno",
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 10),
                "Avenida Reina Mercedes s/n",
                "Sevilla",
                250,
                Estado.BORRADOR,
                Modalidad.PRESENCIAL,
                "Escuela Técnica"
        );

        Evento evento3 = new Evento(
                3,
                "Transformando el aula con IA",
                "Evento sobre inteligencia artificial aplicada a la educación",
                LocalDate.of(2026, 12, 12),
                LocalDate.of(2026, 12, 12),
                "Avenida Alcalde Luis Uruñuela, 1",
                "Sevilla",
                300,
                Estado.BORRADOR,
                Modalidad.HIBRIDO,
                "FIBES"
        );

        Evento evento4 = new Evento(
                4,
                "Seguridad en Redes Modernas",
                "Evento sobre ciberseguridad y redes",
                LocalDate.of(2026, 11, 5),
                LocalDate.of(2026, 11, 5),
                "Avenida Alcalde Luis Uruñuela, 1",
                "Sevilla",
                300,
                Estado.ABIERTO,
                Modalidad.HIBRIDO,
                "FIBES"
        );
        Evento evento5 = new Evento(
                3,
                "CiberTech Summit 2026",
                "Jornada especializada en ciberseguridad, redes y hacking ético",
                LocalDate.of(2026, 11, 14),
                LocalDate.of(2026, 11, 15),
                "Calle Arquitectura 12",
                "Madrid",
                400,
                Estado.ABIERTO,
                Modalidad.HIBRIDO,
                "Centro de Innovación Digital"
        );

        RepoEventos rep2 = new RepoEventos();
        //rep2.crearEvento(evento5);

        Evento evento6 = new Evento(
                4,
                "AI & Data Future Expo 2026",
                "Evento sobre inteligencia artificial, big data y desarrollo de soluciones cloud",
                LocalDate.of(2026, 9, 20),
                LocalDate.of(2026, 9, 22),
                "Avenida de la Tecnología 45",
                "Barcelona",
                600,
                Estado.ABIERTO,
                Modalidad.PRESENCIAL,
                "Fira Tecnológica Barcelona"
        );

       // rep2.crearEvento(evento6);

        Tematica t = new Tematica(12,"Tema de la Ponencia");


        Ponencia ponencia = new Ponencia(

                1,
                "Título",
                t,
                120,
                LocalDate.now(),
                LocalDateTime.now(),
                "Ubicación",
                Nivel.INTERMEDIO,
                Tipo.CHARLA,
                Formato.HIBRIDO
        );
        t.setId_Tematica(ponencia.getTematica().getId_Tematica()); // o 12 directamente
        t.setTema(ponencia.getTematica().getTema());

        ponencia.setTematica(t);


        PonenciaRepo repo = new PonenciaRepo();
        repo.crearPonencia(ponencia);



        List<Evento> eventos = new ArrayList<Evento>();

       // System.out.println(eventos);



        /*eventos = repE.listarEvento();
        System.out.println(eventos);



         List<Estado> estados = new ArrayList<Estado>();
         EstadoRepo repo3 = new EstadoRepo();
         estados = repo3.listarEstados();
        System.out.println(estados);

        List<Ponencia> ponencias = new ArrayList<Ponencia>();
        PonenciaRepo ponenciaRepo = new PonenciaRepo();
        ponencias = ponenciaRepo.listarPonencias();
        System.out.println(ponencias);*/


    }
}
