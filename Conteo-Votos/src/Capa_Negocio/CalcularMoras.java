/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_Negocio;

import Capa_Datos.BdConexion;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author GLARA
 */
public class CalcularMoras {

    static java.sql.Connection conn;

    public static void moras() {
        String sql = "INSERT INTO mora (mora , proyeccionpagos_idproyeccionpagos) SELECT '10', idproyeccionpagos from proyeccionpagos WHERE fechavencimiento < CURRENT_DATE()and estado=false and asignado=true and mes_idmes <> '13' and monto > '0' and idproyeccionpagos not in (select proyeccionpagos_idproyeccionpagos from mora)";
        int n = 0;

        conn = BdConexion.getConexion();

        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement(sql);
            n = ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        if (n > 0) {
            JOptionPane.showMessageDialog(null, "El sistema encontro pagos atrasados y ha calculado Moras", "Moras Calculadas", JOptionPane.INFORMATION_MESSAGE);
        } else {
            //JOptionPane.showMessageDialog(null, "No Se encontraron moras que calcular");
        }

    }

}
