package data;

import struct.FlowGraph;

/**
 * Data structure containing information regarding user input & flow graph
 * @param <T> Generic type of {@link struct.Graph}
 */
public class PipelineContext<T> {
    private FlowGraph<T> system;
    private String[] inputParams;

    public PipelineContext(FlowGraph<T> system, String[] inputParams) {
        this.system = system;
        this.inputParams = inputParams;
    }

    /** Accessors & Mutators **/

    public FlowGraph<T> getSystem() {
        return system;
    }

    public String[] getInputParams() {
        return inputParams;
    }

    public void setSystem(FlowGraph<T> system) {
        this.system = system;
    }

    public void setInputParams(String[] inputParams) {
        this.inputParams = inputParams;
    }

}
