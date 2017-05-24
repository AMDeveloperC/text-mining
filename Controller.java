import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.GregorianCalendar;

/**
 * This class provide a controller to chech the correct format of several kind of code
 **/
public class Controller {

	public Controller() {}


	/**
	 * Check if the parameter is a valid string (not emplty)
	 */
	public boolean isValid(String toChechString) { return !toChechString.equals(""); }


	/**
	 * Check if the parameter represents a valid driving license number (italian)
	 **/
	public boolean isValidPatente(String number) {
		Pattern patente = Pattern.compile("[a-zA-Z]{2}\\d{7}[a-zA-Z]");
		Matcher m = patente.matcher(number);
		return m.matches();
	}


	/**
	 * Check if the parameter represents a valid document number (italian)
	 */
	public boolean isValidIdentity(String number) {
		Pattern identita = Pattern.compile("[a-zA-Z]{2}\\d{7}");
		Matcher m = identita.matcher(number);
		return m.matches();
	}


	/**
	 * Check if the parameter represents a valid code (codice fiscale)
	 */
	public boolean isValidCode(String toCheck) {
		Pattern p = Pattern.compile("[a-zA-Z]{6}\\d{2}[a-zA-Z]\\d{2}[a-zA-Z]\\d{3}[a-zA-Z]");
		Matcher m = p.matcher(toCheck);
		return m.matches();
	}


	/**
	 * Check if the parameter represents a valid data
	 */
	private static boolean isDateValid (int day, int month, int year) {
		GregorianCalendar cal = new GregorianCalendar(year, month-1, day);
		cal.setLenient (false);
		try {
			cal.get(Calendar.DATE);
			return true;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
	}


	/**
	 * Chech if the parameter represents a correct data italian format
	 */
	public boolean isValidData(String toCheck) {
		Pattern data = Pattern.compile("\\d{1,2}[-/]\\d{1,2}[-/]\\d+");  
		Matcher m = data.matcher(toCheck);
		if(m.matches()) {
			String date[] = toCheck.split("[-/]");
			try {
				return isDateValid(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
			}
			catch (NumberFormatException e) {
				System.out.println("Formato non numerico");
			}
		}
		return false;
	}

}
