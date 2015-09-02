package utilities;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.MatteBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LCRT
 */
public class MakeLink {
 
    
    //open link
    //open Jframe
    //open internaframe
    
    
    public static final Color LINK_COLOR = Color.blue; 
    public static final javax.swing.border.Border LINK_BORDER = BorderFactory.createEmptyBorder(0, 0, 1, 0); 
    public static final MatteBorder HOVER_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 0, LINK_COLOR); 
    
    public static JButton makeLink(JButton button){ 
        button.setBorder(LINK_BORDER); 
        button.setForeground(LINK_COLOR); 
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        button.setFocusPainted(false); 
        button.setRequestFocusEnabled(false); 
        button.setContentAreaFilled(false);
     
        return button; 
    }  
 
}
