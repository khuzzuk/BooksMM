import java.io.File;

public class MyClass {
    MyClassDelegator delegator;

    public MyClass() {
        delegator = new MyClassDelegator();
    }

    public boolean doStuff(String name){
        return delegator.createFile(name).exists();
    }
    class MyClassDelegator{
        File createFile(String name){
            return new File(name);
        }
    }
}
