package Ignite_8.ComputeGrid_3;

import Util.IgniteModels.Address;
import Util.IgniteModels.Employee;
import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;

import java.util.Arrays;
import java.util.Collection;

public class ComputeBinTaskExec extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runClusteredExample(ComputeBinTaskExec.class);
    }

    @Override
    public void start() {
        try (Ignite ignite = Ignition.start()) {
            System.out.println("Binary Task Execution Example: ");

            if (ignite.cluster().forRemotes().nodes().isEmpty()) {
                System.out.println("Start a remote node");
            }

            Collection<Employee> seahawks = getEmployees();
            seahawks.forEach(System.out::println);

            Collection<BinaryObject> bins = ignite.binary().toBinary(seahawks);

            Long averageSalary = ignite.compute(
                    ignite.cluster().forRemotes()).execute(new ComputeClientTask(), bins);

            System.out.println("Average Salary: " + averageSalary);
        }
    }

    private static Collection<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        "Russell Wilson",
                        1000000,
                        new Address("800 Occidental Ave ", 98134),
                        Arrays.asList("Quarterbacks", "Offense")
                ),
                new Employee(
                        "Chris Carson",
                        100000,
                        new Address("800 Occidental Ave ", 98134),
                        Arrays.asList("RunningBacks", "Offense")
                ),
                new Employee(
                        "Marshawn Lynch",
                        100000,
                        new Address("800 Occidental Ave ", 98134),
                        Arrays.asList("RunningBacks", "Offense")
                ),
                new Employee(
                        "Tyler Lockett",
                        100000,
                        new Address("800 Occidental Ave ", 98134),
                        Arrays.asList("WideReceivers", "Offense")
                ),
                new Employee(
                        "D.K. Metcalf",
                        100000,
                        new Address("800 Occidental Ave ", 98134),
                        Arrays.asList("WideReceivers", "Offense")
                )
        );
    }
}
