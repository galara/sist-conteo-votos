package Capa_Datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author GLARA
 */
public class AccesoDatos {

    public ResultSet rs;
    public PreparedStatement ps;
    public Statement st;
    public String sql;
    public int numRegistros = 0;
    public int finalPag = 5000;
    public int inicioPag = 0;

    /**
     *
     * @param nombreTabla nombre de la Tabla
     * @param nomColumnaCambiar nombre de la columan de la cual se cambiara el
     * valor
     * @param nomColumnaId nombre de la columnas con las claves primarias
     * @param id clave primaria
     * @param nuevoValor nuevo valor.
     * @return
     */
    public int eliminacionTemporal(String nombreTabla, String nomColumnaCambiar, String nomColumnaId, Object id, Object nuevoValor) {
        int resultado = 1;

        Number vl = null;
        Number nv = null;
        if (esNumerico(id)) {
            vl = (Number) id;
        } else {
            id = "'" + id + "'";
        }

        if (esNumerico(nuevoValor)) {
            nv = (Number) nuevoValor;
        } else {
            nuevoValor = "'" + nuevoValor + "'";
        }

        String sql = "UPDATE " + nombreTabla + " SET " + nomColumnaCambiar + " = " + (nv == null ? nuevoValor : nv) + " where " + nomColumnaId + "=" + (vl == null ? id : vl) + "";
        try {
            resultado = BdConexion.getStatement().executeUpdate(sql);
        //System.out.print(resultado);
        
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        //System.out.println(sql);
        return resultado;
    }

    /**
     * Si el dato no esta en otras tablas se borrara totalmente de la BD
     *
     * @param nombreTabla nombre de la Tabla
     * @param nomColumnaId nombre de la columnas con las claves primarias
     * @param id clave primaria
     *
     * Si el dato esta en otras tablas no se borrara totalmente de la BD solo se
     * le cambiara el estado
     * @param nomColumnaCambiar nombre de la columan de la cual se cambiara el
     * valor si no es posible eliminarlo totalmetne de la bd
     * @return
     */
    public int eliminacionReal(String nombreTabla, String nomColumnaCambiar, String nomColumnaId, Object id) {
        sql = "delete from " + nombreTabla + " where " + nomColumnaId + OpSql.IGUAL + "'" + id + "'";
        int opcion = 0;
        try {
            opcion = BdConexion.getStatement().executeUpdate(sql);
            BdConexion.cerrarEnlacesConexion(BdConexion.SOLO_STATEMENT);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1451) {
                Object nuevoValor = "0";
                opcion = eliminacionTemporal(nombreTabla, nomColumnaCambiar, nomColumnaId, id, nuevoValor);
            } else {
                JOptionPane.showMessageDialog(null, ex);
            }
        }

        return opcion;
    }

