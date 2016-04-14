package fr.inria.controlflow;

import org.junit.Ignore;
import org.junit.Test;
import spoon.processing.AbstractProcessor;
import spoon.processing.ProcessingManager;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import static fr.inria.controlflow.BranchKind.BRANCH;
import static fr.inria.controlflow.BranchKind.STATEMENT;
import static fr.inria.controlflow.ForwardFlowBuilderVisitorTest.buildGraph;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by marodrig on 14/10/2015.
 */
public class ForwardFlowBuilderVisitorTest {

    public static ControlFlowGraph buildGraph(String folder, final String methodName, boolean simplify)
            throws Exception {
        final ControlFlowBuilder visitor = new ControlFlowBuilder();

        Factory factory = new SpoonMetaFactory().buildNewFactory(folder, 5);
        ProcessingManager pm = new QueueProcessingManager(factory);
        pm.addProcessor(new AbstractProcessor<CtMethod>() {
            @Override
            public void process(CtMethod element) {
                if (element.getSimpleName().equals(methodName)) {
                    visitor.build(element);
                }
            }

        });
        pm.process();


        ControlFlowGraph graph = visitor.getResult();
        if (simplify) graph.simplifyConvergenceNodes();

        System.out.println(graph.toGraphVisText());

        return graph;
    }

    public ControlFlowGraph testMethod(final String methodName, boolean simplify,
                                       Integer branchCount, Integer stmntCount, Integer totalCount) throws Exception {
        ControlFlowGraph graph = buildGraph(
                this.getClass().getResource("/control-flow").toURI().getPath(), methodName, simplify);
        if (branchCount != null) assertEquals((int) branchCount, graph.branchCount());
        if (stmntCount != null) assertEquals((int) stmntCount, graph.statementCount());
        if (totalCount != null) assertEquals((int) totalCount, graph.vertexSet().size());

        return graph;

        /*
        PrintWriter out = new PrintWriter("C:\\MarcelStuff\\DATA\\graph.dot");
        out.println(graph.toGraphVisText());
        out.close();

        graph.simplifyConvergenceNodes();
        PrintWriter out2 = new PrintWriter("C:\\MarcelStuff\\DATA\\graphsimplified.dot");
        out2.println(graph.toGraphVisText());
        out2.close();*/

    }


    /**
     * Test some topology properties
     *
     * @param graph                   Graph to test
     * @param edgesBranchesStatement  Number of edges going from branches to statements
     * @param edgesBranchesBranches   Number of edges going from branches to branches
     * @param edgesStatementStatement Number of edges going from statements to statements
     * @param totalEdges              Total number of edges
     */
    private void testEdges(ControlFlowGraph graph, Integer edgesBranchesStatement, Integer edgesBranchesBranches,
                           Integer edgesStatementStatement, Integer totalEdges) {

        int bs, bb, ss;
        bs = bb = ss = 0;

        for (ControlFlowEdge e : graph.edgeSet()) {
            if (e.getSourceNode().getKind() == BRANCH && e.getTargetNode().getKind() == STATEMENT) bs++;
            else if (e.getSourceNode().getKind() == BRANCH && e.getTargetNode().getKind() == BRANCH) bb++;
            else if (e.getSourceNode().getKind() == STATEMENT && e.getTargetNode().getKind() == STATEMENT) ss++;
        }

        if (edgesBranchesStatement != null) {
            assertEquals((int) edgesBranchesStatement, bs);
        }

        if (edgesBranchesBranches != null) {
            assertEquals((int) edgesBranchesBranches, bb);
        }

        if (edgesStatementStatement != null) {
            assertEquals((int) edgesStatementStatement, ss);
        }

        if (totalEdges != null) assertEquals((int) totalEdges, graph.edgeSet().size());
    }

    /*
    @Ignore("")
    @Test
    public void testBreakComplex() throws Exception {
        testMethod("continueAndBreak", true, 6,22,39);
    }*/

    @Test
    public void testBreakSimpleLabeled() throws Exception {
        testMethod("simpleBreakLabeled", true, 3, 12, 25);
    }

