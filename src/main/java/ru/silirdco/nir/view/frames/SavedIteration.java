package ru.silirdco.nir.view.frames;

import java.util.Collection;

public class SavedIteration {
    private boolean nulll = false;
    private boolean one = false;
    private boolean universal = false;
    private boolean substitution = false;
    private boolean megaoperation = false;
    private boolean superposition = false;
    private boolean cross = false;
    private boolean union = false;
    private boolean addition = false;
    private boolean transposition = false;
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

    public boolean isNulll() {
        return nulll;
    }

    public void setNulll(boolean nulll) {
        this.nulll = nulll;
    }

    public boolean isOne() {
        return one;
    }

    public void setOne(boolean one) {
        this.one = one;
    }

    public boolean isUniversal() {
        return universal;
    }

    public void setUniversal(boolean universal) {
        this.universal = universal;
    }

    public boolean isSubstitution() {
        return substitution;
    }

    public void setSubstitution(boolean substitution) {
        this.substitution = substitution;
    }

    public boolean isMegaoperation() {
        return megaoperation;
    }

    public void setMegaoperation(boolean megaoperation) {
        this.megaoperation = megaoperation;
    }

    public boolean isSuperposition() {
        return superposition;
    }

    public void setSuperposition(boolean superposition) {
        this.superposition = superposition;
    }

    public boolean isCross() {
        return cross;
    }

    public void setCross(boolean cross) {
        this.cross = cross;
    }

    public boolean isUnion() {
        return union;
    }

    public void setUnion(boolean union) {
        this.union = union;
    }

    public boolean isAddition() {
        return addition;
    }

    public void setAddition(boolean addition) {
        this.addition = addition;
    }

    public boolean isTransposition() {
        return transposition;
    }

    public void setTransposition(boolean transposition) {
        this.transposition = transposition;
    }
}
