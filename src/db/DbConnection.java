
package db;

/**
 *
 * @author USER
 */
public class DbConnection {
    //metodo que retorne una conexion para conectar a la base de datos mysql usando jdbc    
    public static java.sql.Connection getConnection() throws java.sql.SQLException {
        String url = "jdbc:mysql://localhost:3306/db_cobros"; // Cambia esto por tu URL de conexión
        String user = "root"; // Cambia esto por tu usuario
        String password = ""; // Cambia esto por tu contraseña
        
        return java.sql.DriverManager.getConnection(url, user, password);   
    }
    
}
