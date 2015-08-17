package connection_utilies;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LCRT
 */
public class InsertUpdateDeleteCopia {

        private int codpel; 
        
        private int numcopia;
       private int codestado;

        
        // private int id; 
        //1 insert ,2 Update,3 delete
        private int state;  
  
        public InsertUpdateDeleteCopia(int codpel,int numcopia ,int codestado,int state)  
        {  
            this.codpel = codpel;
            this.numcopia=numcopia;
            this.codestado=codestado;
            this.state = state;  
        }  
  
        public int getCodpel()  
        {  
            return codpel;  
        }  
  
        public int getNumcopia()  
        {  
            return numcopia;  
        }  
        
        
          public int getCodestado()  
        {  
            return codestado;  
        }  
          
          
        public int getState()  
        {  
            return state;  
        }  
 
    
}
