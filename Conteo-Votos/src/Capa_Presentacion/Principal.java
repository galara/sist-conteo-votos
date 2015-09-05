package Capa_Presentacion;

import BackupMySQL.Backup;
import BackupMySQL.RestaurarBackup;
import Capa_Negocio.AccesoUsuario;
import Capa_Negocio.AddForms;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 * @author Glara
 */
public class Principal extends javax.swing.JFrame {

    public Principal() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("/Recursos/milogo.png")).getImage());
        usuario.setText(AccesoUsuario.getUsuario());
    }

    private void cerrarVentana() {
        int count = dp.getComponentCount();
        JInternalFrame[] cm = dp.getAllFrames();

//        for (int i = 0; i < cm.length; i++) {
//            System.out.print(cm[i].getName() + "\n");
//        }
        if (count == 0) {
            int nu = JOptionPane.showConfirmDialog(this, "¿Desea Cerrar esta ventana?", "Cerrar Sistema", JOptionPane.YES_NO_OPTION);

            if (nu == JOptionPane.YES_OPTION || nu == 0) {
                System.exit(0);
            } else {

            }

        } else if (count > 0) {
            JOptionPane.showMessageDialog(null, "Para cerrar el Systema primero debe cerrar los formularios abiertos " + "( " + (count) + " )");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        dp = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        sistema = new javax.swing.JLabel();
        usuario = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        msalir = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mcandidatura = new javax.swing.JMenuItem();
        mpartido = new javax.swing.JMenuItem();
        mcandidato = new javax.swing.JMenuItem();
        mmesa = new javax.swing.JMenuItem();
        mcentro = new javax.swing.JMenuItem();
        mvotos = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        musuario = new javax.swing.JMenuItem();
        GestionarBD = new javax.swing.JMenu();
        mbackup = new javax.swing.JMenuItem();
        mrestaurarbackup = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        mresultados = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        jMenuItem4.setText("jMenuItem4");

        jMenuItem5.setText("jMenuItem5");

        jMenuItem6.setText("jMenuItem6");

        jMenuItem7.setText("jMenuItem7");

        jMenuItem15.setText("jMenuItem15");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Principal SYSTEMA DE CONTEO DE VOTOS 2015");
        setName("Principal"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        dp.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout dpLayout = new javax.swing.GroupLayout(dp);
        dp.setLayout(dpLayout);
        dpLayout.setHorizontalGroup(
            dpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 950, Short.MAX_VALUE)
        );
        dpLayout.setVerticalGroup(
            dpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );

        getContentPane().add(dp);

        jPanel1.setPreferredSize(new java.awt.Dimension(549, 30));

        sistema.setFont(new java.awt.Font("Script MT Bold", 1, 21)); // NOI18N
        sistema.setForeground(new java.awt.Color(51, 51, 255));
        sistema.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sistema.setText("Usuario :");
        sistema.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        usuario.setFont(new java.awt.Font("Script MT Bold", 1, 21)); // NOI18N
        usuario.setForeground(new java.awt.Color(51, 51, 255));
        usuario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        usuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(sistema, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 703, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sistema, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(jPanel1);

        jMenuBar1.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        jMenu1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/folder.png"))); // NOI18N
        jMenu1.setText("Archivo");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        msalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        msalir.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        msalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/salir.png"))); // NOI18N
        msalir.setText("Salir");
        msalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msalirActionPerformed(evt);
            }
        });
        jMenu1.add(msalir);

        jMenuBar1.add(jMenu1);

        jMenu3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cog.png"))); // NOI18N
        jMenu3.setText("Mantenimiento");
        jMenu3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        mcandidatura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/group.png"))); // NOI18N
        mcandidatura.setText("Candidatura");
        mcandidatura.setName("Candidatura"); // NOI18N
        mcandidatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcandidaturaActionPerformed(evt);
            }
        });
        jMenu3.add(mcandidatura);

        mpartido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/book_addresses.png"))); // NOI18N
        mpartido.setText("Pardido Politico");
        mpartido.setName("Partido Politico"); // NOI18N
        mpartido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mpartidoActionPerformed(evt);
            }
        });
        jMenu3.add(mpartido);

        mcandidato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/status_online.png"))); // NOI18N
        mcandidato.setText("Candidato");
        mcandidato.setName("Candidato"); // NOI18N
        mcandidato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcandidatoActionPerformed(evt);
            }
        });
        jMenu3.add(mcandidato);

        mmesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        mmesa.setText("Mesa de votación");
        mmesa.setName("Mesa Votacion"); // NOI18N
        mmesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mmesaActionPerformed(evt);
            }
        });
        jMenu3.add(mmesa);

        mcentro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/carrera.png"))); // NOI18N
        mcentro.setText("Centro Votación");
        mcentro.setName("Centro Votacion"); // NOI18N
        mcentro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcentroActionPerformed(evt);
            }
        });
        jMenu3.add(mcentro);

        mvotos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/application_form.png"))); // NOI18N
        mvotos.setText("Ingreso Votos");
        mvotos.setName("Ingreso Votos"); // NOI18N
        mvotos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mvotosActionPerformed(evt);
            }
        });
        jMenu3.add(mvotos);

        jMenuBar1.add(jMenu3);

        jMenu2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/application_form.png"))); // NOI18N
        jMenu2.setText("Sistema");
        jMenu2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        musuario.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        musuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/login.png"))); // NOI18N
        musuario.setText("Usuario");
        musuario.setName("Usuario"); // NOI18N
        musuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                musuarioActionPerformed(evt);
            }
        });
        jMenu2.add(musuario);

        GestionarBD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconobackup.jpg"))); // NOI18N
        GestionarBD.setText("Gestionar Base de Datos");
        GestionarBD.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        GestionarBD.setName("Gestionar BD"); // NOI18N

        mbackup.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        mbackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/generarBK.png"))); // NOI18N
        mbackup.setText("Generar Backup");
        mbackup.setName("Backup BD"); // NOI18N
        mbackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbackupActionPerformed(evt);
            }
        });
        GestionarBD.add(mbackup);

        mrestaurarbackup.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        mrestaurarbackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/restaurarBD.png"))); // NOI18N
        mrestaurarbackup.setText("Restaurar Backup");
        mrestaurarbackup.setName("Backup Restaurar"); // NOI18N
        mrestaurarbackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mrestaurarbackupActionPerformed(evt);
            }
        });
        mrestaurarbackup.setVisible(false);
        GestionarBD.add(mrestaurarbackup);

        jMenu2.add(GestionarBD);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/update.png"))); // NOI18N
        jMenuItem3.setText("Borrar datos de Mesa");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/report.png"))); // NOI18N
        jMenu5.setText("Informes");
        jMenu5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        mresultados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/calculator.png"))); // NOI18N
        mresultados.setText("Resultados Preliminares");
        mresultados.setName("Resultados Preliminares"); // NOI18N
        mresultados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mresultadosActionPerformed(evt);
            }
        });
        jMenu5.add(mresultados);

        jMenuBar1.add(jMenu5);

        jMenu4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu4.setText("Ayuda");
        jMenu4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/information.png"))); // NOI18N
        jMenuItem1.setText("Acerca de");
        jMenuItem1.setName("Acerca de Principal"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void msalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msalirActionPerformed
        // TODO add your handling code here:
        cerrarVentana();

    }//GEN-LAST:event_msalirActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formWindowClosing

    private void musuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_musuarioActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(musuario.getName()) == true) {
            Usuario frmUsuario = new Usuario();
            if (frmUsuario == null) {
                frmUsuario = new Usuario();
            }
            AddForms.adminInternalFrame(dp, frmUsuario);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_musuarioActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(jMenuItem1.getName()) == true) {
            Ayuda frmAyuda = new Ayuda();
            if (frmAyuda == null) {
                frmAyuda = new Ayuda();
            }
            AddForms.adminInternalFrame(dp, frmAyuda);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mbackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbackupActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mbackup.getName()) == true) {
            Backup frmBackup = new Backup();
            if (frmBackup == null) {
                frmBackup = new Backup();
            }
            AddForms.adminInternalFrame(dp, frmBackup);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mbackupActionPerformed

    private void mrestaurarbackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mrestaurarbackupActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mrestaurarbackup.getName()) == true) {
            RestaurarBackup frmRestaurarBackup = new RestaurarBackup();
            if (frmRestaurarBackup == null) {
                frmRestaurarBackup = new RestaurarBackup();
            }
            AddForms.adminInternalFrame(dp, frmRestaurarBackup);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mrestaurarbackupActionPerformed

    private void mcandidaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcandidaturaActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mcandidatura.getName()) == true) {
            Cargo_politico form = new Cargo_politico();
            if (form == null) {
                form = new Cargo_politico();
            }
            AddForms.adminInternalFrame(dp, form);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mcandidaturaActionPerformed

    private void mpartidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mpartidoActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mpartido.getName()) == true) {
            Partido_politico form = new Partido_politico();
            if (form == null) {
                form = new Partido_politico();
            }
            AddForms.adminInternalFrame(dp, form);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mpartidoActionPerformed

    private void mcandidatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcandidatoActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mcandidato.getName()) == true) {
            Candidato form = new Candidato();
            if (form == null) {
                form = new Candidato();
            }
            AddForms.adminInternalFrame(dp, form);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mcandidatoActionPerformed

    private void mmesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mmesaActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mmesa.getName()) == true) {
            Mesa form = new Mesa();
            if (form == null) {
                form = new Mesa();
            }
            AddForms.adminInternalFrame(dp, form);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mmesaActionPerformed

    private void mcentroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcentroActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mcentro.getName()) == true) {
            Centro form = new Centro();
            if (form == null) {
                form = new Centro();
            }
            AddForms.adminInternalFrame(dp, form);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mcentroActionPerformed

    private void mvotosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mvotosActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mvotos.getName()) == true) {
            Ingreso_Votos form = new Ingreso_Votos();
            if (form == null) {
                form = new Ingreso_Votos();
            }
            AddForms.adminInternalFrame(dp, form);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mvotosActionPerformed

    private void mresultadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mresultadosActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(mresultados.getName()) == true) {
            Conteo_Votos form = new Conteo_Votos();
            if (form == null) {
                form = new Conteo_Votos();
            }
            AddForms.adminInternalFrame(dp, form);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_mresultadosActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.getIdusuario()==1 || AccesoUsuario.getIdusuario()==2 || AccesoUsuario.getIdusuario()==3) {
            EliminarDatosrMesa form = new EliminarDatosrMesa();
            if (form == null) {
                form = new EliminarDatosrMesa();
            }
            AddForms.adminInternalFrame(dp, form);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */

        /*
         * Create and display the form
         */
        //java.awt.EventQueue.invokeLater(() -> {
        new Principal().setVisible(true);

        //});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu GestionarBD;
    public static javax.swing.JDesktopPane dp;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuItem mbackup;
    private javax.swing.JMenuItem mcandidato;
    private javax.swing.JMenuItem mcandidatura;
    private javax.swing.JMenuItem mcentro;
    private javax.swing.JMenuItem mmesa;
    private javax.swing.JMenuItem mpartido;
    private javax.swing.JMenuItem mrestaurarbackup;
    private javax.swing.JMenuItem mresultados;
    private javax.swing.JMenuItem msalir;
    private javax.swing.JMenuItem musuario;
    private javax.swing.JMenuItem mvotos;
    private javax.swing.JLabel sistema;
    private javax.swing.JLabel usuario;
    // End of variables declaration//GEN-END:variables
}
