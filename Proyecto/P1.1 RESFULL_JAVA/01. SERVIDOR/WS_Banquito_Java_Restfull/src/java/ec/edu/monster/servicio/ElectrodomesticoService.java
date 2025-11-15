/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.servicio;

/**
 *
 * @author sdiegx
 */
import ec.edu.monster.db.DatabaseConnection;
import ec.edu.monster.modelo.Electrodomesticos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectrodomesticoService {


    public List<Electrodomesticos> getAll() throws SQLException {
        List<Electrodomesticos> electrodomesticoList= new ArrayList<>();
        String sql = "SELECT * FROM ELECTRODOMESTICO";

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Electrodomesticos electrodomestico = new Electrodomesticos();
                electrodomestico.setId(rs.getInt("Id"));
                electrodomestico.setNombre(rs.getString("Nombre"));
                electrodomestico.setPrecio(rs.getDouble("Precio"));
                electrodomestico.setFotoUrl(rs.getString("FotoUrl"));

                electrodomesticoList.add(electrodomestico);
            }
        }

        return electrodomesticoList;
    }

    public Electrodomesticos getById(int id) throws SQLException {
        Electrodomesticos electrodomesticos = null;
        String sql = "SELECT * FROM ELECTRODOMESTICO WHERE Id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    electrodomesticos = new Electrodomesticos();
                    electrodomesticos.setId(rs.getInt("Id"));
                    electrodomesticos.setNombre(rs.getString("Nombre"));
                    electrodomesticos.setPrecio(rs.getDouble("Precio"));
                    electrodomesticos.setFotoUrl(rs.getString("FotoUrl"));
                }
            }
        }

        return electrodomesticos;
    }

    public void create(Electrodomesticos e) throws SQLException {
        String sql = "INSERT INTO ELECTRODOMESTICO (Nombre, Precio, FotoUrl) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setDouble(2, e.getPrecio());
            ps.setString(3, e.getFotoUrl());

            ps.executeUpdate();
        }
    }

    public boolean update(int id, Electrodomesticos electrodomesticos) throws SQLException {
        String sql = "UPDATE ELECTRODOMESTICO SET Nombre = ?, Precio = ?, FotoUrl = ? WHERE Id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, electrodomesticos.getNombre());
            ps.setDouble(2, electrodomesticos.getPrecio());
            ps.setString(3, electrodomesticos.getFotoUrl());
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM ELECTRODOMESTICO WHERE Id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}

