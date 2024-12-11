/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

/**
 *
 * @author Ernesto
 */
public class Conexion {

    private static String URL;
    
    private static String server_ip, server_port, server_user, server_pass, server_bd;
    private static final Properties properties = new Properties();
    
    private static void GET_PROPERTIES() {
        try {
            properties.load(new FileInputStream(new File("config.properties")));
            server_ip = String.valueOf(properties.get("server_ip"));
            
            server_port = String.valueOf(properties.get("server_port"));
            server_bd = String.valueOf(properties.get("server_bd"));
            server_user = ds(String.valueOf(properties.get("server_user")), "javax.crypto.spec.SecretKeySpec@16f33");
            server_pass = ds(String.valueOf(properties.get("server_pass")), "javax.crypto.spec.SecretKeySpec@16f33");
            
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        GET_PROPERTIES();
        URL = "jdbc:sqlserver://" + server_ip + ":" + server_port + ";databaseName=" + server_bd + ";encrypt=true;trustServerCertificate=true";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, server_user, server_pass);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar el driver de la base de datos", "Error SQL", JOptionPane.ERROR_MESSAGE);
            throw new SQLException("No se pudo cargar el driver de la base de datos", e);
        }
    }
    
    private static String ds(String de, String cs) {
        try {
            byte[] ce = cs.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            ce = sha.digest(ce);
            ce = Arrays.copyOf(ce, 16);
            SecretKeySpec sk = new SecretKeySpec(ce, "AES");
            SecretKeySpec sk1 = sk;
            Cipher cp = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cp.init(Cipher.DECRYPT_MODE, sk1);
            byte[] be = Base64.getDecoder().decode(de);
            byte[] dd = cp.doFinal(be);
            String var = new String(dd);
            return var;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | java.lang.IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Error al Conectar con el Servidor, Su solicitud no se ha podido procesar.\n\nContacte con Soporte Tecnico\nCodigo de Error: Clave SQL JDBC - Incorrecto", "Información", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        return null;
    }
    
   
    
}
