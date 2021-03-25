package io.github.a5h73y.platecommands.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date Time Utility methods.
 */
public class DateTimeUtils {

	public static final String DD_MM_YYYY_HH_MM_SS = "[dd/MM/yyyy | HH:mm:ss]";

	/**
	 * Display current date and time.
	 *
	 * @return formatted datetime DD/MM/YYYY | HH:MM:SS
	 */
	public static String getDisplayDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.format(DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS));
	}
}
