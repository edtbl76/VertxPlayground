package Util.IgniteModels;

import org.apache.ignite.cache.affinity.AffinityKey;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class Person implements Serializable {

    private static final AtomicLong ID_GEN = new AtomicLong();

    private Long id;
    private Long orgId;
    private String firstName;
    private String lastName;
    private String resume;
    double salary;

    private transient AffinityKey<Long> key;

    public Person() {}

    public Person(Org org, String firstName, String lastName, double salary, String resume) {
        id = ID_GEN.incrementAndGet();
        orgId = org.getId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.resume = resume;
    }

    public Person(Long id, Long orgId, String firstName, String lastName, double salary, String resume) {
        this.id = id;
        this.orgId = orgId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.resume = resume;
    }

    public Person(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AffinityKey<Long> key(){
        if (key == null)
            key = new AffinityKey<>(id, orgId);
        return key;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id +
                ", orgId=" + orgId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", resume='" + resume + '\'' +
                ", salary=" + salary +
                ", key=" + key +
                '}';
    }
}
