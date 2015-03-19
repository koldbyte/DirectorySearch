package com.koldbyte.filesearch.lib;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexValidator {
	public static Boolean isValid(String regex){
        try {
            Pattern.compile(regex);
        } catch (PatternSyntaxException exception) {
            return false;
        }
       return true;
	}
}
