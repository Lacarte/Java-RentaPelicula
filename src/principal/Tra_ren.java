package principal;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import utilities.Clock;
import utilities.ConnectionManager;
import utilities.DBSql;
import utilities.EmailValidator;
import utilities.ObjPeliculaCopelNumcopia;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LCRT
 */
public class Tra_ren extends javax.swing.JInternalFrame {

    String fecha = "01/01/1990";
    java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.util.Date fechaDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static Connection con = ConnectionManager.getInstance().getConnection();
    private static DefaultTableModel tbm = null;
    static LinkedList llstAddRemovePelicula = null;

/////////
    EmailValidator emailValidator = new EmailValidator();

    DBSql sql = null;
    int progVal = 0;
    public static int dias = 0;
    public static int codCli = 0;
    int rowIdData = 0;
    int lastInsertedId = 0;
    String confirmStringToDelete = null;
    String tableName = "tbtercero";
    String idColname = "codter";

    // String descriptionColname = "titpel";
    /**
     * Creates new form man_peliculas
     */
    public Tra_ren() {
        initComponents();
        sql = new DBSql();
        clock(lblDateTime);

        tbm = (DefaultTableModel) tblPelicula.getModel();
        llstAddRemovePelicula = new LinkedList();

        // txtNom.setDocument(new LimitTextfield(32));
        //  txtNom.requestFocus();
        populateFillDiasDuracion();
        // tblPelicula.sete

        //greyOutCombobox(cmbDuracion, false);
        greyOutTable(tblPelicula, false);
        lblUsuario.setText(Principal.userName);
        tblPelicula.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent tme) {
                lblInfo.setText("Ctd" + tblPelicula.getRowCount());
                //System.out.println(">> "+tme  );
                double totalPrecio = 0;
                for (int i = 0; i < tblPelicula.getRowCount(); i++) {
                    totalPrecio += Double.parseDouble((String) tblPelicula.getValueAt(i, 5));
                }
                double roundOff = Math.round(totalPrecio * 100) / 100D;
                lblTotal.setText("" + roundOff);

                ///// add remove codpel numcopia
                addRemovePeliculaToLinkedlist();
            }
        });

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
                        // txtBusqueda.setText(null);
                        // txtBusqueda.requestFocus();
                        break;
                    case KeyEvent.VK_F4:
                        System.out.println("nuevo");
                        btnNuevo.doClick();
                        break;
                    case KeyEvent.VK_F5:
                        System.out.println("Guardar");
                        btnFacturar.doClick();
                        break;
                    case KeyEvent.VK_F6:
                        System.out.println("Eliminar");
                        // btnEliminar.doClick();
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

    ////////////////// clock/////////////////////
    public void clock(JLabel jb) {
        Clock c1 = new Clock(jb);
        Thread t1 = new Thread(c1);
        t1.start();
    }

    //  txtBusqueda.setText("Busqueda /f3");
    ///////////////////////clear all
    private void clearNew() {
        llstAddRemovePelicula.clear();
        tableName = "tbcliente";
        lblCliCedula.setText("////////////////");
        lblCliCode.setText("////////////////");
        lblCliName.setText("////////////////");
        cmbDuracion.setSelectedIndex(0);
        lblDuracion.setText("[//]");
        lblTotal.setText("0.00");
        codCli = 0;
        rowIdData = 0;
        lastInsertedId = 0;
        dias = 0;
        tbm.setRowCount(0);
    }

    void hideColTable() {

        tblPelicula.getColumnModel().getColumn(7).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(7).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(7).setWidth(0);

        tblPelicula.getColumnModel().getColumn(8).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(8).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(8).setWidth(0);

        tblPelicula.getColumnModel().getColumn(9).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(9).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(9).setWidth(0);

    }

    void greyOutCombobox(JComboBox cmb, boolean state) {
        cmb.setEditable(state);
        cmb.setEnabled(state);
    }

    void greyOutTable(JTable tbl, boolean state) {
        tbl.clearSelection();
        if (state) {
            tbl.setBackground(new java.awt.Color(255, 255, 255));
            tbl.setEnabled(state);
        } else {
            tbl.setBackground(new java.awt.Color(204, 204, 204));
            tbl.setEnabled(state);
        }
        //tbl.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(true);
        tbl.setGridColor(new java.awt.Color(153, 153, 153));

    }

    public static void AddRowToModel(int dias, int codpel, int numcopia) {
        System.out.println("boom added>>>" + Tra_ren.dias + " " + codpel + " " + numcopia);
        String sqlCallProcedure = "CALL proc_pel_precio(" + dias + "," + codpel + "," + numcopia + ")";
        try {
            Statement sta = con.createStatement(); // ......................ANLE A.....
            ResultSet rs = sta.executeQuery(sqlCallProcedure);
            while (rs.next()) {
                tbm.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)});
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public void addRemovePeliculaToLinkedlist() {
        llstAddRemovePelicula.clear();
        for (int i = 0; i < tblPelicula.getRowCount(); i++) {
            llstAddRemovePelicula.add(new ObjPeliculaCopelNumcopia(Integer.parseInt((String) tbm.getValueAt(i, 0)), Integer.parseInt((String) tbm.getValueAt(i, 2))));
        }

    }

    public void populateFillDiasDuracion() {
        cmbDuracion.removeAllItems();
        cmbDuracion.addItem(new String("//////////////////////////"));
        int maxDuracion = 30;
        for (int i = 1; i <= maxDuracion; i++) {

            cmbDuracion.addItem(new Integer(i));
        }
    }

    public void getSelectRowIdData() {
        try {
            int selectedRow = tblPelicula.getSelectedRow();
            int idData = Integer.parseInt((tblPelicula.getModel().getValueAt(selectedRow, 0).toString()));
            rowIdData = idData;
            String chk = tblPelicula.getModel().getValueAt(selectedRow, 5).toString();
            confirmStringToDelete = "";
            for (int i = 0; i < tblPelicula.getColumnCount(); i++) {
                String strData = (tblPelicula.getModel().getValueAt(selectedRow, i).toString()) + " ";
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
            con.setAutoCommit(false);
            String sql = query;
            Statement sta = con.createStatement();
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
                //   System.out.println("LAst ID<<>>" + lastInsertedId);
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
            con.setAutoCommit(false);
            String sql = query;
            Statement sta = con.createStatement();
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

    private void facturar() {
        System.out.println("Facturacion");
        if (Principal.userCode == null) {
            System.out.println("Please Select  a user");
        }
        if (codCli == 0) {
            JOptionPane.showMessageDialog(this, "Por favor Selecione un Cliente");

        } else {
            if (tblPelicula.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No se puede facturar sin Pelicula");
            }
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

        jpopDeletePelicula = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        btnFacturar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblCliCode = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblCliName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblCliCedula = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPelicula = new javax.swing.JTable();
        lblInfo = new javax.swing.JLabel();
        btnBuscarPelicula = new javax.swing.JButton();
        cmbDuracion = new javax.swing.JComboBox();
        lblDuracion = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblDateTime = new javax.swing.JLabel();

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/Entypo_2796(58)_24.png"))); // NOI18N
        jMenuItem1.setText("Eliminar Pelicula");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jpopDeletePelicula.add(jMenuItem1);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Renta");
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

        btnFacturar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/save8.png"))); // NOI18N
        btnFacturar.setText("Facturar /f5");
        btnFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/limpiar.png"))); // NOI18N
        btnNuevo.setText("NUEVO  /f4");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnFacturar, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
            .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Del Cliente"));
        jPanel2.setFocusCycleRoot(true);

        jLabel1.setText("<html>N° Cliente:</html>");

        lblCliCode.setText("////////////////");

        jButton1.setText("Buscar Cliente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblCliName.setText("////////////////");

        jLabel2.setText("Nombre  :");

        jLabel10.setText("Cedula :");

        lblCliCedula.setText("////////////////");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCliCode, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(lblCliName, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(lblCliCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addGap(219, 219, 219))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCliCode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblCliName))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblCliCedula))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jButton1))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Renta");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Pelicula"));

        tblPelicula.setBackground(new java.awt.Color(204, 204, 204));
        tblPelicula.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODPEL", "TITULO PELICULA", "Nro COPIA", "DIAS", "PRECIOXDIA", "PRECIO", "FECHA DEVOLUCION"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPelicula.setGridColor(new java.awt.Color(153, 153, 153));
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

        lblInfo.setText("[]");

        btnBuscarPelicula.setText("Buscar Pelicula");
        btnBuscarPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPeliculaActionPerformed(evt);
            }
        });

        cmbDuracion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDuracion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDuracionItemStateChanged(evt);
            }
        });
        cmbDuracion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbDuracionFocusLost(evt);
            }
        });
        cmbDuracion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbDuracionMouseClicked(evt);
            }
        });

        lblDuracion.setText("[//]");

        jLabel6.setText("Dias(duracion)");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDuracion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(221, 221, 221)
                .addComponent(btnBuscarPelicula)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jScrollPane1)
                            .addGap(5, 5, 5))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(815, Short.MAX_VALUE)))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarPelicula)
                    .addComponent(cmbDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDuracion)
                    .addComponent(jLabel6))
                .addGap(0, 189, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblInfo)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Precio"));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("TOTAL : $RD ");

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotal.setText("0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblTotal))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jLabel4.setText("Usuario :");

        lblUsuario.setText("lblUsuario");

        lblDateTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDateTime.setText("[]");
        lblDateTime.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 376, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDateTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUsuario)))
                        .addGap(31, 31, 31))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDateTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(lblUsuario))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
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
        String ObjButtons[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(this, "Desea Limpiar Todo? ", "Confirmacion...?", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[0]);

        if (PromptResult == JOptionPane.YES_OPTION) {
            clearNew();
        }

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturarActionPerformed
        // TODO add your handling code here:

        String ObjButtons[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(this, "Desea Facturar? ", "Confirmacion...?", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[0]);

        if (PromptResult == JOptionPane.YES_OPTION) {
            facturar();
        }
    }//GEN-LAST:event_btnFacturarActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);


    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);

    }//GEN-LAST:event_formInternalFrameDeactivated

    private void tblPeliculaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPeliculaMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_tblPeliculaMouseClicked

    private void tblPeliculaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPeliculaMouseReleased
        // TODO add your handling code here:

        ///polulate subtitulo
        getSelectRowIdData();

        //PopUp
        if (evt.isPopupTrigger()) {

            if (tblPelicula.getRowCount() > 0 && tblPelicula.getSelectedRow() != -1) {
                jpopDeletePelicula.show(evt.getComponent(), evt.getX(), evt.getY());
            }

            // jlSutitulo source=(jlSutitulo)evt.getSource();
            // jl source = (JdSubtitulo)evt.getSource();
            //  int row = source.rowAtPoint( e.getPoint() );
            //  int column = source.columnAtPoint( e.getPoint() );
            //  if (! source.isRowSelected(row))
            //    source.changeSelection(row, column, false, false);
            // popup.show(e.getComponent(), e.getX(), e.getY());
            // tblPelicula.re(tblPelicula.getSelectedRow());
        }

    }//GEN-LAST:event_tblPeliculaMouseReleased

    private void cmbDuracionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbDuracionMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() >= 2) {
            greyOutCombobox(cmbDuracion, true);
        }
    }//GEN-LAST:event_cmbDuracionMouseClicked

    private void cmbDuracionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbDuracionFocusLost
        // TODO add your handling code here:
        greyOutCombobox(cmbDuracion, false);
        // System.out.println("lost focus");
    }//GEN-LAST:event_cmbDuracionFocusLost

    private void cmbDuracionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDuracionItemStateChanged
        // TODO add your handling code here:
        System.out.println("itemchanged");
        greyOutCombobox(cmbDuracion, false);

        if (cmbDuracion.getSelectedIndex() == 0) {
            dias = 0;
            greyOutTable(tblPelicula, false);
            lblDuracion.setText("[//]");
            btnBuscarPelicula.setEnabled(false);

        } else {

            greyOutTable(tblPelicula, true);
            //  System.out.println(">>x" + cmbDuracion.getItemAt(cmbDuracion.getSelectedIndex()));
            lblDuracion.setText("" + cmbDuracion.getItemAt(cmbDuracion.getSelectedIndex()));
            btnBuscarPelicula.setEnabled(true);
            // dias=(int) cmbDuracion.getItemAt(cmbDuracion.getSelectedIndex());
            //System.out.println(">>" + cmbDuracion.getItemAt(cmbDuracion.getSelectedIndex()));
            if (cmbDuracion.getSelectedItem() == null) {
                //  System.out.println("li null");
                dias = 0;
            } else {
                dias = (int) cmbDuracion.getSelectedItem();
                //  System.out.println("li pa null");
            }

        }


    }//GEN-LAST:event_cmbDuracionItemStateChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel tm = (DefaultTableModel) (tblPelicula.getModel());
        tm.removeRow(tblPelicula.getSelectedRow());

        addRemovePeliculaToLinkedlist();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        JdTodoLosClientesRenta todoclirenta = new JdTodoLosClientesRenta(null, closable);
        todoclirenta.setModal(true);
        todoclirenta.setLocationRelativeTo(this);
        todoclirenta.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnBuscarPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPeliculaActionPerformed
        // TODO add your handling code here:
        JdTodaLasPeliculasRenta pel = new JdTodaLasPeliculasRenta(null, closable);
        pel.setModal(true);
        pel.setLocationRelativeTo(this);
        pel.setVisible(true);

    }//GEN-LAST:event_btnBuscarPeliculaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPelicula;
    private javax.swing.JButton btnFacturar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox cmbDuracion;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu jpopDeletePelicula;
    public static javax.swing.JLabel lblCliCedula;
    public static javax.swing.JLabel lblCliCode;
    public static javax.swing.JLabel lblCliName;
    private javax.swing.JLabel lblDateTime;
    private javax.swing.JLabel lblDuracion;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTable tblPelicula;
    // End of variables declaration//GEN-END:variables

}
