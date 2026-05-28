package com.metrics.analyzer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.metrics.model.FileMetrics;
import com.metrics.model.MethodMetrics;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class MethodAnalyzer {

    private final Path filePath;
    private final String content;
    private final List<String> allMethodSignatures;
    private final FileMetrics fileMetrics;
    private final Set<String> definedMethods;
    private final Set<String> calledMethods;

    public MethodAnalyzer(Path filePath, String content, List<String> allMethodSignatures) {
        this.filePath = filePath;
        this.content = content;
        this.allMethodSignatures = allMethodSignatures;
        this.fileMetrics = new FileMetrics();
        this.fileMetrics.setFilePath(filePath.toString());
        this.fileMetrics.setFileName(filePath.getFileName().toString());
        this.definedMethods = new HashSet<>();
        this.calledMethods = new HashSet<>();
    }

    public void analyze(CompilationUnit cu) {
        countLines();
        countClassDeclarations(cu);
        analyzeMethods(cu);
        detectDeadMethods();
        computeAverages();
    }

    private void countLines() {
        String[] lines = content.split("\n", -1);
        fileMetrics.setTotalLines(lines.length);

        int codeLines = 0;
        int commentLines = 0;
        int blankLines = 0;
        boolean inBlockComment = false;

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.isEmpty()) {
                blankLines++;
                continue;
            }

            if (inBlockComment) {
                commentLines++;
                if (trimmed.contains("*/")) {
                    inBlockComment = false;
                }
                continue;
            }

            if (trimmed.startsWith("//")) {
                commentLines++;
                continue;
            }

            if (trimmed.startsWith("/*")) {
                commentLines++;
                if (!trimmed.contains("*/") || !trimmed.endsWith("*/")) {
                    inBlockComment = true;
                }
                continue;
            }

            if (trimmed.startsWith("*") && inBlockComment) {
                commentLines++;
                if (trimmed.contains("*/")) {
                    inBlockComment = false;
                }
                continue;
            }

            codeLines++;
        }

        fileMetrics.setTotalLoc(codeLines);
        fileMetrics.setCommentLines(commentLines);
        fileMetrics.setBlankLines(blankLines);
    }

    private void countClassDeclarations(CompilationUnit cu) {
        List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
        fileMetrics.setClassCount(classes.size());
    }

    private void analyzeMethods(CompilationUnit cu) {
        List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

        for (MethodDeclaration md : methods) {
            MethodMetrics mm = new MethodMetrics();
            mm.setMethodName(md.getNameAsString());
            mm.setClassName(getClassName(md));
            mm.setLineNumber(md.getBegin().map(p -> p.line).orElse(0));
            mm.setParameterCount(md.getParameters().size());

            String signature = buildSignature(md);
            definedMethods.add(signature);

            mm.setLoc(calculateMethodLoc(md));
            mm.setCyclomaticComplexity(calculateComplexity(md));
            mm.setHalsteadVocabulary(calculateHalsteadVocabulary(md));
            mm.setHalsteadLength(calculateHalsteadLength(md));
            mm.setHalsteadVolume(mm.getHalsteadLength() * (Math.log(mm.getHalsteadVocabulary()) / Math.log(2)));
            if (mm.getHalsteadVocabulary() == 0) {
                mm.setHalsteadVolume(0);
            }
            mm.setMaintainabilityIndex(calculateMaintainabilityIndex(mm));
            mm.setDeadCode(false);
            mm.setCalledExternally(allMethodSignatures.contains(signature));

            detectCodeSmells(mm, md);
            collectCalledMethods(md);

            fileMetrics.addMethod(mm);
        }
    }

    private String buildSignature(MethodDeclaration md) {
        String name = md.getNameAsString();
        String params = md.getParameters().stream()
                .map(p -> p.getType().asString())
                .collect(Collectors.joining(","));
        return name + "(" + params + ")";
    }

    private String getClassName(Node node) {
        while (node != null) {
            if (node instanceof ClassOrInterfaceDeclaration) {
                return ((ClassOrInterfaceDeclaration) node).getNameAsString();
            }
            node = node.getParentNode().orElse(null);
        }
        return "Unknown";
    }

    private int calculateMethodLoc(MethodDeclaration md) {
        return md.getEnd().map(end -> end.line - md.getBegin().get().line + 1).orElse(0);
    }

    private int calculateComplexity(MethodDeclaration md) {
        int complexity = 1;

        complexity += md.findAll(IfStmt.class).size();
        complexity += md.findAll(WhileStmt.class).size();
        complexity += md.findAll(ForStmt.class).size();
        complexity += md.findAll(ForEachStmt.class).size();
        complexity += md.findAll(SwitchEntry.class).size();
        complexity += md.findAll(CatchClause.class).size();
        complexity += md.findAll(ConditionalExpr.class).size();
        complexity += md.findAll(DoStmt.class).size();

        return complexity;
    }

    private int calculateHalsteadVocabulary(MethodDeclaration md) {
        String methodBody = md.getBody().map(b -> b.toString()).orElse("");
        String[] tokens = methodBody.split("\\s+|(?=[{}();,.+\\-*/%&|^~!<>=])|(?<=[{}();,.+\\-*/%&|^~!<>=])");

        Set<String> uniqueTokens = new HashSet<>();
        for (String token : tokens) {
            String t = token.trim();
            if (!t.isEmpty() && !t.equals("\n") && !t.equals("\r")) {
                uniqueTokens.add(t);
            }
        }
        return uniqueTokens.size();
    }

    private int calculateHalsteadLength(MethodDeclaration md) {
        String methodBody = md.getBody().map(b -> b.toString()).orElse("");
        String[] tokens = methodBody.split("\\s+|(?=[{}();,.+\\-*/%&|^~!<>=])|(?<=[{}();,.+\\-*/%&|^~!<>=])");

        int count = 0;
        for (String token : tokens) {
            String t = token.trim();
            if (!t.isEmpty() && !t.equals("\n") && !t.equals("\r")) {
                count++;
            }
        }
        return count;
    }

    private double calculateMaintainabilityIndex(MethodMetrics mm) {
        double mi = 171 - 5.2 * Math.log(mm.getHalsteadVolume() > 0 ? mm.getHalsteadVolume() : 1)
                - 0.23 * mm.getCyclomaticComplexity()
                - 16.2 * Math.log(mm.getLoc() > 0 ? mm.getLoc() : 1);

        return Math.max(0, Math.min(100, mi));
    }

    private void detectCodeSmells(MethodMetrics mm, MethodDeclaration md) {
        if (mm.getParameterCount() > 4) {
            mm.addSmell("Too many parameters (" + mm.getParameterCount() + ") — consider extracting a parameter object");
        }
        if (mm.getCyclomaticComplexity() > 10) {
            mm.addSmell("High cyclomatic complexity (" + mm.getCyclomaticComplexity() + ") — consider refactoring");
        }
        if (mm.getLoc() > 50) {
            mm.addSmell("Method too long (" + mm.getLoc() + " lines) — consider extracting smaller methods");
        }
        if (mm.getMaintainabilityIndex() < 40) {
            mm.addSmell("Low maintainability index (" + String.format("%.1f", mm.getMaintainabilityIndex()) + ") — hard to maintain");
        }
    }

    private void collectCalledMethods(MethodDeclaration md) {
        List<MethodCallExpr> calls = md.findAll(MethodCallExpr.class);
        for (MethodCallExpr call : calls) {
            String name = call.getNameAsString();
            String args = call.getArguments().stream()
                    .map(a -> a.calculateResolvedType().map(t -> t.asString()).orElse("?"))
                    .collect(Collectors.joining(","));
            calledMethods.add(name + "(" + args + ")");

            calledMethods.add(name);
        }
    }

    private void detectDeadMethods() {
        for (MethodMetrics mm : fileMetrics.getMethods()) {
            boolean isCalled = calledMethods.contains(mm.getMethodName());
            boolean isMain = mm.getMethodName().equals("main");
            if (!isCalled && !isMain) {
                mm.setDeadCode(true);
                fileMetrics.addDeadMethod(mm.getMethodName());
                fileMetrics.addCodeSmell("Unused method: " + mm.getMethodName() + "()");
            }
        }
    }

    private void computeAverages() {
        List<MethodMetrics> methods = fileMetrics.getMethods();
        if (methods.isEmpty()) {
            fileMetrics.setAverageComplexity(0);
            fileMetrics.setAverageMaintainabilityIndex(0);
            return;
        }

        double totalComplexity = 0;
        double totalMi = 0;
        for (MethodMetrics mm : methods) {
            totalComplexity += mm.getCyclomaticComplexity();
            totalMi += mm.getMaintainabilityIndex();
        }

        fileMetrics.setAverageComplexity(totalComplexity / methods.size());
        fileMetrics.setAverageMaintainabilityIndex(totalMi / methods.size());
    }

    public FileMetrics getFileMetrics() {
        return fileMetrics;
    }
}