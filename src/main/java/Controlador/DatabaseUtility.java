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
                Object[] row = new Object[7];
                row[0] = rs.getString("Tipo");
                row[1] = rs.getInt("Año");
                row[2] = rs.getInt("Nro");
                row[3] = rs.getString("DNI");
                row[4] = rs.getString("Empleado");
                row[5] = rs.getDouble("Aporte");
                row[6] = rs.getDouble("Deuda");
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
