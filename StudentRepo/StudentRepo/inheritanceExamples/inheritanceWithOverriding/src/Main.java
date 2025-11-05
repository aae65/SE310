public class Main {
    public static void main(String[] args){
        BaseClass base = new BaseClass();
        DerivedClass derived = new DerivedClass();

        // Call func1 from BaseClass
        base.func1();
        base.func2();
        base.func3();

        // Call func1 from Derived class
        derived.func1();
        derived.func2();
        derived.func3();

        // Create a BaseClass type, instantiated as a DerivedClass. What will print?
        BaseClass baseDerived = new DerivedClass();
        derived.func1();
        derived.func2();
        derived.func3();
    }
}
