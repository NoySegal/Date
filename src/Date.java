/**
 * This class represents a Date object
 *
 * @author Noy Segal
 * @version 2020a
 */
public class Date {
    private int _day;
    private int _month;
    private int _year;

    //months of the year
    private final int JAN = 1;
    private final int FEB = 2;
    private final int MAR = 3;
    private final int APR = 4;
    private final int MAY = 5;
    private final int JUN = 6;
    private final int JUL = 7;
    private final int AUG = 8;
    private final int SEP = 9;
    private final int OCT = 10;
    private final int NOV = 11;
    private final int DEC = 12;

    //constants that represents different days min/max of different months
    private final int MIN_DAY = 1;
    private final int DAYS_LONG_MONTH = 31;
    private final int DAYS_SHORT_MONTH = 30;
    private final int DAYS_FEB_NO_LEAP_YEAR = 28;
    private final int DAYS_FEB_LEAP_YEAR = 29;

    //constants of year limits
    private final int MAX_YEAR = 9999;
    private final int MIN_YEAR = 1000;

    //constant of default date
    private final int DEFAULT_DAY = 1;
    private final int DEFAULT_MONTH = 1;
    private final int DEFAULT_YEAR = 2000;

    private final int MONTHS_IN_YEAR = 12;

    /**
     * Constructs a Date object.
     * Creates a new Date object if the date is valid, otherwise creates the date 1/1/2000
     *
     * @param day   the day in the month (1-31)
     * @param month the month in the year (1-12)
     * @param year  the year (4 digits)
     */
    public Date(int day, int month, int year) {
        if (validDate(day, month, year)) {
            _day = day;
            _month = month;
            _year = year;
        } else {
            _day = DEFAULT_DAY;
            _month = DEFAULT_MONTH;
            _year = DEFAULT_YEAR;
        }
    }

    /**
     * Copy constructor for Date.
     * Construct a Date with the same variables as another date.
     *
     * @param other the Date object from which to construct the new date
     */
    public Date(Date other) {
        _day = other._day;
        _month = other._month;
        _year = other._year;
    }

    /**
     * Checks if a date is a valid
     *
     * @param day   the day of the month (1-31)
     * @param month the month in the year (1-12)
     * @param year  the year (4 digits)
     * @return True if date is valid
     */
    private boolean validDate(int day, int month, int year) {
        boolean flagYear = false;
        boolean flagMonth = false;
        boolean flagDay = false;

        boolean isLeap = isLeapYear(year);

        //Checks if year is valid
        if (year >= MIN_YEAR && year <= MAX_YEAR) {
            flagYear = true;
        }

        //Checks if month and day are valid
        //Months of max 31 days
        if (month == JAN || month == MAR || month == MAY || month == JUL ||
                month == AUG || month == OCT || month == DEC) {
            if (day >= MIN_DAY && day <= DAYS_LONG_MONTH) {
                flagDay = true;
                flagMonth = true;
            }
        }
        //Months of max 30 days
        else if (month == APR || month == JUN || month == SEP || month == NOV) {
            if (day >= MIN_DAY && day <= DAYS_SHORT_MONTH) {
                flagDay = true;
                flagMonth = true;
            }
        }
        //Special case of February
        else if (month == FEB) {
            if (isLeap) {
                if (day >= MIN_DAY && day <= DAYS_FEB_LEAP_YEAR) {
                    flagDay = true;
                    flagMonth = true;
                }
            } else { //not a leap year
                if (day >= MIN_DAY && day <= DAYS_FEB_NO_LEAP_YEAR) {
                    flagDay = true;
                    flagMonth = true;
                }
            }
        }

        return flagDay && flagMonth && flagYear;
    }

