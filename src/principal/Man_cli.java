package principal;

import utilities.ConnectionManager;
import utilities.DBSql;
import utilities.EmailValidator;
import utilities.LimitTextfield;
import utilities.ResultsetTable;
import utilities.TimeValidator;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static principal.Man_pel.llstAddRemoveCopia;
import static principal.Man_pel.lstActorModel;
import static principal.Man_pel.lstSubtituloModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LCRT
 */
public class Man_cli extends javax.swing.JInternalFrame {

    String fecha = "01/01/1990";
    java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.util.Date fechaDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static Connection transCon = ConnectionManager.getInstance().getConnection();

/////////
    EmailValidator emailValidator = new EmailValidator();

    DBSql sql = null;
    int progVal = 0;
    int rowIdData = 0;
    int lastInsertedId = 0;
    String confirmStringToDelete = null;

    String tableName = "tbtercero";
    String idColname = "codter";
    // String descriptionColname = "titpel";

    /**
     * Creates new form man_peliculas
     */
    public Man_cli() {
        initComponents();
        sql = new DBSql();
        try {

            this.fechaDate = formato.parse(fecha);
            jdcFecNac.setDate(fechaDate);
        } catch (ParseException ex) {
            Logger.getLogger(Man_cli.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtNom.setDocument(new LimitTextfield(32));

        txtApe.setDocument(new LimitTextfield(32));

        txtBusqueda.setDocument(new LimitTextfield(32));

      //  txtCed.setDocument(new LimitTextfield(13));
        txtCor.setDocument(new LimitTextfield(64));

        txtNom.requestFocus();
        populateCliente();

        txtBusqueda.setText("Busqueda /f3 (CEDULA)");

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
        txtApe.setText(null);
        txtCed.setText(null);
        txtCor.setText(null);
        txtTel.setText(null);
        txtDir.setText(null);

        chcEstado.setSelected(true);
        populateCliente();
        txtNom.requestFocus();
        cmbSex.setSelectedIndex(0);
        tableName = "tbcliente";

        //  lstSubtituloModel.clear();
        // lstActorModel.clear();
        // llstAddRemoveCopia.clear();
        //remove rows
        //DefaultTableModel dtm = (DefaultTableModel) tblCliente.getModel();
        //dtm.setRowCount(0);
        rowIdData = 0;
        lastInsertedId = 0;

    }

    void hideColTable() {

        tblCliente.getColumnModel().getColumn(7).setMinWidth(0);
        tblCliente.getColumnModel().getColumn(7).setMaxWidth(0);
        tblCliente.getColumnModel().getColumn(7).setWidth(0);

        tblCliente.getColumnModel().getColumn(8).setMinWidth(0);
        tblCliente.getColumnModel().getColumn(8).setMaxWidth(0);
        tblCliente.getColumnModel().getColumn(8).setWidth(0);

        tblCliente.getColumnModel().getColumn(9).setMinWidth(0);
        tblCliente.getColumnModel().getColumn(9).setMaxWidth(0);
        tblCliente.getColumnModel().getColumn(9).setWidth(0);

    }

    public void populateCliente() {

        String sqlQuery = "SELECT * FROM (SELECT tercli.codter AS CODCLI,tercli.nomter AS NOMBRE, per.apeper  AS APELLIDO,per.cedper AS CEDULA, tercli.telter AS TEL,cli.codest AS ACTIVO,tercli.corter AS CORREO,tercli.dirter,sexper,tercli.fecnac FROM (SELECT * FROM tbtercero ter WHERE codter IN(SELECT codter FROM tbcliente)) AS tercli INNER JOIN tbpersona per ON tercli.codter=per.codper INNER JOIN tbcliente cli ON cli.codter=tercli.codter)AS vtb ORDER BY CODCLI DESC";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblCliente.remove(this);
        try {
            tblCliente.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblCliente.getRowCount());

            hideColTable();

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void populateClienteViaFilter() {

        String sqlQuery = "SELECT * FROM (SELECT tercli.codter AS CODCLI,tercli.nomter AS NOMBRE, per.apeper  AS APELLIDO,per.cedper AS CEDULA, tercli.telter AS TEL,cli.codest AS ACTIVO,tercli.corter AS CORREO,tercli.dirter,sexper,tercli.fecnac FROM (SELECT * FROM tbtercero ter WHERE codter IN(SELECT codter FROM tbcliente)) AS tercli INNER JOIN tbpersona per ON tercli.codter=per.codper INNER JOIN tbcliente cli ON cli.codter=tercli.codter)AS vtb  WHERE cedula LIKE '%" + txtBusqueda.getText().trim() + "%'  ORDER BY CODCLI DESC";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblCliente.remove(this);
        try {
            tblCliente.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblCliente.getRowCount());
            hideColTable();

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getSelectRowIdData() {
        try {
            int selectedRow = tblCliente.getSelectedRow();

            int idData = Integer.parseInt((tblCliente.getModel().getValueAt(selectedRow, 0).toString()));

            rowIdData = idData;

            /////////////////////////////set combobox////////////////////////
            //selectedValueCombobox(cmbGenero, tblPelicula.getModel().getValueAt(selectedRow, 2));
            /////////////////for update//////////////////       
            ////////mete string lan nan textfield la///////////////
            txtNom.setText(tblCliente.getModel().getValueAt(selectedRow, 1).toString());
            txtApe.setText(tblCliente.getModel().getValueAt(selectedRow, 2).toString());
            txtCed.setText(tblCliente.getModel().getValueAt(selectedRow, 3).toString());
            txtTel.setText(tblCliente.getModel().getValueAt(selectedRow, 4).toString());

            txtCor.setText(tblCliente.getModel().getValueAt(selectedRow, 6).toString());

            txtDir.setText(tblCliente.getModel().getValueAt(selectedRow, 7).toString());

            cmbSex.setSelectedItem(tblCliente.getModel().getValueAt(selectedRow, 8).toString());

            String chk = tblCliente.getModel().getValueAt(selectedRow, 5).toString();

            //checkbox
            if (chk.equalsIgnoreCase("1")) {
                chcEstado.setSelected(true);
            } else {
                chcEstado.setSelected(false);
            }

            ///Date convertion
            String inputDateStr = tblCliente.getModel().getValueAt(selectedRow, 9).toString();
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/YYYY");
            ///////////////////////////
            Date date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);

            //System.out.println(">>++"+outputDateStr);
            //add outputDateStr  to format and convert it to date 
            jdcFecNac.setDate((Date) new SimpleDateFormat("dd/MM/yyyy").parse(outputDateStr));

            //fecha
            //  txtCed.setYear(Integer.parseInt((String) tblPelicula.getModel().getValueAt(selectedRow, 4)));
            ///////////////////pran tout string ki nan row la///////////////////
            confirmStringToDelete = "";
            for (int i = 0; i < tblCliente.getColumnCount(); i++) {

                String strData = (tblCliente.getModel().getValueAt(selectedRow, i).toString()) + " ";
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
        tblCliente = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtNom = new javax.swing.JTextField();
        txtApe = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCed = new javax.swing.JFormattedTextField();
        cmbSex = new javax.swing.JComboBox();
        chcEstado = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDir = new javax.swing.JTextPane();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jdcFecNac = new com.toedter.calendar.JDateChooser();
        txtTel = new javax.swing.JFormattedTextField();
        txtCor = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblInfo = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Mantenimiento Cliente");
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

        txtBusqueda.setText("Buscar /f3");
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
                .addGap(31, 31, 31)
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

        tblCliente.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClienteMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblClienteMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblCliente);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Del Cliente"));
        jPanel2.setFocusCycleRoot(true);

        txtNom.setFocusCycleRoot(true);

        txtApe.setFocusCycleRoot(true);

        jLabel4.setText("N° Cedula/ *");

        try {
            txtCed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###-#######-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCed.setFocusCycleRoot(true);
        txtCed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCedKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedKeyTyped(evt);
            }
        });

        cmbSex.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "////////////////////////", "M", "F" }));
        cmbSex.setFocusCycleRoot(true);

