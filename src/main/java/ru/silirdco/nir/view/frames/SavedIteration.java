package ru.silirdco.nir.view.frames;

import ru.silirdco.nir.core.entities.Multioperation;

import java.util.Collection;
import java.util.Date;

public class SavedIteration {
    private Date date;
    private Collection<String> initialMultioperations;
    private Collection<String> finalMultioperations;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
