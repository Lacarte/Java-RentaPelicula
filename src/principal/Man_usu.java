package principal;

import utilities.ConnectionManager;
import utilities.DBSql;
import utilities.DatajCombobox;
import utilities.EmailValidator;
import utilities.LimitTextfield;
import utilities.ResultsetTable;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LCRT
 */
public class Man_usu extends javax.swing.JInternalFrame {

    String fecha = "01/01/1990";
    java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.util.Date fechaDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static Connection transCon = ConnectionManager.getInstance().getConnection();

/////////
    EmailValidator emailValidator = new EmailValidator();

    DBSql sql = null;
    int progVal = 0;
    public static int rowIdData = 0;
    int lastInsertedId = 0;
    public static boolean yacliente = false;
    String confirmStringToDelete = null;

    String tableName = "tbusuario";
    String idColname = "codusu";
    // String descriptionColname = "titpel";

    /**
     * Creates new form man_peliculas
     */
    public Man_usu() {
        initComponents();
        sql = new DBSql();
        try {

            this.fechaDate = formato.parse(fecha);

        } catch (ParseException ex) {
            Logger.getLogger(Man_usu.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtNom.setDocument(new LimitTextfield(32));

        txtCon.setDocument(new LimitTextfield(32));

        txtBusqueda.setDocument(new LimitTextfield(32));

        txtNom.requestFocus();
        populateUsuario();

        txtBusqueda.setText("Busqueda /f3 (CEDULA O por Nombre)");

        comboboxDisplay(cmbTipUsu, "tb_tipo_usuario", "codtipusu", "destipusu");

        ////tab index order
//        IndexedFocusTraversalPolicy policy = new IndexedFocusTraversalPolicy();
//policy.addIndexedComponent(jTextField1);
//policy.addIndexedComponent(jTextField2);
//policy.addIndexedComponent(jTextField3);
//policy.addIndexedComponent(jTextField4);
//policy.addIndexedComponent(jTextField5);
//policy.addIndexedComponent(jTextField6);
//setFocusTraversalPolicy(policy);
//        
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

    //  txtBusqueda.setText("Busqueda /f3");
    ///////////////////////clear all
    private void clearNew() {

        txtNom.setText(null);
        txtCon.setText(null);
        //   txtCed.setText(null);
        // txtCor.setText(null);
        // txtTel.setText(null);
        // txtDir.setText(null);

        chcEstado.setSelected(true);
        populateUsuario();
        txtNom.requestFocus();
        cmbTipUsu.setSelectedIndex(0);
        lblNombreCompleto.setText("[]");
        lblcodEmp.setText("/////////////////////");
       // cmbSuc.setSelectedIndex(0);

        //  lstSubtituloModel.clear();
        // lstActorModel.clear();
        // llstAddRemoveCopia.clear();
        //remove rows
        //DefaultTableModel dtm = (DefaultTableModel) tblCliente.getModel();
        //dtm.setRowCount(0);
        rowIdData = 0;
        lastInsertedId = 0;

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

    void hideColTable() {
        /*
         tblUsu.getColumnModel().getColumn(7).setMinWidth(0);
         tblUsu.getColumnModel().getColumn(7).setMaxWidth(0);
         tblUsu.getColumnModel().getColumn(7).setWidth(0);

         tblUsu.getColumnModel().getColumn(8).setMinWidth(0);
         tblUsu.getColumnModel().getColumn(8).setMaxWidth(0);
         tblUsu.getColumnModel().getColumn(8).setWidth(0);

         tblUsu.getColumnModel().getColumn(9).setMinWidth(0);
         tblUsu.getColumnModel().getColumn(9).setMaxWidth(0);
         tblUsu.getColumnModel().getColumn(9).setWidth(0);

         tblUsu.getColumnModel().getColumn(10).setMinWidth(0);
         tblUsu.getColumnModel().getColumn(10).setMaxWidth(0);
         tblUsu.getColumnModel().getColumn(10).setWidth(0);
         */
    }

    public void populateUsuario() {

        String sqlQuery = "SELECT * FROM (SELECT usu.codusu AS CODUSU,usu.nomusu as 'NOMBRE USUARIO',usu.conusu as 'CONTRASENA',tipusu.destipusu as 'TIPO USUARIO',CONCAT(ter.nomter,' ',per.apeper) as 'NOMBRE COMPLETO',per.cedper AS 'CEDULA',usu.codest 'ESTADO' FROM tbtercero ter INNER JOIN tbpersona per ON ter.codter=per.codper\n"
                + "INNER JOIN tbempleado emp ON per.codper=emp.codemp INNER JOIN tbusuario usu ON\n"
                + "usu.codusu=emp.codemp INNER JOIN tb_tipo_usuario tipusu ON tipusu.codtipusu=usu.codtipusu\n"
                + "LEFT JOIN tbsucursal suc ON suc.codsuc=emp.codsuc) AS vtb";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblUsu.remove(this);
        try {
            tblUsu.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblUsu.getRowCount());

            hideColTable();

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void populateUsuarioViaFilter() {

        String sqlQuery = "SELECT * FROM (SELECT usu.codusu AS CODUSU,usu.nomusu as 'NOMBRE USUARIO',usu.conusu as 'CONTRASENA',tipusu.destipusu as 'TIPO USUARIO',CONCAT(ter.nomter,' ',per.apeper) as 'NOMBRE COMPLETO',per.cedper AS 'CEDULA',usu.codest 'ESTADO' FROM tbtercero ter INNER JOIN tbpersona per ON ter.codter=per.codper\n"
                + "INNER JOIN tbempleado emp ON per.codper=emp.codemp INNER JOIN tbusuario usu ON\n"
                + "usu.codusu=emp.codemp INNER JOIN tb_tipo_usuario tipusu ON tipusu.codtipusu=usu.codtipusu\n"
                + "LEFT JOIN tbsucursal suc ON suc.codsuc=emp.codsuc) AS vtb WHERE cedula LIKE '%" + txtBusqueda.getText().trim() + "%'  OR `NOMBRE USUARIO` LIKE '%" + txtBusqueda.getText().trim() + "%'  ORDER BY CODUSU DESC";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblUsu.remove(this);
        try {
            tblUsu.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblUsu.getRowCount());
            hideColTable();

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            int selectedRow = tblUsu.getSelectedRow();

            int idData = Integer.parseInt((tblUsu.getModel().getValueAt(selectedRow, 0).toString()));

            rowIdData = idData;

            String chk = tblUsu.getModel().getValueAt(selectedRow, 6).toString();
            //System.out.println("hello check" + chk);
            //checkbox
            if (chk.equalsIgnoreCase("1")) {
                chcEstado.setSelected(true);
            } else {
                chcEstado.setSelected(false);
            }

            
             selectedValueCombobox(cmbTipUsu, tblUsu.getModel().getValueAt(selectedRow, 3));
            /////////////////////////////set combobox////////////////////////
            //selectedValueCombobox(cmbGenero, tblPelicula.getModel().getValueAt(selectedRow, 2));
            /////////////////for update//////////////////       
            ////////mete string lan nan textfield la///////////////
            lblNombreCompleto.setText(tblUsu.getModel().getValueAt(selectedRow, 4).toString());
            lblcodEmp.setText("" + idData);

            txtNom.setText(tblUsu.getModel().getValueAt(selectedRow, 1).toString());
            txtCon.setText(tblUsu.getModel().getValueAt(selectedRow, 2).toString());
            // txtDir.setText(tblCliente.getModel().getValueAt(selectedRow, 7).toString());
            cmbTipUsu.setSelectedItem(tblUsu.getModel().getValueAt(selectedRow, 8).toString());

            //  selectedValueCombobox(cmbSuc, tblCliente.getModel().getValueAt(selectedRow, 9));
            ///Date convertion
            String inputDateStr = tblUsu.getModel().getValueAt(selectedRow, 9).toString();
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/YYYY");
            ///////////////////////////
            Date date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);

            
            //System.out.println(">>++"+outputDateStr);
            //add outputDateStr  to format and convert it to date 
            // jdcFecNac.setDate((Date) new SimpleDateFormat("dd/MM/yyyy").parse(outputDateStr));
            //fecha
            //  txtCed.setYear(Integer.parseInt((String) tblPelicula.getModel().getValueAt(selectedRow, 4)));
            ///////////////////pran tout string ki nan row la///////////////////
            confirmStringToDelete = "";
            for (int i = 0; i < tblUsu.getColumnCount(); i++) {

                String strData = (tblUsu.getModel().getValueAt(selectedRow, i).toString()) + " ";
                //System.out.println(""+strData);
                confirmStringToDelete += strData;
            }

        } catch (Exception e) {
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
    boolean setTransaction(String query
    ) {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsu = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtNom = new javax.swing.JTextField();
        txtCon = new javax.swing.JTextField();
        cmbTipUsu = new javax.swing.JComboBox();
        chcEstado = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblcodEmp = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblNombreCompleto = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblInfo = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Mantenimiento Usuario");
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

        jPanel1.setFocusCycleRoot(true);

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/eliminar.png"))); // NOI18N
        btnEliminar.setText("ELIMINAR /f6");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/save8.png"))); // NOI18N
        btnGuardar.setText("GUARDAR /f5");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/limpiar.png"))); // NOI18N
        btnNuevo.setText("NUEVO  /f4");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        txtBusqueda.setText("Buscar /f3 Cedula O Por Nombre");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        tblUsu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblUsu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblUsuMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsu);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Del Usuario"));
        jPanel2.setFocusCycleRoot(true);

        chcEstado.setSelected(true);
        chcEstado.setText("Activo");

        jLabel2.setText("Nombre Usuario *");

        jLabel3.setText("Contraseña *");

        jLabel1.setText("<html>N° Empleado(Usuario)</html>");

        lblcodEmp.setText("////////////////////////////////////");

        jLabel10.setText("Tipo Usuario");

        jLabel13.setText("Estado *");

        jButton1.setText("Empleado");
        jButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/zoom_icon&24.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblNombreCompleto.setText("[]");

        jLabel6.setText("Nombre Completo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(284, 284, 284)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(cmbTipUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(chcEstado))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(247, 247, 247)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)
                                .addGap(22, 22, 22)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNom)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblcodEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jButton1))
                            .addComponent(txtCon, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(lblNombreCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtCon, txtNom});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreCompleto)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblcodEmp)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTipUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chcEstado)
                    .addComponent(jLabel13))
                .addGap(0, 35, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Mantenimiento Usuario");

        lblInfo.setText("lbInfo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(31, 31, 31))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblInfo)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        String ObjButtons[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(this, "Desea Salir?", "Salir?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if (PromptResult == JOptionPane.YES_OPTION) {
            // ConnectionManager.getInstance().close();
            this.dispose();
        }
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        clearNew();

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:

        DatajCombobox codtipusu = (DatajCombobox) cmbTipUsu.getSelectedItem();

        boolean todoBien = true;

        if (txtNom.getText().isEmpty() || txtCon.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Por favor llene todo los campos");

        } else {

            if (cmbTipUsu.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(this, "Por favor Elige un tipo de usuario");

            } else {

                if (rowIdData == 0) {

                    JOptionPane.showMessageDialog(this, "Por favor Elige un Empleado");

                } else {

              
                        ///////////////pass/////////////////
                        //check if the id is already the to update it
                        if (sql.checkidWithIdColname(tableName, idColname, rowIdData)) {
                            //System.err.println("li la deja");
                            //update Usuario
                            // 
                            String sqlQueryUpdateUsuario = "UPDATE `tbusuario` SET `nomusu`='" + txtNom.getText() + "',conusu='" + txtCon.getText().trim() + "',codest=" + chcEstado.isSelected() + ",codtipusu=" + codtipusu.getId() + " WHERE `codusu`=" + rowIdData + "";
                            System.out.println("sql>>" + sqlQueryUpdateUsuario);

                            if (!setTransaction(sqlQueryUpdateUsuario)) {
                                System.out.println("false sqlQueryUpdateUsuario");

                                todoBien = false;

                            }

                            if (todoBien) {

                                //if all the transactions are ok commit
                                if (todoBien) {
                                    try {
                                        transCon.commit();
                                        JOptionPane.showMessageDialog(this, "Actualizado exitosamente");
                                        clearNew();
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(this, "No se ha podido Guardar");

                                    JOptionPane.showMessageDialog(this, "Ocurre Un error minetras actualizando ");
                                    try {
                                        System.out.println("Rollback");
                                        transCon.rollback();
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Man_usu.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }

                            }

                        } else {
                            
                            //insert 
                            //save usuario//
                         
                                  if (sql.dataDescriptionExist(tableName, "nomusu", txtNom.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Ya , existe este Nombre de Usuario elige otro");

                    } else {

                            
                            String sqlQueryInsertUsuario = "INSERT INTO `tbusuario` (`codusu`, `nomusu`, `conusu`, `codest`, `codtipusu`) VALUES ('" + rowIdData + "', '" + txtNom.getText() + "', '" + txtCon.getText().trim() + "', " + chcEstado.isSelected() + ", " + codtipusu.getId() + ")";
                            System.out.println("sql>>" + sqlQueryInsertUsuario);

                            if (!setTransaction(sqlQueryInsertUsuario)) {
                                System.out.println("false sqlQueryInsertUsuario");

                                todoBien = false;
                            }

                            //if all the transactions are ok commit
                            if (todoBien) {
                                try {
                                    transCon.commit();

                                    JOptionPane.showMessageDialog(this, "Guardado exitosamente");
                                    clearNew();
                                } catch (SQLException ex) {
                                    Logger.getLogger(Man_pel.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "No se ha podido Guardar");

                                try {
                                    System.out.println("Rollback");
                                    transCon.rollback();
                                } catch (SQLException ex) {
                                    Logger.getLogger(Man_usu.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }

                        }
                    }
                }
            }

        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);


    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);

    }//GEN-LAST:event_formInternalFrameDeactivated

    private void txtBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusGained
        // TODO add your handling code here:
        txtBusqueda.setText(null);
    }//GEN-LAST:event_txtBusquedaFocusGained

    private void txtBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusLost
        // TODO add your handling code here:
        txtBusqueda.setText("Buscar /f3 Cedula O Por Nombre");

    }//GEN-LAST:event_txtBusquedaFocusLost

    private void tblUsuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuMouseClicked
        // TODO add your handling code here:
        getSelectRowIdData();

    }//GEN-LAST:event_tblUsuMouseClicked

    private void tblUsuMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuMouseReleased
        // TODO add your handling code here:

        ///polulate subtitulo
        getSelectRowIdData();

    }//GEN-LAST:event_tblUsuMouseReleased

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        // TODO add your handling code here:

        populateUsuarioViaFilter();

    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:

        if (rowIdData == 0) {
            JOptionPane.showMessageDialog(null, "Por favor elige el elemento a borrar");
        } else {

            String ObjButtons[] = {"Si", "No"};
            int PromptResult = JOptionPane.showOptionDialog(this, "Desea Borrar? [ " + confirmStringToDelete + " ]", "Confirmacion...?", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[0]);

            if (PromptResult == JOptionPane.YES_OPTION) {

                if (sql.deleteWithIdColname("tbcliente", "codter", rowIdData)) {
                    //JOptionPane.showMessageDialog(null,"Borrado exitosamente");
                    clearNew();
                    confirmStringToDelete = null;
                    rowIdData = 0;
                }
            }

            //////////////////
        }


    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JdTodoLosEmpleados todoemp = new JdTodoLosEmpleados(null, closable);
        todoemp.setModal(true);
        todoemp.setLocationRelativeTo(this);
        todoemp.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    public static javax.swing.JCheckBox chcEstado;
    public static javax.swing.JComboBox cmbTipUsu;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInfo;
    public static javax.swing.JLabel lblNombreCompleto;
    public static javax.swing.JLabel lblcodEmp;
    private javax.swing.JTable tblUsu;
    private javax.swing.JTextField txtBusqueda;
    public static javax.swing.JTextField txtCon;
    public static javax.swing.JTextField txtNom;
    // End of variables declaration//GEN-END:variables

}
