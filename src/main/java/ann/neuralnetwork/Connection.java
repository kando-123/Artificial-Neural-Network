package ann.neuralnetwork;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a connection between two neurons in a neural network.
 */
public class Connection implements Serializable
{
    /**
     * The weight of the connection.
     */
    public double weight;
    /**
     * The tail neuron of the connection.
     */
    public final Neuron tail;
    /**
     * The head neuron of the connection.
     */
    public final Neuron head;

    /**
     * The random number generator.
     */
    private static final Random random = new Random();

    /**
     * Constructs a new connection with the specified tail and head neurons.
     *
     * @param tail the tail neuron.
     * @param head the head neuron.
     */
    private Connection(Neuron tail, Neuron head)
    {
        weight = random.nextDouble(-1.0, +1.0);
        this.tail = tail;
        this.head = head;
    }

    /**
     * Joins the specified neurons together.
     *
     * @param tail the tail neuron.
     * @param head the head neuron.
     */
    public static void joinNeurons(Neuron tail, Neuron head)
    {
        Connection connection = new Connection(tail, head);
        tail.addOutputConnection(connection);
        head.addInputConnection(connection);
    }

    /**
     * Method provides a string representation of the connection.
     *
     * @return the string representation of the connection.
     */
    @Override
    public String toString()
    {
        return String.format("Connection[weight = %.3f]", weight);
    }

    /**
     * Method for deserializing the connection.
     *
     * @param newWeight the new weight of the connection.
     */
    public void deserialize(double newWeight)
    {
        weight = newWeight;
    }

    /**
     * Method for serializing the connection.
     *
     * @return the weight of the connection.
     */
    public double serialize()
    {
        return weight;
    }
}
