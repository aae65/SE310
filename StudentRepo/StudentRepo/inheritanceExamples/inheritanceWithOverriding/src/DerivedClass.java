public class DerivedClass extends BaseClass{
    @Override
    public void func1(){ System.out.println("Derived - func1"); }

    @Override
    protected void func2(){ System.out.println("Derived - func2"); }
}
