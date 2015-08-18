/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;

/**
 *
 * @author LCRT
 */
class IndexedFocusTraversalPolicy extends 
  FocusTraversalPolicy {

   private ArrayList<Component> components = 
      new ArrayList<Component>();

   public void addIndexedComponent(Component component) {
        components.add(component);
   }

   @Override
   public Component getComponentAfter(Container aContainer, 
               Component aComponent) {
        int atIndex = components.indexOf(aComponent);
        int nextIndex = (atIndex + 1) % components.size();
        return components.get(nextIndex);
   }

   @Override
   public Component getComponentBefore(Container aContainer,
         Component aComponent) {
        int atIndex = components.indexOf(aComponent);
        int nextIndex = (atIndex + components.size() - 1) %
                components.size();
        return components.get(nextIndex);
   }

   @Override
   public Component getFirstComponent(Container aContainer) {
        return components.get(0);
   }

 
    @Override
    public Component getLastComponent(Container cntnr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Component getDefaultComponent(Container cntnr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}