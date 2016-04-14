package fr.inria.dataflow;

import fr.inria.controlflow.ControlFlowNode;

/**
 * Created by marodrig on 12/11/2015.
 */
public interface ControlFlowVisitor {

    /**
     * Visit a node of the graph
     * @param n Node to visit
     * @return True if the running of the graph must stop
     */
    boolean visitStatement(ControlFlowNode n);
    /**
     * Visit a node of the graph
     * @param n Node to visit
     * @return True if the running of the graph must stop
     */
    boolean visitBranch(ControlFlowNode n);
    /**
     * Visit a node of the graph
     * @param n Node to visit
     * @return True if the running of the graph must stop
     */
    boolean visitBegin(ControlFlowNode n);
    /**
     * Visit a node of the graph
     * @param n Node to visit
     * @return True if the running of the graph must stop
     */
    boolean visitBlockBegin(ControlFlowNode n);
    /**
     * Visit a node of the graph
     * @param n Node to visit
     * @return True if the running of the graph must stop
     */
    boolean visitBlockEnd(ControlFlowNode n);
    /**
     * Visit a node of the graph
     * @param n Node to visit
     * @return True if the running of the graph must stop
     */
    boolean visitConvergence(ControlFlowNode n);
    /**
     * Visit a node of the graph
     * @param n Node to visit
     * @return True if the running of the graph must stop
     */
    boolean visitExit(ControlFlowNode n);
}