    /**
     * Checks if year is a leap year
     *
     * @param year the year
     * @return True if the received year is a leap year
     */
    private boolean isLeapYear(int year) {
        boolean flag = false;

        if (year % 400 == 0) {
            flag = true;
        } else if (year % 100 == 0) {
            flag = false;
        } else if (year % 4 == 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * Check if this date is after other date
     *
     * @param other date to compare this date to
     * @return true if this date is after other date
     */
    public boolean after(Date other) {
        return other.before(this);
    }

    /**
     * Check if this date is before other date
     *
     * @param other date to compare this date to
     * @return true if this date is before other date
     */
    public boolean before(Date other) {
        boolean flag = false;

        if (_year < other._year) {
            flag = true;
        } else if (_year > other._year) {
            flag = false;
        }
        //Same year
        else {
            if (_month < other._month) {
                flag = true;
            } else if (_month > other._month) {
                flag = false;
            }
            //same month
            else {
                if (_day < other._day) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }

        return flag;
    }

    /**
     * calculate the day of the week that this date occurs on 0-Saturday 1-Sunday 2-Monday etc.
     *
     * @return number between 0-6 that represents the current day: 0 - Saturday, 1 - Sunday, etc.
     */
    public int dayInWeek() {
        int D = _day;
        int M = _month;
        int Y;
        int C;
        //January and February treated as 13 and 14 as of last year.
        if (M < MAR) {
            M += MONTHS_IN_YEAR;

            //Below March, year is treated to be the last year.
            Y = (_year - 1) % 100;
            C = (_year - 1) / 100;
        } else {
            //dividing by 100 to get first two digits and last two digits of the year
            Y = _year % 100;
            C = _year / 100;
        }

        //using the formula: Day = (D + (26×(M+1))/10 + Y + Y/4 + C/4 - 2×C) mod 7
        int res = (D + (26 * (M + 1)) / 10 + Y + Y / 4 + C / 4 - 2 * C) % 7;

        //Sometimes the result is negative, so Math.floorMod fix the issue by transferring the result to 0-6
        return Math.floorMod(res, 7);
    }

    /**
     * Calculates the difference in days between two dates
     *
     * @param other is another date
     * @return difference in days between this date and other date.
     */
    public int difference(Date other) {
        return Math.abs(calculateDate(_day, _month, _year) - calculateDate(other._day, other._month, other._year));
    }

    /**
     * Checks if two dates are equal
     *
     * @param other the other date which is being compared
     * @return True if both dates are the same
     */
    public boolean equals(Date other) {
        return (_day == other._day) && (_month == other._month) && (_year == other._year);
    }

    /**
     * @return the day of the date
     */
    public int getDay() {
        return _day;
    }

    /**
     * @return the month of the date
     */
    public int getMonth() {
        return _month;
    }

    /**
     * @return the year of the date
     */
    public int getYear() {
        return _year;
    }

    /**
     * sets the day (only if date remains valid)
     *
     * @param dayToSet the day to set if valid date
     */
    public void setDay(int dayToSet) {
        if (validDate(dayToSet, _month, _year)) {
            _day = dayToSet;
        }
    }

    /**
     * set the month (only if date remains valid)
     *
     * @param monthToSet the month to set if valid date
     */
    public void setMonth(int monthToSet) {
        if (validDate(_day, monthToSet, _year)) {
            _month = monthToSet;
        }
    }

    /**
     * sets the year (only if date remains valid)
     *
     * @param yearToSet the year to set if valid
     */
    public void setYear(int yearToSet) {
        if (validDate(_day, _month, yearToSet)) {
            _year = yearToSet;
        }
    }

    /**
     * Calculates next day's date
     *
     * @return a new Date object that represents tomorrow's date
     */
    public Date tomorrow() {
        Date tomorrowDate = new Date(this);

        if (validDate(tomorrowDate._day + 1, tomorrowDate._month, tomorrowDate._year)) {
            tomorrowDate._day += 1;
        }
        //end of the month
        else if (validDate(MIN_DAY, tomorrowDate._month + 1, tomorrowDate._year)) {
            tomorrowDate._day = MIN_DAY;
            tomorrowDate._month += 1;
        }
        //end of the year
        else if (validDate(MIN_DAY, JAN, tomorrowDate._year + 1)) {
            tomorrowDate._day = MIN_DAY;
            tomorrowDate._month = JAN;
            tomorrowDate._year += 1;
        }
        //Max date supported (31.12.9999)
        else {
            tomorrowDate._day = DEFAULT_DAY;
            tomorrowDate._month = DEFAULT_MONTH;
            tomorrowDate._year = DEFAULT_YEAR;
        }

        return tomorrowDate;
    }

    /**
     * @return a string representation of this date (dd/mm/yyyy)
     */
    public String toString() {
        String dateHolder = "";

        if (_day < 10) {
            dateHolder += "0" + _day + "/";
        } else {
            dateHolder += _day + "/";
        }

        if (_month < 10) {
            dateHolder += "0" + _month + "/";
        } else {
            dateHolder += _month + "/";
        }

        dateHolder += _year;

        return dateHolder;
    }

    /**
     * Computes the day number since the beginning of the Christian counting of years
     *
     * @param day   the day of the month (1-31)
     * @param month the month in the year (1-12)
     * @param year  the year (4 digits)
     * @return number of days since the beginning of the Christian counting of years
     */
    private int calculateDate(int day, int month, int year) {
        if (month < 3) {
            year--;
            month = month + 12;
        }
        return 365 * year + year / 4 - year / 100 + year / 400 + ((month + 1) * 306) / 10 + (day - 62);
    }

    public static void main(String[] args) {
        System.out.println("********** Test Date - Started **********");
        System.out.println("\n1. Testing Constructors and toString:");
        Date d1 = new Date(3, 5, 1998);
        System.out.println("\td1:" + d1);
        Date d2 = new Date(d1);
        System.out.println("\td2:" + d2);
        System.out.println("\n2. Testing accessors and mutators:");
        d1.setDay(8);
        d1.setMonth(10);
        d1.setYear(2016);
        System.out.println("\td1:" + d1);
        System.out.println("\tday of d1:" + d1.getDay());
        System.out.println("\tmonth of d1:" + d1.getMonth());
        System.out.println("\tyear of d1:" + d1.getYear());
        System.out.println("\n3. Testing equals method:");
        Date d3 = new Date(3, 5, 1998);
        Date d4 = new Date(3, 5, 1998);
        System.out.println("\td3:" + d3);
        System.out.println("\td4:" + d4);
        if (d3.equals(d4))
            System.out.println("\td3 is the same date as d4");
        else
            System.out.println("\td3 isn't the same date as d4");
        System.out.println("\n4. Testing before method:");
        if (d3.before(d1))
            System.out.println("\td3 is before d1");
        else
            System.out.println("\td3 isn't before d1");
        System.out.println("\n5. Testing after method:");
        if (d3.after(d1))
            System.out.println("\td3 is after d1");
        else
            System.out.println("\td3 isn't after d1");
        System.out.println("\n6. Testing difference method:");
        System.out.println("\tThe difference in days between dates d1 and d3 is : " + d1.difference(d3));
        System.out.println("\n7. Testing dayInWeek method:");
        Date d5 = new Date(6, 11, 2016);
        System.out.println("\t" + d5 + " occurs on : " + d5.dayInWeek());
        System.out.println("\n8. Testing tomorrow method:");
        Date d6 = new Date(6, 11, 2016);
        System.out.println("\t" + d6 + " tomorrow is : " + d6.tomorrow());
        Date d7 = new Date(31, 12, 2019);
        System.out.println("\t" + d7 + " tomorrow is : " + d7.tomorrow());


        Date d8 = new Date(29, 2, 2019);
        System.out.println("Noy date is: " + d8);
        Date d9 = d8.tomorrow();
        System.out.println("NoyTomorrow date is (should be 1.1.2000): " + d9);
        System.out.println("Noy weekday is: " + d8.dayInWeek());
        System.out.println("Distance between: " + d8.difference(new Date(31, 12, 2019)));
        System.out.println("\n********** Test Date - Finished **********\n");
    }
}
