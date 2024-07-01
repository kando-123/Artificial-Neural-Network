package ann.neuralnetwork;

import java.io.Serializable;
import java.util.*;

public class Layer implements Serializable
{
    private final List<Neuron> neurons;

    public Layer(int size, double learningRate)
    {
        assert (size > 0);

        neurons = new ArrayList<>(size);
        for (int i = 0; i < size; ++i)
        {
            neurons.add(new Neuron(learningRate));
        }
    } 

    public static void joinLayers(Layer prev, Layer next)
    {
        assert (prev != null && next != null);

        for (var tail : prev.neurons)
        {
            for (var head : next.neurons)
            {
                Connection.joinNeurons(tail, head);
            }
        }
    }

    public void assign(List<Double> input)
    {
        assert (input.size() == neurons.size());

        int i = 0;
        for (var neuron : neurons)
        {
            neuron.setValue(input.get(i++));
        }
    }

    public void computeValues()
    {
        for (var neuron : neurons)
        {
            neuron.computeValue();
        }
        
//        List<Thread> threads = new ArrayList<>(neurons.size());
//        for (var neuron : neurons)
//        {
//            Thread thread = new Thread(() -> neuron.computeValue());
//            thread.start();
//            threads.add(thread);
//        }
//        for (var thread : threads)
//        {
//            try
//            {
//                thread.join();
//            }
//            catch (InterruptedException e)
//            {
//                System.err.println(e);
//            }
//        }
    }

    public List<Double> exportValues()
    {
        List<Double> result = new ArrayList<>(neurons.size());
        for (var neuron : neurons)
        {
            result.add(neuron.getValue());
        }
        return result;
    }

    public double calculateError(List<Double> desiredOutputs)
    {
        double aggregateError = 0.0;
        for (int i = 0; i < desiredOutputs.size(); ++i)
        {
            double partialError = neurons.get(i).getValue() - desiredOutputs.get(i);
            aggregateError += partialError * partialError;
        }
        return aggregateError;
    }

    public void computeOutputGradients(List<Double> desiredOutputs)
    {
        assert (desiredOutputs.size() == neurons.size());
        
        int i = 0;
        for (var neuron : neurons)
        {
            neuron.computeOutputGradient(desiredOutputs.get(i++));
        }
        
//        int i = 0;
//        List<Thread> threads = new ArrayList<>(neurons.size());
//        for (var neuron : neurons)
//        {
//            Double desiredOutput = desiredOutputs.get(i++);
//            Thread thread = new Thread(() -> neuron.computeOutputGradient(desiredOutput));
//            thread.start();
//            threads.add(thread);
//        }
//        for (var thread : threads)
//        {
//            try
//            {
//                thread.join();
//            }
//            catch (InterruptedException e)
//            {
//                System.err.println(e);
//            }
//        }
    }

    public void computeHiddenGradients()
    {
        for (var neuron : neurons)
        {
            neuron.computeHiddenGradient();
        }
        
//        List<Thread> threads = new ArrayList<>(neurons.size());
//        for (var neuron : neurons)
//        {
//            Thread thread = new Thread(() -> neuron.computeHiddenGradient());
//            thread.start();
//            threads.add(thread);
//        }
//        for (var thread : threads)
//        {
//            try
//            {
//                thread.join();
//            }
//            catch (InterruptedException e)
//            {
//                System.err.println(e);
//            }
//        }
    }

    public void updateInputs()
    {
        for (var neuron : neurons)
        {
            neuron.updateInputs();
        }
        
//        List<Thread> threads = new ArrayList<>(neurons.size());
//        for (var neuron : neurons)
//        {
//            Thread thread = new Thread(() -> neuron.updateInputs());
//            thread.start();
//            threads.add(thread);
//        }
//        for (var thread : threads)
//        {
//            try
//            {
//                thread.join();
//            }
//            catch (InterruptedException e)
//            {
//                System.err.println(e);
//            }
//        }
    }

    @Override
    public String toString()
    {
        StringBuilder description = new StringBuilder();
        description.append("Layer[")
                .append(neurons.size())
                .append(" neuron(s)]\n");
        for (var neuron : neurons)
        {
            description.append(neuron.toString());
        }

        return description.toString();
    }
    
    public List<List<Double>> serialize()
    {
        List<List<Double>> lists = new ArrayList<>(neurons.size());
        for (var neuron : neurons)
        {
            List<Double> list = neuron.serialize();
            lists.add(list);
        }
        return lists;
    }
    
    public int size()
    {
        return neurons.size();
    }
    
    public void deserialize(List<List<Double>> weights) throws Exception
    {
        if (weights.size() == neurons.size())
        {
            for (int i = 0; i < neurons.size(); ++i)
            {
                Neuron neuron = neurons.get(i);
                List<Double> list = weights.get(i);
                neuron.deserialize(list); // might throw
            }
        }
        else
        {
            throw new Exception("Layer.setInputWeights");
        }
    }
}
