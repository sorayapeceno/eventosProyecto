package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Estado;
import dam.primero.modelos.eventos_participantes.Modelo.Evento;
import dam.primero.modelos.eventos_participantes.Modelo.Modalidad;
import dam.primero.modelos.ventas.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepoEventos {
    private MySqlConector conector;

    //Constructor
    public RepoEventos() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

        //Metodos
        public Evento  crearEvento(Evento evento) {
            boolean creado = false;

            String query = """
        INSERT INTO evento
        (Nombre, Descripcion, Fecha_Inicio, Fecha_Fin,
         Direccion, Ciudad, Capacidad, Estado, Modalidad, Lugar)
        VALUES (?, ?, ?,?, ?,?, ?, ?, ?,?)
        """;

            try (PreparedStatement ps = this.conector.getConnect().prepareStatement(query)) {

                ps.setString(1, evento.getNombre());
                ps.setString(2, evento.getDescripcion());

                // LocalDate -> java.sql.Date
                ps.setDate(3, java.sql.Date.valueOf(evento.getFechaInicio()));
                ps.setDate(4, java.sql.Date.valueOf(evento.getFechaFin()));

                ps.setString(5, evento.getDireccion());
                ps.setString(6, evento.getCiudad());
                ps.setInt(7, evento.getCapacidad());

                // Enum -> String
                ps.setString(8, evento.getEstado().name().toUpperCase());
                ps.setString(9, evento.getModalidad().name().toUpperCase());

                ps.setString(10, evento.getLugar());
               int numActualizado =  ps.executeUpdate();
               if(numActualizado == 1)
               {

               }
               else
               {
                   throw new MyException("Error al dar de alta un evento: "+evento);
               }



                } catch (SQLException | MyException e) {
                    System.out.println("Error al crear evento: " + e.getMessage());
                }
            return evento;

        }


            public List<Evento> listarEvento() {

              List<Evento> eventos = new ArrayList<>();

                String query = "select * from evento;";

                Statement stmt = null;
                ResultSet rs = null;

                try {

                    stmt = this.conector.getConnect().createStatement();
                    rs = stmt.executeQuery(query);

                    while (rs.next()) {

                        int id_Evento = rs.getInt("id_Evento");
                        String nombre = rs.getString("Nombre");
                        String descripcion = rs.getString("Descripcion");
                        LocalDate fechaInicio = rs.getDate("Fecha_Inicio").toLocalDate();
                        LocalDate fechaFin = rs.getDate("Fecha_Fin").toLocalDate();
                        String direccion = rs.getString("Direccion");
                        String ciudad = rs.getString("Ciudad");
                        int capacidad = rs.getInt("Capacidad");
                        Estado estado = Estado.valueOf(rs.getString("Estado").toUpperCase());
                        Modalidad modalidad = Modalidad.valueOf(rs.getString("Modalidad").toUpperCase());
                        String lugar = rs.getString("Lugar");

                        Evento evento = new Evento(id_Evento, nombre, descripcion, fechaInicio, fechaFin, direccion,ciudad,
                                capacidad,estado,modalidad,lugar);

                        eventos.add(evento);
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                return eventos;
            }
    }
