package ann.neuralnetwork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a backup of a neural network.
 */
public class Backup
{
    /**
     * The topology of the network.
     */
    private List<Integer> topology;
    /**
     * The learning rate of the network.
     */
    private double learningRate;
    /**
     * The connection weights of the network.
     */
    private List<List<List<Double>>> weights;

    /**
     * Constructs a new backup.
     */
    public Backup()
    {

    }

    /**
     * Constructs a new backup with the specified topology, learning rate, and connection weights.
     *
     * @param topology the topology of the network.
     * @param learningRate the learning rate of the network.
     * @param weights the connection weights of the network.
     */
    public Backup(List<Integer> topology, double learningRate, List<List<List<Double>>> weights)
    {
        this.topology = topology;
        this.learningRate = learningRate;
        this.weights = weights;
    }

    /**
     * Returns the topology of the network.
     *
     * @return the topology of the network.
     */
    public List<Integer> getTopology()
    {
        return topology;
    }

    /**
     * Returns the learning rate of the network.
     *
     * @return the learning rate of the network.
     */
    public double getLearningRate()
    {
        return learningRate;
    }

    /**
     * Returns the connection weights of the network.
     *
     * @return the connection weights of the network.
     */
    public List<List<List<Double>>> getWeights()
    {
        return weights;
    }

    /**
     * Saves the backup to the specified file.
     *
     * @param path the path of the file to save the backup to.
     * @return true if the backup was saved successfully, false otherwise.
     */
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

    /**
     * Reads the backup from the specified file.
     *
     * @param path the path of the file to read the backup from.
     * @return true if the backup was read successfully, false otherwise.
     */
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
