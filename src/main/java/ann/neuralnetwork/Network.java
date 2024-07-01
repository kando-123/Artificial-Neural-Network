package ann.neuralnetwork;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a neural network.
 */
public class Network implements Serializable
{
    /**
     * The layers of the network.
     */
    private final List<Layer> layers;
    /**
     * The input layer of the network.
     */
    private final Layer inputLayer;
    /**
     * The output layer of the network.
     */
    private final Layer outputLayer;
    /**
     * The size of the input layer.
     */
    private final int inputSize;
    /**
     * The size of the output layer.
     */
    private final int outputSize;
    /**
     * The learning rate of the network.
     */
    private final double learningRate;

    /**
     * Constructs a new network with the specified topology and learning rate.
     *
     * @param topology the topology of the network.
     * @param learningRate the learning rate of the network.
     */
    public Network(List<Integer> topology, double learningRate)
    {
        assert (topology.size() > 1);

        this.learningRate = learningRate;
        layers = new ArrayList<>(topology.size());
        for (var size : topology)
        {
            assert (size > 0);

            layers.add(new Layer(size, learningRate));
        }
        inputLayer = layers.get(0);
        outputLayer = layers.get(layers.size() - 1);
        inputSize = topology.get(0);
        outputSize = topology.get(topology.size() - 1);
        for (int i = 1; i < layers.size(); ++i)
        {
            Layer previous = layers.get(i - 1);
            Layer current = layers.get(i);
            Layer.joinLayers(previous, current);
        }
    }

    /**
     * Constructs a new network from the specified backup.
     *
     * @param backup the backup to construct the network from.
     * @throws Exception if the backup is invalid.
     */
    public Network(Backup backup) throws Exception
    {
        this(backup.getTopology(), backup.getLearningRate());
        deserialize(backup.getWeights());
    }

    /**
     * Propagates the input forward through the network.
     *
     * @param input the input to propagate.
     */
    private void propagateForward(List<Double> input)
    {
        assert (input.size() == inputSize);
        
        inputLayer.assign(input);
        for (int i = 1; i < layers.size(); ++i)
        {
            layers.get(i).computeValues();
        }
    }

    /**
     * Propagates the error backward through the network.
     *
     * @param desiredOutputs the desired outputs.
     */
    private void propagateBackward(List<Double> desiredOutputs)
    {
        assert (desiredOutputs.size() == outputSize);
        
        outputLayer.computeOutputGradients(desiredOutputs);
        for (int i = layers.size() - 2; i > 0; --i)
        {
            layers.get(i).computeHiddenGradients();
        }
        
        for (int i = layers.size() - 1; i > 0; --i)
        {
            layers.get(i).updateInputs();
        }
    }

    /**
     * Computes the output for the specified input.
     *
     * @param input the input to compute the output for.
     * @return the output of the network.
     * @throws Exception if the input is invalid.
     */
    public List<Double> computeFor(List<Double> input) throws Exception
    {
        if (input.size() == inputSize)
        {
            propagateForward(input);
            return outputLayer.exportValues();
        }
        else
        {
            throw new Exception("Network.computeFor : incompatible vectors");
        }
    }

    /**
     * Trains the network with the specified training record.
     *
     * @param training the training record.
     * @throws Exception if the training record is invalid.
     */
    public void trainRecord(IORecord training) throws Exception
    {
        propagateForward(training.getInputs());
        propagateBackward(training.getOutputs());
    }

    /**
     * Tests the network with the specified test record.
     *
     * @param test the test record.
     * @return the error of the network.
     * @throws Exception if the test record is invalid.
     */
    public double testRecord(IORecord test) throws Exception
    {
        propagateForward(test.getInputs());
        return outputLayer.calculateError(test.getOutputs());
    }

    /**
     * Returns the learning rate of the network.
     *
     * @return the learning rate of the network.
     */
    @Override
    public String toString()
    {
        StringBuilder description = new StringBuilder();
        description.append("Network[")
                .append(layers.size())
                .append(" layers, learningRate = ")
                .append(learningRate)
                .append("]\n");
        for (var layer : layers)
        {
            description.append(layer.toString());
        }

        return description.toString();
    }

    /**
     * Serializes the network.
     *
     * @return the serialized network.
     */
    public Backup serialize()
    {
        List<Integer> topology = new ArrayList<>(layers.size());
        for (var layer : layers)
        {
            topology.add(layer.size());
        }
        List<List<List<Double>>> lists = new ArrayList<>(layers.size());
        for (var layer : layers)
        {
            List<List<Double>> list = layer.serialize();
            lists.add(list);
        }
        return new Backup(topology, learningRate, lists);
    }

    /**
     * Deserializes the network.
     *
     * @param weights the serialized network.
     * @throws Exception if the number of weights is invalid.
     */
    public void deserialize(List<List<List<Double>>> weights) throws Exception
    {
        if (weights.size() == layers.size())
        {
            for (int i = 0; i < layers.size(); ++i)
            {
                Layer layer = layers.get(i);
                List<List<Double>> list = weights.get(i);
                layer.deserialize(list);
            }
        }
        else
        {
            throw new Exception("Network.deserialize");
        }
    }

    /**
     * Returns the topology of the network.
     *
     * @return the topology of the network.
     */
    public List<Integer> getTopology()
    {
        List<Integer> topology = new ArrayList<>(layers.size());
        for (var layer : layers)
        {
            topology.add(layer.size());
        }
        return topology;
    }
}
