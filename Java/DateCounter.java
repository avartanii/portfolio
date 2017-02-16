import java.util.Arrays;

public class DateCounter{
	
	public static boolean isLeapYear(int year) {
		if ((!(year % 100 == 0) && year % 4 == 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static int daysInMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		} else if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		} else {
			return 30;
		}
	}

	public static boolean isValidDate(int year, int month, int day) {
		if (year > 0) {
			if (month >= 1 && month <= 12){
				if (day >= 1 && day <= daysInMonth(year, month)) {
					return true;
				}
			}
		}
		return false;
	}

	public static int daysBetween(int year0, int month0, int day0, int year1, int month1, int day1) {
		int days = 0;
		int temp;

		
		if (year1 < year0 || year1 == year0) {
			temp = year0;
			year0 = year1;
			year1 = temp;
			if (month1 < month0 || month1 == month0) {
				temp = month0;
				month0 = month1;
				month1 = temp;
				if (day1 < day0 || day1 == day0) {
					temp = day0;
					day0 = day1;
					day1 = temp;
				}
			}
		}

		while (!(year0 == year1 && month0 == month1 && day0 == day1)) {
			days++;
			day0++;
			if(!isValidDate(year0, month0, day0)) {
				month0++;
				day0 = 1;
				if(!isValidDate(year0, month0, day0)) {
					month0 = 1;
					year0++;
				}
			}
		}
		if (isValidDate(year0, month0, day0) && isValidDate(year1, month1, day1)) {
			return days;
		} else {
			return -1;
		}
	}

	public static String dayOfTheWeek(int year, int month, int day) {
		int monthIndex = month - 1;
		int[] monthTable = new int[12];
		int[] yearTable = new int[4];

		if (isLeapYear(year)) {
			monthTable[0] = 6;
			monthTable[1] = 2;
		} else {
			monthTable[0] = 0;
			monthTable[1] = 3;
		}

		monthTable[2] = 3;
		monthTable[3] = 6;
		monthTable[4] = 1;
		monthTable[5] = 4;
		monthTable[6] = 6;
		monthTable[7] = 2;
		monthTable[8] = 5;
		monthTable[9] = 0;
		monthTable[10] = 3;
		monthTable[11] = 5;

		//String length
		int yearLength = String.valueOf(year).length();
		//First two digits of year
		int yearInit = Integer.parseInt(Integer.toString(year).substring(0, 2));
		//Last two digits of year
		int yearEnd = Integer.parseInt(Integer.toString(year).substring(yearLength - 2, yearLength));
		//Year variable
		int yearNumber;

		if (yearInit % 4 == 0) {
			yearNumber = 6;
		} else if (yearInit % 4 == 1) {
			yearNumber = 4;
		} else if (yearInit % 4 == 2) {
			yearNumber = 2;
		} else {
			yearNumber = 0;
		}

		String[] week = new String[7];
		week[0] = "Sunday";
		week[1] = "Monday";
		week[2] = "Tuesday";
		week[3] = "Wednesday";
		week[4] = "Thursday";
		week[5] = "Friday";
		week[6] = "Saturday";

		int dayFormula = (day + monthTable[monthIndex] + yearEnd +  (yearEnd / 4) + yearNumber) % 7;


		String answerDay = week[dayFormula];

		return answerDay;
	}

	public static boolean hasLeapSecond(int year) {
		boolean leapSecond;
		int[] leapSecondYears = new int[]{1972, 1973, 1974, 1975, 1976, 1977, 1978, 1979, 1981, 1982, 1983, 1985, 1987, 1989, 1990, 1992, 1993, 1994, 1995, 1997, 1998, 2005, 2008, 2012, 2015};
		
		for (int i : leapSecondYears) {
			if (year == i) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasLeapSecond(int year, int month, int day) {
		int[] leapSecondDates = new int []{1972, 1973, 1974, 1975, 1976, 1977, 1978, 1979, 1981, 1982, 1983, 1985, 1987, 1989, 1990, 1992, 1993, 1994, 1995, 1997, 1998, 2005, 2008, 2012};
		if ((year == 1972 || year == 1973 || year == 1974 || year == 1975 || year == 1976 || year == 1977 || year == 1978 || year == 1979 || year == 1987 || year == 1989 || year == 1990 || year == 1995 || year == 1998 || year == 2005 || year == 2008) && month == 12 && day == 31) {
			return true;
		} else if ((year == 1972 || year == 1981 || year == 1982 || year == 1983 || year == 1985 || year == 1992 || year == 1993 || year == 1994 || year == 1997 || year == 2012 || year == 2015) && month == 6 && day == 30){
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		try{
			int[] argts = new int[args.length];
			int answer = daysBetween(argts[0], argts[1], argts[2], argts[3], argts[4], argts[5]);
			
			for (int i = 0; i < args.length; i++) {
				argts[i] = Integer.parseInt(args[i]);
			}

			if (answer == -1) {
				System.out.println(answer);
			} else {
				System.out.println("One or more of the supplied dates is not valid.");
			}
		} catch (ArrayIndexOutOfBoundsException e)  {
			System.out.println("Usage: java DateCounter <year0> <month0> <day0> <year1> <month1> <day1>");
		} catch (NumberFormatException e2) {
			System.out.println("One or more of the supplied dates is not valid.");
		}
	}

}