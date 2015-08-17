/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Capa_Datos.BdConexion;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.*;

/**
 *
 * @author GLARA
 */
public class Recibodepago {

    static Connection conn;

    public static void comprobante(int parameter, String horario) {
        try {
            String theReport = "Recibo3.jasper";
            if (theReport == null) {
                System.exit(2);
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(theReport);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.exit(3);
            }

            conn = BdConexion.getConexion();
            Map parametro = new HashMap();
            int idrecibo = (parameter);
            parametro.put("idrecibo", idrecibo);
            parametro.put("horario", horario);

            JasperPrint impresor = JasperFillManager.fillReport(masterReport, parametro, conn);
            //JasperPrintManager.printReport(impresor, false);
            JasperViewer jviewer = new JasperViewer(impresor, false);
            jviewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            jviewer.setTitle("Comprobante de Pago");
            jviewer.setVisible(true);

        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

}
