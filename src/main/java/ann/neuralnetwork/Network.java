package ann.neuralnetwork;

import java.io.Serializable;
import java.util.*;

public class Network implements Serializable
{
    private final List<Layer> layers;
    private final Layer inputLayer;
    private final Layer outputLayer;
    private final int inputSize;
    private final int outputSize;
    private final double learningRate;

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
    
    public Network(Backup backup) throws Exception
    {
        this(backup.getTopology(), backup.getLearningRate());
        deserialize(backup.getWeights());
    }

    private void propagateForward(List<Double> input)
    {
        assert (input.size() == inputSize);
        
        inputLayer.assign(input);
        for (int i = 1; i < layers.size(); ++i)
        {
            layers.get(i).computeValues();
        }
    }

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
        
    public void trainRecord(IORecord training) throws Exception
    {
        propagateForward(training.getInputs());
        propagateBackward(training.getOutputs());
    }
    
    public double testRecord(IORecord test) throws Exception
    {
        propagateForward(test.getInputs());
        return outputLayer.calculateError(test.getOutputs());
    }

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
