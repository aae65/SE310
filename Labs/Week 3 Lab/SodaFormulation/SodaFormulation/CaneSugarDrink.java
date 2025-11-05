public abstract class CaneSugarDrink extends CokeDrinkProduct {
    protected String sweetener = "Cane Sugar";

    public CaneSugarDrink(String drinkName) {
        this.name = drinkName;
    }

    public String getSweetener() { return this.sweetener; }
    public String getName() { return this.name; }
}
