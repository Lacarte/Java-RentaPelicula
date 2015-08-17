/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author LCRT
 */
public class YearValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String YEAR_PATTERN = "^\\d{4}";

    //Construtor
    public YearValidator() {
        pattern = Pattern.compile(YEAR_PATTERN);
    }

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String hex) {
        boolean rtn = false;
        matcher = pattern.matcher(hex);
        //return matcher.matches();

        if (matcher.matches()) {

            int testValue = Integer.parseInt(hex);
            //years interval
            int min = 1900;
            int max = 2020;

            try {
                if ((testValue >= min) && (testValue <= max)) {
                    rtn = true;
                } else {
                    rtn = false;
                }
            } catch (Exception ex) {
                if (ex instanceof NumberFormatException) {
                    rtn = false;
                }

                // Ignore all other exceptions, e.g. illegal state exception
            }

        }

        return rtn;
    }

}
