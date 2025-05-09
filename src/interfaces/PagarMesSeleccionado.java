
package interfaces;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author USER
 */
@FunctionalInterface
public interface PagarMesSeleccionado {
    public void pagarMes(Date fecha, Time hora, int idCliente, double montoPagado, String periodo);
}
