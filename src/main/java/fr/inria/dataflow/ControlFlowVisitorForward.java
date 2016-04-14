package fr.inria.dataflow;

import fr.inria.controlflow.ControlFlowNode;

/**
 * Created by marodrig on 12/11/2015.
 */
public abstract class ControlFlowVisitorForward extends AbstractControlFlowVisitor {

    /**
     * Runs the graph backwards starting from a node visiting all nodes until
     * it reach the node pased as parameters or some node signals an stop
     *
     * @param statementNode Statement node to stop
     */
    public void run(ControlFlowNode statementNode) {

        /*
        Set<ControlFlowNode> visited = new HashSet<>();
        Stack<ControlFlowNode> stack = new Stack<>();

        if (statementNode.getParent() == null) {
            throw new InvalidArgumentException(new String[]{"The node has no parent"});
        }
        ControlFlowGraph graph = statementNode.getParent();

        stack.push(graph.findNodesOfKind(BranchKind.BEGIN).get(0));
        do {
            ControlFlowNode n = stack.pop();

            //Finished when we arrive at the node
            //and the stack is empty
            if (n.equals(statementNode) && stack.empty()) return;

            //Skip this node if we have already visited
            if (n.getKind().equals(BranchKind.BEGIN)) continue;
            if (visited.contains(n)) continue;
            else visited.add(n);

            //Visit the node

            //Visit forward
            for (ControlFlowEdge e : graph.outgoingEdgesOf(n)) {
                n = e.getTargetNode();
                if (n.equals(statementNode)) return ;
                stack.push(n);
            }
        } while (!stack.empty());*/
    }

}
