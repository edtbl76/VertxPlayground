package Ignite_8.ComputeGrid_3;

import org.apache.ignite.IgniteException;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.apache.ignite.lang.IgniteBiTuple;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ComputeClientTask extends ComputeTaskSplitAdapter<Collection<BinaryObject>, Long> {

    @Override
    protected Collection<? extends ComputeJob> split(
            int i,
            Collection<BinaryObject> binaryObjects) throws IgniteException {

        Collection<ComputeClientJob> jobs = new ArrayList<>();
        Collection<BinaryObject> employees = new ArrayList<>();

        for (BinaryObject bo : binaryObjects) {
            employees.add(bo);

            if(employees.size() == 3) {
                jobs.add(new ComputeClientJob(employees));
                employees = new ArrayList<>(3);
            }
        }

        if (!employees.isEmpty())
            jobs.add(new ComputeClientJob(employees));

        return jobs;
    }

    @Nullable
    @Override
    public Long reduce(List<ComputeJobResult> list) throws IgniteException {
        long sum = 0;
        int count = 0;

        for (ComputeJobResult result : list) {
            IgniteBiTuple<Long, Integer> tuple = result.getData();
            sum += tuple.get1();
            count += tuple.get2();
        }
        return sum / count;
    }

    private static class ComputeClientJob extends ComputeJobAdapter {
        private final Collection<BinaryObject> employees;

        private ComputeClientJob(Collection<BinaryObject> employees) {
            this.employees = employees;
        }

        @Override
        public Object execute() throws IgniteException {
            long sum = 0;
            int count = 0;

            for (BinaryObject employee: employees) {
                System.out.println("Processing employee: " + employee.field("name"));

                // NOTE: we aren't deserializing the entire object... just the pieces we need!
                long salary  = employee.field("salary");
                sum += salary;
                count++;
            }

            return new IgniteBiTuple<>(sum, count);
        }
    }
}
