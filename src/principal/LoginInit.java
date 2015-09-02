/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import utilities.DBSql;
import utilities.LimitTextfield;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.edisoncor.gui.util.WindowsUtil;

/**
 *
 * @author LCRT
 */
public class LoginInit extends javax.swing.JFrame implements Runnable {

    /**
     * Creates new form LOGIN
     */
    DBSql sql = null;
    LinkedList UserData = null;

    
    public LoginInit() {
        initComponents();
        sql = new DBSql();
        UserData = new LinkedList();
        txtUsuario.setDocument(new LimitTextfield(24));
        txtPassword.setDocument(new LimitTextfield(24));
        txtUsuario.setText("USUARIO");
        txtPassword.setText("123455666");
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
        txtUsuario = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        EXIT1 = new javax.swing.JLabel();
        EXIT = new javax.swing.JLabel();
        lblInfo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        timer1.setDelay(950L);
        timer1.setOnceOnly(true);
        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUsuario.setText("Usuario");
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioFocusGained(evt);
            }
        });
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
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 250, 30));

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
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 250, 30));

        btnIngresar.setText("INGRESAR");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        getContentPane().add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 230, 120, 30));

        EXIT1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EXIT1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/questions1.png"))); // NOI18N
        EXIT1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EXIT1MouseClicked(evt);
            }
        });
        getContentPane().add(EXIT1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 40, 30));

        EXIT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EXIT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/shutdown1.png"))); // NOI18N
        EXIT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EXITMouseClicked(evt);
            }
        });
        getContentPane().add(EXIT, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 300, 40, 30));

        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInfo.setText("[]");
        getContentPane().add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 235, 150, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system-icons/BG.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 380));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioFocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_txtUsuarioFocusGained

    private void EXITMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EXITMouseClicked
        // TODO add your handling code here:
        String ObjButtons[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(this, "Desea Salir?", "Salir?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if (PromptResult == JOptionPane.YES_OPTION) {
            // ConnectionManager.getInstance().close();
            System.exit(0);
        }

    }//GEN-LAST:event_EXITMouseClicked

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        // TODO add your handling code here:
        txtPassword.setText(null);
    }//GEN-LAST:event_txtPasswordFocusGained

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
        
        //linkedlist data
        //exist
        //codter
        //nombre
        //usuario
        //tipousuario
        
        boolean exist = (boolean) llResult.get(0);

        if (exist) {
            UserData = llResult;
            lblInfo.setText("Bienvenido  ////> ");

            timer1.start();
        } else {

            lblInfo.setText("Intente de nuevo");
            txtUsuario.requestFocus();

        }


    }//GEN-LAST:event_btnIngresarActionPerformed

    private void EXIT1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EXIT1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_EXIT1MouseClicked

    private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
        // TODO add your handling code here:
        System.out.println("Welcome");
        this.dispose();
        Principal prnl = new Principal(UserData);
        prnl.setExtendedState(MAXIMIZED_BOTH);
        prnl.setVisible(true);
        
    }//GEN-LAST:event_timer1OnTime

    private void txtUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Enter was pressed. Your code goes here.
            txtPassword.requestFocus();
        }
     
        //evt.getComponent();
       
      JTextField  tf=(JTextField) evt.getComponent();
        System.out.println("lg"+tf.getText().length());
        if (tf.getText().length()>=10) {
            evt.consume();
        }
        
    }//GEN-LAST:event_txtUsuarioKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EXIT;
    private javax.swing.JLabel EXIT1;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblInfo;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        this.setVisible(true);
        this.toFront();
        com.sun.awt.AWTUtilities.setWindowOpaque(this, false);

        //to make the foreground  of jframe transparent
        WindowsUtil.makeWindowsOpacity(this, 1.0f);
    }
}
