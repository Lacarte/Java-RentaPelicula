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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import static principal.Man_pel.llstAddRemoveActor;

import report.ClassRenta;
import utilities.Clock;
import utilities.ConnectionManager;
import utilities.DBSql;
import utilities.EmailValidator;
import utilities.ObjPeliculaCopelNumcopia;
import utilities.ResultsetTable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LCRT
 */
public class Tra_dev extends javax.swing.JInternalFrame {

    String fecha = "01/01/1990";
    java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.util.Date fechaDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static Connection transCon = ConnectionManager.getInstance().getConnection();
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
    double itbis = 0;
    String confirmStringToDelete = null;
    String tableName = "tbtercero";
    String idColname = "codter";

    // String descriptionColname = "titpel";
    /**
     * Creates new form man_peliculas
     */
    public Tra_dev() {
        initComponents();
        sql = new DBSql();
        btnBusca.setVisible(false);
        // clock(lblDateTime);

        tbm = (DefaultTableModel) tblDevolucion.getModel();
        llstAddRemovePelicula = new LinkedList();

        // txtNom.setDocument(new LimitTextfield(32));
        //  txtNom.requestFocus();
        // tblPelicula.sete
        //greyOutCombobox(cmbDuracion, false);
        selectedTable(tblDevolucion, false);
        //  lblUsuario.setText(Principal.userName);
        tblDevolucion.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent tme) {
                lblInfo.setText("Ctd " + tblDevolucion.getRowCount());
                //System.out.println(">> "+tme  );
                double subTotalPrecio = 0;
                for (int i = 0; i < tblDevolucion.getRowCount(); i++) {
                    subTotalPrecio += Double.parseDouble((String) tblDevolucion.getValueAt(i, 5));
                }

                //   lblSubTotal.setText("" + roundOff(subTotalPrecio));
                itbis = (subTotalPrecio * 18) / 100;

                //   lblItbis.setText("" + roundOff(itbis));
                // lblTotal.setText("" + roundOff(itbis + subTotalPrecio));
                ///// add remove codpel numcopia
                addRemovePeliculaToLinkedlist();
            }
        });

        tglbtnSelTod.setEnabled(false);
        btnDevolver.setEnabled(false);

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
                        //      btnNuevo.doClick();
                        break;
                    case KeyEvent.VK_F5:
                        System.out.println("Guardar");
                        btnDevolver.doClick();
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

    double roundOff(double num) {
        double roundOffSubtotal = Math.round(num * 100) / 100D;
        return roundOffSubtotal;
    }

    //  txtBusqueda.setText("Busqueda /f3");
    ///////////////////////clear all
    private void clearNew() {
        llstAddRemovePelicula.clear();
        tableName = "tbcliente";
        lblCliCedula.setText("////////////////");
        lblCliCode.setText("////////////////");
        lblCliName.setText("////////////////");

        //  lblSubTotal.setText("0.00");
        codCli = 0;
        rowIdData = 0;
        lastInsertedId = 0;
        dias = 0;
        tbm.setRowCount(0);
    }

    void hideColTable() {
        /* */
        tblDevolucion.getColumnModel().getColumn(8).setMinWidth(0);
        tblDevolucion.getColumnModel().getColumn(8).setMaxWidth(0);
        tblDevolucion.getColumnModel().getColumn(8).setWidth(0);

        tblDevolucion.getColumnModel().getColumn(9).setMinWidth(0);
        tblDevolucion.getColumnModel().getColumn(9).setMaxWidth(0);
        tblDevolucion.getColumnModel().getColumn(9).setWidth(0);

        tblDevolucion.getColumnModel().getColumn(10).setMinWidth(0);
        tblDevolucion.getColumnModel().getColumn(10).setMaxWidth(0);
        tblDevolucion.getColumnModel().getColumn(10).setWidth(0);

        tblDevolucion.getColumnModel().getColumn(11).setMinWidth(0);
        tblDevolucion.getColumnModel().getColumn(11).setMaxWidth(0);
        tblDevolucion.getColumnModel().getColumn(11).setWidth(0);

        tblDevolucion.getColumnModel().getColumn(12).setMinWidth(0);
        tblDevolucion.getColumnModel().getColumn(12).setMaxWidth(0);
        tblDevolucion.getColumnModel().getColumn(12).setWidth(0);

        tblDevolucion.getColumnModel().getColumn(13).setMinWidth(0);
        tblDevolucion.getColumnModel().getColumn(13).setMaxWidth(0);
        tblDevolucion.getColumnModel().getColumn(13).setWidth(0);

    }

    void greyOutCombobox(JComboBox cmb, boolean state) {
        cmb.setEditable(state);
        cmb.setEnabled(state);
    }

    void selectedTable(JTable tbl, boolean state) {
        tbl.clearSelection();
        if (state) {
            tbl.setBackground(new java.awt.Color(51, 153, 255));
            tbl.setEnabled(state);
        } else {
            tbl.setBackground(new java.awt.Color(255, 255, 255));
            //tbl.setEnabled(state);
        }
        //tbl.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(true);
        tbl.setGridColor(new java.awt.Color(153, 153, 153));

    }

    private void populateDevolucion(String codcli) {

        String sqlQuery = "SELECT *,DATEDIFF(NOW(),vtb2.DiaVencimiento ) AS 'DIAS RETRAZADOS'  FROM (\n"
                + "SELECT * FROM \n"
                + "(\n"
                + "SELECT ren.codren AS CODFAC,CONCAT(per.apeper,' ',ter.nomter) As NOMBRE ,pel.titpel AS TITULO,detren.numcopia AS 'NUMCOPIA',ren.fecren AS `FECHA RENTA`,detren.durren AS DIAS ,(ren.fecren + INTERVAL detren.durren DAY) AS DiaVencimiento ,now() As HOY,ren.codcli,per.cedper,ter.dirter,ter.telter,detren.codpel,estpel.desest FROM tbrenta ren INNER JOIN\n"
                + "tb_detalle_renta detren ON ren.codren=detren.codren\n"
                + "INNER JOIN tbtercero ter ON ren.codcli=ter.codter\n"
                + "INNER JOIN tbpersona per ON ter.codter=per.codper\n"
                + "INNER JOIN tbpelicula pel ON pel.codpel=detren.codpel\n"
                + "INNER JOIN tbpelicula_copia pelcop ON detren.numcopia=pelcop.numcopia AND detren.codpel=pelcop.codpel\n"
                + "INNER JOIN tb_estado_pelicula estpel ON pelcop.codestado=estpel.codestpel\n"
                + "WHERE detren.entregada=0\n"
                + ") as vtb1\n"
                + ") AS vtb2 WHERE vtb2.codcli=" + codcli + " ORDER BY  `DIAS RETRAZADOS` desc";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);
        ResultsetTable rst = new ResultsetTable();
        tblDevolucion.remove(this);
        try {
            tblDevolucion.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblDevolucion.getRowCount());
            hideColTable();

            if (tblDevolucion.getRowCount() <= 0) {
                tglbtnSelTod.setEnabled(false);
                btnDevolver.setEnabled(false);
            } else {

                tglbtnSelTod.setEnabled(true);
                btnDevolver.setEnabled(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Tra_dev.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void callReport(int idRenta) {

        System.out.println("report");

        ClassRenta rc = new ClassRenta();
        try {
            rc.generarReporte(idRenta);
            System.out.println("lastIdRepor" + idRenta);
        } catch (JRException ex) {
            Logger.getLogger(Tra_dev.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Tra_dev.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void AddRowToModel(int dias, int codpel, int numcopia) {
        System.out.println("boom added>>>" + Tra_dev.dias + " " + codpel + " " + numcopia);
        String sqlCallProcedure = "CALL proc_pel_precio(" + dias + "," + codpel + "," + numcopia + ")";
        try {
            Statement sta = transCon.createStatement(); // ......................ANLE A.....
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
        for (int i = 0; i < tblDevolucion.getRowCount(); i++) {
            llstAddRemovePelicula.add(new ObjPeliculaCopelNumcopia(Integer.parseInt((String) tbm.getValueAt(i, 0)), Integer.parseInt((String) tbm.getValueAt(i, 2))));
        }

    }

    public void getSelectRowIdData() {
        try {
            int selectedRow = tblDevolucion.getSelectedRow();
            int idData = Integer.parseInt((tblDevolucion.getModel().getValueAt(selectedRow, 0).toString()));
            rowIdData = idData;
            String chk = tblDevolucion.getModel().getValueAt(selectedRow, 5).toString();
            confirmStringToDelete = "";
            for (int i = 0; i < tblDevolucion.getColumnCount(); i++) {
                String strData = (tblDevolucion.getModel().getValueAt(selectedRow, i).toString()) + " ";
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
                //   System.out.println("LAst ID<<>>" + lastInsertedId);
                // transCon.commit();
            } else {
                success = false;
            }
        } catch (SQLException ex) {
            System.err.println("excep >>" + ex);
            success = false;
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

    private void devolver() {

        boolean todoBien = true;

        if (tglbtnSelTod.isSelected()) {
            for (int i = 0; i < tblDevolucion.getRowCount(); i++) {
                System.out.println("fac" + tblDevolucion.getValueAt(i, 0) + " codpel " + tblDevolucion.getValueAt(i, 12) + " numcopia " + tblDevolucion.getValueAt(i, 3));
               
                String sqlQueryUpdateEntregada = "UPDATE `tb_detalle_renta` SET `entregada`='1' WHERE (`codren`='"+tblDevolucion.getValueAt(i, 0)+"' AND  `codpel`='" + tblDevolucion.getValueAt(i, 12) + "' AND `numcopia`='" + tblDevolucion.getValueAt(i, 3) + "')";
                String sqlQueryUpdateEstado = "UPDATE `tbpelicula_copia` SET `codestado`='1' WHERE (`codpel`='" + tblDevolucion.getValueAt(i, 12) + "' AND `numcopia`='" + tblDevolucion.getValueAt(i, 3) + "')";

                if (!setTransaction(sqlQueryUpdateEntregada)) {
                    todoBien = false;
                    System.out.println("<<>>" + sqlQueryUpdateEntregada);

                }

                if (!setTransaction(sqlQueryUpdateEstado)) {
                    todoBien = false;
                    System.out.println("<<>>" + sqlQueryUpdateEstado);
                }

            }

            //if all the transactions are ok commit
            if (todoBien) {
                ///Update estado pelicula
                try {
                    transCon.commit();
                    System.out.println("commit");

                    populateDevolucion(lblCliCode.getText());
                    JOptionPane.showMessageDialog(this, "Devolucion exitosa");
                    clearNew();
                } catch (SQLException ex) {
                    Logger.getLogger(Tra_dev.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                try {
                    transCon.rollback();
                    System.out.println("roll back ");
                    JOptionPane.showMessageDialog(this, "occure Un error");

                } catch (SQLException ex) {
                    Logger.getLogger(Tra_dev.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        tblDevolucion = new javax.swing.JTable();
        lblInfo = new javax.swing.JLabel();
        lblInfo2 = new javax.swing.JLabel();
        tglbtnSelTod = new javax.swing.JToggleButton();
        btnDevolver = new javax.swing.JButton();
        btnBusca = new javax.swing.JButton();

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
        setTitle("Devolucion");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
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
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCliCode, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                    .addComponent(lblCliName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCliCedula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblCliCedula)
                    .addComponent(jButton1))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Devolucion");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Info Renta"));

        tblDevolucion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODREN", "CODPEL", "TITULO PELICULA", "Nro COPIA", "DIAS", "FECHA DEVOLUCION"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDevolucion.setGridColor(new java.awt.Color(153, 153, 153));
        tblDevolucion.getTableHeader().setReorderingAllowed(false);
        tblDevolucion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDevolucionMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDevolucionMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblDevolucion);

        lblInfo.setText("[]");

        lblInfo2.setText("\\\\");

            tglbtnSelTod.setText("SELECCIONAR TODAS");
            tglbtnSelTod.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tglbtnSelTodActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblInfo2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tglbtnSelTod, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1101, Short.MAX_VALUE))
                    .addGap(5, 5, 5))
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblInfo)
                        .addComponent(lblInfo2)
                        .addComponent(tglbtnSelTod))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addContainerGap())
            );

            btnDevolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/save8.png"))); // NOI18N
            btnDevolver.setText("Devolver /f5");
            btnDevolver.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnDevolverActionPerformed(evt);
                }
            });

            btnBusca.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnBuscaActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(btnBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDevolver, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(25, 25, 25))
                .addGroup(layout.createSequentialGroup()
                    .addGap(533, 533, 533)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(3, 3, 3)
                            .addComponent(btnDevolver, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(44, 44, 44)
                            .addComponent(btnBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnDevolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolverActionPerformed
        // TODO add your handling code here:

        String ObjButtons[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(this, "Desea Devolver? ", "Confirmacion...?", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[0]);

        if (PromptResult == JOptionPane.YES_OPTION) {
            devolver();
        }
    }//GEN-LAST:event_btnDevolverActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);


    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);

    }//GEN-LAST:event_formInternalFrameDeactivated

    private void tblDevolucionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDevolucionMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_tblDevolucionMouseClicked

    private void tblDevolucionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDevolucionMouseReleased
        // TODO add your handling code here:

        ///polulate subtitulo
        getSelectRowIdData();

        //PopUp
        if (evt.isPopupTrigger()) {

            if (tblDevolucion.getRowCount() > 0 && tblDevolucion.getSelectedRow() != -1) {
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

    }//GEN-LAST:event_tblDevolucionMouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel tm = (DefaultTableModel) (tblDevolucion.getModel());
        tm.removeRow(tblDevolucion.getSelectedRow());

        addRemovePeliculaToLinkedlist();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        JdTodoLosClientesDevolucion todocliDevolucion = new JdTodoLosClientesDevolucion(null, closable);
        todocliDevolucion.setModal(true);
        todocliDevolucion.setLocationRelativeTo(this);
        todocliDevolucion.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaActionPerformed
        // TODO add your handling code here:
        populateDevolucion(lblCliCode.getText());
    }//GEN-LAST:event_btnBuscaActionPerformed

    private void tglbtnSelTodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnSelTodActionPerformed

        if (tglbtnSelTod.isSelected()) {
            selectedTable(tblDevolucion, true);
        } else {
            selectedTable(tblDevolucion, false);

        }
    }//GEN-LAST:event_tglbtnSelTodActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnBusca;
    private javax.swing.JButton btnDevolver;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu jpopDeletePelicula;
    public static javax.swing.JLabel lblCliCedula;
    public static javax.swing.JLabel lblCliCode;
    public static javax.swing.JLabel lblCliName;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblInfo2;
    private javax.swing.JTable tblDevolucion;
    private javax.swing.JToggleButton tglbtnSelTod;
    // End of variables declaration//GEN-END:variables

}
