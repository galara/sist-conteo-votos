/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackupMySQL;

import Capa_Datos.BdConexion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author GLARA
 */
public class BackupDiario {

    public static void GenerarBackupDiarioMySQL() {

        String currentDir = System.getProperty("user.dir");
        String currentDirDelete = System.getProperty("user.dir");

        Calendar c = Calendar.getInstance();
        Calendar cdelete = Calendar.getInstance();

        //Para generar un nuevo Backup
        c.add(Calendar.MONTH, 1);
        String fecha = "" + c.get(Calendar.DATE);
        fecha = fecha + "-" + c.get(Calendar.MONTH);//String.valueOf(c.get(Calendar.MONTH));
        fecha = fecha + "-" + cdelete.get(Calendar.YEAR);

        //Para buscar y borrar un Backup
        cdelete.add(Calendar.MONTH, 1);
        cdelete.add(Calendar.DAY_OF_YEAR, -8);
        String fechadelete = "" + cdelete.get(Calendar.DATE);
        fechadelete = fechadelete + "-" + cdelete.get(Calendar.MONTH);//String.valueOf(c.get(Calendar.MONTH));
        fechadelete = fechadelete + "-" + cdelete.get(Calendar.YEAR);

        currentDir = currentDir + "\\Backup_" + fecha + ".sql"; //Nuevo Backup
        currentDirDelete = currentDirDelete + "\\Backup_" + fechadelete + ".sql"; //Boarrar Backup

        File fichero = new File(currentDir); //Para verificar si el Backup ya esxiste si no existe crearlo
        File ficherodelete = new File(currentDirDelete); //Para verificar si el Backup esxiste si existe borrarlo

        boolean existe = fichero.exists();
        boolean existedelete = ficherodelete.exists();

        if (existe == false) {
            try {
                Runtime runtime = Runtime.getRuntime();
                FileWriter fw;

                fw = new FileWriter(currentDir);

                Process child = runtime.exec("C:\\Archivos de programa\\MySQL\\MySQL Server 5.5\\bin\\mysqldump -u " + BdConexion.user + " -p" + BdConexion.pass + " --default-character_set=utf8 " + BdConexion.dataBase);
                InputStreamReader irs = new InputStreamReader(child.getInputStream());
                BufferedReader br = new BufferedReader(irs);

                String line;
                while ((line = br.readLine()) != null) {
                    fw.write(line + "\n");
                }
                fw.close();
                irs.close();
                br.close();

                if (existedelete == true) {
                    ficherodelete.delete();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error no se Genero el Backup del día por el siguiente motivo:" + ex.getMessage());
            }
        }
    }

}
