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
public class TrainXOR
{
    public static void main(String[] args)
    {
        /* Prepare the network. */
        Network network = new Network(Arrays.asList(3, 4, 4, 1), 0.05);
        
        /* Prepare the training set. */
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
        try
        {
            /* Train. */
            for (int epoch = 0; epoch < 1000; ++epoch)
            {
                for (var record : records)
                {
                    network.trainRecord(record);
                }
            }
            
            /* Backup the network. */
            Backup backup = network.serialize();
            backup.saveToFile("XOR3.txt");
            
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
            System.out.println(e);
        }
    }
}
