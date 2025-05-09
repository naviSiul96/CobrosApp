package logica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.Mes;


/**
 *
 * @author USER
 */
public class Metodos {

    public static ResultSet consulta(String sql) throws java.sql.SQLException {
        ResultSet rs = getStatementConn().executeQuery(sql);

        return rs;
    }

    public static void registrar(String sql) throws java.sql.SQLException {
        getStatementConn().executeUpdate(sql);
    }

    public static Statement getStatementConn() throws java.sql.SQLException {
        Connection conn = db.DbConnection.getConnection();
        return conn.createStatement();
    }

    //metodo que genera consulta sql tipo select para obtener todos los registros de una tabla usando LIKE para obtener las conincidencias
    public static ResultSet consultaLike(String campo, String valor) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE " + campo + " LIKE '%" + valor + "%'";

        return consulta(sql);
    }

    public static ResultSet consultaPagoMensual(int idCliente, String periodo) throws SQLException {
        String sql = "SELECT COUNT(*) AS pagos_realizados "
                + "FROM cobros "
                + "WHERE id_cliente=" + idCliente
                + " AND periodo= '" + periodo + "'";

        return consulta(sql);
    }

    public static void insertarPago(
            Date fecha,
            Time hora,
            int idCliente,
            double monto,
            String periodo) throws SQLException {

        String sql = "INSERT INTO cobros (fecha, hora, id_cliente, monto_pagado, periodo)"
                + " VALUES ('"
                + fecha + "','"
                + hora + "',"
                + idCliente + ","
                + monto + ",'"
                + periodo + "')";

        System.out.println("" + sql);
        registrar(sql);
    }

    public static List<Mes> obtenerMesesNoPagadosDeUnCliente(int idCliente, String year) {
        List<String> meses = obtenerMesesHastaActual(year);
        List<Mes> noPagadosYPagados = new ArrayList<>();

        String sql = "SELECT periodo FROM cobros WHERE id_cliente = ? AND periodo IN ("
                + String.join(",", meses.stream().map(m -> "?").toArray(String[]::new)) + ")";
        
        PreparedStatement stmt;
        try {
            stmt = getStatementConn().getConnection().prepareStatement(sql);

            stmt.setInt(1, idCliente);
            for (int i = 0; i < meses.size(); i++) {
                stmt.setString(i + 2, meses.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            List<String> pagados = new ArrayList<>();
            while (rs.next()) {
                pagados.add(rs.getString("periodo"));
            }

            for (String mes : meses) {
                if (!pagados.contains(mes)) {
                    noPagadosYPagados.add(new Mes(mes, false));
                }else{
                    noPagadosYPagados.add(new Mes(mes, true));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al obtener meses no pagados " + ex.getMessage());
        }

        return noPagadosYPagados;
    }

    public static List<String> obtenerMesesHastaActual(String year) {
        List<String> meses = new ArrayList<>();
        int mesActual = java.time.LocalDate.now().getMonthValue();

        for (int i = 1; i <= mesActual; i++) {
            YearMonth ym = YearMonth.of(Integer.parseInt(year), i);
            meses.add(ym.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        }

        return meses;
    }
    
    public static float getMontoMensual(){
        String sql = "SELECT monto WHERE id=1";
        float monto = 0;
        try {
            ResultSet rs = consulta(sql);
            monto = rs.getFloat("monto");
        } catch (SQLException ex) {
            System.err.println("Error al obtener el monto");
        }
        return monto;
    }
}
