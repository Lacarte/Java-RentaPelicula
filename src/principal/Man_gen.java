package principal;

import utilities.DBSql;
import utilities.ResultsetTable;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Man_gen extends javax.swing.JInternalFrame {

    DBSql sql = null;
    String confirmStringToDelete = null;
    //to control update
    int rowIdData;

    String tableName = "tbgenero";
    String idColname = "codgen";
    String descriptionColname = "desgen";

    /**
     * Creates new form man_peliculas
     */
    public Man_gen() {
        initComponents();
        sql = new DBSql();
        clearNew();
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

    ///custom function.....
//////clear and populate////////////  
    public void clearNew() {
        tfGenero.setText(null);
        populateGenero(tableName);
        tfGenero.requestFocus();
        rowIdData = 0;
    }

    //////////////////populate table////////////////////
    public void populateGenero(String tblname) {

        ResultSet rs = sql.displaytbWithIdColname(tblname, idColname);

        ResultsetTable rst = new ResultsetTable();
        jtGenero.remove(this);
        try {
            jtGenero.setModel(rst.rstomodel(rs));
            lbInfo.setText("Ctd : " + jtGenero.getRowCount());

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getSelectRowIdData() {
        try {
            int selectedRow = jtGenero.getSelectedRow();

            int idData = Integer.parseInt((jtGenero.getModel().getValueAt(selectedRow, 0).toString()));

            rowIdData = idData;
            /////////////////for update//////////////////       
            ////////mete string lan nan textfield la///////////////
            tfGenero.setText(jtGenero.getModel().getValueAt(selectedRow, 1).toString());

            ///////////////////pran tout string ki nan row la///////////////////
            confirmStringToDelete = "";
            for (int i = 0; i < jtGenero.getColumnCount(); i++) {

                String strData = (jtGenero.getModel().getValueAt(selectedRow, i).toString()) + " ";
                //System.out.println(""+strData);
                confirmStringToDelete += strData;
            }

        } catch (Exception e) {
        }

    }

    public void populateGeneroViaFilter(String tblname) {

        ResultSet rs = sql.displaytb(tblname, "SELECT * from " + tableName + " WHERE (" + idColname + " LIKE '%" + txtBusqueda.getText() + "%')  OR (" + descriptionColname + " LIKE '%" + txtBusqueda.getText() + "%');");

        ResultsetTable rst = new ResultsetTable();
        jtGenero.remove(this);
        try {
            jtGenero.setModel(rst.rstomodel(rs));
            lbInfo.setText("Ctd : " + jtGenero.getRowCount());

        } catch (SQLException ex) {
            Logger.getLogger(Man_gen.class
                    .getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tfGenero = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtGenero = new javax.swing.JTable();
        txtBusqueda = new javax.swing.JTextField();
        lbInfo = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Mantenimiento Genero");
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

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/eliminar.png"))); // NOI18N
        btnEliminar.setText("ELIMINAR /f6");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/floppydisk3.png"))); // NOI18N
        btnGuardar.setText("GUARDAR /f5");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/limpiar.png"))); // NOI18N
        btnNuevo.setText("NUEVO /f4");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnGuardar)
                .addGap(20, 20, 20)
                .addComponent(btnEliminar)
                .addContainerGap(183, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        tfGenero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfGeneroKeyReleased(evt);
            }
        });

        jLabel3.setText("Descripcion Genero");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(292, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Mantenimiento Genero");

        jtGenero.setModel(new javax.swing.table.DefaultTableModel(
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
        jtGenero.getTableHeader().setReorderingAllowed(false);
        jtGenero.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jtGeneroMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtGenero);

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

        lbInfo.setText(">>");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbInfo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(48, 48, 48))
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
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

    private void tfGeneroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfGeneroKeyReleased
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (c == KeyEvent.VK_ENTER) {

            if (tfGenero.getText().isEmpty()) {

            } else {
                btnGuardar.doClick();
            }

        }

    }//GEN-LAST:event_tfGeneroKeyReleased

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        //check if the text is already there
        if (sql.dataDescriptionExist(tableName, descriptionColname, tfGenero.getText().trim())) {

            JOptionPane.showMessageDialog(this, "Este dato ya existe");

        } else {

            //check if the id is already the to update it
            if (sql.checkidWithIdColname(tableName, idColname, rowIdData)) {
                //System.err.println("li la deja");
                if (sql.dosUpdate(tableName, descriptionColname, tfGenero.getText().trim(),idColname, rowIdData)) {
                    JOptionPane.showMessageDialog(null, "Actualizado exitosamente");
                    clearNew();

                }

            } else {

                //check if it's empty
                if (tfGenero.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "No Puede estar Vacio");
                    tfGenero.requestFocus();
                } else {

                    if (sql.dosGuardarWithDescColname(tableName, descriptionColname, tfGenero.getText().trim())) {
                        JOptionPane.showMessageDialog(null, "Guardado exitosamente");
                        clearNew();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se ha podido Guardar");
                    }

                }

            }

        }


    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        clearNew();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void jtGeneroMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtGeneroMouseReleased
        // TODO add your handling code here:
       // System.out.print("/---/");
        getSelectRowIdData();
       // System.out.println(rowIdData + "---" + confirmStringToDelete);


    }//GEN-LAST:event_jtGeneroMouseReleased

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        // TODO add your handling code here:
        txtBusqueda.setText(txtBusqueda.getText().trim());
        populateGeneroViaFilter(tableName);
        if (txtBusqueda.getText().isEmpty()) {
            populateGenero(tableName);
        }

    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
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

    private void txtBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusGained
        // TODO add your handling code here:
        txtBusqueda.setText(null);
    }//GEN-LAST:event_txtBusquedaFocusGained

    private void txtBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusquedaFocusLost
        // TODO add your handling code here:
        txtBusqueda.setText("Buscar /f3");

    }//GEN-LAST:event_txtBusquedaFocusLost

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);

    }//GEN-LAST:event_formInternalFrameDeactivated

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);

    }//GEN-LAST:event_formInternalFrameActivated


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtGenero;
    private javax.swing.JLabel lbInfo;
    private javax.swing.JTextField tfGenero;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}
