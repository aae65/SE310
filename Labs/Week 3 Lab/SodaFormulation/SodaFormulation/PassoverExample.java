import net.sourceforge.zmanim.hebrewcalendar.JewishDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Sean Grimes, sean@seanpgrimes.com
 *
 * Coke typically produces sweetened, non-diet drinks using corn syrup. However,
 * leading up to passover they will switch part of their production to cane sugar to
 * meet Kosher guidelines for Passover. The code below simulates that switch.
 */
public class PassoverExample {
    public static void main(String[] args){
        List<CokeDrinkProduct> producedDrinks = new ArrayList<>();
        int yearLen = 366; // 2020 is a leap year

        // Get a Calendar object and start Jan 1, 2020.
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, 0, 00);

        // Note, as seen above we're just simulating a year, 2020 in particular. This
        // loop starts on Jan 1st 2020 and runs through Dec 31st 2020 (note 2020 is a
        // leap year). The simulated calendar date is passed to the isPassover function
        // to check if we're currently in Passover and in need of a cane-sugar drink
        // instead of a corn-based drink.
        for(int i = 0; i < yearLen; ++i){
            // Check for non-Passover first, since it happens more frequently
            if(!isPassover(cal)){
                producedDrinks.add(new CokeWithCornSyrup());
                producedDrinks.add(new FantaWithCornSyrup());
                producedDrinks.add(new SpriteWithCornSyrup());
            }
            // Switch to cane sugar production when it's Passover
            else{
                producedDrinks.add(new CokeWithSugar());
                producedDrinks.add(new FantaWithSugar());
                producedDrinks.add(new SpriteWithSugar());
            }

            // Increment the calendar by 1 single day
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        for(CokeDrinkProduct cdp : producedDrinks){
            System.out.println(cdp.getName());
            System.out.println(cdp.getSweetener());
            System.out.println("-----");
        }
    }

    /**
     * Using the amazingly complicated calendar library from zmanin, determine if it's
     * currently Passover. NOTE: Seriously, this isn't a simple calculation to make.
     */
    private static boolean isPassover(Calendar fakeCalendarDate){
        // Passover starts on the 15th day of the 1st month (Nisan) of the Jewish
        // calendar and runs through the 22nd day of Nisan (outside of Israel). Perform a
        // simple check on the current month, and the current day in the current month
        // (of the Jewish calendar)
        JewishDate jd = new JewishDate();
        // Set the Calendar to whatever we pass in, instead of using the real date
        jd.setDate(fakeCalendarDate);

        if(jd.getJewishMonth() != 1) return false;
        int jewishDay = jd.getJewishDayOfMonth();
        if(jewishDay >= 15 && jewishDay <= 22) return true;
        return false;

    }
}
