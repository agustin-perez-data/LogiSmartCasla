package com.logismart.datos.mapper;

import com.logismart.datos.dominio.Envio;
import com.logismart.datos.dominio.EstadoEnvio;
import java.sql.*;

public class EnvioMapper {
    private final Connection conexion;

    public EnvioMapper(Connection conexion) {
        this.conexion = conexion;
    }

    public void insert(Envio envio) {
        String sql = "INSERT INTO envios (id, origen, destino, peso, fecha_creacion, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, envio.getId());
            stmt.setString(2, envio.getOrigen());
            stmt.setString(3, envio.getDestino());
            stmt.setDouble(4, envio.getPeso());
            stmt.setTimestamp(5, Timestamp.valueOf(envio.getFechaCreacion()));
            stmt.setString(6, envio.getEstado().toString());
            stmt.executeUpdate();
            System.out.println("[EnvioMapper] INSERT id=" + envio.getId());
        } catch (SQLException e) {
            System.out.println("[EnvioMapper] ERROR insert: " + e.getMessage());
        }
    }

    public Envio findById(int id) {
        String sql = "SELECT * FROM envios WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Envio envio = new Envio(
                    rs.getInt("id"),
                    rs.getString("origen"),
                    rs.getString("destino"),
                    rs.getDouble("peso")
                );
                envio.setEstado(EstadoEnvio.valueOf(rs.getString("estado")));
                return envio;
            }
        } catch (SQLException e) {
            System.out.println("[EnvioMapper] ERROR findById: " + e.getMessage());
        }
        return null;
    }

    public void update(Envio envio) {
        String sql = "UPDATE envios SET origen=?, destino=?, peso=?, estado=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, envio.getOrigen());
            stmt.setString(2, envio.getDestino());
            stmt.setDouble(3, envio.getPeso());
            stmt.setString(4, envio.getEstado().toString());
            stmt.setInt(5, envio.getId());
            stmt.executeUpdate();
            System.out.println("[EnvioMapper] UPDATE id=" + envio.getId());
        } catch (SQLException e) {
            System.out.println("[EnvioMapper] ERROR update: " + e.getMessage());
        }
    }

    public void delete(Envio envio) {
        String sql = "DELETE FROM envios WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, envio.getId());
            stmt.executeUpdate();
            System.out.println("[EnvioMapper] DELETE id=" + envio.getId());
        } catch (SQLException e) {
            System.out.println("[EnvioMapper] ERROR delete: " + e.getMessage());
        }
    }
}
