public class clock {
    public int hour;
    public int min;
    public String tod;

    public clock(int hour, int min, String tod) {
        this.hour = hour;
        this.min = min;
        this.tod = tod;

        if (min < 10) {
            System.out.println("Initial Time: " + hour + ":0" + min + " " + tod + ".");
        } else {
            System.out.println("Initial Time: " + hour + ":" + min + " " + tod + ".");
        }
    }

    void showTime() {
        if (min < 10) {
            System.out.println(hour + ":0" + min + " " + tod);
        } else {
            System.out.println(hour + ":" + min + " " + tod);
        }
    }
}
