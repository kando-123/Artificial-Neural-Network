package ann.neuralnetwork;

import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class IORecord
{
    private final List<Double> inputs;
    private final List<Double> outputs;
    
    public IORecord(List<Double> inputs, List<Double> outputs)
    {
        this.inputs = inputs;
        this.outputs = outputs;
    }
    
    public List<Double> getInputs()
    {
        return inputs;
    }
    
    public List<Double> getOutputs()
    {
        return outputs;
    }
}
