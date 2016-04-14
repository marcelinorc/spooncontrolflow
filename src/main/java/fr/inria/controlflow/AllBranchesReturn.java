package fr.inria.controlflow;

import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtElement;

import java.util.List;
import java.util.Set;

/**
 * An algorithm that takes a CtElement, builds a graph for it and checks that all branches return
 * something or that there is no branch returns anything
 * <p/>
 * Created by marodrig on 04/01/2016.
 */
public class AllBranchesReturn {

    /**
     * Finds if all branches returns
     *
     * @param element
     * @return True if all branches return or none return
     */
    public boolean execute(CtElement element) {
        ControlFlowBuilder builder = new ControlFlowBuilder();
        ControlFlowGraph graph = builder.build(element);
        graph.simplify();
        //System.out.println(graph.toGraphVisText());
        //System.out.println(graph.toGraphVisText());


        List<ControlFlowNode> exits = graph.findNodesOfKind(BranchKind.EXIT);

        int returnCount = 0;
        int incomingCount = -1;
        for (ControlFlowNode n : exits) {
            Set<ControlFlowEdge> edges = graph.incomingEdgesOf(n);
            incomingCount = edges.size();
            for (ControlFlowEdge in : edges) {
                if (in.getSourceNode().getStatement() != null &&
                        in.getSourceNode().getStatement() instanceof CtReturn) returnCount++;
            }
        }
        return returnCount == incomingCount || returnCount == 0;
    }

}
