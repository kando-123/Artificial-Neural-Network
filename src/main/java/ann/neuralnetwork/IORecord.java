package ann.neuralnetwork;

import java.util.*;

/**
 * Represents a single input-output record for a neural network.
 */
public class IORecord
{
    /**
     * The input values of the record.
     */
    private final List<Double> inputs;
    /**
     * The output values of the record.
     */
    private final List<Double> outputs;

    /**
     * Constructs a new input-output record with the specified input and output values.
     *
     * @param inputs the input values.
     * @param outputs the output values.
     */
    public IORecord(List<Double> inputs, List<Double> outputs)
    {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    /**
     * Returns the input values of the record.
     *
     * @return the input values.
     */
    public List<Double> getInputs()
    {
        return inputs;
    }

    /**
     * Returns the output values of the record.
     *
     * @return the output values.
     */
    public List<Double> getOutputs()
    {
        return outputs;
    }
}
