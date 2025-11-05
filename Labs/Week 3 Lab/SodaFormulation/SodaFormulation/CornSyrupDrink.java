public abstract class CornSyrupDrink extends CokeDrinkProduct {
    protected String sweetener = "Corn Syrup";

    public CornSyrupDrink(String drinkName) {
        this.name = drinkName;
    }

    public String getSweetener() { return this.sweetener; }
    public String getName() { return this.name; }
}
