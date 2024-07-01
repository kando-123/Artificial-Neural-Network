package ann.main;

import java.util.Arrays;
import java.util.List;
import ann.neuralnetwork.Backup;
import ann.neuralnetwork.IORecord;
import ann.neuralnetwork.Network;

/**
 *
 * @author Kay Jay O'Nail
 */
public class TestXOR
{
    public static void main(String... args)
    {
        /* Read the backup. */
        Backup backup = new Backup();
        if (backup.readFromFile("./XOR3.txt"))
        {
            try
            {
                /* Retrieve the network. */
                Network network = new Network(backup);

                /* Prepare the testing set. */
                List<IORecord> records = Arrays.asList(
                        new IORecord(Arrays.asList(0.0, 0.0, 0.0), Arrays.asList(0.0)),
                        new IORecord(Arrays.asList(0.0, 0.0, 1.0), Arrays.asList(1.0)),
                        new IORecord(Arrays.asList(0.0, 1.0, 1.0), Arrays.asList(0.0)),
                        new IORecord(Arrays.asList(0.0, 1.0, 0.0), Arrays.asList(1.0)),
                        new IORecord(Arrays.asList(1.0, 1.0, 0.0), Arrays.asList(0.0)),
                        new IORecord(Arrays.asList(1.0, 1.0, 1.0), Arrays.asList(1.0)),
                        new IORecord(Arrays.asList(1.0, 0.0, 1.0), Arrays.asList(0.0)),
                        new IORecord(Arrays.asList(1.0, 0.0, 0.0), Arrays.asList(1.0))
                );

                /* Test. */
                double averageError = 0.0;
                for (var record : records)
                {
                    double error = network.testRecord(record);
                    System.out.println("Particular error = %.6f".formatted(error));
                    averageError += error;
                }
                averageError /= (double) records.size();
                System.out.println("Average error = %.6f".formatted(averageError));
            }
            catch (Exception e)
            {
                System.err.println(e);
            }
        }
        else
        {
            System.err.println("File \"XOR3.txt\" was not read.");
        }
    }
}
