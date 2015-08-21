package principal;

import utilities.ConnectionManager;
import utilities.DBSql;
import utilities.DataJlist;
import utilities.DatajCombobox;
import utilities.InsertUpdateDeleteCopia;
import utilities.InsertDeleteSubtituloActor;
import utilities.LimitTextfield;
import utilities.ResultsetTable;
import utilities.TimeValidator;
import utilities.YearValidator;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LCRT
 */
public class Man_pel extends javax.swing.JInternalFrame {

    /**
     * Creates new form man_peliculas
     */
    public static DefaultListModel lstSubtituloModel;
    public static DefaultListModel lstActorModel;
    TimeValidator timeValidador = new TimeValidator();
    YearValidator yearValidador = new YearValidator();

    DBSql sql = null;
    int progVal = 0;
    int rowIdData = 0;
    int lastInsertedId = 0;
    String confirmStringToDelete = null;
    int maxIdActor = 0;

    boolean switchFirstCheckIncrementData = false;

    static LinkedList llstAddRemoveSubtitulo = null;
    static LinkedList llstAddRemoveActor = null;
    static LinkedList llstAddRemoveCopia = null;

    String tableName = "tbpelicula";
    String idColname = "codpel";
    String descriptionColname = "titpel";

    JComboBox dialogcmbEstadoOption = null;
    private static Connection transCon = ConnectionManager.getInstance().getConnection();

