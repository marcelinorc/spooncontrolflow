package fr.inria.controlflow;

import spoon.reflect.declaration.CtElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A node of the control flow
 * <p/>
 * <p/>
 * Created by marodrig on 13/10/2015.
 */
public class ControlFlowNode {

    public static int count = 0;

    private int id;

    /**
     * Çontrol flow graph containing this node
     */
    ControlFlowGraph parent;

    public BranchKind getKind() {
        return kind;
    }

    public void setKind(BranchKind kind) {
        this.kind = kind;
    }

    private BranchKind kind;

    /**
     * Statement that is going to be pointed to by this node
     */
    CtElement statement;

    List<Value> input;

    List<Value> output;

    //An object you can tag to the node
    Object tag;

    /**
     * Visitor containing the transfer functions for each node
     */
    TransferFunctionVisitor visitor;

    public ControlFlowNode(CtElement statement, ControlFlowGraph parent, BranchKind kind) {
        this.kind = kind;
        this.parent = parent;
        this.statement = statement;
        ++count;
        id = count;
    }


    public ControlFlowNode(CtElement statement, ControlFlowGraph parent) {
        this.statement = statement;
        this.parent = parent;
        ++count;
        id = count;
    }

    /**
     * Performs the transfer using a given visitor
     *
     * @param visitor
     */
    public void transfer(TransferFunctionVisitor visitor) {
        this.visitor = visitor;
        transfer();
    }

    /**
     * Perform the transfer function
     */
    public void transfer() {
        if (statement != null && visitor != null) {
            output = visitor.transfer(statement);
        } else {
            throw new RuntimeException("Unable to perform the transfer function. Statement or visitor are null.");
        }

    }

    public int getId() {
        return id;
    }

    /**
     * Obtains the siblings of a control node. Siblings are the nodes in parallel branches
     *
     * @return
     */
    public List<ControlFlowNode> siblings() {
        ArrayList<ControlFlowNode> result = new ArrayList<ControlFlowNode>();
        for (ControlFlowNode n : prev())
            for (ControlFlowNode nn : n.next())
                if (!nn.equals(this)) result.add(nn);
        return result;
    }

    /**
     * List of nodes that can be executed just after this one
     *
     * @return
     */
    public List<ControlFlowNode> next() {
        ArrayList<ControlFlowNode> result = new ArrayList<ControlFlowNode>();
        for (ControlFlowEdge e : parent.outgoingEdgesOf(this)) {
            result.add(e.getTargetNode());
        }
        return result;
    }

    /**
     * List of nodes that could be executed just before this one
     *
     * @return
     */
    public List<ControlFlowNode> prev() {
        ArrayList<ControlFlowNode> result = new ArrayList<ControlFlowNode>();
        for (ControlFlowEdge e : parent.incomingEdgesOf(this)) {
            result.add(e.getSourceNode());
        }
        return result;
    }

    public List<Value> getOutput() {
        if (output == null) transfer();
        return output;
    }

    public CtElement getStatement() {
        return statement;
    }

    public void setStatement(CtElement statement) {
        this.statement = statement;
    }

    public List<Value> getInput() {
        return input;
    }

    public void setInput(List<Value> input) {
        this.input = input;
    }

    public ControlFlowGraph getParent() {
        return parent;
    }

    public void setParent(ControlFlowGraph parent) {
        this.parent = parent;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        if (statement != null) return id + " - " + statement.toString();
        else return kind + "_" + id;
    }

}
