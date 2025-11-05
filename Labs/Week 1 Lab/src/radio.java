public class radio {
    public String station;
    private boolean on;
    private String volume;

    public radio(boolean on, String station) {
        this.on = on;
        this.station = station;
    }

    public void getStatus() {
        if (on) {
            System.out.println("The radio is turned on and is playing " + station + ".");
        } else {
            System.out.println("The radio is turned off.");
        }
    }
    
    public String setStation(String station) {
        return "Station has been changed to " + station + ".";
    }
    
    public String setVolume(String volume) {
        this.volume = volume;
        return "Volume has been changed to " + volume + ".";
    }

    public String setStatus(boolean on) {
        this.on = on;
        return "Status has been changed to " + on + ".";
    }

    public String getStation() {
        return "The radio is playing " + station + ".";
    }
}
