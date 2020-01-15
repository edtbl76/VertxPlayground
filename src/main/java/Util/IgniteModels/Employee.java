package Util.IgniteModels;

import java.util.Collection;

public class Employee {

    private String name;
    private long salary;
    private Address addr;
    private Collection<String> departments;

    public Employee() {}

    public Employee(String name, long salary, Address addr, Collection<String> departments) {
        this.name =  name;
        this.salary = salary;
        this.addr = addr;
        this.departments = departments;
    }

    public String getName() {
        return name;
    }

    public long getSalary() {
        return salary;
    }

    public Collection<String> getDepartments() {
        return departments;
    }

    public Address getAddr() {
        return addr;
    }


    @Override
    public String toString() {
        return "Employee{" + "name='" + name + '\'' +
                ", salary=" + salary +
                ", addr=" + addr +
                ", departments=" + departments +
                '}';
    }
}
