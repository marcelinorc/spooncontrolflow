package fr.inria.dataflow;

import fr.inria.controlflow.ControlFlowNode;

/**
 * Created by marodrig on 13/11/2015.
 */
public abstract class AbstractControlFlowVisitor implements ControlFlowVisitor {

    protected boolean visit(ControlFlowNode n) {
        //Visit the node
        switch (n.getKind()) {
            case BEGIN:
                return visitBegin(n);
            case BLOCK_BEGIN:
                return visitBlockBegin(n);
            case BLOCK_END:
                return visitBlockEnd(n);
            case BRANCH:
                return visitBranch(n);
            case STATEMENT:
                return visitStatement(n);
            default:return false;
        }
    }

}
