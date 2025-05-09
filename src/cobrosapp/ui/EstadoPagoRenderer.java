/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cobrosapp.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author USER
 */
public class EstadoPagoRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String estado = value.toString();
        if ("Pagado".equalsIgnoreCase(estado)) {
            c.setBackground(new Color(0x2E8B57));
            c.setForeground(Color.WHITE);
        } else if ("No Pagado".equalsIgnoreCase(estado)) {
            c.setBackground(new Color(0xB03A2E));
            c.setForeground(Color.WHITE);
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
        }

        return c;
    }
}
