public abstract class CokeDrinkProduct {
    // All products need a name, store it here to be shared by all sub-classes
    protected String name;

    abstract String getSweetener();
    abstract String getName();
}
