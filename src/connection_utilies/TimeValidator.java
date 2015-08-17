/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package connection_utilies;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author LCRT
 */
public class TimeValidator {
 
    
    	private Pattern pattern;
	private Matcher matcher;
 
	private static final String TIME_PATTERN ="([0-9]?[0-9]|9[0-9]):[0-5][0-9]:[0-5][0-9]";

        
        //Construtor
	public TimeValidator() {
		pattern = Pattern.compile(TIME_PATTERN);
	}
 
        
        
        
	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex) {
 
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
    
    
    
}
