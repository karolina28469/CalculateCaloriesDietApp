package karolina;

import java.sql.Timestamp;
import java.util.Calendar;

public class Utils {

    public static void displayException(Exception e) {
        //System.out.println("### Blad otwarcia bazy: ###");
        e.printStackTrace();
        //System.out.println("### Exit ###");
        //System.exit(0);
    }

    public static Timestamp poczatekDnia(Timestamp time, int liczbaDni) {
        // = new Timestamp(System.currentTimeMillis());
        Calendar kalendarz = Calendar.getInstance();
        kalendarz.setTime(time);
        kalendarz.add(Calendar.DAY_OF_WEEK, liczbaDni);
        Timestamp time2 = new Timestamp(kalendarz.get(Calendar.YEAR) - 1900, kalendarz.get(Calendar.MONTH), kalendarz.get(Calendar.DATE), 0, 0, 0,0);
        kalendarz.set(kalendarz.get(Calendar.YEAR), kalendarz.get(Calendar.MONTH), kalendarz.get(Calendar.DATE), 0, 0, 0);
        //Timestamp time2 = new Timestamp(kalendarz.getTimeInMillis());
        return time2;
    }
}