        chcEstado.setSelected(true);
        chcEstado.setText("Activo");

        jLabel2.setText("Nombre *");

        jLabel3.setText("Apellido *");

        jLabel1.setText("<html>N° Cliente</html>");

        jLabel8.setText("////////////////////////////////////");

        jLabel9.setText("Detalle Direccion *");

        jScrollPane3.setViewportView(txtDir);

        jLabel10.setText("Sexo *");

        jLabel11.setText("Telefono *");

        jdcFecNac.setDateFormatString("dd/MM/yyyy");

        try {
            txtTel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(###) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelKeyTyped(evt);
            }
        });

        txtCor.setFocusCycleRoot(true);
        txtCor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorFocusLost(evt);
            }
        });

        jLabel12.setText("Fecha de Nacimiento *");

        jLabel6.setText("Email *");

        jLabel13.setText("Estado *");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNom)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCed)
                            .addComponent(txtApe, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(cmbSex, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(179, 179, 179)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel6)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chcEstado)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdcFecNac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTel)
                    .addComponent(txtCor)
                    .addComponent(jScrollPane3))
                .addGap(63, 63, 63))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtApe, txtNom});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel12))
                    .addComponent(jdcFecNac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtCed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chcEstado)
                            .addComponent(jLabel13))
                        .addGap(0, 9, Short.MAX_VALUE))))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Mantenimiento Cliente");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
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

    private void txtCorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorFocusLost
        // TODO add your handling code here:
        // System.out.println(">>"+);
        if (!emailValidator.validate(txtCor.getText())) {
            JOptionPane.showMessageDialog(this, "Email mal introducido ");
            txtCor.requestFocus();
        }


    }//GEN-LAST:event_txtCorFocusLost

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        clearNew();

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:

            // System.out.println("formated"+txtCed.getText().trim().length());
        boolean todoBien = true;

        if (txtNom.getText().isEmpty() || txtApe.getText().isEmpty() || txtCed.getText().isEmpty() || txtCor.getText().isEmpty() || txtDir.getText().isEmpty() || txtTel.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Por favor llene todo los campos");

        } else {

            if (cmbSex.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(this, "Por favor define el Sexo");

            } else {

                //cedula length shoud be 13
                if (txtCed.getText().trim().length() < 13) {
                JOptionPane.showMessageDialog(this, "Introduce una Cedula valida");

                } else {
                ///////////////pass/////////////////
                    //check if the id is already the to update it
                    if (sql.checkidWithIdColname(tableName, idColname, rowIdData)) {
                    //System.err.println("li la deja");
                        //  String sqlQuerytercero = "UPDATE " + tableName + " SET  nomter "='" + txtDescrip.getText() + "',precio='" + txtPrecio.getText() + "' WHERE " + idColname + "=" + rowIdData;

                        //update persone
                        String sqlQueryUpdateTercero = "UPDATE `rentapelicula`.`tbtercero` SET `nomter` = '" + txtNom.getText() + "', `fecnac` = '" + sdf.format(jdcFecNac.getDate()) + "', `telter` = '" + txtTel.getText().trim() + "', `corter` = '" + txtCor.getText().trim() + "', `fecreg` = CURRENT_TIMESTAMP, `dirter` = '" + txtDir.getText().trim() + "'  WHERE `tbtercero`.`codter` = " + rowIdData + ";";
                        System.out.println("sql>>" + sqlQueryUpdateTercero);

                        if (!setTransaction(sqlQueryUpdateTercero)) {
                            todoBien = false;

                        }

                        String sqlQueryUpdatePersona = "UPDATE `rentapelicula`.`tbpersona` SET `apeper` = '" + txtApe.getText().trim() + "', `sexper` = '" + cmbSex.getSelectedItem().toString() + "', `cedper` = '" + txtCed.getText().trim() + "' WHERE `tbpersona`.`codper` = " + rowIdData + ";";
                        System.out.println("sql>>" + sqlQueryUpdatePersona);

                        if (!setTransaction(sqlQueryUpdatePersona)) {

                            todoBien = false;
                        }

                        String sqlQueryUpdateCliente = "UPDATE `rentapelicula`.`tbcliente` SET `codest` = " + chcEstado.isSelected() + " WHERE `tbcliente`.`codter` = " + rowIdData + ";";
                        System.out.println("sql>>" + sqlQueryUpdateCliente);

                        if (!setTransaction(sqlQueryUpdateCliente)) {
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
                                JOptionPane.showMessageDialog(null, "No se ha podido Guardar");

                                JOptionPane.showMessageDialog(this, "Ocurre Un error minetras actualizando ");
                                try {
                                    System.out.println("Rollback");
                                    transCon.rollback();
                                } catch (SQLException ex) {
                                    Logger.getLogger(Man_cli.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }

                        }

                    } else {
                        //save tercero
                        String sqlQuerytercero = "INSERT INTO `rentapelicula`.`tbtercero` (`codter`, `nomter`, `fecnac`, `telter` ,`corter`, `fecreg`, `dirter`) VALUES (NULL, '" + txtNom.getText().trim() + "', '" + sdf.format(jdcFecNac.getDate()) + "', '" + txtTel.getText().trim() + "','" + txtCor.getText().trim() + "', CURRENT_TIMESTAMP, '" + txtDir.getText().trim() + "');";
                        System.out.println("sql>>" + sqlQuerytercero);

                        if (!insertLeaderTransaction(sqlQuerytercero)) {

                            todoBien = false;

                        } else {
                            //save persona
                            String sqlQuerypersona = "INSERT INTO `rentapelicula`.`tbpersona` (`codper`, `apeper`, `sexper`, `cedper`) VALUES ('" + lastInsertedId + "', '" + txtApe.getText().trim() + "', '" + cmbSex.getSelectedItem().toString() + "', '" + txtCed.getText().trim() + "');";

                            if (!setTransaction(sqlQuerypersona)) {
                                todoBien = false;
                            }

                            //save cliente
                            String sqlQueryCliente = "INSERT INTO `rentapelicula`.`tbcliente` (`codter`,`codest`) VALUES ('" + lastInsertedId + "'," + chcEstado.isSelected() + ");";
                            if (!setTransaction(sqlQueryCliente)) {
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
                                    Logger.getLogger(Man_cli.class.getName()).log(Level.SEVERE, null, ex);
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
        txtBusqueda.setText("Busqueda /f3");

    }//GEN-LAST:event_txtBusquedaFocusLost

    private void txtCedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCedKeyReleased

    private void txtTelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyTyped
        // TODO add your handling code here:

        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_ENTER)
                || (c == KeyEvent.VK_PERIOD))) {
            JOptionPane.showMessageDialog(this, "Digite Solo numero en este campo");
            evt.consume();
        }
    }//GEN-LAST:event_txtTelKeyTyped

    private void txtCedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedKeyTyped
        // TODO add your handling code here:

        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_ENTER)
                || (c == KeyEvent.VK_PERIOD))) {
            JOptionPane.showMessageDialog(this, "Digite Solo numero en este campo");
            evt.consume();
        }
    }//GEN-LAST:event_txtCedKeyTyped

    private void tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_tblClienteMouseClicked

    private void tblClienteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseReleased
        // TODO add your handling code here:

        ///polulate subtitulo
        getSelectRowIdData();

    }//GEN-LAST:event_tblClienteMouseReleased

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        // TODO add your handling code here:

        populateClienteViaFilter();

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox chcEstado;
    private javax.swing.JComboBox cmbSex;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private com.toedter.calendar.JDateChooser jdcFecNac;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JTable tblCliente;
    private javax.swing.JTextField txtApe;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JFormattedTextField txtCed;
    private javax.swing.JFormattedTextField txtCor;
    private javax.swing.JTextPane txtDir;
    private javax.swing.JTextField txtNom;
    private javax.swing.JFormattedTextField txtTel;
    // End of variables declaration//GEN-END:variables

}
