package ec.edu.monster.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Grupo 10
 *
 */

public class AccesoDB {

    public static Connection getConnection() throws SQLException {
        Connection cn = null;
        try {
            // Datos MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/eurekabank?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "Nacional178059");
        } catch (SQLException e) {
            throw e;
        } 
        catch (ClassNotFoundException e) {
            throw new SQLException("Error, no se encuentra el driver");
        } 
        catch (Exception e) {
            throw new SQLException("Error, no se tiene acceso al servidor");
        }
        return cn;
    }
}