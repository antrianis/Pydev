package org.python.pydev.parser;

import org.python.pydev.core.ICallback;
import org.python.pydev.core.Tuple;
import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Assign;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.Expr;
import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.Import;
import org.python.pydev.parser.jython.ast.ImportFrom;
import org.python.pydev.parser.jython.ast.Module;
import org.python.pydev.parser.visitors.NodeUtils;

public class PyParserErrorsTest extends PyParserTestBase {

    public static void main(String[] args) {
        try {
            PyParserErrorsTest test = new PyParserErrorsTest();
            test.setUp();
            test.testErrorHandled15();
            test.tearDown();
            System.out.println("Finished");
            junit.textui.TestRunner.run(PyParserErrorsTest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PyParser.USE_FAST_STREAM = true;
    }
    

    public void testSuccessWithError() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "class A:\n" +
                    "    def method1(self, *args, **kwargs):\n" +
                    "        "; 
        
                    
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                ClassDef c = (ClassDef) m.body[0];
                FunctionDef func = (FunctionDef) c.body[0];
                assertEquals("method1", NodeUtils.getRepresentationString(func));
                return true;
            }
        });
    }
    
    
    public void testErrorHandled0() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "a = 10\n" +
                    "a."; 
                
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                Assign assign = (Assign) m.body[0];
                assertNotNull(assign);
                Expr expr = (Expr) m.body[1];
                Attribute attr = (Attribute)expr.value;
                assertEquals("a.!<MissingName>!", NodeUtils.getFullRepresentationString(attr));
                return true;
            }
        });
    }
    
    public void testErrorHandled() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
                String s = ""+
                    "class C:             \n" +  
                    "                     \n" +    
                    "    def makeit(self):\n" +     
                    "        pass         \n" +     
                    "                     \n" +       
                    "class D(C.:          \n" +  
                    "                     \n" +    
                    "    def a(self):     \n" +   
                    "        pass         \n";        
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                ClassDef d = (ClassDef) m.body[1];
                assertEquals("D", NodeUtils.getRepresentationString(d));
                return true;
            }
        });
    }
    
    
    public void testErrorHandled2() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
        
                String s = ""+
                "class Test(unit \n" +
                "                \n" +
                "    def meth1():\n" +
                "        pass    \n";
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                ClassDef c = (ClassDef) m.body[0];
                assertEquals(1, c.body.length);
                FunctionDef f = (FunctionDef) c.body[0];
                assertEquals("meth1", NodeUtils.getRepresentationString(f));
                return true;
            }
        });
    }
    
    
    public void testErrorHandled3() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
    
                String s = ""+
                "class Test(unit \n" +
                "                \n" +
                "def meth1():\n" +
                "    pass    \n";
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(2, m.body.length);
                ClassDef c = (ClassDef) m.body[0];
                assertEquals("Test", NodeUtils.getRepresentationString(c));
                FunctionDef f = (FunctionDef) m.body[1];
                assertEquals("meth1", NodeUtils.getRepresentationString(f));
                return true;
            }
        });
    }
    
    
    public void testErrorHandled4() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "class A:\n" +
                    "    def method1(self, *args, **kwargs):\n" +
                    "        "; 
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                ClassDef c = (ClassDef) m.body[0];
                assertEquals("A", NodeUtils.getRepresentationString(c));
                FunctionDef f = (FunctionDef) c.body[0];
                assertEquals("method1", NodeUtils.getRepresentationString(f));
                return true;
            }
        });
    }
    
    public void testErrorHandled5() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "import Imp\n" +
                    "\n" +
                    "eu s\n" +
                    ""; 
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertTrue(m.body.length > 0);
                Import c = (Import) m.body[0];
                assertEquals("Imp", NodeUtils.getRepresentationString(c.names[0]));
                return true;
            }
        });
    }
    
    public void testErrorHandled6() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "a = [\n" +
                    "1, 2, \n" +
                    "\n" +
                    ""; 
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                Assign assign = (Assign) m.body[0];
                assertEquals("a", NodeUtils.getRepresentationString(assign.targets[0]));
                assertEquals("[]", NodeUtils.getRepresentationString(assign.value));
                return true;
            }
        });
    }
    
    
    public void testErrorHandled7() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "a = {\n" +
                    "1: 2, \n" +
                    "\n" +
                    ""; 
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                Assign assign = (Assign) m.body[0];
                assertEquals("a", NodeUtils.getRepresentationString(assign.targets[0]));
                assertEquals("{}", NodeUtils.getRepresentationString(assign.value));
                return true;
            }
        });
    }
    
    public void testErrorHandled8() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "a = {\n" +
                    "1: \n" +
                    "\n" +
                    ""; 
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                Assign assign = (Assign) m.body[0];
                assertEquals("a", NodeUtils.getRepresentationString(assign.targets[0]));
                assertEquals("{}", NodeUtils.getRepresentationString(assign.value));
                return true;
            }
        });
    }
    
    public void testErrorHandled9() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "def m1(a b):\n" +
                    "\n" +
                    "\n" +
                    ""; 
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                FunctionDef func = (FunctionDef) m.body[0];
                assertEquals("m1", NodeUtils.getRepresentationString(func));
                return true;
            }
        });
    }
    
    
    public void testErrorHandled10() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
    
                String s = 
                    "class drDropTarget(bbb uehos):\n" +
                    "    def __init__(self, window):\n" +
                    "        a = {ao: window. \n" +
                    "            window.,\n" +
                    "            windo\n" +
                    "            }\n" +
                    ""; 
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                ClassDef cdef = (ClassDef) m.body[0];
                assertEquals("drDropTarget", NodeUtils.getRepresentationString(cdef));
                assertEquals(1, cdef.body.length);
                FunctionDef fdef = (FunctionDef) cdef.body[0];
                assertEquals("__init__", NodeUtils.getRepresentationString(fdef));
                return true;
            }
        });
    }
    
    
    public void testErrorHandled11() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
                String s = 
                    "class drDropTarget(bbb uehos):\n" +
                    "    def __init__(self, window):\n" +
                    "        kk = {ao: window. \n" +
                    "            window.,\n" +
                    "            windo\n" +
                    "            }\n" +
                    "    def method2(self, window):\n" +
                    "        kk = {ao: window. \n" +
                    "            window.,\n" +
                    "            windo\n" +
                    "            }\n" +
                    "    def method3(self, window):\n" +
                    "        kk = {ao: window. \n" +
                    "            window.,\n" +
                    "            windo\n" +
                    "            }\n" +
                    ""; 
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                ClassDef cdef = (ClassDef) m.body[0];
                assertEquals("drDropTarget", NodeUtils.getRepresentationString(cdef));
                assertEquals(3, cdef.body.length);
                assertEquals("__init__", NodeUtils.getRepresentationString((FunctionDef) cdef.body[0]));
                assertEquals("method2", NodeUtils.getRepresentationString((FunctionDef) cdef.body[1]));
                assertEquals("method3", NodeUtils.getRepresentationString((FunctionDef) cdef.body[2]));
                return true;
            }
        });
    }
    
    
    public void testErrorHandled12() {
        
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
        
            public Boolean call(Integer arg) {
                String s = 
                    "class LinkedList:                      \n"+
                    "    def __init__(self,content='Null'): \n" +
                    "        if not content:                \n"+
                    "            self.first=content         \n"+
                    "            self.last=content          \n"+
                    "        else:                          \n"+
                    "            self.first='Null'          \n"+
                    "            self.last='Null'           \n"+
                    "        self.content=content           \n"+
                    "        self.                          \n";
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(1, m.body.length);
                ClassDef cdef = (ClassDef) m.body[0];
                assertEquals("LinkedList", NodeUtils.getRepresentationString(cdef));
                assertEquals(1, cdef.body.length);
                assertEquals("__init__", NodeUtils.getRepresentationString((FunctionDef) cdef.body[0]));
                return true;
            }
        });
        
    }
    
    public void testErrorHandled13() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
                String s = 
                    "class LinkedList:                      \n" +
                    "    def m1(self):"+
                    "        self.content=content           \n"+
                    "        self thueo ueo                 \n" +
                    "" +
                    "class B:\n";
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals(2, m.body.length);
                ClassDef cdef = (ClassDef) m.body[0];
                assertEquals("LinkedList", NodeUtils.getRepresentationString(cdef));
                assertEquals(1, cdef.body.length);
                assertEquals("m1", NodeUtils.getRepresentationString((FunctionDef) cdef.body[0]));
                assertEquals("B", NodeUtils.getRepresentationString(m.body[1]));
                return true;
            }
        });
        
    }
    
    
    public void testErrorHandled14() {
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
                String s = 
                    "from a import AAA\n"+
                    "from b import\n"+
                    "BBB\n"+
                    "\n"+
                    "\n";
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
                assertEquals("AAA", NodeUtils.getRepresentationString(((ImportFrom)m.body[0]).names[0]));
                return true;
            }
        });
        
    }
    
    
    public void testErrorHandled15() {
//        PyParser.DEBUG_SHOW_PARSE_ERRORS = true;
        checkWithAllGrammars(new ICallback<Boolean, Integer>(){
            
            public Boolean call(Integer arg) {
                String s = 
                    "class Bar(object):\n"+
                    "    TYPE = 10\n"+
                    "\n"+
                    "class Foo:\n"+
                    "\n"+
                    "    def Meth0(self):\n"+
                    "        if xxx.GetType() & Bar.\n"+
                    "        return 'x' % (1,\n"+
                    "           2)\n"+
                    "\n"+
                    "    def Meth1(self):\n"+
                    "        if target == 'topology':\n"+
                    "            if 1:\n"+
                    "                pass\n"+
                    "            else:\n"+
                    "                pass\n"+
                    "        else:\n"+
                    "            pass\n"+
                    "        return ret\n"+
                    "\n";
                
                Tuple<SimpleNode, Throwable> tup = parseILegalDocSuccessfully(s);
                Module m = (Module) tup.o1;
//                assertEquals(2, m.body.length);
                assertEquals("Bar", NodeUtils.getRepresentationString(m.body[0]));
                assertEquals("Foo", NodeUtils.getRepresentationString(m.body[1]));
                return true;
            }
        });
        
    }

    

}