    @Test
    public void testBreakSimple() throws Exception {
        testMethod("simpleBreak", true, 3, 12, 25);
//        fail();
    }


    @Test
    public void testContinueSimple() throws Exception {
        testMethod("simpleContinueTo", true, 3, 6, 17);
    }

    //Test some mixed conditions
    @Test
    public void testifThenElseBlock() throws Exception {
        testMethod("simple", false, 0, 2, 6);
    }

    //Test some mixed conditions
    @Test
    public void testSwitch() throws Exception {
        testMethod("switchTest", false, 1, 13, 28);
    }

    //Test some mixed conditions
    @Test
    public void testSimple() throws Exception {
        ControlFlowGraph graph = testMethod("simple", false, 0, 2, 6);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 0, 0, 1, 5);
    }

    //Test some mixed conditions

    @Test
    public void testMixed() throws Exception {
        //int branchCount, int stmntCount, int totalCount
        ControlFlowGraph graph = testMethod("mixed", false, 2, 5, 15);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 1, 0, 1, null);
    }

    @Test
    public void testMixedSimplified() throws Exception {
        ControlFlowGraph graph = testMethod("mixed", true, 2, 5, 13);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 3, 0, 1, null);
    }

    @Test
    public void testCtFor() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctFor", true, 1, 4, 9);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 2, 0, 1, null);
    }

    @Test
    public void testCtForBlock() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctForBlock", true, 1, 5, 12);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 1, 0, 1, null);
    }

    @Test
    public void testIfThenBlock() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ifThenBlock", true, 1, 3, 10);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 1, 0, 1, null);
    }

    @Test
    public void testIfThenElse() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ifThenElse", true, 1, 3, 8);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 2, 0, 1, null);
    }

    @Test
    public void testIfThen() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ifThen", true, 1, 2, 7);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 2, 0, 0, null);
    }

    @Test
    public void testCtForEachBlock() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctForEachBlock", true, 1, 5, 12);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 1, 0, 2, null);
    }

    @Test
    public void testCtForEach() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctForEach", true, 1, 4, 9);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 2, 0, 1, null);
    }

    @Test
    public void testCtWhileBlock() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctWhileBlock", false, 1, 5, 13);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 0, 0, 2, null);
    }

    @Test
    public void testCtWhileBlockSimplify() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctWhileBlock", true, 1, 5, 12);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 1, 0, 2, null);
    }

    @Test
    public void testCtWhile() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctWhile", true, 1, 4, 9);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 2, 0, 1, null);
    }

    @Test
    public void testCtDoWhileBlock() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctDoWhileBlock", false, 1, 5, 14);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 0, 0, 2, null);
    }

    @Test
    public void testCtDoWhileBlockSimplify() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctDoWhileBlock", true, 1, 5, 12);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 1, 0, 2, null);
    }

    @Test
    public void testCtDoWhile() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("ctDoWhile", false, 1, 4, 11);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 0, 0, 1, null);
    }

    @Test
    public void testConditional() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("conditional", false, 1, 3, 9);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 2, 0, 0, null);
    }

    @Test
    public void testNestedConditional() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("nestedConditional", false, 2, 5, 13);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 3, 1, 0, null);
    }

    @Test
    public void testNestedIf() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("nestedIfs", false, 3, 6, null);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 2, 0, 2, null);
    }

    @Test
    public void testInvocation() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("invocation", true, 1, 2, null);
        //Branches-Statement Branches-Branches sStatement-Statement total
        testEdges(graph, 1, 0, 0, null);
    }

    @Test
    public void testtestCase() throws Exception {
        //branchCount, stmntCount, totalCount
        ControlFlowGraph graph = testMethod("complex1", true, null, null, null);
        graph.simplifyBlockNodes();
        System.out.println(graph.toGraphVisText());
        //Branches-Statement Branches-Branches sStatement-Statement total
        //testEdges(graph, 2, 0, 2, null);
    }


}
