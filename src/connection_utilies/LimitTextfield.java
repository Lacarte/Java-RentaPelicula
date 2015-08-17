/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection_utilies;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author LCRT
 */
public class LimitTextfield extends PlainDocument {
      private int limit = 0;

    public LimitTextfield(int limit) {

        super();
        this.limit=limit;
    }

      @Override
    public void insertString(int offs, String str, AttributeSet a)
      throws BadLocationException {
       if (str==null)return;

if ((getLength()+str.length()) <= limit) {
	super.insertString(offs,str,a);
}

    }

}
