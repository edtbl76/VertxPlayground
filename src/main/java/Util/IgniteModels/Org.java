package Util.IgniteModels;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicLong;

public class Org {

    private static final AtomicLong ID_GEN = new AtomicLong();

    private Long id;
    private String name;
    private Address addr;
    private OrgType type;

    private Timestamp lastUpdated;

    public Org() { }

    public Org(String name) {
        id = ID_GEN.incrementAndGet();
        this.name = name;
    }

    public Org(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Org(String name, Address addr, OrgType type, Timestamp lastUpdated) {
        id = ID_GEN.incrementAndGet();
        this.name = name;
        this.addr = addr;
        this.type = type;
        this.lastUpdated = lastUpdated;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }

    public OrgType getType() {
        return type;
    }

    public void setType(OrgType type) {
        this.type = type;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        String sb = "Org{" + "id=" + id +
                ", name='" + name + '\'' +
                ", addr=" + addr +
                ", type=" + type +
                ", lastUpdated=" + lastUpdated +
                '}';
        return sb;
    }
}
