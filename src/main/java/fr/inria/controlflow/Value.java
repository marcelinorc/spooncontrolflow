package fr.inria.controlflow;

import spoon.reflect.declaration.CtVariable;

import java.util.ArrayList;
import java.util.IdentityHashMap;

/**
 * Value traveling the data-flow
 * <p/>
 * Created by marodrig on 13/10/2015.
 */
public class Value {


    public CtVariable getVariable() {
        return variable;
    }

    public void setVariable(CtVariable variable) {
        this.variable = variable;
    }

    /**
     * Variable holding this value
     */
    CtVariable variable;



}
