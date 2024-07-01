package ann.neuralnetwork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Backup
{
    private List<Integer> topology;
    private double learningRate;
    private List<List<List<Double>>> weights;

    public Backup()
    {

    }

    public Backup(List<Integer> topology, double learningRate, List<List<List<Double>>> weights)
    {
        this.topology = topology;
        this.learningRate = learningRate;
        this.weights = weights;
    }

    public List<Integer> getTopology()
    {
        return topology;
    }

    public double getLearningRate()
    {
        return learningRate;
    }

    public List<List<List<Double>>> getWeights()
    {
        return weights;
    }

    public boolean saveToFile(String path)
    {
        boolean success = true;
        try (FileWriter writer = new FileWriter(path))
        {
            writer.write(((Integer) topology.size()).toString());
            writer.write("\r\n");
            for (Integer size : topology)
            {
                writer.write(size.toString());
                writer.write(" ");
            }
            writer.write("\r\n");
            writer.write(((Double) learningRate).toString());
            writer.write("\r\n\r\n\r\n");
            for (int i = 1; i < weights.size(); ++i)
            {
                List<List<Double>> layer = weights.get(i);
                for (int j = 0; j < layer.size(); ++j)
                {
                    List<Double> neuron = layer.get(j);
                    for (int k = 0; k < neuron.size(); ++k)
                    {
                        Double connection = neuron.get(k);
                        writer.write(connection.toString());
                        writer.write(" ");
                    }
                    writer.write("\r\n");
                }
                writer.write("\r\n");
            }
        }
        catch (IOException e)
        {
            success = false;
        }
        return success;
    }

    public boolean readFromFile(String path)
    {
        boolean success = true;

        Scanner scanner = null;
        try
        {
            File file = new File(path);
            scanner = new Scanner(file);

            /* Network Topology */
            int networkSize = scanner.nextInt();
            topology = new ArrayList<>(networkSize);
            for (int i = 0; i < networkSize; ++i)
            {
                topology.add(scanner.nextInt());
            }

            /* Learning Rate */
            
            learningRate = Double.parseDouble(scanner.next());

            /* Connection Weights */
            
            weights = new ArrayList<>(networkSize);
            
            /* Input Layer */
            
            List<List<Double>> inputLayer = new ArrayList<>(topology.getFirst());
            for (int j = 0; j < topology.getFirst(); ++j)
            {
                List<Double> list = Arrays.asList(0.0);
                inputLayer.add(list);
            }
            weights.add(inputLayer);
            for (int i = 1; i < networkSize; ++i)
            {
                /* Layer */
                
                int thisLayerSize = topology.get(i);
                int prevLayerSize = topology.get(i - 1);
                List<List<Double>> layerWeights = new ArrayList<>(thisLayerSize);
                for (int j = 0; j < thisLayerSize; ++j)
                {
                    /* Neuron */
                    
                    List<Double> neuronWeights = new ArrayList<>(prevLayerSize + 1);
                    for (int k = 0; k < prevLayerSize; ++k)
                    {
                        /* Connection */
                        
                        double connectionWeight = Double.parseDouble(scanner.next());
                        neuronWeights.add(connectionWeight);
                    }
                    
                    /* Bias */
                    
                    double bias = Double.parseDouble(scanner.next());
                    neuronWeights.add(bias);
                    
                    layerWeights.add(neuronWeights);
                }
                weights.add(layerWeights);
            }
        }
        catch (Exception e)
        {
            success = false;
        }
        if (scanner != null)
        {
            scanner.close();
        }

        return success;
    }
}
