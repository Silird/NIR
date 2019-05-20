package ru.silirdco.nir.view.frames;

import ru.silirdco.nir.core.util.entity.Operations;

import java.util.Collection;

public class SavedIteration {
    private Operations operations;
    private String name;
    private Collection<String> initialMultioperations;
    private Collection<String> finalMultioperations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getInitialMultioperations() {
        return initialMultioperations;
    }

    public void setInitialMultioperations(Collection<String> initialMultioperations) {
        this.initialMultioperations = initialMultioperations;
    }

    public Collection<String> getFinalMultioperations() {
        return finalMultioperations;
    }

    public void setFinalMultioperations(Collection<String> finalMultioperations) {
        this.finalMultioperations = finalMultioperations;
    }

    public Operations getOperations() {
        return operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }
}
