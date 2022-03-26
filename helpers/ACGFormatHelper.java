package com.americacg.cargavirtual.web.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ACGFormatHelper {

	public static String format(Float value, String decimalSeparator) {

		if (decimalSeparator == null || "".equals(decimalSeparator.trim())){
			decimalSeparator = ".";
		}
		
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
		
		DecimalFormat df = new DecimalFormat();
		
		df.setDecimalFormatSymbols(decimalFormatSymbols);
		df.setGroupingUsed(false);
		
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.CEILING);

		if(value == null) {
			value = 0f;
		}
		
		return df.format(BigDecimal.valueOf(value));
	}
	
}
