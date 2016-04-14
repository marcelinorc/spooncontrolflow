package fr.inria.controlflow;

import org.jgrapht.DirectedGraph;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.*;
import spoon.reflect.visitor.CtVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Parent for all transfer function visitors.
 *
 * These visitors are meant to be the transfer functions of control flow nodes.
 *
 * Created by marodrig on 13/10/2015.
 */
public abstract class TransferFunctionVisitor implements CtVisitor {

    /**
     * Control graph over the data flow is being exists
     */
    DirectedGraph<CtStatement, CtStatement> controlGraph;

    /**
     * Outputs of the statement calling the transfer.
     */
    protected List<Value> output;

    public DirectedGraph<CtStatement, CtStatement> getControlGraph() {
        return controlGraph;
    }

    public void setControlGraph(DirectedGraph<CtStatement, CtStatement> controlGraph) {
        this.controlGraph = controlGraph;
    }

    /**
     * Output of the last node that called the transfer function
     *
     * @return
     */
    public List<Value> getOutput() {
        if ( output == null ) output = new ArrayList<Value>();
        return output;
    }

    public List<Value> transfer(CtElement statement) {
        output = new ArrayList<Value>();
        statement.accept(this);
        return output;
    }



}
