public class alarmClock extends clock {
    public int aHour;
    public int aMin;
    public String aTod;
    public boolean status;

    public alarmClock(int aHour, int aMin, String aTod, boolean status, int hour, int min, String tod) {
        super(hour, min, tod);
        this.aHour = aHour;
        this.aMin = aMin;
        this.aTod = aTod;
        this.status = status;
    }

    public static void alert(String message) {
        System.out.println(message);
    }

    public void snooze() {
        if (status) {
            if (aMin + 9 == 60) {
                aMin = (aMin + 9) - 60;
                aHour++;
            } else {
                aMin += 9;
            }
            System.out.println("Alarm has been snoozed.");
        } else {
            System.out.println("The alarm is off! It can't be snoozed!");
        }
    }

    public void changeStatus() {
        if (status) {
            status = false;
            System.out.println("Alarm has been turned off.");
        } else {
            status = true;
            System.out.println("Alarm has been turned on.");
        }
    }
}
