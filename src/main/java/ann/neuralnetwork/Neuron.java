package ann.neuralnetwork;

import java.util.*;

/**
 * Represents a single neuron in a neural network.
 */
public class Neuron
{

    /**
     * The input value of the neuron.
     */
    private double inputValue;
    /**
     * The output value of the neuron.
     */
    private double outputValue;
    /**
     * The input connections of the neuron.
     */
    private final List<Connection> inputConnections;
    /**
     * The output connections of the neuron.
     */
    private final List<Connection> outputConnections;
    /**
     * The gradient of the neuron.
     */
    private double gradient;
    /**
     * The bias of the neuron.
     */
    private double bias;
    /**
     * The learning rate of the neuron.
     */
    private final double rate;

    /**
     * The random number generator.
     */
    private static final Random random = new Random();

    /**
     * Constructs a new neuron with the specified learning rate.
     *
     * @param learningRate the learning rate of the neuron.
     */
    public Neuron(double learningRate)
    {
        assert (learningRate > 0.0 && learningRate < 1.0);

        rate = learningRate;
        bias = random.nextDouble(-1.0, +1.0);
        inputConnections = new ArrayList<>();
        outputConnections = new ArrayList<>();
    }

    /**
     * Adds an input connection to the neuron.
     *
     * @param connection the input connection to add.
     */
    public void addInputConnection(Connection connection)
    {
        inputConnections.add(connection);
    }

    /**
     * Adds an output connection to the neuron.
     *
     * @param connection the output connection to add.
     */
    public void addOutputConnection(Connection connection)
    {
        outputConnections.add(connection);
    }

    /**
     * Sets the value of the neuron.
     *
     * @param newValue the new value of the neuron.
     */
    public void setValue(double newValue)
    {
        inputValue = newValue;
        outputValue = transferFunction(inputValue);
    }

    /**
     * Returns the value of the neuron.
     *
     * @return the value of the neuron.
     */
    public double getValue()
    {
        return outputValue;
    }

    /**
     * Transfers the input value through the transfer function.
     *
     * @param x the input value.
     * @return the output value.
     */
    private static double transferFunction(double x)
    {
        return Math.tanh(x);
    }

    /**
     * Returns the derivative of the transfer function.
     *
     * @param x the input value.
     * @return the derivative of the transfer function.
     */
    private static double transferDerivative(double x)
    {
        double y = Math.tanh(x);
        return 1.0 - y * y;
    }

    /**
     * Computes the value of the neuron.
     */
    public void computeValue()
    {
        inputValue = bias;
        for (var connection : inputConnections)
        {
            inputValue += connection.tail.getValue() * connection.weight;
        }
        outputValue = transferFunction(inputValue);
    }

    /**
     * Computes the output gradient of the neuron.
     *
     * @param desiredOutput the desired output value.
     */
    public void computeOutputGradient(double desiredOutput)
    {
//        gradient = 2.0 * (outputValue - desiredOutput) * transferDerivative(inputValue);
        gradient = 2.0 * (outputValue - desiredOutput) * transferDerivative(outputValue);
    }

    /**
     * Computes the hidden gradient of the neuron.
     */
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

    /**
     * Returns the gradient of the neuron.
     *
     * @return the gradient of the neuron.
     */
    public double getGradient()
    {
        return gradient;
    }

    /**
     * Updates the weights of the neuron.
     */
    public void updateInputs()
    {
        for (var connection : inputConnections)
        {
            connection.weight -= rate * gradient * connection.tail.getValue();
        }
        bias -= rate * gradient;
    }

    /**
     * Method to generate a string representation of the neuron.
     *
     * @return string representation of the neuron.
     */
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

    /**
     * Serializes the neuron.
     *
     * @return the serialized neuron.
     */
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

    /**
     * Deserializes the neuron.
     *
     * @param weights the serialized neuron.
     * @throws Exception if the number of weights is invalid.
     */
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
