/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class DatabaseUtility {
    public static List<Object[]> executeQuery(String query) {
        List<Object[]> results = new ArrayList<>();
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getInt("Nro");
                row[1] = rs.getString("Documento");
                row[2] = rs.getString("Empleado");
                row[3] = rs.getDouble("Aporte");
                row[4] = rs.getDouble("Deuda");
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
