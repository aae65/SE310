import java.util.Objects;

public class alarmClockRadio {
    static alarmClock myAlarm = new alarmClock(8, 5, "AM", true, 8, 0, "AM");
    static radio myRadio = new radio(true, "1060 AM");
    public static void main(String[] args) {
        myRadio.getStatus();
        tick();
        myAlarm.snooze();
        tick();
        myAlarm.changeStatus();
    }

    public static void tick() {
        for (int h = myAlarm.hour; h < 12; h++) {
            for (int m = myAlarm.min; m < 60; m++) {
                myAlarm.min++;
                myAlarm.showTime();
                if (myAlarm.aHour == myAlarm.hour && myAlarm.aMin == myAlarm.min && Objects.equals(myAlarm.aTod, myAlarm.tod)) {
                    alarmClock.alert(myRadio.getStation());
                    return;
                }
            }
            myAlarm.hour++;
        }
    }

}
