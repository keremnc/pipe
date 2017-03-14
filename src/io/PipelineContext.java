package io;

import data.PipeSystem;

public class PipelineContext<T> {
    private PipeSystem<T> system;
    private String[] inputParams;

    public PipelineContext(PipeSystem<T> system, String[] inputParams) {
        this.system = system;
        this.inputParams = inputParams;
    }

    /** Accessors & Mutators **/

    public PipeSystem<T> getSystem() {
        return system;
    }

    public String[] getInputParams() {
        return inputParams;
    }

    public void setSystem(PipeSystem<T> system) {
        this.system = system;
    }

    public void setInputParams(String[] inputParams) {
        this.inputParams = inputParams;
    }

}
