/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package principal;

/**
 *
 * @author LCRT
 */
public class BootLoader {
      public static void main(String[] args) {

           new  Thread(new LoginInit()).start();

    } 
}
