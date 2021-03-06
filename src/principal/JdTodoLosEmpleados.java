package principal;

import utilities.DBSql;
import utilities.DataJlist;
import utilities.LimitTextfield;
import utilities.ResultsetTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
    //don't forget to add this code in windos active to repopulate the subtitle tale
//populateIdioma(tableName);
public class JdTodoLosEmpleados extends javax.swing.JDialog {

    DBSql sql = null;
    String tableName = "tbtercero";
    String idColname = "codter";
    String confirmStringToDelete = null;



    int progVal = 1;
    int rowIdData = 0;

    public JdTodoLosEmpleados(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sql = new DBSql();

          int progVal = 1;
        txtBusqueda.requestFocus();
        populateEmpleado();
        jLabel2.setVisible(false);
        txtBusqueda.setDocument(new LimitTextfield(24));

    }

    public void populateEmpleado() {

        String sqlQuery = "SELECT * FROM (SELECT teremp.codter AS CODEMP,teremp.nomter AS NOMBRE, per.apeper  AS APELLIDO,per.cedper AS CEDULA, teremp.telter AS TEL,emp.codest AS ACTIVO,teremp.corter AS CORREO,teremp.dirter,sexper,teremp.fecnac,su.dessuc FROM (SELECT * FROM tbtercero ter WHERE codter IN(SELECT codter FROM tbempleado)) AS teremp INNER JOIN tbpersona per ON teremp.codter=per.codper INNER JOIN tbempleado emp ON emp.codemp=teremp.codter INNER JOIN tbsucursal su  ON su.codsuc=emp.codsuc)AS vtb ORDER BY CODEMP DESC";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblEmpleado.remove(this);
        try {
            tblEmpleado.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblEmpleado.getRowCount());

            hideColTable();

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    void hideColTable() {

        
                tblEmpleado.getColumnModel().getColumn(4).setMinWidth(0);
        tblEmpleado.getColumnModel().getColumn(4).setMaxWidth(0);
        tblEmpleado.getColumnModel().getColumn(4).setWidth(0);

        tblEmpleado.getColumnModel().getColumn(5).setMinWidth(0);
        tblEmpleado.getColumnModel().getColumn(5).setMaxWidth(0);
        tblEmpleado.getColumnModel().getColumn(5).setWidth(0);

        tblEmpleado.getColumnModel().getColumn(6).setMinWidth(0);
        tblEmpleado.getColumnModel().getColumn(6).setMaxWidth(0);
        tblEmpleado.getColumnModel().getColumn(6).setWidth(0);
        
        tblEmpleado.getColumnModel().getColumn(7).setMinWidth(0);
        tblEmpleado.getColumnModel().getColumn(7).setMaxWidth(0);
        tblEmpleado.getColumnModel().getColumn(7).setWidth(0);

        tblEmpleado.getColumnModel().getColumn(8).setMinWidth(0);
        tblEmpleado.getColumnModel().getColumn(8).setMaxWidth(0);
        tblEmpleado.getColumnModel().getColumn(8).setWidth(0);

        tblEmpleado.getColumnModel().getColumn(9).setMinWidth(0);
        tblEmpleado.getColumnModel().getColumn(9).setMaxWidth(0);
        tblEmpleado.getColumnModel().getColumn(9).setWidth(0);

    }

    public void populateEmpleadoViaFilter() {

        String sqlQuery = "SELECT * FROM (SELECT teremp.codter AS CODEMP,teremp.nomter AS NOMBRE, per.apeper  AS APELLIDO,per.cedper AS CEDULA, teremp.telter AS TEL,emp.codest AS ACTIVO,teremp.corter AS CORREO,teremp.dirter,sexper,teremp.fecnac,su.dessuc FROM (SELECT * FROM tbtercero ter WHERE codter IN(SELECT codter FROM tbempleado)) AS teremp INNER JOIN tbpersona per ON teremp.codter=per.codper INNER JOIN tbempleado emp ON emp.codemp=teremp.codter INNER JOIN tbsucursal su  ON su.codsuc=emp.codsuc)AS vtb WHERE cedula LIKE '%" + txtBusqueda.getText().trim() + "%'  ORDER BY CODEMP DESC";

        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblEmpleado.remove(this);
        try {
            tblEmpleado.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblEmpleado.getRowCount());
            hideColTable();

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getSelectRowIdData() {
        try {
            int selectedRow = tblEmpleado.getSelectedRow();

            int idData = Integer.parseInt((tblEmpleado.getModel().getValueAt(selectedRow, 0).toString()));

            rowIdData = idData;

            /////////////////////////////set combobox////////////////////////
            //selectedValueCombobox(cmbGenero, tblPelicula.getModel().getValueAt(selectedRow, 2));
            /////////////////for update//////////////////       
            ////////mete string lan nan textfield la///////////////
            Man_emp.txtNom.setText(tblEmpleado.getModel().getValueAt(selectedRow, 1).toString());
            Man_emp.txtApe.setText(tblEmpleado.getModel().getValueAt(selectedRow, 2).toString());
            Man_emp.txtCed.setText(tblEmpleado.getModel().getValueAt(selectedRow, 3).toString());
            Man_emp.txtTel.setText(tblEmpleado.getModel().getValueAt(selectedRow, 4).toString());

            Man_emp.txtCor.setText(tblEmpleado.getModel().getValueAt(selectedRow, 6).toString());

            Man_emp.txtDir.setText(tblEmpleado.getModel().getValueAt(selectedRow, 7).toString());

            Man_emp.cmbSex.setSelectedItem(tblEmpleado.getModel().getValueAt(selectedRow, 8).toString());

            String chk = tblEmpleado.getModel().getValueAt(selectedRow, 5).toString();

            //checkbox
            if (chk.equalsIgnoreCase("1")) {
                Man_emp.chcEstado.setSelected(true);
            } else {
                Man_emp.chcEstado.setSelected(false);
            }

            ///Date convertion
            String inputDateStr = tblEmpleado.getModel().getValueAt(selectedRow, 9).toString();
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/YYYY");
            ///////////////////////////
            Date date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);

            //System.out.println(">>++"+outputDateStr);
            //add outputDateStr  to format and convert it to date 
            Man_emp.jdcFecNac.setDate((Date) new SimpleDateFormat("dd/MM/yyyy").parse(outputDateStr));

            //fecha
            //  txtCed.setYear(Integer.parseInt((String) tblPelicula.getModel().getValueAt(selectedRow, 4)));
            ///////////////////pran tout string ki nan row la///////////////////
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

        timer1 = new org.netbeans.examples.lib.timerbean.Timer();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmpleado = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        lblInfo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();

        timer1.setDelay(2L);
        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Empleados");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        tblEmpleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        tblEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblEmpleadoMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmpleado);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Empleados");

        txtBusqueda.setText("Buscar por #Cedula");
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

        lblInfo.setText("jLabel2");

        jProgressBar1.setBackground(new java.awt.Color(255, 255, 255));
        jProgressBar1.setForeground(new java.awt.Color(0, 102, 255));
        jProgressBar1.setBorderPainted(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBusqueda))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(lblInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        // TODO add your handling code here:
        txtBusqueda.setText(txtBusqueda.getText().trim());
        populateEmpleadoViaFilter();
        if (txtBusqueda.getText().isEmpty()) {
            populateEmpleado();
        }
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void txtBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusGained
        // TODO add your handling code here:
        txtBusqueda.setText(null);
    }//GEN-LAST:event_txtBusquedaFocusGained

    private void txtBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusLost
        // TODO add your handling code here:
        txtBusqueda.setText("Buscar por #Cedula");
    }//GEN-LAST:event_txtBusquedaFocusLost

