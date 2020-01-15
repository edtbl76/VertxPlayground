package Util.IgniteModels;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

import java.util.Objects;

public class EmployeeKey {
    private int id;

    @AffinityKeyMapped
    private int orgId;

    public EmployeeKey() {}

    public EmployeeKey(int id, int orgId) {
        this.id = id;
        this.orgId = orgId;
    }

    public int getId() {
        return id;
    }

    public int getOrgId() {
        return orgId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeKey that = (EmployeeKey) o;
        return id == that.id &&
                orgId == that.orgId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orgId);
    }

    @Override
    public String toString() {
        return "EmployeeKey{" + "id=" + id +
                ", orgId=" + orgId +
                '}';
    }
}
