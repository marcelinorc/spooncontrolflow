package fr.inria.dataflow;

/**
 * Created by marodrig on 12/11/2015.
 */
public abstract class ControlFlowVisitorBackwards extends AbstractControlFlowVisitor {


    /**
     * Runs the graph backwards starting from a node visiting all nodes until
     * it reach the begin or some node signals an stop
     *
     * @param statementNode Statement node to stop
     */
    /*
    public void run(ControlFlowNode statementNode) throws InvalidArgumentException {

        Set<ControlFlowNode> visited = new HashSet<>();
        Stack<ControlFlowNode> stack = new Stack<>();

        if (statementNode.getParent() == null) {
            throw new InvalidArgumentException(new String[]{"The node has no parent"});
        }
        ControlFlowGraph graph = statementNode.getParent();

        stack.push(graph.findNodesOfKind(BranchKind.BEGIN).get(0));
        do {
            ControlFlowNode n = stack.pop();

            //Don't do anything on the begin node
            if (n.getKind().equals(BranchKind.BEGIN)) continue;

            //Skip this node if we have already visited
            if (visited.contains(n)) continue;
            else visited.add(n);

            if ( visit(n) ) return;

            //Visit backwards
            for (ControlFlowEdge e : graph.incomingEdgesOf(n)) {
                n = e.getSourceNode();
                stack.push(n);
            }
        } while (!stack.empty());
    }

    public void doRun(ControlFlowNode n, Set<ControlFlowNode> visited) {

    }*/

}
