package tdp.exercises;

public class Employee extends Person {
    private final int id;
    private final boolean active;

    public Employee(int id, String name, boolean active) {
        super(name);
        this.id = id;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }
}
