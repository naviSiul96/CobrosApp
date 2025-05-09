package logica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public static void insertarPago(
            Date fecha,
            Time hora,
            int idToma,
            double monto,
            String periodo) throws SQLException {

        String sql = "INSERT INTO cobros (fecha, hora, id_toma, monto_pagado, periodo)"
                + " VALUES ('"
                + fecha + "','"
                + hora + "',"
                + idToma + ","
                + monto + ",'"
                + periodo + "')";

        registrar(sql);
    }

    public static ResultSet obtenerMesesNoPagadosDeUnCliente(int idCliente, int year) throws SQLException {
        String sql = "SELECT m.mes, t.numero_toma, t.id_toma, "
                + "CASE WHEN c.id IS NOT NULL THEN 'Pagado' ELSE 'No Pagado' END AS estado "
                + "FROM ( "
                + "  SELECT LPAD(mes_num, 2, '0') AS mes "
                + "  FROM (SELECT 1 AS mes_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 "
                + "        UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 "
                + "        UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) AS meses "
                + "  WHERE mes_num <= MONTH(CURRENT_DATE()) "
                + ") AS m "
                + "CROSS JOIN ( "
                + "  SELECT id AS id_toma, numero_toma FROM toma WHERE id_cliente = ? "
                + ") AS t "
                + "LEFT JOIN cobros c ON c.id_toma = t.id_toma AND c.periodo = CONCAT(?, '-', m.mes)";
        ResultSet rs = null;
        try {
            PreparedStatement ps = getStatementConn().getConnection().prepareStatement(sql);
            ps.setInt(1, idCliente);
            ps.setInt(2, year);

            rs = ps.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.err.println("Error al ejecutar consulta sql de obtener pagos y tomas " + ex.getMessage());
        }
        return rs;
    }

    public static float getMontoMensual() {
        String sql = "SELECT monto FROM couta WHERE id=1";
        float monto = 0;
        try {
            ResultSet rs = consulta(sql);
            rs.next();
            monto = rs.getFloat("monto");
        } catch (SQLException ex) {
            System.err.println("Error al obtener el monto " + ex.getMessage());
        }
        return monto;
    }

    public static String obtenerNombreMes(int numeroMes) {
        return Month.of(numeroMes).getDisplayName(TextStyle.FULL, new Locale("es"));
    }

    public static int obtenerNumeroMes(String nombreMes) {
        for (Month m : Month.values()) {
            if (m.getDisplayName(TextStyle.FULL, new Locale("es")).equalsIgnoreCase(nombreMes)) {
                return m.getValue();
            }
        }
        throw new IllegalArgumentException("Nombre de mes inválido: " + nombreMes);
    }

    public static boolean validarCobroCorrecto(int idToma, int idCliente) {
        PreparedStatement ps;
        boolean estado = true;
        try {
            ps = getStatementConn().getConnection().prepareStatement("SELECT id FROM toma WHERE id = ? AND id_cliente = ?");
            ps.setInt(1, idToma);
            ps.setInt(2, idCliente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                estado = true;
            } else {
                estado = false;
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar toma de cliente " + ex.getMessage());
        }
        return estado;
    }
}
