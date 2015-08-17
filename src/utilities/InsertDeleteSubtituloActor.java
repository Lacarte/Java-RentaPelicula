package utilities;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LCRT
 */
public class InsertDeleteSubtituloActor {

        private int id; 
        //true insert ,false delete
        private boolean state;  
  
        public InsertDeleteSubtituloActor(int id, boolean state)  
        {  
            this.id = id;  
            this.state = state;  
        }  
  
        public int getId()  
        {  
            return id;  
        }  
  
        public boolean getState()  
        {  
            return state;  
        }  
 
    
}
