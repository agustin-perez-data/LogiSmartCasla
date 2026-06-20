package com.logismart.datos.mapper;

import com.logismart.datos.dominio.CentroDistribucion;
import java.sql.*;

public class CentroDistribucionMapper {
    private final Connection conexion;

    public CentroDistribucionMapper(Connection conexion) {
        this.conexion = conexion;
    }

    public void insert(CentroDistribucion centro) {
        String sql = "INSERT INTO centros (id, nombre, ciudad, capacidad, ocupacion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, centro.getId());
            stmt.setString(2, centro.getNombre());
            stmt.setString(3, centro.getCiudad());
            stmt.setDouble(4, centro.getCapacidad());
            stmt.setDouble(5, centro.getOcupacion());
            stmt.executeUpdate();
            System.out.println("[CentroMapper] INSERT id=" + centro.getId());
        } catch (SQLException e) {
            System.out.println("[CentroMapper] ERROR insert: " + e.getMessage());
        }
    }

    public CentroDistribucion findById(int id) {
        String sql = "SELECT * FROM centros WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CentroDistribucion centro = new CentroDistribucion(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("ciudad"),
                    rs.getDouble("capacidad")
                );
                centro.setOcupacion(rs.getDouble("ocupacion"));
                return centro;
            }
        } catch (SQLException e) {
            System.out.println("[CentroMapper] ERROR findById: " + e.getMessage());
        }
        return null;
    }

    public void update(CentroDistribucion centro) {
        String sql = "UPDATE centros SET nombre=?, ciudad=?, capacidad=?, ocupacion=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, centro.getNombre());
            stmt.setString(2, centro.getCiudad());
            stmt.setDouble(3, centro.getCapacidad());
            stmt.setDouble(4, centro.getOcupacion());
            stmt.setInt(5, centro.getId());
            stmt.executeUpdate();
            System.out.println("[CentroMapper] UPDATE id=" + centro.getId());
        } catch (SQLException e) {
            System.out.println("[CentroMapper] ERROR update: " + e.getMessage());
        }
    }

    public void delete(CentroDistribucion centro) {
        String sql = "DELETE FROM centros WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, centro.getId());
            stmt.executeUpdate();
            System.out.println("[CentroMapper] DELETE id=" + centro.getId());
        } catch (SQLException e) {
            System.out.println("[CentroMapper] ERROR delete: " + e.getMessage());
        }
    }
}
