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
import java.util.TimeZone;
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
import report.ClassPeliculaRentada;

import report.ClassPeliculaRetrazada;
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
public class Visual_pelicula_retrazada extends javax.swing.JInternalFrame {

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
    String orderQuery = "ORDER BY CODFAC DESC";

    // String descriptionColname = "titpel";
    /**
     * Creates new form man_peliculas
     */
    public Visual_pelicula_retrazada() {
        initComponents();
        sql = new DBSql();

        tbm = (DefaultTableModel) tblRetrazo.getModel();
        llstAddRemovePelicula = new LinkedList();

        // txtNom.setDocument(new LimitTextfield(32));
        //  txtNom.requestFocus();
        // tblPelicula.sete
        //greyOutCombobox(cmbDuracion, false);
        //  greyOutTable(tblRetrazo, false);
        populateRetrazo(orderQuery);
        btnImpCli.setEnabled(false);
    }

///////////////////////event for function keys///////////////////////
    //dont forget to add these codes
    //add this code to windowsactived event
    //KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    //add this code to windowsdesactived event
    // KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);
    //Events for Functions on form
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
        lblCodCli.setText("////////////////");
        lblNomCli.setText("////////////////");
        codCli = 0;
        rowIdData = 0;
        lastInsertedId = 0;
        dias = 0;
        tbm.setRowCount(0);
    }

    void hideColTable() {
        /* */
        tblRetrazo.getColumnModel().getColumn(8).setMinWidth(0);
        tblRetrazo.getColumnModel().getColumn(8).setMaxWidth(0);
        tblRetrazo.getColumnModel().getColumn(8).setWidth(0);

        tblRetrazo.getColumnModel().getColumn(9).setMinWidth(0);
        tblRetrazo.getColumnModel().getColumn(9).setMaxWidth(0);
        tblRetrazo.getColumnModel().getColumn(9).setWidth(0);

        tblRetrazo.getColumnModel().getColumn(10).setMinWidth(0);
        tblRetrazo.getColumnModel().getColumn(10).setMaxWidth(0);
        tblRetrazo.getColumnModel().getColumn(10).setWidth(0);

        tblRetrazo.getColumnModel().getColumn(11).setMinWidth(0);
        tblRetrazo.getColumnModel().getColumn(11).setMaxWidth(0);
        tblRetrazo.getColumnModel().getColumn(11).setWidth(0);

        tblRetrazo.getColumnModel().getColumn(12).setMinWidth(0);
        tblRetrazo.getColumnModel().getColumn(12).setMaxWidth(0);
        tblRetrazo.getColumnModel().getColumn(12).setWidth(0);

        tblRetrazo.getColumnModel().getColumn(13).setMinWidth(0);
        tblRetrazo.getColumnModel().getColumn(13).setMaxWidth(0);
        tblRetrazo.getColumnModel().getColumn(13).setWidth(0);

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

    private void populateRetrazo(String oQ) {

        String sqlQuery = "SELECT * FROM( \n"
                + "SELECT *,DATEDIFF(NOW(),vtb2.DiaVencimiento ) AS 'DIAS RETRAZADOS'  FROM (\n"
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
                + "WHERE  vtb1.DiaVencimiento < NOW()  \n"
                + ") AS vtb2 "+ oQ+") vtb3 WHERE `DIAS RETRAZADOS`>0";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblRetrazo.remove(this);
        try {

            tblRetrazo.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblRetrazo.getRowCount());

            RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tblRetrazo.getModel());

            tblRetrazo.setRowSorter(sorter);

            hideColTable();

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
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
            Logger.getLogger(Visual_pelicula_retrazada.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Visual_pelicula_retrazada.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getSelectRowIdData() {
        try {
            int selectedRow = tblRetrazo.getSelectedRow();
            int idData = Integer.parseInt((tblRetrazo.getModel().getValueAt(selectedRow, 0).toString()));
            rowIdData = idData;
            String chk = tblRetrazo.getModel().getValueAt(selectedRow, 5).toString();
            confirmStringToDelete = "";
            for (int i = 0; i < tblRetrazo.getColumnCount(); i++) {
                String strData = (tblRetrazo.getModel().getValueAt(selectedRow, i).toString()) + " ";
                //System.out.println(""+strData);
                confirmStringToDelete += strData;
            }
        } catch (Exception e) {
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblCodCli = new javax.swing.JLabel();
        lblNomCli = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblCliCedula = new javax.swing.JLabel();
        lblDirCli = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTelCli = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblCount = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnImpCli = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRetrazo = new javax.swing.JTable();
        lblInfo = new javax.swing.JLabel();
        cmbCol = new javax.swing.JComboBox();
        cmbOrder = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        btnImpTodo = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

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
        setTitle("Pelicula Retrazada");
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Del Cliente"));
        jPanel2.setFocusCycleRoot(true);

        jLabel1.setText("<html>N° Cliente:</html>");

        lblCodCli.setText("////////////////");

        lblNomCli.setText("////////////////");

        jLabel2.setText("Nombre  :");

        jLabel10.setText("Cedula :");

        lblCliCedula.setText("////////////////");

        lblDirCli.setText("////////////////");

        jLabel11.setText("Direccion :");

        lblTelCli.setText("////////////////");

        jLabel12.setText("Tel :");

        lblCount.setText("[ ]");

        jLabel13.setText("Cantidad Pel.");

        btnImpCli.setText("IMPRIMIR ESTE CLIENTE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNomCli, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCliCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDirCli, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTelCli, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCount, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnImpCli, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblCliCedula, lblCodCli, lblDirCli, lblNomCli, lblTelCli});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCodCli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblNomCli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblCliCedula))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblDirCli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblTelCli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(lblCount)
                    .addComponent(btnImpCli)))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Pelicula Retrazada");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Pelicula"));

        tblRetrazo.setAutoCreateRowSorter(true);
        tblRetrazo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblRetrazo.setGridColor(new java.awt.Color(153, 153, 153));
        tblRetrazo.getTableHeader().setReorderingAllowed(false);
        tblRetrazo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRetrazoMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblRetrazoMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblRetrazo);

        lblInfo.setText("[]");

        cmbCol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CODFAC", "DIAS RETRAZADOS" }));
        cmbCol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbColItemStateChanged(evt);
            }
        });

        cmbOrder.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DESC", "ASC" }));
        cmbOrder.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbOrderItemStateChanged(evt);
            }
        });

        jLabel3.setText("ORDENAR");

        btnImpTodo.setText("IMPRIMIR TODAS LA PELICULAS RETRAZADAS");
        btnImpTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpTodoActionPerformed(evt);
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1082, Short.MAX_VALUE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbCol, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnImpTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInfo)
                    .addComponent(cmbCol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(btnImpTodo)
                .addContainerGap())
        );

        jButton2.setText("PELICULAS RENTADAS");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        //    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);


    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
        // TODO add your handling code here:
        //   KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);

    }//GEN-LAST:event_formInternalFrameDeactivated

    private void tblRetrazoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRetrazoMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_tblRetrazoMouseClicked

    private void tblRetrazoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRetrazoMouseReleased
        // TODO add your handling code here:
        btnImpCli.setEnabled(true);
        getValues();

    }//GEN-LAST:event_tblRetrazoMouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void cmbColItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbColItemStateChanged
        // TODO add your handling code here:

        //  System.out.println(cmbCol.getSelectedItem()+" "+cmbOrder.getSelectedItem()) ;      
        //to not call twice()
        if (evt.getStateChange() == 1) {

            orderQuery = "ORDER BY `" + cmbCol.getSelectedItem() + "` " + cmbOrder.getSelectedItem();
            System.out.println(orderQuery + evt.getStateChange());
            populateRetrazo(orderQuery);
        }

    }//GEN-LAST:event_cmbColItemStateChanged

    private void cmbOrderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbOrderItemStateChanged
        // TODO add your handling code here:
        //      System.out.println(cmbCol.getSelectedItem()+" "+cmbOrder.getSelectedItem()) ;   

        //to not call twice()
        if (evt.getStateChange() == 1) {
            orderQuery = "ORDER BY `" + cmbCol.getSelectedItem() + "` " + cmbOrder.getSelectedItem();
            System.out.println(orderQuery + evt.getStateChange());
            populateRetrazo(orderQuery);
        }

    }//GEN-LAST:event_cmbOrderItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        JdVerTodasPeliculasRentadas todaspelrent = new JdVerTodasPeliculasRentadas(null, closable);
        todaspelrent.setModal(true);
        todaspelrent.setLocationRelativeTo(this);
        todaspelrent.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnImpTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpTodoActionPerformed
        // TODO add your handling code here:

        if (tblRetrazo.getRowCount() <= 0) {

            JOptionPane.showMessageDialog(this, "No hay Pelicula Retrazadas");
        }else{
        
          ClassPeliculaRetrazada pr=new ClassPeliculaRetrazada();
            //this.dispose();
            try {
                pr.generarReporte();
            } catch (JRException ex) {
                Logger.getLogger(JdVerTodasPeliculasRentadas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(JdVerTodasPeliculasRentadas.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }


    }//GEN-LAST:event_btnImpTodoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnImpCli;
    private javax.swing.JButton btnImpTodo;
    private javax.swing.JComboBox cmbCol;
    private javax.swing.JComboBox cmbOrder;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu jpopDeletePelicula;
    public static javax.swing.JLabel lblCliCedula;
    public static javax.swing.JLabel lblCodCli;
    public static javax.swing.JLabel lblCount;
    public static javax.swing.JLabel lblDirCli;
    private javax.swing.JLabel lblInfo;
    public static javax.swing.JLabel lblNomCli;
    public static javax.swing.JLabel lblTelCli;
    private javax.swing.JTable tblRetrazo;
    // End of variables declaration//GEN-END:variables

    private void getValues() {

        int selectedRow = tblRetrazo.getSelectedRow();
        if (tblRetrazo.getSelectedRow() > -1) {
            try {
                lblCodCli.setText(tblRetrazo.getValueAt(selectedRow, 8).toString());
                lblNomCli.setText(tblRetrazo.getValueAt(selectedRow, 1).toString());
                lblCliCedula.setText(tblRetrazo.getValueAt(selectedRow, 9).toString());
                lblDirCli.setText(tblRetrazo.getValueAt(selectedRow, 10).toString());
                lblTelCli.setText(tblRetrazo.getValueAt(selectedRow, 11).toString());

                String sqlQueryCantidad = "SELECT COUNT(*) AS Cantidad FROM\n"
                        + "(SELECT *,DATEDIFF(NOW(),vtb2.DiaVencimiento ) AS 'DIAS RETRAZADOS'  FROM (\n"
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
                        + "WHERE  vtb1.DiaVencimiento < NOW() \n"
                        + ") AS vtb2 WHERE vtb2.codcli=" + tblRetrazo.getValueAt(selectedRow, 8).toString() + " ORDER BY  `DIAS RETRAZADOS` desc) AS vtb3";

                System.out.println("sql;;;;" + sqlQueryCantidad);
                lblCount.setText(sql.oneValue(sqlQueryCantidad));

            } catch (Exception ex) {
                Logger.getLogger(Visual_pelicula_retrazada.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