    ///constructor
    public Man_pel() {
        initComponents();
        //First added  item into the list
        sql = new DBSql();

        dialogcmbEstadoOption = new JComboBox();

        lstSubtituloModel = new DefaultListModel();
        lstActorModel = new DefaultListModel();

        llstAddRemoveSubtitulo = new LinkedList();
        llstAddRemoveActor = new LinkedList();
        llstAddRemoveCopia = new LinkedList();

        jProgressBar1.setMaximum(100);
        jProgressBar2.setMaximum(100);

        populatePelicula();
        Man_pel.this.initPopulateSubtitulo();
        initPopulateActor();

        //////////////////////////limit textfields///////////////
        txtTitulo.setDocument(new LimitTextfield(54));
        txtBusqueda.setDocument(new LimitTextfield(54));
        txtSitio.setDocument(new LimitTextfield(256));
        txtDuracion.setDocument(new LimitTextfield(10));

        /////////////fill combobox////////////////
        comboboxDisplay(cmbGenero, "tbGenero", "codgen", "desgen");
        comboboxDisplay(cmbFormato, "tbFormato", "codfor", "desfor");
        comboboxDisplay(cmbClasficacion, "tbclaficacionxprecio", "codcla", "descla");
        comboboxDisplay(cmbIdioma, "tbidioma", "codidi", "desidi");
        comboboxDisplay(cmbIdiOri, "tbidioma", "codidi", "desidi");

        PnlSubtitulo.setBorder(javax.swing.BorderFactory.createTitledBorder("Subtitulo(s): --- "));
        pnlCopia.setBorder(javax.swing.BorderFactory.createTitledBorder("Nro Copia(s): --- "));

        //to resize copia column        
        tblCopia.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblCopia.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblCopia.setAutoResizeMode(tblCopia.AUTO_RESIZE_LAST_COLUMN);

//some issues with this code      
// tblCopia.removeColumn(tblCopia.getColumnModel().getColumn(2));
        tblCopia.getColumnModel().getColumn(2).setMinWidth(0);
        tblCopia.getColumnModel().getColumn(2).setMaxWidth(0);
        tblCopia.getColumnModel().getColumn(2).setWidth(0);

        txtTitulo.requestFocus();

    }

///////////////////////event for function keys///////////////////////
    //dont forget to add these codes
    //add this code to windowsactived event
    //KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    //add this code to windowsdesactived event
    // KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);
    //Events for Functions on form
    KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {

            if (e.getID() == KeyEvent.KEY_RELEASED) {
                int keyCode = e.getKeyCode();

                switch (keyCode) {
                    case KeyEvent.VK_F3:
                        System.out.println("Buscar /f3");
                        txtBusqueda.setText(null);
                        txtBusqueda.requestFocus();
                        break;
                    case KeyEvent.VK_F4:
                        System.out.println("nuevo");
                        btnNuevo.doClick();
                        break;
                    case KeyEvent.VK_F5:
                        System.out.println("Guardar");
                        btnGuardar.doClick();
                        break;
                    case KeyEvent.VK_F6:
                        System.out.println("Eliminar");
                        btnEliminar.doClick();
                        break;
                }

            }

            if (e.getID()
                    == KeyEvent.KEY_TYPED) {
                // System.out.println(e);
            }
            // Pass the KeyEvent to the next KeyEventDispatcher in the chain

            return false;
        }

    };

    ///////////////////////clear all
    private void clearNew() {

        txtTitulo.setText(null);
        txtSitio.setText(null);
        txtDuracion.setText(null);
        JYearAno.setYear(2015);
        populatePelicula();
        txtTitulo.requestFocus();
        cmbGenero.setSelectedIndex(0);
        cmbFormato.setSelectedIndex(0);
        cmbClasficacion.setSelectedIndex(0);
        cmbIdioma.setSelectedIndex(0);
        cmbIdiOri.setSelectedIndex(0);

        lstSubtituloModel.clear();
        lstActorModel.clear();
        llstAddRemoveCopia.clear();

        //remove rows
        DefaultTableModel dtm = (DefaultTableModel) tblCopia.getModel();
        dtm.setRowCount(0);
        rowIdData = 0;
        lastInsertedId = 0;
        PnlSubtitulo.setBorder(javax.swing.BorderFactory.createTitledBorder("Subtitulo(s)" + tblCopia.getRowCount()));
        PnlActor.setBorder(javax.swing.BorderFactory.createTitledBorder("Actor(es) " + tblCopia.getRowCount()));
        pnlCopia.setBorder(javax.swing.BorderFactory.createTitledBorder("Copia(s) " + tblCopia.getRowCount()));

    }

    //initial
    void initPopulateSubtitulo() {
        lstSubtitulo.setModel(lstSubtituloModel);
    }

    //initial
    void initPopulateActor() {
        lstActor.setModel(lstActorModel);
    }

    void populatePelicula() {
        String sqlQuery = "SELECT * FROM(SELECT pel.codpel AS CODPEL,pel.titpel AS 'TITUTO PELICULA',gen.desgen AS GENERO,cla.descla AS CLASIF,CONCAT('',pel.anopel) AS 'AÑO',pel.durpel AS 'DURACIÓN',pel.sitpel AS SITIO,fmt.desfor AS FORMATO,idi.desidi IDIOMA,idiori.desidi AS 'IDIOMA ORIGINAL' \n"
                + "FROM tbpelicula pel INNER JOIN tbgenero gen ON pel.codgen=gen.codgen\n"
                + "INNER JOIN tbclaficacionxprecio cla ON pel.codcla=cla.codcla\n"
                + "INNER JOIN tbformato fmt ON pel.codfor=fmt.codfor\n"
                + "INNER JOIN tbidioma idi ON pel.codidi=idi.codidi\n"
                + "INNER JOIN tbidioma idiori ON pel.codidiori=idiori.codidi) as vtb ORDER BY codpel DESC ";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblPelicula.remove(this);
        try {
            tblPelicula.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblPelicula.getRowCount());
        } catch (SQLException ex) {
            Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void populatePeliculaViaFilter(String titulo) {
        String sqlQuery = "SELECT * FROM(SELECT pel.codpel AS CODPEL,pel.titpel AS 'TITUTO PELICULA',gen.desgen AS GENERO,cla.descla AS CLASIF,CONCAT('',pel.anopel) AS 'AÑO',pel.durpel AS 'DURACIÓN',pel.sitpel AS SITIO,fmt.desfor AS FORMATO,idi.desidi IDIOMA,idiori.desidi AS 'IDIOMA ORIGINAL' \n"
                + "FROM tbpelicula pel INNER JOIN tbgenero gen ON pel.codgen=gen.codgen\n"
                + "INNER JOIN tbclaficacionxprecio cla ON pel.codcla=cla.codcla\n"
                + "INNER JOIN tbformato fmt ON pel.codfor=fmt.codfor\n"
                + "INNER JOIN tbidioma idi ON pel.codidi=idi.codidi\n"
                + "INNER JOIN tbidioma idiori ON pel.codidiori=idiori.codidi) as vtb WHERE `TITUTO PELICULA` LIKE '%" + titulo + "%' ORDER BY codpel DESC ";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblPelicula.remove(this);
        try {
            tblPelicula.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblPelicula.getRowCount());
        } catch (SQLException ex) {
            Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //////////////////
    void populateSubtituloActor(DefaultListModel lstModel, String sqlQuery) {
        lstModel.clear();

        // String sqlQuery = "SELECT c.selectedCodpel,c.`desc`  FROM  tbperson p INNER JOIN person_vs_car pc ON p.selectedCodpel=pc.idperson  INNER JOIN tbcars c ON c.selectedCodpel=pc.idcar WHERE p.selectedCodpel='" + idperson + "'";
        try {
            Statement sta = transCon.createStatement();
            ResultSet rs = sta.executeQuery(sqlQuery);
            while (rs.next()) {
                //   System.out.println("fill" + rs.getInt("id") + " - " + rs.getString("desc"));
                lstModel.addElement(new DataJlist(rs.getInt("id"), rs.getString("desc")));
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        PnlSubtitulo.setBorder(javax.swing.BorderFactory.createTitledBorder("Subtitulo(s) " + lstSubtitulo.getModel().getSize()));
        PnlActor.setBorder(javax.swing.BorderFactory.createTitledBorder("Actor(es) " + lstActor.getModel().getSize()));

    }

    public void populateCopiaPelicula(String sqlQuery) {
        // i put concat in the query 
        //to get the column name right
//System.out.println("sql populateCopiaPelicula >>"+sqlQuery);
        ResultSet rs = sql.displaytb(null, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
//reset populateCopiaPelicula model   
        ((DefaultTableModel) tblCopia.getModel()).setRowCount(0);

        try {
            tblCopia.setModel(rst.rstomodel(rs));
            //sa se pou di konbyen ote ki gen
            pnlCopia.setBorder(javax.swing.BorderFactory.createTitledBorder("Copia(s) " + tblCopia.getRowCount()));

            //to resize copia column        
            tblCopia.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblCopia.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblCopia.setAutoResizeMode(tblCopia.AUTO_RESIZE_LAST_COLUMN);
            //kache denye colon lan 
            //some issues with this code      
            //tblCopia.removeColumn(tblCopia.getColumnModel().getColumn(2));
            tblCopia.getColumnModel().getColumn(2).setMinWidth(0);
            tblCopia.getColumnModel().getColumn(2).setMaxWidth(0);
            tblCopia.getColumnModel().getColumn(2).setWidth(0);

        } catch (SQLException ex) {
            Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //// dont forget to commit tthem after
    //Leader transaction///////////////// is the first data all the rest depend on
    boolean insertLeaderTransaction(String query) {
        boolean success = false;

        try {
            //to add on the top
            transCon.setAutoCommit(false);
            String sql = query;
            Statement sta = transCon.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;

////////////////get the last selectedCodpel from  the first insert///////////////////////
                //Statement stateLastId = transCon.createStatement();
                ResultSet rs = sta.executeQuery("SELECT LAST_INSERT_ID()");
                if (rs != null) {
                    if (rs.next()) {
                        lastInsertedId = rs.getInt(1);
                    }
                    rs.close();
                }
//////////////////////////////////////////////////////////////////////////////
                System.out.println("LAst ID<<>>" + lastInsertedId);
                // transCon.commit();
            } else {
                success = false;
            }
        } catch (SQLException ex) {
            System.err.println(">" + ex);
        }
        return success;
    }

    //transaction/////////////////
    boolean setTransaction(String query) {
        boolean success = false;

        try {
            //to add on the top
            transCon.setAutoCommit(false);
            String sql = query;
            Statement sta = transCon.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
        } catch (SQLException ex) {
            System.err.println(">" + ex);
        }
        return success;
    }

    boolean insertDelete_IN_VS_table(LinkedList LinkedListDataInsertDelete, String tableVsName, String idColunmNameA, String idColunmNameB) {
        boolean todoBienInsertDelete = true;
        String sqlQuery = null;
        for (int i = 0; i < LinkedListDataInsertDelete.size(); i++) {
            InsertDeleteSubtituloActor idd = (InsertDeleteSubtituloActor) LinkedListDataInsertDelete.get(i);
            //System.out.println(">>"+idd.getId()+" "+idd.getState());
            //pou met deyo last insert id li pa bezwen nan loop la
            int selectedCodpel = lastInsertedId;
            if (idd.getState()) {
                sqlQuery = "INSERT INTO " + tableVsName + " VALUES (NULL,'" + idd.getId() + "','" + selectedCodpel + "')";
            } else {
                sqlQuery = "DELETE FROM " + tableVsName + " WHERE " + idColunmNameA + "='" + idd.getId() + "' AND " + idColunmNameB + "='" + selectedCodpel + "'";
            }

            System.out.println(sqlQuery);
            if (!setTransaction(sqlQuery)) {
                todoBienInsertDelete = false;
            }
        }
        return todoBienInsertDelete;
    }

    boolean insertUpdateDelete_Pel_VS_Copia(LinkedList LinkedListDataInsertUpdateDelete, String tableVsName, String codpelColName, String numCopiaColName) {
        boolean todoBienInsertDelete = true;
        String sqlQuery = null;

        for (int i = 0; i < LinkedListDataInsertUpdateDelete.size(); i++) {
            InsertUpdateDeleteCopia iudData = (InsertUpdateDeleteCopia) LinkedListDataInsertUpdateDelete.get(i);
            //pou met deyo last insert id li pa bezwen nan loop la
            int selectedCodpel = lastInsertedId;
            //System.out.println(">>"+idd.getId()+" "+idd.getState());
            switch (iudData.getState()) {
                case 1:
                    sqlQuery = "INSERT INTO " + tableVsName + " VALUES (NULL,'" + selectedCodpel + "','" + iudData.getNumcopia() + "','" + iudData.getCodestado() + "')";
                    System.out.println("insert>>" + sqlQuery);
                    break;
                case 2:
                    sqlQuery = "UPDATE  " + tableVsName + " SET codestado=" + iudData.getCodestado() + " WHERE " + codpelColName + "='" + iudData.getCodpel() + "' AND " + numCopiaColName + "='" + iudData.getNumcopia() + "'";
                    System.out.println("update" + sqlQuery);
                    break;
                case 3:
                    sqlQuery = "DELETE FROM " + tableVsName + " WHERE " + codpelColName + "='" + rowIdData + "' AND " + numCopiaColName + "='" + iudData.getNumcopia() + "'";
                    System.out.println("delete" + sqlQuery);
                    break;
            }

            System.out.println(sqlQuery);
            if (!setTransaction(sqlQuery)) {
                todoBienInsertDelete = false;
            }
        }

        return todoBienInsertDelete;
    }

    private int genNumCopia(int max) {
        maxIdActor = max;
        maxIdActor++;
        return maxIdActor;
    }

    public void comboboxDisplayEstadoPelicula(JComboBox cmb, String tbln, String idColumnName, String descColumnName) {
        try {

            DefaultComboBoxModel valueModel = (DefaultComboBoxModel) cmb.getModel();
            valueModel.removeAllElements();
            valueModel.addElement(new DatajCombobox(0, "Selecciona"));

            String sqlComboEstado = "SELECT * FROM " + tbln + " ORDER BY " + idColumnName + " ASC";
            ResultSet rs = sql.displaytb(tableName, sqlComboEstado);

            while (rs.next()) {
                // System.out.println("CDDD"+rs.getInt(idColumnName));
                valueModel.addElement(new DatajCombobox(rs.getInt(idColumnName), rs.getString(descColumnName)));

            }
        } catch (Exception ex) {
            System.err.println("Error in comboboxDisplayEstadoPelicula :" + ex);
        }

    }

    public void selectedValueCombobox(JComboBox comboBox, Object value) {

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (value == null) {
                comboBox.setSelectedIndex(0);
            } else {
                if ((comboBox.getItemAt(i).toString()).equalsIgnoreCase(value.toString())) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

    }

    public void getSelectRowIdData() {
        try {
            int selectedRow = tblPelicula.getSelectedRow();

            int idData = Integer.parseInt((tblPelicula.getModel().getValueAt(selectedRow, 0).toString()));

            rowIdData = idData;

            /////////////////////////////set combobox////////////////////////
            selectedValueCombobox(cmbGenero, tblPelicula.getModel().getValueAt(selectedRow, 2));
            selectedValueCombobox(cmbFormato, tblPelicula.getModel().getValueAt(selectedRow, 7));
            selectedValueCombobox(cmbClasficacion, tblPelicula.getModel().getValueAt(selectedRow, 3));
            selectedValueCombobox(cmbIdioma, tblPelicula.getModel().getValueAt(selectedRow, 8));
            selectedValueCombobox(cmbIdiOri, tblPelicula.getModel().getValueAt(selectedRow, 9));

            /////////////////for update//////////////////       
            ////////mete string lan nan textfield la///////////////
            txtTitulo.setText(tblPelicula.getModel().getValueAt(selectedRow, 1).toString());
            txtSitio.setText(tblPelicula.getModel().getValueAt(selectedRow, 6).toString());
            JYearAno.setYear(Integer.parseInt((String) tblPelicula.getModel().getValueAt(selectedRow, 4)));
            txtDuracion.setText(tblPelicula.getModel().getValueAt(selectedRow, 5).toString());

            ///////////////////pran tout string ki nan row la///////////////////
            confirmStringToDelete = "";
            for (int i = 0; i < tblPelicula.getColumnCount(); i++) {

                String strData = (tblPelicula.getModel().getValueAt(selectedRow, i).toString()) + " ";
                //System.out.println(""+strData);
                confirmStringToDelete += strData;
            }

        } catch (Exception e) {
        }

    }

    public static void addRemoveElementFromSubtitulo(int id, boolean state) {
        if (state) {
            //  llstAddRemoveSubtitulo.add(state,selectedCodpel);
            llstAddRemoveSubtitulo.add(new InsertDeleteSubtituloActor(id, state));
            System.out.println("Insert // " + id);
        } else {
            llstAddRemoveSubtitulo.add(new InsertDeleteSubtituloActor(id, state));
            System.out.println("Delete // " + id);

        }
        System.out.println("length" + llstAddRemoveSubtitulo.size());
    }

    public static void addRemoveElementFromActor(int id, boolean state) {
        if (state) {
            //  llstAddRemoveSubtitulo.add(state,selectedCodpel);
            llstAddRemoveActor.add(new InsertDeleteSubtituloActor(id, state));
            System.out.println("Insert // " + id);
        } else {
            llstAddRemoveActor.add(new InsertDeleteSubtituloActor(id, state));
            System.out.println("Delete // " + id);

        }

        System.out.println("length" + llstAddRemoveSubtitulo.size());
    }

    public static void addRemoveElementFromCopia(int codpelcopia, int numcopia, int codestado, int state) {
        switch (state) {
            case 1:
                System.out.println("insert!");
                llstAddRemoveCopia.add(new InsertUpdateDeleteCopia(codpelcopia, numcopia, codestado, state));
                break;
            case 2:
                System.out.println("update!");
                llstAddRemoveCopia.add(new InsertUpdateDeleteCopia(codpelcopia, numcopia, codestado, state));
                break;
            case 3:
                System.out.println("delete!");
                llstAddRemoveCopia.add(new InsertUpdateDeleteCopia(codpelcopia, numcopia, codestado, state));
                break;
        }
        System.out.println("length" + llstAddRemoveSubtitulo.size());

    }

//combo
    public void comboboxDisplay(JComboBox cmb, String tblname, String idColname, String desColname) {
        DefaultComboBoxModel value;
        try {

            ResultSet rs = sql.displaytbWithIdColname(tblname, idColname);

            value = new DefaultComboBoxModel();
            cmb.setModel(value);
            cmb.removeAllItems();
            //first element empty
            value.addElement(new DatajCombobox(0, "//////////////////////"));
            while (rs.next()) {
                value.addElement(new DatajCombobox(rs.getInt(idColname), rs.getString(desColname)));
            }
        } catch (Exception ex) {
            System.err.println("" + ex);
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

        jpopDeleteSubtitulo = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        timer1 = new org.netbeans.examples.lib.timerbean.Timer();
        timer2 = new org.netbeans.examples.lib.timerbean.Timer();
        jpopDeleteActor = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jpopDeleteCopia = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbClasficacion = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        cmbFormato = new javax.swing.JComboBox();
        cmbGenero = new javax.swing.JComboBox();
        cmbIdioma = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        txtDuracion = new javax.swing.JFormattedTextField();
        JYearAno = new com.toedter.calendar.JYearChooser();
        cmbIdiOri = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        txtSitio = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        PnlSubtitulo = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSubtitulo = new javax.swing.JList();
        btnAddSubtitulo = new javax.swing.JButton();
        pnlCopia = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCopia = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        PnlActor = new javax.swing.JPanel();
        jProgressBar2 = new javax.swing.JProgressBar();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstActor = new javax.swing.JList();
        btnAddActor = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPelicula = new javax.swing.JTable();
        lblInfo = new javax.swing.JLabel();

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/Entypo_2796(58)_24.png"))); // NOI18N
        jMenuItem1.setText("Eliminar Subtitulo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jpopDeleteSubtitulo.add(jMenuItem1);

        timer1.setDelay(5L);
        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        timer2.setDelay(5L);
        timer2.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer2OnTime(evt);
            }
        });

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/Entypo_229f(70)_24.png"))); // NOI18N
        jMenuItem2.setText("Eliminar Actor");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jpopDeleteActor.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/Entypo_2796(58)_24.png"))); // NOI18N
        jMenuItem3.setText("Eliminar Copia");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jpopDeleteCopia.add(jMenuItem3);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Mantenimiento Pelicula");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameDeactivated(evt);
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Genero*");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Titulo*");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Duracion(Hrs)*");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("N° Pelicula");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Formato*");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Clasif*");

        jLabel8.setText("///////////////////////////////////////");

        cmbClasficacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 Estrella", "2 Estrella", "3 Estrella", "4 Estrella", "5 Estrella" }));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Años");

        cmbFormato.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbGenero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbIdioma.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Idioma");

        try {
            txtDuracion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDuracion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDuracionKeyTyped(evt);
            }
        });

        JYearAno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JYearAnoKeyTyped(evt);
            }
        });

        cmbIdiOri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel12.setText("Idio Orig*");

        txtSitio.setText("www.");

        jLabel13.setText("Sitio");

        PnlSubtitulo.setBorder(javax.swing.BorderFactory.createTitledBorder("Subtitulo :"));

        lstSubtitulo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstSubtitulo.setToolTipText("Para Borrar Click Izquierda");
        lstSubtitulo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lstSubtitulo.setInheritsPopupMenu(true);
        lstSubtitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstSubtituloMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstSubtituloMouseReleased(evt);
            }
        });
        lstSubtitulo.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstSubtituloValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstSubtitulo);

        btnAddSubtitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/Entypo_2795(59)_24.png"))); // NOI18N
        btnAddSubtitulo.setBorderPainted(false);
        btnAddSubtitulo.setContentAreaFilled(false);
        btnAddSubtitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSubtituloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSubtituloLayout = new javax.swing.GroupLayout(PnlSubtitulo);
        PnlSubtitulo.setLayout(PnlSubtituloLayout);
        PnlSubtituloLayout.setHorizontalGroup(
            PnlSubtituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlSubtituloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlSubtituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddSubtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        PnlSubtituloLayout.setVerticalGroup(
            PnlSubtituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSubtituloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlSubtituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(PnlSubtituloLayout.createSequentialGroup()
                        .addComponent(btnAddSubtitulo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlCopia.setBorder(javax.swing.BorderFactory.createTitledBorder("Nro Copia"));

        tblCopia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nro", "Estado", "codEst"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCopia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblCopia.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblCopia.getTableHeader().setReorderingAllowed(false);
        tblCopia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCopiaMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblCopiaMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblCopia);
        if (tblCopia.getColumnModel().getColumnCount() > 0) {
            tblCopia.getColumnModel().getColumn(0).setResizable(false);
            tblCopia.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblCopia.getColumnModel().getColumn(1).setResizable(false);
            tblCopia.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblCopia.getColumnModel().getColumn(2).setResizable(false);
            tblCopia.getColumnModel().getColumn(2).setPreferredWidth(0);
        }

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/Entypo_2795(59)_24.png"))); // NOI18N
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCopiaLayout = new javax.swing.GroupLayout(pnlCopia);
        pnlCopia.setLayout(pnlCopiaLayout);
        pnlCopiaLayout.setHorizontalGroup(
            pnlCopiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCopiaLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlCopiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlCopiaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(43, Short.MAX_VALUE)))
        );
        pnlCopiaLayout.setVerticalGroup(
            pnlCopiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCopiaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlCopiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlCopiaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        PnlActor.setBorder(javax.swing.BorderFactory.createTitledBorder("Actores  :"));

        lstActor.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstActor.setToolTipText("Para Borrar Click Izquierda");
        lstActor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lstActor.setInheritsPopupMenu(true);
        lstActor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstActorMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstActorMouseReleased(evt);
            }
        });
        lstActor.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstActorValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(lstActor);

        btnAddActor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/Entypo_2795(59)_24.png"))); // NOI18N
        btnAddActor.setBorderPainted(false);
        btnAddActor.setContentAreaFilled(false);
        btnAddActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlActorLayout = new javax.swing.GroupLayout(PnlActor);
        PnlActor.setLayout(PnlActorLayout);
        PnlActorLayout.setHorizontalGroup(
            PnlActorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlActorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlActorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddActor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        PnlActorLayout.setVerticalGroup(
            PnlActorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlActorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlActorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addGroup(PnlActorLayout.createSequentialGroup()
                        .addComponent(btnAddActor)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cmbFormato, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbIdiOri, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cmbClasficacion, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSitio, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JYearAno, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cmbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTitulo, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlSubtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(PnlActor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCopia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel8)
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel3)))
                            .addComponent(JYearAno, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbFormato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(cmbIdiOri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbClasficacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(txtSitio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlCopia, javax.swing.GroupLayout.PREFERRED_SIZE, 231, Short.MAX_VALUE)
                            .addComponent(PnlActor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PnlSubtitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Mantenimiento Pelicula");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/eliminar.png"))); // NOI18N
        btnEliminar.setText("ELIMINAR /f6");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 10, 140, 52));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/save8.png"))); // NOI18N
        btnGuardar.setText("GUARDAR /f5");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 140, 52));

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/limpiar.png"))); // NOI18N
        btnNuevo.setText("NUEVO /f4");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jPanel1.add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 131, 52));

        txtBusqueda.setText("Buscar  /f3");
        txtBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBusquedaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusquedaFocusLost(evt);
            }
        });
        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        jPanel1.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 30, 300, -1));

        tblPelicula.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPelicula.getTableHeader().setReorderingAllowed(false);
        tblPelicula.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPeliculaMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblPeliculaMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblPelicula);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1090, 150));

        lblInfo.setText("jLabel11");
        jPanel1.add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(5, 5, 5)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:

        // TODO add your handling code here:
        String ObjButtons[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(this, "Desea Salir?", "Salir?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if (PromptResult == JOptionPane.YES_OPTION) {
            // ConnectionManager.getInstance().close();
            this.dispose();
        }
    }//GEN-LAST:event_formInternalFrameClosing

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:

        String ObjButtons[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(this, "Desea Borrar?", "Confirmacion Borrar?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if (PromptResult == JOptionPane.YES_OPTION) {

            timer1.stop();
            jProgressBar1.setValue(100);
            progVal = jProgressBar1.getValue();

            timer1.start();

        }

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        clearNew();


    }//GEN-LAST:event_btnNuevoActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        int genMaXNumber = 0;

        //get highest copia
        DefaultTableModel model = (DefaultTableModel) tblCopia.getModel();
        //int = Integer.parseInt(model.getValueAt(0,model.getRowCount()-1 ).toString());

        //si se pa update
        //   if (rowIdData == 0) {
        if (model.getRowCount() != 0) {
            int highestNumCopiaFromModel = Integer.parseInt((tblCopia.getModel().getValueAt(model.getRowCount() - 1, 0).toString()));
            genMaXNumber = genNumCopia(highestNumCopiaFromModel);
            model.addRow(new Object[]{genMaXNumber, "Disponible", "1"});
            //System.out.println("MAAAAAXXX " + genMaXNumber);
        } else {
            //VERY FIRST ROW if it doesn;t have ons
            genMaXNumber = 1;
            model.addRow(new Object[]{genMaXNumber, "Disponible", "1"});

        }

        //for (int i = 0; i < tblCopia.getRowCount(); i++) {
        //   System.out.println("value at " + tblCopia.getValueAt(i, 0));
        // }
        //System.out.println("MAXNUM" + genMaXNumber);
        pnlCopia.setBorder(javax.swing.BorderFactory.createTitledBorder("Nro Copia(s): " + tblCopia.getRowCount()));
        llstAddRemoveCopia.add(new InsertUpdateDeleteCopia(0, genMaXNumber, 1, 1));

    }//GEN-LAST:event_jButton6ActionPerformed


    private void btnAddSubtituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSubtituloActionPerformed
        // TODO add your handling code here:
        JdSubtitulo nd = new JdSubtitulo(null, closable);
        nd.setModal(true);
        nd.setLocationRelativeTo(this);
        nd.setVisible(true);
    }//GEN-LAST:event_btnAddSubtituloActionPerformed

    private void lstSubtituloValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstSubtituloValueChanged
        // TODO add your handling code here:

        DataJlist selected_item = (DataJlist) lstSubtitulo.getSelectedValue();
        if (evt.getValueIsAdjusting()) {

            System.out.println("BOOM>>" + selected_item.getId());

        }
    }//GEN-LAST:event_lstSubtituloValueChanged

    private void lstSubtituloMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstSubtituloMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {

            if (!lstSubtitulo.isSelectionEmpty()) {
                jpopDeleteSubtitulo.show(evt.getComponent(), evt.getX(), evt.getY());
            }

            // jlSutitulo source=(jlSutitulo)evt.getSource();
            // jl source = (JdSubtitulo)evt.getSource();
            //  int row = source.rowAtPoint( e.getPoint() );
            //  int column = source.columnAtPoint( e.getPoint() );
            //  if (! source.isRowSelected(row))
            //    source.changeSelection(row, column, false, false);
            // popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }//GEN-LAST:event_lstSubtituloMouseReleased

    private void lstSubtituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstSubtituloMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lstSubtituloMouseClicked

    private void tblPeliculaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPeliculaMouseClicked
        // TODO add your handling code here:


        /*
        
        
         if (evt.getClickCount() == 2) {

         if (tblPelicula.getSelectedRow() > -1) {
         try {
         // print first column value from selected row
         int selectedCodpel = Integer.parseInt(tblPelicula.getValueAt(tblPelicula.getSelectedRow(), 0).toString());
         String fullname =tblPelicula.getValueAt(tblPelicula.getSelectedRow(), 1).toString();
         String birthdate = tblPelicula.getValueAt(tblPelicula.getSelectedRow(), 2).toString();
         //txtDuracion.setText(fullname);

         lastInsertedId = selectedCodpel;
         populateSubtituloDependOnSelectedIDPelicula(selectedCodpel);
         System.out.println("lastid " + lastInsertedId);

         ///Date convertion
         String inputDateStr = birthdate;
         DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
         DateFormat outputFormat = new SimpleDateFormat("dd/MM/YYYY");
         ///////////////////////////
         Date date = inputFormat.parse(inputDateStr);
         String outputDateStr = outputFormat.format(date);

         //System.out.println(">>++"+outputDateStr);
         //add outputDateStr  to format and convert it to date 
         jdBirthday.setDate((Date) new SimpleDateFormat("dd/MM/yyyy").parse(outputDateStr));

         } catch (ParseException ex) {
         Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
         }

         }
        
        
         }
   
         */

    }//GEN-LAST:event_tblPeliculaMouseClicked

    private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
        // TODO add your handling code here:
        progVal--;

        System.out.println("tick : " + progVal);
        jProgressBar1.setValue(progVal);
        if (progVal == 0) {
            ExecuteDeleteAnimationSutitulo();
            timer1.stop();
        }


    }//GEN-LAST:event_timer1OnTime

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:

        String sqlQuery = null;
        boolean todoBien = true;

        DatajCombobox codgen = (DatajCombobox) cmbGenero.getSelectedItem();
        DatajCombobox codcla = (DatajCombobox) cmbClasficacion.getSelectedItem();
        DatajCombobox codfor = (DatajCombobox) cmbFormato.getSelectedItem();
        DatajCombobox codidiori = (DatajCombobox) cmbIdiOri.getSelectedItem();
        DatajCombobox codidi = (DatajCombobox) cmbIdioma.getSelectedItem();

        String queryInsert = "INSERT INTO " + tableName + " VALUES(NULL,'" + txtTitulo.getText() + "','" + codgen.getId() + "','" + codcla.getId() + "','" + JYearAno.getYear() + "','" + txtDuracion.getText() + "','" + txtSitio.getText() + "','" + codfor.getId() + "','" + codidi.getId() + "','" + codidiori.getId() + "')";
        String queryUpdate = "UPDATE " + tableName + " SET titpel='" + txtTitulo.getText() + "', codgen='" + codgen.getId() + "',codcla='" + codcla.getId() + "',anopel='" + JYearAno.getYear() + "',durpel='" + txtDuracion.getText() + "',sitpel='" + txtSitio.getText() + "',codfor='" + codfor.getId() + "',codidi='" + codidi.getId() + "',codidiori='" + codidiori.getId() + "' WHERE " + idColname + "=" + rowIdData;

        ////////////////Validation
        if (!yearValidador.validate("" + JYearAno.getYear())) {
            JOptionPane.showMessageDialog(this, "Por favor Introduce un año de estreno valido");
        } else {
            if (cmbGenero.getSelectedIndex() == 0 || cmbClasficacion.getSelectedIndex() == 0 || cmbFormato.getSelectedIndex() == 0 || cmbIdiOri.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Por favor define todas las Caracteristicas Genero, Formato, Idio Ori, Clasificacion ");

            } else {

                if (txtTitulo.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Por favor Llene el Campo Titulo");
                    txtTitulo.requestFocus();
                } else {

                    if (!timeValidador.validate(txtDuracion.getText())) {

                        JOptionPane.showMessageDialog(this, "La Duracion Esta mal formatada es (Hora >> 00:00:00)");

                    } else {

                        if (tblCopia.getRowCount() <= 0) {
                            JOptionPane.showMessageDialog(this, "Debe por lo menos tener una(1) copia)");

                        } else {
                            System.out.println("Pass SqlQuery" + queryInsert);

                            ////////////////////pass///////////////
                            //check if the selectedCodpel is already the to update it
                            //update
                            if (sql.checkidWithIdColname(tableName, idColname, rowIdData)) {
                                //System.err.println("li la deja");

                                //confimacion de la actualizacion
                                String ObjButtons[] = {"Si", "No"};
                                int PromptResult = JOptionPane.showOptionDialog(this, "Desea Actualizar?", "Seguro de Actualizar?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ObjButtons, ObjButtons[1]);
                                if (PromptResult == JOptionPane.YES_OPTION) {

                                    if (sql.executeQuery(queryUpdate)) {

                                        ///Update all the VS 
                                        lastInsertedId = rowIdData;

                                        if (lastInsertedId != 0) {

                                            if (!insertDelete_IN_VS_table(llstAddRemoveSubtitulo, "tb_subtitulo_vs_pelicula", "codidi", "codpel")) {
                                                todoBien = false;
                                                System.out.println("false : insertDelete_IN_VS_table : tb_subtitulo_vs_pelicula ");
                                            }

                                            if (!insertDelete_IN_VS_table(llstAddRemoveActor, "tb_actor_vs_pelicula", "codact", "codpel")) {
                                                todoBien = false;
                                                System.out.println("false : insertDelete_IN_VS_table : tb_actor_vs_pelicula ");
                                            }

                                            ///////////////insert copia//////////////////////
                                            if (!insertUpdateDelete_Pel_VS_Copia(llstAddRemoveCopia, "tbpelicula_copia", "codpel", "numcopia")) {
                                                todoBien = false;

                                                System.out.println("false : insertUpdateDelete_Pel_VS_Copia : tbpelicula_copia ");

                                            }

                                            System.out.println("Check if todoBien: " + todoBien);

                                        }

                                        //if all the transactions are ok commit
                                        if (todoBien) {
                                            try {
                                                transCon.commit();
                                                JOptionPane.showMessageDialog(this, "Actualizado exitosamente");
                                                clearNew();
                                                llstAddRemoveActor.clear();
                                                llstAddRemoveSubtitulo.clear();
                                                llstAddRemoveCopia.clear();
                                                System.out.println("commit ");

                                            } catch (SQLException ex) {
                                                Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
                                            }

                                        } else {
                                            try {
                                                transCon.rollback();
                                                System.out.println("roll back ");

                                            } catch (SQLException ex) {
                                                Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
                                            }

                                        }

                                    }

                                }

                            } else {

                                if (insertLeaderTransaction(queryInsert)) {

                                    ///insert all the VS 
                                    if (lastInsertedId != 0) {

                                        if (!insertDelete_IN_VS_table(llstAddRemoveSubtitulo, "tb_subtitulo_vs_pelicula", "codidi", "codpel")) {
                                            todoBien = false;
                                            System.out.println("insertDelete_IN_VS_table : tb_subtitulo_vs_pelicula ");

                                        }

                                        if (!insertDelete_IN_VS_table(llstAddRemoveActor, "tb_actor_vs_pelicula", "codact", "codpel")) {
                                            todoBien = false;
                                            System.out.println("insertDelete_IN_VS_table : tb_actor_vs_pelicula ");

                                        }

                                        System.out.println("Check if todoBien: " + todoBien);

                                        ///////////////insert copia//////////////////////
                                        if (!insertUpdateDelete_Pel_VS_Copia(llstAddRemoveCopia, "tbpelicula_copia", "codpel", "numcopia")) {
                                            todoBien = false;
                                            System.out.println("insertUpdateDelete_Pel_VS_Copia : tbpelicula_copia ");

                                        }

                                    }

                                    //if all the transactions are ok commit
                                    if (todoBien) {
                                        try {
                                            transCon.commit();
                                            System.out.println("comit");
                                            JOptionPane.showMessageDialog(this, "Guardado exitosamente");
                                            clearNew();
                                            llstAddRemoveActor.clear();
                                            llstAddRemoveSubtitulo.clear();

                                        } catch (SQLException ex) {
                                            Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else {
                                        try {
                                            transCon.rollback();
                                            System.out.println("roll back ");

                                        } catch (SQLException ex) {
                                            Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                } else {
                                    todoBien = false;
                                    JOptionPane.showMessageDialog(this, "No se ha podido Guardar");
                                    try {
                                        transCon.rollback();
                                        System.out.println("roll back ");

                                    } catch (SQLException ex) {
                                        Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            }

                        }

                    }

                }

            }

        }

       // String queryUpdate = "UPDATE " + tableName + " SET titpel='" + txtTitulo.getText() + "', codgen='" + cg + "',fecnac='" + sdf.format(txtfecnac.getDate()) + "' WHERE " + idColname + "=" + rowIdData;
        // String queryInsert = "INSERT INTO " + tableName + " VALUES(NULL,'" + txtnom.getText() + "','" + txtapel.getText() + "','" + sdf.format(txtfecnac.getDate()) + "')";
/*
    
         */

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:

        //Cascade delete
        if (rowIdData == 0) {
            JOptionPane.showMessageDialog(null, "Por favor elige el elemento a borrar");
        } else {

            String ObjButtons[] = {"Si", "No"};
            int PromptResult = JOptionPane.showOptionDialog(this, "Desea Borrar? [ " + confirmStringToDelete + " ]", "Confirmacion...?", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[0]);

            if (PromptResult == JOptionPane.YES_OPTION) {

                if (sql.deleteWithIdColname(tableName, idColname, rowIdData)) {
                    //JOptionPane.showMessageDialog(null,"Borrado exitosamente");
                    clearNew();
                    confirmStringToDelete = null;
                    rowIdData = 0;
                }
            }

            //////////////////
        }


    }//GEN-LAST:event_btnEliminarActionPerformed

    private void lstActorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstActorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lstActorMouseClicked

    private void lstActorMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstActorMouseReleased
        // TODO add your handling code here:

        if (evt.isPopupTrigger()) {

            if (!lstActor.isSelectionEmpty()) {
                jpopDeleteActor.show(evt.getComponent(), evt.getX(), evt.getY());
            }

            // jlSutitulo source=(jlSutitulo)evt.getSource();
            // jl source = (JdSubtitulo)evt.getSource();
            //  int row = source.rowAtPoint( e.getPoint() );
            //  int column = source.columnAtPoint( e.getPoint() );
            //  if (! source.isRowSelected(row))
            //    source.changeSelection(row, column, false, false);
            // popup.show(e.getComponent(), e.getX(), e.getY());
        }

    }//GEN-LAST:event_lstActorMouseReleased

    private void lstActorValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstActorValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_lstActorValueChanged

    private void btnAddActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActorActionPerformed
        // TODO add your handling code here:
        JdActor nd = new JdActor(null, closable);
        nd.setModal(true);
        nd.setLocationRelativeTo(this);
        nd.setVisible(true);

    }//GEN-LAST:event_btnAddActorActionPerformed

    private void timer2OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer2OnTime
        // TODO add your handling code here:

        progVal--;

        System.out.println("tick : " + progVal);
        jProgressBar2.setValue(progVal);
        if (progVal == 0) {
            ExecuteDeleteAnimationActor();
            timer2.stop();
        }


    }//GEN-LAST:event_timer2OnTime

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:

        String ObjButtons[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(this, "Desea Borrar?", "Confirmacion Borrar?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if (PromptResult == JOptionPane.YES_OPTION) {

            timer2.stop();
            jProgressBar2.setValue(100);
            progVal = jProgressBar2.getValue();

            timer2.start();

        }

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void tblPeliculaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPeliculaMouseReleased
        // TODO add your handling code here:
        getSelectRowIdData();

        ///polulate subtitulo
        String SqlQuerySubtitulo = "SELECT idi.codidi  as id ,idi.desidi as `desc`  FROM  tbpelicula p INNER JOIN tb_subtitulo_vs_pelicula subp ON p.codpel=subp.codpel  INNER JOIN tbidioma idi ON idi.codidi=subp.codidi WHERE p.codpel='" + rowIdData + "'";
        String SqlQueryActor = "SELECT ac.codact as id ,CONCAT(ac.nomact,' ',ac.apeact) as `desc`  FROM  tbpelicula p INNER JOIN tb_actor_vs_pelicula actp ON p.codpel=actp.codpel  INNER JOIN tbactor ac ON ac.codact=actp.codact WHERE p.codpel='" + rowIdData + "'";
        String sqlQueryCopia = "SELECT * FROM (SELECT pelc.numcopia AS Nro,estp.desest AS Estado,estp.codestpel FROM tbpelicula p INNER JOIN tbpelicula_copia pelc ON p.codpel=pelc.codpel INNER JOIN tb_estado_pelicula estp ON pelc.codestado=estp.codestpel WHERE p.codpel='" + rowIdData + "' ) AS vtb";

        //make sure you select something
        if (rowIdData != 0) {

            populateSubtituloActor(lstSubtituloModel, SqlQuerySubtitulo);
            populateSubtituloActor(lstActorModel, SqlQueryActor);
            populateCopiaPelicula(sqlQueryCopia);

        }


    }//GEN-LAST:event_tblPeliculaMouseReleased

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        // TODO add your handling code here:
        populatePeliculaViaFilter(txtBusqueda.getText().trim());

    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void txtBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusLost
        // TODO add your handling code here:
        txtBusqueda.setText("Buscar /f3");

    }//GEN-LAST:event_txtBusquedaFocusLost

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);

    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);


    }//GEN-LAST:event_formInternalFrameDeactivated

    private void tblCopiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCopiaMouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {
            System.out.println("double click");

            //fill vitual  combobox estado
            comboboxDisplayEstadoPelicula(dialogcmbEstadoOption, "tb_estado_pelicula", "codestpel", "desest");

            dialogcmbEstadoOption.setEditable(true);

            ///////////Custom  joption
            String ObjButtons[] = {"Aceptar", "Cancelar"};
            int PromptResult = JOptionPane.showOptionDialog(this, dialogcmbEstadoOption, "Cambiar el Estado Copia ?", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, ObjButtons, ObjButtons[0]);

            if (PromptResult == JOptionPane.YES_OPTION) {

                DatajCombobox itemId = (DatajCombobox) dialogcmbEstadoOption.getSelectedItem();

                if (itemId.getId() != 0) {
                    int selectedRow = tblCopia.getSelectedRow();
                    tblCopia.setValueAt(itemId.getDescription(), selectedRow, 1);
//                    tblCopia.setValueAt(itemId.getId(), selectedRow, 2);
                    int numCopia = Integer.parseInt(tblCopia.getValueAt(selectedRow, 0).toString());

                    System.out.println("id :" + itemId.getId() + " desc: " + itemId.getDescription() + "numc : " + numCopia);
                    llstAddRemoveCopia.add(new InsertUpdateDeleteCopia(rowIdData, numCopia, itemId.getId(), 2));
                }

            }
            ////////////end custom/////////

        }


    }//GEN-LAST:event_tblCopiaMouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:

        //si li diferan de zero sa vle di se yon fil ki la deja lap dele copia a 
        //lap delete from table where codpel=codpel and numcopia=numcopia
        if (rowIdData != 0) {
            // System.out.println("delete send data"+rowIdData+" "+Integer.parseInt((tblCopia.getValueAt(tblCopia.getSelectedRow(), 0).toString())));
            llstAddRemoveCopia.add(new InsertUpdateDeleteCopia(rowIdData, Integer.parseInt((tblCopia.getValueAt(tblCopia.getSelectedRow(), 0).toString())), 0, 3));

        }

        DefaultTableModel model = (DefaultTableModel) tblCopia.getModel();
        model.removeRow(tblCopia.getSelectedRow());
        PnlSubtitulo.setBorder(javax.swing.BorderFactory.createTitledBorder("Subtitulo(s): " + lstSubtitulo.getModel().getSize()));


    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void tblCopiaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCopiaMouseReleased

        // TODO add your handling code here:
        //PopUp
        if (evt.isPopupTrigger()) {

            if (tblCopia.getRowCount() > 0 && tblCopia.getSelectedRow() != -1) {
                jpopDeleteCopia.show(evt.getComponent(), evt.getX(), evt.getY());
            }

            // jlSutitulo source=(jlSutitulo)evt.getSource();
            // jl source = (JdSubtitulo)evt.getSource();
            //  int row = source.rowAtPoint( e.getPoint() );
            //  int column = source.columnAtPoint( e.getPoint() );
            //  if (! source.isRowSelected(row))
            //    source.changeSelection(row, column, false, false);
            // popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }//GEN-LAST:event_tblCopiaMouseReleased

    private void txtBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusGained
        // TODO add your handling code here:
        txtBusqueda.setText(null);
    }//GEN-LAST:event_txtBusquedaFocusGained

    private void JYearAnoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JYearAnoKeyTyped
        // TODO add your handling code here:


    }//GEN-LAST:event_JYearAnoKeyTyped

    private void txtDuracionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDuracionKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_ENTER)
                || (c == KeyEvent.VK_PERIOD))) {
            JOptionPane.showMessageDialog(this, "Digite Solo numero en este campo");
            evt.consume();
        }
    }//GEN-LAST:event_txtDuracionKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JYearChooser JYearAno;
    public static javax.swing.JPanel PnlActor;
    public static javax.swing.JPanel PnlSubtitulo;
    private javax.swing.JButton btnAddActor;
    private javax.swing.JButton btnAddSubtitulo;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox cmbClasficacion;
    private javax.swing.JComboBox cmbFormato;
    private javax.swing.JComboBox cmbGenero;
    private javax.swing.JComboBox cmbIdiOri;
    private javax.swing.JComboBox cmbIdioma;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu jpopDeleteActor;
    private javax.swing.JPopupMenu jpopDeleteCopia;
    private javax.swing.JPopupMenu jpopDeleteSubtitulo;
    private javax.swing.JLabel lblInfo;
    public static javax.swing.JList lstActor;
    public static javax.swing.JList lstSubtitulo;
    public static javax.swing.JPanel pnlCopia;
    private javax.swing.JTable tblCopia;
    private javax.swing.JTable tblPelicula;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    private org.netbeans.examples.lib.timerbean.Timer timer2;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JFormattedTextField txtDuracion;
    private javax.swing.JTextField txtSitio;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables

    private void ExecuteDeleteAnimationSutitulo() {

        DefaultListModel model = (DefaultListModel) lstSubtitulo.getModel();
        int selectedIndex = lstSubtitulo.getSelectedIndex();

        //delete
        if (selectedIndex != -1) {
            //get delete selectedCodpel
            // DataJlist selected_item = (DataJlist) jlCars.getSelectedValue(); //or
            DataJlist selected_item = (DataJlist) lstSubtituloModel.get(selectedIndex);
            //System.out.println("BOOM>>"+selected_item.getId());

            addRemoveElementFromSubtitulo(selected_item.getId(), false);
            model.remove(selectedIndex);
            PnlSubtitulo.setBorder(javax.swing.BorderFactory.createTitledBorder("Subtitulo(s): " + lstSubtitulo.getModel().getSize()));

        }

    }

    private void ExecuteDeleteAnimationActor() {
        DefaultListModel model = (DefaultListModel) lstActor.getModel();
        int selectedIndex = lstActor.getSelectedIndex();

        //delete
        if (selectedIndex != -1) {
            //get delete selectedCodpel
            // DataJlist selected_item = (DataJlist) jlCars.getSelectedValue(); //or
            DataJlist selected_item = (DataJlist) lstActorModel.get(selectedIndex);
            //System.out.println("BOOM>>"+selected_item.getId());

            addRemoveElementFromActor(selected_item.getId(), false);
            model.remove(selectedIndex);
            PnlActor.setBorder(javax.swing.BorderFactory.createTitledBorder("Actor(es): " + lstActor.getModel().getSize()));

        }

    }

}
