package com.logismart.datos.mapper;

import com.logismart.datos.dominio.Cliente;
import java.sql.*;

public class ClienteMapper {
    private final Connection conexion;

    public ClienteMapper(Connection conexion) {
        this.conexion = conexion;
    }

    public void insert(Cliente cliente) {
        String sql = "INSERT INTO clientes (id, nombre, email, telefono) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            stmt.executeUpdate();
            System.out.println("[ClienteMapper] INSERT id=" + cliente.getId());
        } catch (SQLException e) {
            System.out.println("[ClienteMapper] ERROR insert: " + e.getMessage());
        }
    }

    public Cliente findById(int id) {
        String sql = "SELECT * FROM clientes WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            System.out.println("[ClienteMapper] ERROR findById: " + e.getMessage());
        }
        return null;
    }

    public void update(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, email=?, telefono=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
            System.out.println("[ClienteMapper] UPDATE id=" + cliente.getId());
        } catch (SQLException e) {
            System.out.println("[ClienteMapper] ERROR update: " + e.getMessage());
        }
    }

    public void delete(Cliente cliente) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.executeUpdate();
            System.out.println("[ClienteMapper] DELETE id=" + cliente.getId());
        } catch (SQLException e) {
            System.out.println("[ClienteMapper] ERROR delete: " + e.getMessage());
        }
    }
}