    private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
        // TODO add your handling code here:
        jProgressBar1.setValue(progVal++);

        if (progVal >= jProgressBar1.getMaximum()) {
            // Man_pel.lblSubtitulo.setText("Subtitulo : "+Man_pel.lstSubtitulo.getModel().getSize());
            Man_pel.PnlActor.setBorder(javax.swing.BorderFactory.createTitledBorder("Autor(es): " + Man_pel.lstActor.getModel().getSize()));

            this.dispose();
            timer1.stop();
        }

    }//GEN-LAST:event_timer1OnTime

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        populateEmpleado();

    }//GEN-LAST:event_formWindowActivated

    private void tblEmpleadoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadoMouseReleased
        // TODO add your handling code here:

        System.out.println("boom");

        int selectedRow = tblEmpleado.getSelectedRow();

        if (evt.getClickCount() == 2) {
            //sendData(); 
            //evt.

            if (tblEmpleado.getSelectedRow() > -1) {

                confirmStringToDelete = "";
                for (int i = 0; i < 4; i++) {

                    String strData = (tblEmpleado.getModel().getValueAt(selectedRow, i).toString()) + " ";
                    //System.out.println(""+strData);
                    confirmStringToDelete += strData;
                }

                String ObjButtons[] = {"Si", "No"};
                int PromptResult = JOptionPane.showOptionDialog(this, "Desea Convertir el empleado en usuario? [ " + confirmStringToDelete + " ]", "Confirmacion...?", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[0]);

                if (PromptResult == JOptionPane.YES_OPTION) {

                    Man_usu.yacliente = true;
                    int idData = Integer.parseInt((tblEmpleado.getModel().getValueAt(selectedRow, 0).toString()));
                     Man_usu.rowIdData = idData;
                      Man_usu.lblcodEmp.setText(""+idData);
                      Man_usu.lblNombreCompleto.setText((tblEmpleado.getModel().getValueAt(selectedRow, 1).toString())+" "+(tblEmpleado.getModel().getValueAt(selectedRow, 2).toString()));
                      getSelectRowIdData();

                    confirmStringToDelete = null;
                    rowIdData = 0;
                    this.dispose();
                }
            }

        }
    }//GEN-LAST:event_tblEmpleadoMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JdTodoLosEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JdTodoLosEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JdTodoLosEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JdTodoLosEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JdTodoLosEmpleados dialog = new JdTodoLosEmpleados(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JTable tblEmpleado;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables

}
