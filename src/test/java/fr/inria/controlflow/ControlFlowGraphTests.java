package fr.inria.controlflow;

import org.junit.Test;
import spoon.reflect.code.CtStatement;
import spoon.support.reflect.code.CtIfImpl;

import static fr.inria.controlflow.BranchKind.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * Control flow graph tests
 *
 * Created by marodrig on 14/10/2015.
 */
public class ControlFlowGraphTests {

    @Test(expected = NotFoundException.class)
    public void testFindNodeNotFound() throws NotFoundException {
        ControlFlowGraph graph = new ControlFlowGraph();

        CtStatement s = new CtIfImpl();

        ControlFlowNode branch1 = new ControlFlowNode(null, graph, BRANCH);
        ControlFlowNode branch2 = new ControlFlowNode(null, graph, BRANCH);
        graph.addEdge(branch1, branch2);

        ControlFlowNode n = graph.findNode(s);
    }

    @Test
    public void testFindNode() throws NotFoundException {
        ControlFlowGraph graph = new ControlFlowGraph();

        CtStatement s = new CtIfImpl();
        ControlFlowNode branch1 = new ControlFlowNode(s, graph, BRANCH);
        ControlFlowNode branch2 = new ControlFlowNode(null, graph, BRANCH);
        graph.addEdge(branch1, branch2);

        assertEquals(graph.findNode(s), branch1);
    }

    /**
     *     Build this graph (* means fictitious nodes)
     *     X1 -X2 - O1
     *     |    |   |
     *     \ __*1 _/
     *          |
     *          O2
     *
     *     Simplify onto this
     *     X1 -X2 - O1
     *     |    |   |
     *     \__ O2 __/
     */
    @Test
    public void testSimplify() {
        ControlFlowGraph graph = new ControlFlowGraph();

        CtStatement s = new CtIfImpl();

        ControlFlowNode branch1 = new ControlFlowNode(s, graph, BRANCH);
        ControlFlowNode branch2 = new ControlFlowNode(null, graph, BRANCH);
        ControlFlowNode node1 = new ControlFlowNode(null, graph, STATEMENT);
        ControlFlowNode node2 = new ControlFlowNode(null, graph, STATEMENT);
        ControlFlowNode fictitious = new ControlFlowNode(null, graph, CONVERGE);

        graph.addEdge(branch1, branch2);
        graph.addEdge(branch1, fictitious);
        graph.addEdge(branch2, node1);
        graph.addEdge(branch2, fictitious);
        graph.addEdge(node1, fictitious);
        graph.addEdge(fictitious, node2);

        graph.simplifyConvergenceNodes();

        assertTrue(graph.containsEdge(branch1, node2));
        assertTrue(graph.containsEdge(branch2, node2));
        assertTrue(graph.containsEdge(node1, node2));
        assertFalse(graph.containsVertex(fictitious));
    }

    /**
    *     Build this graph (* means fictitious nodes)
    *     X1 -X2 - O1
    *     |    |   |
    *     \ __*1 _/
    *          |
    *         *2
    *          |
    *          O2
    *
    *     Simplify onto this
    *     X1 -X2 - O1
    *     |    |   |
    *     \__ O2 __/
    */
    @Test
    public void testSimplify2() {
        ControlFlowGraph graph = new ControlFlowGraph();

        ControlFlowNode branch1 = new ControlFlowNode(null, graph, BRANCH);
        ControlFlowNode branch2 = new ControlFlowNode(null, graph, BRANCH);
        ControlFlowNode node1 = new ControlFlowNode(null, graph, STATEMENT);
        ControlFlowNode node2 = new ControlFlowNode(null, graph, STATEMENT);
        ControlFlowNode fictitious = new ControlFlowNode(null, graph, CONVERGE);
        ControlFlowNode fictitious2 = new ControlFlowNode(null, graph, CONVERGE);



        graph.addEdge(branch1, branch2);
        graph.addEdge(branch1, fictitious);
        graph.addEdge(branch2, node1);
        graph.addEdge(branch2, fictitious);
        graph.addEdge(node1, fictitious);
        graph.addEdge(fictitious, fictitious2);
        graph.addEdge(fictitious2, node2);

        graph.simplifyConvergenceNodes();

        assertTrue(graph.containsEdge(branch1, node2));
        assertTrue(graph.containsEdge(branch2, node2));
        assertTrue(graph.containsEdge(node1, node2));
        assertFalse(graph.containsVertex(fictitious));
        assertFalse(graph.containsVertex(fictitious2));
    }

    @Test
    public void testCounting() {
        ControlFlowGraph graph = new ControlFlowGraph();

        ControlFlowNode branch1 = new ControlFlowNode(null, graph, BRANCH);
        ControlFlowNode branch2 = new ControlFlowNode(null, graph, BRANCH);
        ControlFlowNode node1 = new ControlFlowNode(null, graph, STATEMENT);
        ControlFlowNode node2 = new ControlFlowNode(null, graph, STATEMENT);
        ControlFlowNode node3 = new ControlFlowNode(null, graph, STATEMENT);
        ControlFlowNode fictitious = new ControlFlowNode(null, graph, CONVERGE);

        graph.addEdge(branch1, branch2);
        graph.addEdge(branch1, fictitious);
        graph.addEdge(branch2, node1);
        graph.addEdge(branch2, fictitious);
        graph.addEdge(node1, fictitious);
        graph.addEdge(fictitious, node2);
        graph.addEdge(node2, node3);

        assertEquals(2, graph.branchCount());
        assertEquals(3, graph.statementCount());
    }

}
