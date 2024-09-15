import annotation.AfterSuite;
import annotation.BeforeSuite;
import annotation.Test;

public class Animal {
    private String name;
    private int age;

    public Animal() {
    }

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("    I'm before suite");
    }

    @AfterSuite
    public static void afterSuite() {
        System.out.println("    I'm after suite");
    }

    @Test(priority = 3)
    public void m3() {
        System.out.println("    I'm priority 3");
        throw new RuntimeException("Something went wrong");
    }

    @Test(priority = 20)
    public void m20() {
        System.out.println("    I'm priority 20");
        throw new RuntimeException("Something went wrong");
    }

    @Test(priority = -1)
    public void mMinusOne() {
        System.out.println("    I'm priority -1");
        throw new RuntimeException("Something went wrong");
    }

    @Test(priority = 8)
    public void m8() {
        System.out.println("    I'm priority 8");
    }


    @Test(priority = 2)
    public void m2() {
        System.out.println("    I'm priority 2");
    }

    @Test(priority = 1)
    static public void m1() {
        System.out.println("    I'm priority 1, static");
    }


    @Test
    public void defaultPriorityMethod() {
        System.out.println("    I'm priority default");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
