package dam.primero.repositorio.ventas;

import dam.primero.modelos.ventas.Asistente;
import dam.primero.config.ventas.MySqlConnectorVentas;
import dam.primero.exception.MyException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsistenteRepository {

    private Asistente mapRow(ResultSet rs) throws SQLException {
        return new Asistente(
            rs.getLong("ID_Asistente"),
            rs.getString("Tematica"),
            rs.getString("Direccion"),
            rs.getString("Observaciones"),
            rs.getString("Bio"),
            rs.getDouble("Total_Gastado"),
            rs.getString("Nivel_Imparticion")
        );
    }

    public List<Asistente> findAll() throws MyException {
        List<Asistente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Asistente ORDER BY ID_Asistente";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new MyException("Error al listar asistentes: " + e.getMessage());
        } finally {
            conector.release();
        }
        return lista;
    }

    public Optional<Asistente> findById(long id) throws MyException {
        String sql = "SELECT * FROM Asistente WHERE ID_Asistente = ?";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new MyException("Error al buscar asistente id=" + id + ": " + e.getMessage());
        } finally {
            conector.release();
        }
        return Optional.empty();
    }

    public void save(Asistente a) throws MyException {
        String sql = "INSERT INTO Asistente " +
                     "(ID_Asistente, Tematica, Direccion, Observaciones, Bio, Total_Gastado, Nivel_Imparticion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            ps.setLong(1, a.getId_Asistente());
            ps.setString(2, a.getTematica());
            ps.setString(3, a.getDireccion());
            ps.setString(4, a.getObservaciones());
            ps.setString(5, a.getBio());
            ps.setDouble(6, a.getTotalGastado());
            ps.setString(7, a.getNivelImparticion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MyException("Error al guardar asistente: " + e.getMessage());
        } finally {
            conector.release();
        }
    }

    public void update(Asistente a) throws MyException {
        String sql = "UPDATE Asistente SET Tematica=?, Direccion=?, Observaciones=?, " +
                     "Bio=?, Total_Gastado=?, Nivel_Imparticion=? " +
                     "WHERE ID_Asistente=?";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            ps.setString(1, a.getTematica());
            ps.setString(2, a.getDireccion());
            ps.setString(3, a.getObservaciones());
            ps.setString(4, a.getBio());
            ps.setDouble(5, a.getTotalGastado());
            ps.setString(6, a.getNivelImparticion());
            ps.setLong(7, a.getId_Asistente());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MyException("Error al actualizar asistente: " + e.getMessage());
        } finally {
            conector.release();
        }
    }

    public void deleteById(long id) throws MyException {
        String sql = "DELETE FROM Asistente WHERE ID_Asistente = ?";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MyException("Error al eliminar asistente id=" + id + ": " + e.getMessage());
        } finally {
            conector.release();
        }
    }

    public void actualizarTotalGastado(long id, double nuevoTotal) throws MyException {
        String sql = "UPDATE Asistente SET Total_Gastado = ? WHERE ID_Asistente = ?";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, nuevoTotal);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MyException("Error al actualizar total gastado: " + e.getMessage());
        } finally {
            conector.release();
        }
    }

    public long getNextId() throws MyException {
        String sql = "SELECT COALESCE(MAX(ID_Asistente), 0) + 1 AS next_id FROM Asistente";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("next_id");
            }
        } catch (SQLException e) {
            throw new MyException("Error al obtener siguiente ID: " + e.getMessage());
        } finally {
            conector.release();
        }
        return 1;
    }
}
