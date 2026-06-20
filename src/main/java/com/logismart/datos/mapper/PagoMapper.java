package com.logismart.datos.mapper;

import com.logismart.datos.dominio.EstadoPago;
import com.logismart.datos.dominio.Pago;
import java.sql.*;

public class PagoMapper {
    private final Connection conexion;

    public PagoMapper(Connection conexion) {
        this.conexion = conexion;
    }

    public void insert(Pago pago) {
        String sql = "INSERT INTO pagos (id, envio_id, monto, fecha, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, pago.getId());
            stmt.setInt(2, pago.getEnvioId());
            stmt.setDouble(3, pago.getMonto());
            stmt.setTimestamp(4, Timestamp.valueOf(pago.getFecha()));
            stmt.setString(5, pago.getEstado().toString());
            stmt.executeUpdate();
            System.out.println("[PagoMapper] INSERT id=" + pago.getId());
        } catch (SQLException e) {
            System.out.println("[PagoMapper] ERROR insert: " + e.getMessage());
        }
    }

    public Pago findById(int id) {
        String sql = "SELECT * FROM pagos WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pago pago = new Pago(
                    rs.getInt("id"),
                    rs.getInt("envio_id"),
                    rs.getDouble("monto")
                );
                pago.setEstado(EstadoPago.valueOf(rs.getString("estado")));
                return pago;
            }
        } catch (SQLException e) {
            System.out.println("[PagoMapper] ERROR findById: " + e.getMessage());
        }
        return null;
    }

    public void update(Pago pago) {
        String sql = "UPDATE pagos SET monto=?, estado=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setDouble(1, pago.getMonto());
            stmt.setString(2, pago.getEstado().toString());
            stmt.setInt(3, pago.getId());
            stmt.executeUpdate();
            System.out.println("[PagoMapper] UPDATE id=" + pago.getId());
        } catch (SQLException e) {
            System.out.println("[PagoMapper] ERROR update: " + e.getMessage());
        }
    }

    public void delete(Pago pago) {
        String sql = "DELETE FROM pagos WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, pago.getId());
            stmt.executeUpdate();
            System.out.println("[PagoMapper] DELETE id=" + pago.getId());
        } catch (SQLException e) {
            System.out.println("[PagoMapper] ERROR delete: " + e.getMessage());
        }
    }
}
