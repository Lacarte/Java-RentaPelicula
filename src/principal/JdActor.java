package principal;

import connection_utilies.DBSql;
import connection_utilies.DataJlist;
import connection_utilies.LimitTextfield;
import connection_utilies.ResultsetTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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


public class JdActor extends javax.swing.JDialog {

    DBSql sql = null;
    String tableName = "tbactor";
    String idColname = "codact";
    String descriptionColname = "nomact";
   
    ArrayList<Integer> arrlIdAlreadyAdded = null;
    ArrayList<Integer> indexTodelete = null;
    
    int progVal = 1;
    
    
    public JdActor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sql = new DBSql();
        
        arrlIdAlreadyAdded = new ArrayList<Integer>();
        indexTodelete = new ArrayList<Integer>();
        
        int progVal = 1;

        
        txtBusqueda.requestFocus();
        populateActor(tableName);
       jLabel2.setVisible(false);

        
        detectIdAlreadyAdded();
        
        txtBusqueda.setDocument(new LimitTextfield(24));
        
    }

    
    
    
    //////////////////populate table////////////////////
    public void populateActor(String tblname) {

        
        String SqlQuery="SELECT codact AS ID ,CONCAT(nomact,' ' ,apeact) AS NOMBRE FROM tbactor";
        
        ResultSet rs = sql.displaytb(tblname, SqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblActor.remove(this);
  //////////////////////try//////////////
                try {

            DefaultTableModel tm = (DefaultTableModel) rst.rstomodel(rs);
            System.out.println(" size of jlist element : " + arrlIdAlreadyAdded.size() + " rowCount:  " + tm.getRowCount());
            for (int i = tm.getRowCount() - 1; i >= 0; i--) {

                for (int j = arrlIdAlreadyAdded.size() - 1; j >= 0; j--) {
                    //the id values of the database table
                    int outId = Integer.parseInt(tm.getValueAt(i, 0).toString());
                    //the id values of the table in the main class
                    int inId = arrlIdAlreadyAdded.get(j);
                    
                    //if they are equal they row should be removed
                    if (outId == inId) {
                        //indexes of the row that should be reorder(sort)   
                        indexTodelete.add(i);
                        System.out.println("egal --- " + outId + " " + inId);
                    }

                }

            }

            //reorder(sort) to delete them from the highest to the lowest
            Collections.sort(indexTodelete);

            for (int i = indexTodelete.size() - 1; i >= 0; i--) {
                System.out.println("Sort" + indexTodelete.get(i));
                tm.removeRow(indexTodelete.get(i));
            }

            tm.fireTableDataChanged();
            tblActor.repaint();

            //reassign de modify model to the table 
            tblActor.setModel(tm);
            //old
            //jtCars.setModel(rst.rstomodel(rs));
            lbInfo.setText("Ctd : " + tblActor.getRowCount());
        } catch (SQLException ex) {
            Logger.getLogger(JdActor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

 //////////////////////end try/////////////////      
        
    }

     void detectIdAlreadyAdded() {
        // System.out.println("boomfsdfsdfsd");
        arrlIdAlreadyAdded.clear();

        for (int i = 0; i < Man_pel.lstActorModel.getSize(); i++) {
            DataJlist selected_item = (DataJlist) Man_pel.lstActorModel.elementAt(i);
            System.out.println("Id FOUND : " + selected_item.getId());
            arrlIdAlreadyAdded.add(selected_item.getId());
        }

        System.out.println("size: " + arrlIdAlreadyAdded.size());
    }

     
     
     
  
    public void populateGeneroViaFilter(String tblname) {

       String SqlQuery="SELECT * FROM (SELECT codact As CODACT,CONCAT(nomact,\" \",apeact) AS NOMBRE FROM tbactor) AS VTBL WHERE (CODACT LIKE '%" + txtBusqueda.getText() + "%')  OR (NOMBRE  LIKE '%" + txtBusqueda.getText() + "%')";
        System.out.println(SqlQuery);
        ResultSet rs = sql.displaytb(tblname, SqlQuery);

        ResultsetTable rst = new ResultsetTable();
        tblActor.remove(this);
        try {
            tblActor.setModel(rst.rstomodel(rs));
            lbInfo.setText("Ctd : " + tblActor.getRowCount());

        } catch (SQLException ex) {
            Logger.getLogger(JdActor.class.getName()).log(Level.SEVERE, null, ex);
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
        tblActor = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        lbInfo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();

        timer1.setDelay(2L);
        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Actor(es)");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        tblActor.setModel(new javax.swing.table.DefaultTableModel(
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
        tblActor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblActorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblActor);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Actores");

        txtBusqueda.setText("Buscar");
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

        lbInfo.setText("jLabel2");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/preloadGif/1.gif"))); // NOI18N

        jProgressBar1.setBackground(new java.awt.Color(255, 255, 255));
        jProgressBar1.setForeground(new java.awt.Color(0, 102, 255));
        jProgressBar1.setBorderPainted(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(lbInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtBusqueda))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbInfo)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        // TODO add your handling code here:
        txtBusqueda.setText(txtBusqueda.getText().trim());
        populateGeneroViaFilter(tableName);
        if (txtBusqueda.getText().isEmpty()) {
           populateActor(tableName);
        }
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void txtBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusGained
        // TODO add your handling code here:
        txtBusqueda.setText(null);
    }//GEN-LAST:event_txtBusquedaFocusGained

    private void txtBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusLost
        // TODO add your handling code here:
        txtBusqueda.setText("Buscar");
    }//GEN-LAST:event_txtBusquedaFocusLost

    private void tblActorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblActorMouseClicked
        // TODO add your handling code here:
        System.out.println("boom");
        if (evt.getClickCount() == 2) {
            //sendData(); 
            //evt.

            if (tblActor.getSelectedRow() > -1) {

                boolean exist = false;
                // print first column value from selected row
                int id = Integer.parseInt(tblActor.getValueAt(tblActor.getSelectedRow(), 0).toString());
                String data = tblActor.getValueAt(tblActor.getSelectedRow(), 1).toString();

                // DataJlist selected _item = (DataJlist) Man_pel.jlSutitulo.getse;
                // DataJlist selected_item = (DataJlist) Man_pel.jlSutitulo.getSelectedValuesList();
                // System.out.println(selected_item);         
                //Man_pel.jlSubtituloModel.getElementAt(id);
                //System.out.println("sfgfdsg>>"+Man_pel.jlSubtituloModel.elementAt(0));
                //System.out.println(">>"+Man_pel.jlSubtituloModel.getElementAt(1));
                for (int i = 0; i < Man_pel.lstActorModel.getSize(); i++) {
                    DataJlist selected_item = (DataJlist) Man_pel.lstActorModel.elementAt(i);
                    //System.out.println("BOOM>>"+selected_item.getId());
                    if (selected_item.getId() == id) {
                        exist = true;
                    }
                }

                if (exist) {
                    JOptionPane.showMessageDialog(this.getOwner(), "Ya existe este Subtitulo.");

                } else {
                    //if it does not exit add it   
                    ////false delete and true for insert
                    Man_pel.addRemoveElementFromActor(id, true);
                    Man_pel.lstActorModel.addElement(new DataJlist(id, data));
                   
                    timer1.start();
                }

            }

        }

    }//GEN-LAST:event_tblActorMouseClicked

    private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
        // TODO add your handling code here:
                jProgressBar1.setValue(progVal++);

        if (progVal >= jProgressBar1.getMaximum()) {
            // Man_pel.lblSubtitulo.setText("Subtitulo : "+Man_pel.lstSubtitulo.getModel().getSize());
          Man_pel.PnlActor.setBorder(javax.swing.BorderFactory.createTitledBorder("Autor(es): "+Man_pel.lstActor.getModel().getSize()));

            
          
            this.dispose();
            timer1.stop();
        }
        
    }//GEN-LAST:event_timer1OnTime

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
                populateActor(tableName);

    }//GEN-LAST:event_formWindowActivated

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
            java.util.logging.Logger.getLogger(JdActor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JdActor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JdActor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JdActor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JdActor dialog = new JdActor(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel lbInfo;
    private javax.swing.JTable tblActor;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables

}
