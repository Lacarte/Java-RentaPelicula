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
    //don't forget to add this code in windos active to repopulate the subtitle tale
//populateIdioma(tableName);
public class JdTodaLasPeliculasRenta extends javax.swing.JDialog {

    DBSql sql = null;
    String tableName = "tbtercero";
    String idColname = "codter";
    String confirmStringToDelete = null;

    ArrayList<Integer> arrlIdAlreadyAdded = null;
    ArrayList<Integer> indexTodelete = null;

    int progVal = 1;
    int rowIdData = 0;

    public JdTodaLasPeliculasRenta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sql = new DBSql();

        arrlIdAlreadyAdded = new ArrayList<Integer>();
        indexTodelete = new ArrayList<Integer>();

        int progVal = 1;
        txtBusqueda.requestFocus();
        populatePelicula();
        jLabel2.setVisible(false);
        txtBusqueda.setDocument(new LimitTextfield(24));

    }

    ////compare codpe numcopia to the other codpe numcopia  and remove the copia
    void removeAlreadyAddedPelicula() {

        DefaultTableModel tm = (DefaultTableModel) tblPelicula.getModel();

        for (int i = 0; i < Tra_ren.llstAddRemovePelicula.size(); i++) {
            ObjPeliculaCopelNumcopia opcn = (ObjPeliculaCopelNumcopia) Tra_ren.llstAddRemovePelicula.get(i);
            System.out.println(">>DATAZ <> codpel:" + opcn.getCodpel() + " numcopia:" + opcn.getNumcopia());
            for (int j = 0; j < tblPelicula.getRowCount(); j++) {
                if (opcn.getCodpel() == Integer.parseInt((String) tblPelicula.getValueAt(j, 0)) && opcn.getNumcopia() == Integer.parseInt((String) tblPelicula.getValueAt(j, 2))) {
                    System.out.println("boom remove this row");
                    tm.removeRow(j);
                }
                System.out.println("comparison>> " + opcn.getCodpel() + " -- " + tblPelicula.getValueAt(j, 0) + " <> " + opcn.getNumcopia() + " -- " + tblPelicula.getValueAt(j, 2));
            }

        }
    }

    void hideColTable() {

        /* 
         tblCliente.getColumnModel().getColumn(4).setMinWidth(0);
         tblCliente.getColumnModel().getColumn(4).setMaxWidth(0);
         tblCliente.getColumnModel().getColumn(4).setWidth(0);

         tblCliente.getColumnModel().getColumn(5).setMinWidth(0);
         tblCliente.getColumnModel().getColumn(5).setMaxWidth(0);
         tblCliente.getColumnModel().getColumn(5).setWidth(0);
         */
        tblPelicula.getColumnModel().getColumn(6).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(6).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(6).setWidth(0);

        tblPelicula.getColumnModel().getColumn(7).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(7).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(7).setWidth(0);

        tblPelicula.getColumnModel().getColumn(8).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(8).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(8).setWidth(0);

        tblPelicula.getColumnModel().getColumn(9).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(9).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(9).setWidth(0);

        tblPelicula.getColumnModel().getColumn(10).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(10).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(10).setWidth(0);

        tblPelicula.getColumnModel().getColumn(11).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(11).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(11).setWidth(0);

        tblPelicula.getColumnModel().getColumn(12).setMinWidth(0);
        tblPelicula.getColumnModel().getColumn(12).setMaxWidth(0);
        tblPelicula.getColumnModel().getColumn(12).setWidth(0);

    }

    public void populatePelicula() {

        String Where = null;

        if (rdoDisponible.isSelected()) {
            Where = "WHERE `ESTADO`='disponible' ";
        } else {
            Where = "";
        }

        String sqlQuery = "SELECT * FROM(SELECT pel.codpel AS CODPEL,pel.titpel AS 'TITUTO PELICULA',pelcop.numcopia AS 'Nro COPIA',estpel.desest As 'ESTADO',gen.desgen AS GENERO,CONCAT('',pel.anopel) AS 'AÑO',cla.descla AS CLASIF,estpel.codestpel 'CODE ESTADO',pel.durpel AS 'DURACIÓN',pel.sitpel AS SITIO,fmt.desfor AS FORMATO,idi.desidi IDIOMA,idiori.desidi AS 'IDIOMA ORIGINAL' \n"
                + "FROM tbpelicula pel INNER JOIN tbgenero gen ON pel.codgen=gen.codgen\n"
                + "               INNER JOIN tbclaficacionxprecio cla ON pel.codcla=cla.codcla\n"
                + "               INNER JOIN tbformato fmt ON pel.codfor=fmt.codfor\n"
                + "               INNER JOIN tbidioma idi ON pel.codidi=idi.codidi\n"
                + "               INNER JOIN tbidioma idiori ON pel.codidiori=idiori.codidi\n"
                + "            INNER JOIN tbpelicula_copia pelcop ON pelcop.codpel=pel.codpel\n"
                + "								INNER JOIN tb_estado_pelicula estpel ON estpel.codestpel=pelcop.codestado\n"
                + ") as vtb " + Where + "  ORDER BY codpel DESC";

        //   System.out.println("populate sql" + sqlQuery);
        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblPelicula.remove(this);
        try {
            tblPelicula.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblPelicula.getRowCount());
            hideColTable();
            removeAlreadyAddedPelicula();

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void populatePeliculaViaFilter() {

        String Where = null;

        if (rdoDisponible.isSelected()) {
            Where = "WHERE `ESTADO`='disponible' AND ";
        } else {
            Where = "WHERE";
        }

        String sqlQuery = "SELECT * FROM(SELECT pel.codpel AS CODPEL,pel.titpel AS 'TITUTO PELICULA',pelcop.numcopia AS 'Nro COPIA',estpel.desest As 'ESTADO',gen.desgen AS GENERO,CONCAT('',pel.anopel) AS 'AÑO',cla.descla AS CLASIF,estpel.codestpel 'CODE ESTADO',pel.durpel AS 'DURACIÓN',pel.sitpel AS SITIO,fmt.desfor AS FORMATO,idi.desidi IDIOMA,idiori.desidi AS 'IDIOMA ORIGINAL' \n"
                + "FROM tbpelicula pel INNER JOIN tbgenero gen ON pel.codgen=gen.codgen\n"
                + "               INNER JOIN tbclaficacionxprecio cla ON pel.codcla=cla.codcla\n"
                + "               INNER JOIN tbformato fmt ON pel.codfor=fmt.codfor\n"
                + "               INNER JOIN tbidioma idi ON pel.codidi=idi.codidi\n"
                + "               INNER JOIN tbidioma idiori ON pel.codidiori=idiori.codidi\n"
                + "            INNER JOIN tbpelicula_copia pelcop ON pelcop.codpel=pel.codpel\n"
                + "								INNER JOIN tb_estado_pelicula estpel ON estpel.codestpel=pelcop.codestado\n"
                + ") as vtb  " + Where + " `TITUTO PELICULA` LIKE '%" + txtBusqueda.getText().trim() + "%' ORDER BY codpel DESC";

        // System.out.println("populate sql" + sqlQuery);
        ResultSet rs = sql.displaytb(tableName, sqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblPelicula.remove(this);
        try {
            tblPelicula.setModel(rst.rstomodel(rs));
            lblInfo.setText("Ctd : " + tblPelicula.getRowCount());

            hideColTable();
            removeAlreadyAddedPelicula();
        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getSelectRowIdData() {
        try {
            int selectedRow = tblPelicula.getSelectedRow();

            int idData = Integer.parseInt((tblPelicula.getModel().getValueAt(selectedRow, 0).toString()));

            rowIdData = idData;

            /////////////////////////////set combobox////////////////////////
            //selectedValueCombobox(cmbGenero, tblPelicula.getModel().getValueAt(selectedRow, 2));
            /////////////////for update//////////////////       
            ////////mete string lan nan textfield la///////////////
            Man_emp.txtNom.setText(tblPelicula.getModel().getValueAt(selectedRow, 1).toString());
            Man_emp.txtApe.setText(tblPelicula.getModel().getValueAt(selectedRow, 2).toString());
            Man_emp.txtCed.setText(tblPelicula.getModel().getValueAt(selectedRow, 3).toString());
            Man_emp.txtTel.setText(tblPelicula.getModel().getValueAt(selectedRow, 4).toString());

            Man_emp.txtCor.setText(tblPelicula.getModel().getValueAt(selectedRow, 6).toString());

            Man_emp.txtDir.setText(tblPelicula.getModel().getValueAt(selectedRow, 7).toString());

            Man_emp.cmbSex.setSelectedItem(tblPelicula.getModel().getValueAt(selectedRow, 8).toString());

            String chk = tblPelicula.getModel().getValueAt(selectedRow, 5).toString();

            //checkbox
            if (chk.equalsIgnoreCase("1")) {
                Man_emp.chcEstado.setSelected(true);
            } else {
                Man_emp.chcEstado.setSelected(false);
            }

            ///Date convertion
            String inputDateStr = tblPelicula.getModel().getValueAt(selectedRow, 9).toString();
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPelicula = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        lblInfo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        rdoDisponible = new javax.swing.JRadioButton();
        rdoTodas = new javax.swing.JRadioButton();

        timer1.setDelay(2L);
        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Peliculas");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        tblPelicula.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPelicula.getTableHeader().setReorderingAllowed(false);
        tblPelicula.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPeliculaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPelicula);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Peliculas");

        txtBusqueda.setText("Buscar /f3  por Titulo, Estado, Genero");
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

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/preloadGif/1.gif"))); // NOI18N

        jProgressBar1.setBackground(new java.awt.Color(255, 255, 255));
        jProgressBar1.setForeground(new java.awt.Color(0, 102, 255));
        jProgressBar1.setBorderPainted(false);

        buttonGroup1.add(rdoDisponible);
        rdoDisponible.setSelected(true);
        rdoDisponible.setText("Disponible");
        rdoDisponible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDisponibleActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoTodas);
        rdoTodas.setText("Todas");
        rdoTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTodasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 256, Short.MAX_VALUE)
                        .addComponent(rdoDisponible)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdoTodas)
                        .addGap(50, 50, 50)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoDisponible)
                    .addComponent(rdoTodas))
                .addGap(10, 10, 10)
                .addComponent(lblInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        populatePeliculaViaFilter();
        if (txtBusqueda.getText().isEmpty()) {
            populatePelicula();
        }
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void txtBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusGained
        // TODO add your handling code here:
        txtBusqueda.setText(null);
    }//GEN-LAST:event_txtBusquedaFocusGained

    private void txtBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusLost
        // TODO add your handling code here:
        txtBusqueda.setText("Buscar por Titulo, Estado, Genero");
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
        populatePelicula();

    }//GEN-LAST:event_formWindowActivated

    private void rdoDisponibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDisponibleActionPerformed
        // TODO add your handling code here:
        populatePelicula();
    }//GEN-LAST:event_rdoDisponibleActionPerformed

    private void rdoTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTodasActionPerformed
        // TODO add your handling code here:
        populatePelicula();
    }//GEN-LAST:event_rdoTodasActionPerformed

    private void tblPeliculaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPeliculaMouseClicked
        // TODO add your handling code here:

        int selectedRow = tblPelicula.getSelectedRow();

        if (evt.getClickCount() == 2) {
            //sendData(); 
            //evt.

            System.out.println("boom double click");

            if (tblPelicula.getSelectedRow() > -1) {

                confirmStringToDelete = "";
                for (int i = 0; i < 4; i++) {

                    String strData = (tblPelicula.getModel().getValueAt(selectedRow, i).toString()) + " ";
                    //System.out.println(""+strData);
                    confirmStringToDelete += strData;
                }

                String ObjButtons[] = {"Si", "No"};
                int PromptResult = JOptionPane.showOptionDialog(this, "Desea agregar esa pelicula? [ " + confirmStringToDelete + " ]", "Confirmacion...?", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, ObjButtons, ObjButtons[0]);

                if (PromptResult == JOptionPane.YES_OPTION) {

                    int codpel = Integer.parseInt((tblPelicula.getModel().getValueAt(selectedRow, 0).toString()));

                    int numcopia = Integer.parseInt((tblPelicula.getModel().getValueAt(selectedRow, 2).toString()));

                    Tra_ren.AddRowToModel(Tra_ren.dias, codpel, numcopia);

                    System.out.println(">>>" + Tra_ren.dias + " " + codpel + " " + numcopia);

                    getSelectRowIdData();

                    confirmStringToDelete = null;
                    rowIdData = 0;
                    this.dispose();
                }
            }

        }
    }//GEN-LAST:event_tblPeliculaMouseClicked

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
            java.util.logging.Logger.getLogger(JdTodaLasPeliculasRenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JdTodaLasPeliculasRenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JdTodaLasPeliculasRenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JdTodaLasPeliculasRenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JdTodaLasPeliculasRenta dialog = new JdTodaLasPeliculasRenta(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JRadioButton rdoDisponible;
    private javax.swing.JRadioButton rdoTodas;
    private javax.swing.JTable tblPelicula;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables

}
