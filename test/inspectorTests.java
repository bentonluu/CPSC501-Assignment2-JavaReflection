import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class inspectorTests {
    private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void setOutputStream() {
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void clearOutputStream() {
        outputStream.reset();
    }

    @Test
    public void printClassTest() {
        new Inspector().printClass(String.class, 0);

        String expected = "Class: class java.lang.String";
        Assert.assertThat(outputStream.toString(), CoreMatchers.containsString(expected));
    }

    @Test
    public void printSuperclassTest() {
        new Inspector().printSuperclass(String.class, "", false, 0);

        String expected = "Superclass: class java.lang.Object";
        Assert.assertThat(outputStream.toString(), CoreMatchers.containsString(expected));
    }

    @Test
    public void printInterfaceTest() {
        new Inspector().printInterfaces(String.class, "", false, 0);

        String expected = "Interface: interface java.io.Serializable";
        Assert.assertThat(outputStream.toString(), CoreMatchers.containsString(expected));
    }

    @Test
    public void printConstructorTest() {
        new Inspector().printConstructor(String.class, "", false, 0);

        String expected = "Constructor name: java.lang.String\n" +
                "Constructor parameter type: class [B\n" +
                "Constructor parameter type: int\n" +
                "Constructor parameter type: int\n" +
                "Constructor modifier: public";
        Assert.assertThat(outputStream.toString(), CoreMatchers.containsString(expected));
    }

    @Test
    public void printMethodTest() {
        new Inspector().printMethods(String.class, "", false, 0);

        String expected = "Method name: equals\n" +
                "Method exception: None\n" +
                "Method parameter type: class java.lang.Object\n" +
                "Method return type: boolean\n" +
                "Method modifier: public";
        Assert.assertThat(outputStream.toString(), CoreMatchers.containsString(expected));
    }

    @Test
    public void printFieldTest() {
        new Inspector().printFields(String.class, "", false, 0);

        String expected = "Field name: hash\n" +
                "Field type: int\n" +
                "Field modifier: private\n" +
                "Field value: 0";
        Assert.assertThat(outputStream.toString(), CoreMatchers.containsString(expected));
    }

    @Test
    public void printRecursiveTrue() {
        new Inspector().printFields(String.class, "T", true, 0);

        String expected = "Field name: hash\n" +
                "Field type: int\n" +
                "Field modifier: private\n" +
                "Field value: 0\n" +
                "\n" +
                "\tCLASS\n" +
                "\tClass: int\n" +
                "\n" +
                "\tSUPERCLASS (int)\n" +
                "\tSuperclass: null\n" +
                "\n" +
                "\tINTERFACES (int)\n" +
                "\tNo interface(s) found\n" +
                "\n" +
                "\tCONSTRUCTOR (int)\n" +
                "\tNo constructor(s) found\n" +
                "\n" +
                "\tMETHODS (int)\n" +
                "\tNo method(s) found\n" +
                "\n" +
                "\tFIELDS (int)\n" +
                "\tNo field(s) found";
        Assert.assertThat(outputStream.toString(), CoreMatchers.containsString(expected));
    }

    @Test
    public void printFormatTest() {
        new Inspector().formatPrint("TEST", 2);

        String expected = "\t\tTEST\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void tabIndicationTest() {
        String expected = "\t\t\t";
        Assert.assertEquals(expected, new Inspector().tabIndication(3));
    }
}
