package fr.inria.controlflow;

import org.junit.Test;
import spoon.processing.AbstractProcessor;
import spoon.processing.ProcessingManager;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.QueueProcessingManager;
import spoon.support.reflect.declaration.CtMethodImpl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by marodrig on 04/01/2016.
 */
public class AllBranchesReturnTest {
/*
    private ModifierKind getInvocationMethodVisibility(CtInvocation inv) {
        if (inv.getExecutable().getDeclaration() != null &&
                inv.getExecutable().getDeclaration() instanceof CtMethodImpl)
            return (inv.getExecutable().getDeclaration()).getVisibility();
        return null;
    }

    @Test
    public void testSegment2() throws Exception {
        final Factory factory = new SpoonMetaFactory().buildNewFactory(
                "C:\\MarcelStuff\\DATA\\DIVERSE\\input_programs\\MATH_3_2\\src\\main\\java", 7);
        //        "C:\\MarcelStuff\\DATA\\DIVERSE\\input_programs\\easymock-light-3.2\\src\\main\\javaz", 7);
        ProcessingManager pm = new QueueProcessingManager(factory);


        AbstractProcessor<CtMethod> p = new AbstractProcessor<CtMethod>() {
            @Override
            public void process(CtMethod ctMethod) {
                List<CtFor> fors = ctMethod.getElements(new TypeFilter<CtFor>(CtFor.class));
                if (ctMethod.getBody() == null || ctMethod.getBody().getStatements() == null) return;

                int size = ctMethod.getBody().getStatements().size();

                if (size > 6 || fors.size() < 1 || !hasInterfaceVariables(ctMethod) ) return;

                printMethod(ctMethod);

            }
        };

        pm.addProcessor(p);
        pm.process();
    }

    private boolean hasInterfaceVariables(CtMethod ctMethod) {
        List<CtVariableAccess> vars =
                ctMethod.getElements(new TypeFilter<CtVariableAccess>(CtVariableAccess.class));
        for ( CtVariableAccess a : vars ) {
            try {
                if (!a.getVariable().getDeclaration().getModifiers().contains(ModifierKind.FINAL) &&
                        a.getVariable().getType().isInterface()) return true;
            } catch (Exception e) {
                System.out.print(".");
            }
        }
        return false;
    }

    private void printMethod(CtMethod ctMethod) {
        System.out.println(ctMethod.getPosition().toString());
        System.out.println(ctMethod);
        //System.out.println(invName);
        System.out.println("+++++++++++++++++++++++++++++++++++++");

    }

    private void printStaticInvocations(CtMethodImpl ctMethod) {
        List<CtInvocation> invs = ctMethod.getElements(new TypeFilter<CtInvocation>(CtInvocation.class));
        boolean staticInv = true;
        boolean abstractVarAccess = false;
        String invName = "";
        for (CtInvocation inv : invs) {
            ModifierKind mk = getInvocationMethodVisibility(inv);
            if (inv.getExecutable().isStatic() &&
                    (mk == ModifierKind.PRIVATE || mk == ModifierKind.PROTECTED)) {
                invName = inv.toString();
                staticInv = true;
                break;
            }
        }
        if( staticInv) {
            System.out.println(ctMethod.getPosition().toString());
            System.out.println(ctMethod);
            System.out.println(invName);
            System.out.println("+++++++++++++++++++++++++++++++++++++");
        }
    }*/

    public void testSegment(AbstractProcessor processor) throws Exception {
        //ControlFlowGraph graph = buildGraph(this.getClass().getResource("/control-flow").toURI().getPath(),
        //        "nestedIfSomeNotReturning", false);

        Factory factory = new SpoonMetaFactory().buildNewFactory(
                this.getClass().getResource("/control-flow").toURI().getPath(), 7);
        ProcessingManager pm = new QueueProcessingManager(factory);
        pm.addProcessor(processor);
        pm.process();
    }

    @Test
    public void nestedIfSomeNotReturning() throws Exception {
        testSegment(new AbstractProcessor<CtIf>() {
            @Override
            public void process(CtIf element) {
                CtMethod m = element.getParent().getParent(CtMethod.class);
                if (m != null && m.getSimpleName().equals("nestedIfSomeNotReturning"))
                    if (element.getCondition().toString().contains("b < 1")) {
                        AllBranchesReturn alg = new AllBranchesReturn();
                        assertFalse(alg.execute(element));
                    }
            }
        });
    }

    @Test
    public void testNestedIfAllReturning() throws Exception {
        testSegment(new AbstractProcessor<CtIf>() {
            @Override
            public void process(CtIf element) {
                CtMethod m = element.getParent().getParent(CtMethod.class);
                if (m != null && m.getSimpleName().equals("nestedIfAllReturning"))
                    if (element.getCondition().toString().contains("a > 0")) {
                        AllBranchesReturn alg = new AllBranchesReturn();
                        assertTrue(alg.execute(element));
                    }
            }
        });
    }

}
