package fr.inria.controlflow;

import java.util.HashMap;

import static fr.inria.controlflow.BranchKind.*;

/**
 * Prints the control flow in .Dot for GraphVis to visualize
 * <p/>
 * Created by marodrig on 14/10/2015.
 */
public class GraphVisPrettyPrinter {

    private final ControlFlowGraph graph;

    public GraphVisPrettyPrinter(ControlFlowGraph graph) {
        this.graph = graph;
    }

    public String print() {
        StringBuilder sb = new StringBuilder("digraph ").append(graph.getName()).append(" { \n");
        //sb.append("exit [shape=doublecircle];\n");
        sb.append("node [fontsize = 8];\n");


        int i = 0;
        HashMap<ControlFlowNode, Integer> nodeIds = new HashMap<ControlFlowNode, Integer>();
        for (ControlFlowNode n : graph.vertexSet()) {
            printNode(++i, n, sb);
            nodeIds.put(n, i);
        }

        for (ControlFlowEdge e : graph.edgeSet()) {
            if (e.isBackEdge())
                sb.append(nodeIds.get(e.getSourceNode())).append(" -> ").
                        append(nodeIds.get(e.getTargetNode())).append("[style=dashed];\n ");
            else
                sb.append(nodeIds.get(e.getSourceNode())).append(" -> ").
                        append(nodeIds.get(e.getTargetNode())).append(" ;\n ");
        }

        sb.append("\n }");
        return sb.toString();
    }


    private String printNode(int i, ControlFlowNode n, StringBuilder sb) {
        String labelStr = " [shape=rectangle, label=\"";
        if (n.getKind() == BRANCH) labelStr = " [shape=diamond, label=\"";
        else if (n.getKind() == BEGIN) labelStr = " [shape=Mdiamond, label=\"";
        else if (n.getKind() == BLOCK_BEGIN || n.getKind() == BLOCK_END)
            labelStr = " [shape=rectangle, style=filled, fillcolor=gray, label=\"";
        else if (n.getKind() == EXIT) labelStr = " [shape=doublecircle, label=\"";
        else if (n.getKind() == CONVERGE) labelStr = " [shape=point label=\"";

        sb.append(i).append(labelStr).append(n.toString().replace("\"", "quot ")).append(" \"]").append(";\n");
        return sb.toString();
    }

}
