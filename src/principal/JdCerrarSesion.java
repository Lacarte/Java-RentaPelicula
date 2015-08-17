/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import utilities.DBSql;
import utilities.LimitTextfield;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 *
 * @author LCRT
 */
public class JdCerrarSesion extends javax.swing.JDialog {

    /**
     * Creates new form JdCerrarSesion
     */
    DBSql sql = null;
    LinkedList existResultSendData = null;

    public JdCerrarSesion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        sql = new DBSql();
        existResultSendData = new LinkedList();
        txtUsuario.setDocument(new LimitTextfield(24));
        txtPassword.setDocument(new LimitTextfield(24));
        txtUsuario.setText("USUARIO");

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
        timer2 = new org.netbeans.examples.lib.timerbean.Timer();
        txtUsuario = new javax.swing.JTextField();
        btnIngresar = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        lblInfo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        timer1.setDelay(1500L);
        timer1.setOnceOnly(true);
        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        timer2.setDelay(1500L);
        timer2.setOnceOnly(true);
        timer2.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer2OnTime(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUsuario.setText("USUARIO");
        txtUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUsuarioMouseClicked(evt);
            }
        });
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyReleased(evt);
            }
        });
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, 276, 28));

        btnIngresar.setText("CAMBIAR/c");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        getContentPane().add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, 145, 31));

        txtPassword.setText("jPasswordField1");
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 276, 27));

        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInfo.setText("[]");
        getContentPane().add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 245, 120, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/cerrar-sesionBG.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Enter was pressed. Your code goes here.
            btnIngresar.doClick();
        }
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void txtUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUsuarioMouseClicked
        // TODO add your handling code here:
        txtUsuario.setText(null);

    }//GEN-LAST:event_txtUsuarioMouseClicked

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        // TODO add your handling code here:
        System.out.println("Boom");

        String sqlQuery = "SELECT * FROM (SELECT ter.codter,CONCAT(ter.nomter,' ',per.apeper) as nombre ,usu.nomusu as usuario,usu.conusu as contrasena,tipusu.destipusu as tipousuario FROM tbtercero ter INNER JOIN tbpersona per ON ter.codter=per.codper\n"
                + "INNER JOIN tbempleado emp ON per.codper=emp.codemp INNER JOIN tbusuario usu ON\n"
                + "usu.codusu=emp.codemp INNER JOIN tb_tipo_usuario tipusu ON tipusu.codtipusu=usu.codtipusu\n"
                + "LEFT JOIN tbsucursal suc ON suc.codsuc=emp.codsuc) AS vtb";

        LinkedList llResult = new LinkedList();

        llResult = sql.Authentification(txtUsuario.getText().trim(), txtPassword.getText().trim(), sqlQuery);

        boolean exist = (boolean) llResult.get(0);

        if (exist) {
            existResultSendData = llResult;
            lblInfo.setText("Bienvenido  ////> ");

            timer1.start();
            
            
        } else {

            lblInfo.setText("Intente de nuevo");
            txtUsuario.requestFocus();

        }


    }//GEN-LAST:event_btnIngresarActionPerformed

    private void txtUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Enter was pressed. Your code goes here.
            txtPassword.requestFocus();
        }


    }//GEN-LAST:event_txtUsuarioKeyReleased

    private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
        // TODO add your handling code here:

        //hide parent
        
        this.getParent().setVisible(false);
  
        this.getParent().setEnabled(false);
        this.getParent().invalidate();
        this.getParent().removeAll();

        timer2.start();


    }//GEN-LAST:event_timer1OnTime

    private void timer2OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer2OnTime
        // TODO add your handling code here:

        Principal prnl = new Principal();
        prnl.setExtendedState(MAXIMIZED_BOTH);
        prnl.setVisible(true);
        timer2.stop();
    }//GEN-LAST:event_timer2OnTime

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        // TODO add your handling code here:
        txtPassword.setText(null);
    }//GEN-LAST:event_txtPasswordFocusGained

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
            java.util.logging.Logger.getLogger(JdCerrarSesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JdCerrarSesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JdCerrarSesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JdCerrarSesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JdCerrarSesion dialog = new JdCerrarSesion(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblInfo;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    private org.netbeans.examples.lib.timerbean.Timer timer2;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
