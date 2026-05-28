package com.metrics;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.metrics.analyzer.CyclomaticComplexityCalculator;
import com.metrics.analyzer.HalsteadMetricsCalculator;
import com.metrics.analyzer.LineCounter;
import com.metrics.analyzer.MaintainabilityIndexCalculator;
import com.metrics.model.MethodMetrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetricsAnalyzerTest {

    private final JavaParser javaParser = new JavaParser();
    private final CyclomaticComplexityCalculator complexityCalc = new CyclomaticComplexityCalculator();
    private final HalsteadMetricsCalculator halsteadCalc = new HalsteadMetricsCalculator();
    private final MaintainabilityIndexCalculator miCalc = new MaintainabilityIndexCalculator();

    @Test
    void testParsing() {
        String code = """
                public class Test {
                    public void foo() { int x = 1; }
                }
                """;
        ParseResult<CompilationUnit> result = javaParser.parse(code);
        assertTrue(result.getResult().isPresent());
        List<MethodDeclaration> methods = result.getResult().get().findAll(MethodDeclaration.class);
        assertEquals(1, methods.size());
        assertEquals("foo", methods.get(0).getNameAsString());
    }

    @Test
    void testCyclomaticComplexitySimple() {
        String code = """
                public class Test {
                    public void foo() { int x = 1; }
                }
                """;
        CompilationUnit cu = javaParser.parse(code).getResult().get();
        MethodDeclaration md = cu.findAll(MethodDeclaration.class).get(0);
        assertEquals(1, complexityCalc.calculate(md));
    }

    @Test
    void testCyclomaticComplexityWithConditionals() {
        String code = """
                public class Test {
                    public void foo(int x) {
                        if (x > 0) {
                            System.out.println("positive");
                        } else if (x < 0) {
                            System.out.println("negative");
                        } else {
                            System.out.println("zero");
                        }
                        for (int i = 0; i < 10; i++) {
                            System.out.println(i);
                        }
                        while (x > 0) {
                            x--;
                        }
                    }
                }
                """;
        CompilationUnit cu = javaParser.parse(code).getResult().get();
        MethodDeclaration md = cu.findAll(MethodDeclaration.class).get(0);
        // Base 1 + 2 ifs + 1 for + 1 while = 5
        assertEquals(5, complexityCalc.calculate(md));
    }

    @Test
    void testCyclomaticComplexityWithSwitch() {
        String code = """
                public class Test {
                    public void foo(int x) {
                        switch (x) {
                            case 1: break;
                            case 2: break;
                            default: break;
                        }
                    }
                }
                """;
        CompilationUnit cu = javaParser.parse(code).getResult().get();
        MethodDeclaration md = cu.findAll(MethodDeclaration.class).get(0);
        // Base 1 + 3 switch entries (case 1, case 2, default) = 4
        assertEquals(4, complexityCalc.calculate(md));
    }

    @Test
    void testCyclomaticComplexityWithTernary() {
        String code = """
                public class Test {
                    public String foo(int x) {
                        return x > 0 ? "positive" : "negative";
                    }
                }
                """;
        CompilationUnit cu = javaParser.parse(code).getResult().get();
        MethodDeclaration md = cu.findAll(MethodDeclaration.class).get(0);
        // Base 1 + 1 ternary = 2
        assertEquals(2, complexityCalc.calculate(md));
    }

    @Test
    void testLineCounter() {
        String code = "line1\n// comment\n\nline2\n/* block */\n";
        LineCounter lc = new LineCounter(code);
        assertEquals(5, lc.getTotalLines());
        assertEquals(2, lc.getCodeLines());
        assertEquals(2, lc.getCommentLines());
        assertEquals(1, lc.getBlankLines());
    }

    @Test
    void testHalsteadMetrics() {
        String code = """
                public class Test {
                    public int add(int a, int b) {
                        return a + b;
                    }
                }
                """;
        CompilationUnit cu = javaParser.parse(code).getResult().get();
        MethodDeclaration md = cu.findAll(MethodDeclaration.class).get(0);
        int vocab = halsteadCalc.calculateVocabulary(md);
        int length = halsteadCalc.calculateLength(md);
        assertTrue(vocab > 0);
        assertTrue(length > 0);
        double volume = halsteadCalc.calculateVolume(vocab, length);
        assertTrue(volume > 0);
    }

    @Test
    void testMaintainabilityIndex() {
        MethodMetrics mm = new MethodMetrics();
        mm.setLoc(5);
        mm.setCyclomaticComplexity(1);
        mm.setHalsteadVolume(20);
        mm.setHalsteadVocabulary(10);
        mm.setHalsteadLength(15);

        double mi = miCalc.calculate(mm);
        assertTrue(mi >= 0 && mi <= 100);
        assertTrue(mi > 50);
    }

    @Test
    void testDirectoryScannerInvalidPath() {
        com.metrics.scanner.DirectoryScanner scanner = new com.metrics.scanner.DirectoryScanner();
        assertThrows(java.io.IOException.class, () -> scanner.scanForJavaFiles("nonexistent/dir"));
    }

    @Test
    void testComplexityLabels() {
        assertEquals("Low", complexityCalc.getComplexityLabel(1));
        assertEquals("Low", complexityCalc.getComplexityLabel(5));
        assertEquals("Medium", complexityCalc.getComplexityLabel(7));
        assertEquals("High", complexityCalc.getComplexityLabel(15));
    }

    @Test
    void testMiLabels() {
        assertEquals("Good", miCalc.getMiLabel(80));
        assertEquals("Moderate", miCalc.getMiLabel(50));
        assertEquals("Poor", miCalc.getMiLabel(20));
    }

    @Test
    void testFullPipeline() throws Exception {
        String code = """
                public class TestPipeline {
                    public int simple() { return 1; }
                    public int complex(int x) {
                        if (x > 0) {
                            if (x > 10) {
                                return 10;
                            }
                            return x;
                        }
                        return 0;
                    }
                }
                """;

        CompilationUnit cu = javaParser.parse(code).getResult().get();
        List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
        assertEquals(2, methods.size());

        for (MethodDeclaration md : methods) {
            int cc = complexityCalc.calculate(md);
            MethodMetrics mm = new MethodMetrics();
            mm.setMethodName(md.getNameAsString());
            mm.setLoc(md.getEnd().get().line - md.getBegin().get().line + 1);
            mm.setCyclomaticComplexity(cc);
            mm.setHalsteadVocabulary(halsteadCalc.calculateVocabulary(md));
            mm.setHalsteadLength(halsteadCalc.calculateLength(md));
            mm.setHalsteadVolume(halsteadCalc.calculateVolume(mm.getHalsteadVocabulary(), mm.getHalsteadLength()));
            mm.setMaintainabilityIndex(miCalc.calculate(mm));

            assertTrue(mm.getLoc() > 0);
            assertTrue(mm.getMaintainabilityIndex() >= 0);
        }
    }
}