    public int getNumeroRegistros(String nomTabla, String columnaContar, String columaCod, Object valor) {
        Number vl = null;
        int numReg = 0;
        if (esNumerico(valor)) {
            vl = (Number) valor;
        } else {
            valor = "'" + valor + "'";
        }
        String sql2 = "select count(" + columnaContar + ") as nreg from " + nomTabla + (columaCod != null ? OpSql.WHERE + columaCod + OpSql.IGUAL + (vl != null ? vl : valor) : "");

        try {
            rs = BdConexion.getResultSet(sql2);
            while (rs.next()) {
                numReg = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return numReg;
    }

    public int getNumeroRegistros(String nomTabla, String columnaContar) {
        return getNumeroRegistros(nomTabla, columnaContar, null, null);
    }

    public Object getSuma(String nomTabla, String columna, String columaCod, Object valor) {
        Number vl = null;
        Object sum = null;
        if (esNumerico(valor)) {
            vl = (Number) valor;
        } else {
            valor = "'" + valor + "'";
        }

        String sql2 = "select sum(" + columna + ") from " + nomTabla + " " + (columaCod != null ? OpSql.WHERE + columaCod + OpSql.IGUAL + (vl != null ? vl : valor) : "");
        //System.out.println(sql2);
        try {
            rs = BdConexion.getResultSet(sql2);
            while (rs.next()) {
                sum = rs.getObject(1);
            }
            //st.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        return sum;
    }

    public Object getSuma(String nomTabla, String columna) {
        return getSuma(nomTabla, columna, null, null);
    }

    /**
     * verifica si un Objetc es de tipo numerico
     *
     * @param valor
     * @return
     */
    public boolean esNumerico(Object valor) {
        if (valor instanceof Number) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Obtiene los datos de una tabla segun una condicion ejem: where id = ?
     *
     * @param nombreTabla nombre de la tabla
     * @param campos nombres de los campos
     * @param columnaId columna id
     * @param id valor id
     * @return
     */
//    public ResultSet getRegistros(String nombreTabla, String[] campos, String columnaId, Object id) {
//
//        Number vl = null;
//        if (esNumerico(id)) {
//            vl = (Number) id;
//        } else {
//            id = "'" + id + "'";
//        }
//
//        sql = "select " + (campos != null ? (campos.length == 0 ? "* " : generarArrayAString(campos)) : "* ") + " from "
//                + nombreTabla + (columnaId != null ? (!columnaId.isEmpty() ? OpSql.WHERE + columnaId + OpSql.IGUAL + (vl == null ? id : vl) : "") : "") + OpSql.LIMIT + this.inicioPag + "," + this.finalPag;
//
//        rs = BdConexion.getResultSet(sql);
//        System.out.println(sql);
//        return rs;
//    }
    /**
     * Para varias condiciones where ejem: where id = ? and estado = 'T'
     *
     * @param nombreTabla nombre de la tabla
     * @param campos nombres de los campos
     * @param columnaId columnas id
     * @param id valores id
     * @param inner , condiciones INNER JOINS ejem: INNER JOIN profesor on
     * horario.id=profesor.id
     * @return
     */
    public ResultSet getRegistros(String nombreTabla, String[] campos, String[] columnaId, Object[] id, String inner) {

        try {
            String condiciones = unirColumnasValores(columnaId, id);
            if (inner.isEmpty()) {
                sql = "select " + (campos != null ? (campos.length == 0 ? "* " : generarArrayAString(campos)) : "* ") + " from "
                        + nombreTabla + (condiciones != null ? OpSql.WHERE + condiciones : "") + OpSql.LIMIT + this.inicioPag + "," + this.finalPag;
            } else if (!inner.isEmpty()) {
                sql = "select " + (campos != null ? (campos.length == 0 ? "* " : generarArrayAString(campos)) : "* ") + " from "
                        + nombreTabla + (condiciones != null ? inner + " " + OpSql.WHERE + condiciones : "");
            }
            //System.out.print("\n"+sql);
            rs = BdConexion.getResultSet(sql);
            //No se cierra el rs ya que no se podria retornar el rs
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        return rs;
    }

//    /**  Se utiliza  getRegistros poe eso se comento
//     * modificar comentarios Para varias condiciones where ejem: where id = ?
//     * and estado = 'T'
//     *
//     * @param nombreTabla nombre de la tabla
//     * @param campos nombres de los campos
//     * @param columnaId columnas id
//     * @param id valores id
//     * @return
//     */
//    public ResultSet getRegistrosCombo(String nombreTabla, String[] campos, String[] columnaId, Object[] id) {
//
//        try {
//            String condiciones = unirColumnasValores(columnaId, id);
//            sql = "select " + (campos != null ? (campos.length == 0 ? "* " : generarArrayAString(campos)) : "* ") + " from "
//                    + nombreTabla + (condiciones != null ? OpSql.WHERE + condiciones : "");
//            rs = BdConexion.getResultSet(sql);
//            //No se cierra el rs ya que no se podria retornar 
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, ex);
//        }
//
//        return rs;
//    }
    /**
     * Obtiene los datos de una tabla segun una condicion ejem: where nombre
     * like '% dato '
     *
     * @param nombreTabla nombre de la tabla
     * @param campos nombres de los campos
     * @param columnaId columna id
     * @param id valor id
     * @return
     */
    private ResultSet getRegistroLike(String nombreTabla, String[] campos, String columnaid, String id,String inner) {
        try {
            sql = "select " + (campos != null ? (campos.length == 0 ? "* " : generarArrayAString(campos)) : "* ") + " from " + nombreTabla + inner+" "+OpSql.WHERE + columnaid + OpSql.LIKE + "'" + OpSql.CUALQUIERA + id + OpSql.CUALQUIERA + "'";
            //System.out.print(sql);
            rs = BdConexion.getResultSet(sql);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return rs;
    }

    public ResultSet selectPorLike(String nombreTabla, String[] campos, String columnaid, String id, String inner) {
        return this.getRegistroLike(nombreTabla, campos, columnaid, id,inner);
    }

    /**
     * Une cada una de las columnas con su condicion a comprar ejem: AND columna
     * = valor
     *
     * @param columnas columnas id
     * @param valores valores id
     * @return
     */
    public String unirColumnasValores(String[] columnas, Object[] valores) {
        String cad = null;
        if (columnas == null || valores == null) {
            return null;
        }
        if (columnas != null && valores != null) {
            if (columnas.length > 0 && valores.length > 0) {

                cad = "";
                for (int i = 0; i < columnas.length; i++) {
                    Number nb = null;
                    Object val = null;
                    if (esNumerico(valores[i])) {
                        nb = (Number) valores[i];
                    } else {
                        val = "'" + valores[i]/*+OpSql.WHERE*/ + "'";
                    }
                    cad += (i == 0 ? "" : OpSql.AND) + columnas[i] + (nb == null ?/*OpSql.WHERE+*/ OpSql.IGUAL : OpSql.IGUAL) + (nb == null ? val : nb) + " ";

                }
            }

        }

        return cad;
    }

    /**
     * Actualiza un registro indicando una condicion ejem: where columnaId =
     * 'id'
     *
     * @param nomTabla nombre tabla
     * @param cnls lista de columnas y sus valor ejem: nombre='test'
     * @param columnaId campo id
     * @param id id
     * @return
     */
    public int actualizarRegistro(String nomTabla, String[] cnls, String columnaId, Object id) {
        Number vl = null;
        if (esNumerico(id)) {
            vl = (Number) id;
        } else {
            id = "'" + id + "'";
        }

        sql = "update " + nomTabla + " set " + generarArrayAString(cnls) + OpSql.WHERE + columnaId + OpSql.IGUAL + (vl == null ? id : vl);
        //System.out.print(sql);
        int op = 0;
        try {
            op = BdConexion.getStatement().executeUpdate(sql);
            BdConexion.cerrarEnlacesConexion(BdConexion.SOLO_STATEMENT);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return op;
    }

    public int actualizarRegistro(String nomTabla, String cnls, String columnaId, Object id) {
        //System.out.print(cnls);
        return actualizarRegistro(nomTabla, new String[]{cnls}, columnaId, id);
    }

    /**
     * Agrega nuevos registros a la tabla indicada
     *
     * @param nombreTabla nombre tabla
     * @param campos lista de columnas ejem:nombre
     * @param valores valores ejem: 'test'
     * @return
     */
    public int agregarRegistro(String nombreTabla, String[] campos, Object[] valores) {

        sql = "insert into " + nombreTabla + "(" + generarArrayAString(campos) + ") values("
                + formatearValores(valores) + ")";
        int op = 0;
        try {
            op = BdConexion.getStatement().executeUpdate(sql);
            BdConexion.cerrarEnlacesConexion(BdConexion.SOLO_STATEMENT);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        //System.out.println(sql);
        return op;
    }
    
    /**
     * Agrega nuevos registros a la tabla indicada
     *
     * @param sql
     * @param nombreTabla nombre tabla
     * @param campos lista de columnas ejem:nombre
     * @param valores valores ejem: 'test'
     * @return
     */
    public int agregarRegistrosql(String sql) {

        int op = 0;
        try {
            op = BdConexion.getStatement().executeUpdate(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            BdConexion.cerrarEnlacesConexion(BdConexion.SOLO_STATEMENT);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return op;
    }

    /**
     * Agrega resitros usando un objetos PreparedStatemen y un procedimiento
     * almacenado
     *
     * @param nombreTabla , nombre de ta bala en la BD param campos , estan el
     * el procedimiento almacenado por lo que no son necesarios
     * @param valores , los valores que se guardaran en la BD
     * @return
     */
    public int agregarRegistroPs(String nombreTabla/*, String[] campos*/, Object[] valores) {

        sql = "CALL " + nombreTabla/*+"("+generarArrayAString(campos)+") values */ + "(" + formatearValores(valores.length) + ")";
        //System.out.println(sql);
        int op = 0;
        try {
            ps = BdConexion.getPreparedStatement(sql);
            setValores(ps, valores);
            op = ps.executeUpdate();
            //System.out.print(op);
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        return op;
    }

    /**
     * Agrega resitros usando un objetos PreparedStatemen
     *
     * @param nombreTabla
     * @param campos
     * @param valores
     * @return
     */
    public int agregarRegistroPss(String nombreTabla, String[] campos, Object[] valores) {

        sql = "insert into " + nombreTabla + "(" + generarArrayAString(campos) + ") values("
                + formatearValores(valores.length) + ")";
        //System.out.println(sql);
        int op = 0;
        try {
            ps = BdConexion.getPreparedStatement(sql);
            setValores(ps, valores);
            op = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return op;
    }

    /**
     * se debe especificar el valor da la calve primaria
     *
     * @param nomTabla nombre tabla
     * @param cnls lista de columnas y sus valor ejem: nombre= ?
     * @param columnaId campo id
     * @param id id
     * @return
     */
    public int actualizarRegistroPs(String nomTabla, String cnls, Object[] valores) {

        sql = "update " + nomTabla + " set " + cnls;
        //System.out.print(sql+valores);
        int op = 0;
        try {
            //System.out.println("actuaizar registro valores" + valores);
            ps = BdConexion.getPreparedStatement(sql);
            setValores(ps, valores);
            op = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema con" + ex);
        }

        //System.out.println("n"+sql);
        return op;
    }

    /**
     * Obtiene el ultimo resitro de una tabla de una columna indicada
     *
     * @param table
     * @param column
     * @return
     */
    public ResultSet getUltimoRegistro(String table, String column) {
        sql = "select * from " + table + " order by " + column + " desc limit 0,1";
        try {
            rs = BdConexion.getResultSet(sql);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        return rs;
    }
    
    /**
     * Obtiene el ultimo resitro de una tabla de una columna indicada
     *
     * @param table
     * @param column
     * @return
     */
    public ResultSet getRegistroProc(String table) {
        sql = table;
        //System.out.print(table+"\n\n\n");
        try {
            rs = BdConexion.getResultSet(sql);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return rs;
    }

    /**
     * establece valores a un objeto PreparedStatement para luego ser ejecutado
     *
     * @param ps
     * @param valores
     */
    private void setValores(PreparedStatement ps, Object[] valores) {
        try {
            for (int i = 0; i < valores.length; i++) {
                //System.out.print(valores[i]+"\n");
                
                if (getInt(valores[i]) != null) {
                    ps.setInt(i + 1, getInt(valores[i]));
                } else if (getDouble(valores[i]) != null) {
                    ps.setDouble(i + 1, getDouble(valores[i]));
                } else if (getFloat(valores[i]) != null) {
                    ps.setFloat(i + 1, getFloat(valores[i]));
                } else {
                    if (valores[i] != null) {
                        ps.setString(i + 1, valores[i].toString());
                    } else {
                        ps.setString(i + 1, null);
                        ps.setCharacterStream(i, null);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private Integer getInt(Object valor) {
        Integer vl = null;
        if (valor instanceof Integer) {
            vl = (Integer) valor;
        }
        return vl;
    }

    private Double getDouble(Object valor) {
        Double vl = null;
        if (valor instanceof Double) {
            vl = (Double) valor;
        }
        return vl;
    }

    private Float getFloat(Object valor) {
        Float vl = null;
        if (valor instanceof Float) {
            vl = (Float) valor;
        }
        return vl;
    }

    /**
     * Verifica si un objetos es numerico
     *
     * @param valor
     * @param obj no se usa para nadaa
     * @return
     */
    public Number esNumerico(Object valor, Object obj) {
        Number nb = null;
        if (valor instanceof Number) {
            nb = (Number) valor;
            return nb;
        } else {
            return null;
        }
    }

    /**
     * Este metodo genera una cadena con simbolos "?" separada por comas <br/>
     *
     * @param num cantidad de repeticiones
     *
     * @return
     */
    public String formatearValores(int num) {
        String cad = "";
        if (num > 0) {
            cad = "";
            for (int i = 0; i < num; i++) {
                cad += (i == 0 ? " " : ", ") + "?";
            }
        }
        return cad;
    }

    /**
     * Este metodo une los items de un array en una cadena, pero separado por
     * una coma<br/>
     * Y ademas verifica el tipo de dato de cada item, si el item no fuera
     * numeric, entonces<br/>
     * envuelve este item entre comiilas.
     *
     * @param valores
     * @return cadena
     */
    public String formatearValores(Object[] valores) {
        String cad = "* ";
        Number nb = null;
        if (valores != null) {
            if (valores.length > 0) {
                cad = "";
                if (valores.length == 1) {
                    nb = esNumerico(valores[0], null);
                    cad += (nb == null ? "'" + valores[0] + "'" : nb);
                    return cad;
                }
                for (int i = 0; i < valores.length; i++) {
                    nb = esNumerico(valores[i], valores[i]);
                    cad += (i == 0 ? " " : ", ") + (nb != null ? nb : "'" + valores[i] + "'");
                }
            }
        }
        return cad;
    }

    /**
     * Este metodo une los items de una array en una cadena, pero separado por
     * una coma
     *
     * @param campos array de cadenas
     * @return cadena
     */
    public String generarArrayAString(String[] campos) {
        String cad = "* ";
        if (campos != null) {
            if (campos.length > 0) {
                cad = "";
                if (campos.length == 1) {
                    return cad + campos[0];
                }
                for (int i = 0; i < campos.length; i++) {
                    cad += (i == 0 ? " " : ", ") + campos[i];
                }
            }
        }
        return cad;
    }

    /**
     *
     * @param cad cadena
     * @param separador simbolo para dividir la cadena
     * @return devuelve array de cadenas
     */
    public String[] stringToArray(String cad, String separador) {
        return cad.split(separador);
    }

    /**
     *
     * @param cad cadena
     * @param separador simbolo separador, utilizado para partir la cadena
     * @param simbolo simbolo que asigna a cada item que se genere de la cadena
     * @return cadena: (nome = ?)
     */
    public String adjuntarSimbolo(String cad, String separador, String simbolo) {
        String[] campos = stringToArray(cad, separador);
        String ncad = "";
        for (int i = 0; i < campos.length; i++) {
            ncad += (i == 0 ? " " : ", ") + campos[i] + OpSql.IGUAL + simbolo;
        }

        return ncad;
    }

    /**
     * Este metodo verifica si existe un dato ejem: codigo y devuelve un boolean
     * true si este es encontrado y false si no es encontrado
     *
     * @param nomTabla
     * @param nomCampo
     * @param value
     * @return
     */
    public boolean existe(String nomTabla, String nomCampo, Object value) {
        Number nb = esNumerico(value, null);
        sql = "select * from " + nomTabla + OpSql.WHERE + nomCampo + OpSql.IGUAL + (nb != null ? nb : "'" + value + "'");
        boolean encontrado = false;
        try {
            rs = BdConexion.getResultSet(sql);
            if (rs.next()) {
                encontrado = true;
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        return encontrado;
    }

}
