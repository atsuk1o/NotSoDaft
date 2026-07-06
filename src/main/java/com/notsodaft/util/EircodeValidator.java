package com.notsodaft.util;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class EircodeValidator {
    private static final Pattern EIRCODE_PATTERN =
        Pattern.compile("^[AC-FHKNPRTV-Y]{1}[0-9]{2}\\s?[AC-FHKNPRTV-Y0-9]{4}$", Pattern.CASE_INSENSITIVE);

    public boolean isValid(String eircode){
        if(eircode == null || eircode.trim().isEmpty()) return false;
        return EIRCODE_PATTERN.matcher(eircode.trim()).matches();
    }

    public String format(String eircode){
        if(eircode == null) return null;
        String clean = eircode.trim().toUpperCase().replace(" ", "");
        if(clean.length() == 7){
            return clean.substring(0, 3) + " " + clean.substring(3);
        }
        return clean;
    }
}