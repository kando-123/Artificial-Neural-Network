package ann.neuralnetwork;

import java.util.*;

public class Neuron
{
    private double inputValue;
    private double outputValue;
    private final List<Connection> inputConnections;
    private final List<Connection> outputConnections;
    private double gradient;
    private double bias;
    private final double rate;

    private static final Random random = new Random();

    public Neuron(double learningRate)
    {
        assert (learningRate > 0.0 && learningRate < 1.0);
        
        rate = learningRate;
        bias = random.nextDouble(-1.0, +1.0);
        inputConnections = new ArrayList<>();
        outputConnections = new ArrayList<>();
    }

    public void addInputConnection(Connection connection)
    {
        inputConnections.add(connection);
    }

    public void addOutputConnection(Connection connection)
    {
        outputConnections.add(connection);
    }
    
    public void setValue(double newValue)
    {
        inputValue = newValue;
        outputValue = transferFunction(inputValue);
    }
    
    public double getValue()
    {
        return outputValue;
    }

    private static double transferFunction(double x)
    {
        return Math.tanh(x);
    }

    private static double transferDerivative(double x)
    {
        double y = Math.tanh(x);
        return 1.0 - y * y;
    }

    public void computeValue()
    {
        inputValue = bias;
        for (var connection : inputConnections)
        {
            inputValue += connection.tail.getValue() * connection.weight;
        }
        outputValue = transferFunction(inputValue);
    }

    public void computeOutputGradient(double desiredOutput)
    {
//        gradient = 2.0 * (outputValue - desiredOutput) * transferDerivative(inputValue);
        gradient = 2.0 * (outputValue - desiredOutput) * transferDerivative(outputValue);
    }

    public void computeHiddenGradient()
    {
        double sum = 0.0;
        for (var connection : outputConnections)
        {
            sum += connection.head.getGradient() * connection.weight;
        }
        //gradient = sum * transferDerivative(inputValue);
        gradient = sum * transferDerivative(outputValue);
    }
    
    public double getGradient()
    {
        return gradient;
    }

    public void updateInputs()
    {
        for (var connection : inputConnections)
        {
            connection.weight -= rate * gradient * connection.tail.getValue();
        }
        bias -= rate * gradient;
    }

    @Override
    public String toString()
    {
        StringBuilder description = new StringBuilder();
        description.append("\tNeuron[")
                .append(inputConnections.size())
                .append(" input(s), ")
                .append(outputConnections.size())
                .append(" output(s), bias = ")
                .append(bias)
                .append("]\n");
        for (var connection : inputConnections)
        {
            description.append("\t (i) ")
                    .append(connection.toString())
                    .append("\n");

        }
        for (var connection : outputConnections)
        {
            description.append("\t (o) ")
                    .append(connection.toString())
                    .append("\n");
        }

        return description.toString();
    }
    
    public List<Double> serialize()
    {
        List<Double> weights = new ArrayList<>(inputConnections.size() + 1);
        for (var connection : inputConnections)
        {
            double weight = connection.serialize();
            weights.add(weight);
        }
        weights.add(bias);
        return weights;
    }
    
    public void deserialize(List<Double> weights) throws Exception
    {
        if (weights.size() == inputConnections.size() + 1)
        {
            for (int i = 0; i < inputConnections.size(); ++i)
            {
                Connection connection = inputConnections.get(i);
                double weight = weights.get(i);
                connection.deserialize(weight);
            }
            bias = weights.getLast();
        }
        else
        {
            throw new Exception("Neuron.setInputWeights");
        }
    }
}
