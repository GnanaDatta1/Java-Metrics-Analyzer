package com.metrics.analyzer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.*;

public class DeadCodeDetector {

    public Set<String> findDefinedMethods(CompilationUnit cu) {
        Set<String> methods = new HashSet<>();
        List<MethodDeclaration> decls = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration md : decls) {
            methods.add(md.getNameAsString());
        }
        return methods;
    }

    public Set<String> findCalledMethods(CompilationUnit cu) {
        Set<String> methods = new HashSet<>();
        List<MethodCallExpr> calls = cu.findAll(MethodCallExpr.class);
        for (MethodCallExpr call : calls) {
            methods.add(call.getNameAsString());
        }
        return methods;
    }

    public List<String> detectDeadMethods(CompilationUnit cu) {
        Set<String> defined = findDefinedMethods(cu);
        Set<String> called = findCalledMethods(cu);

        List<String> deadMethods = new ArrayList<>();
        for (String method : defined) {
            if (!called.contains(method) && !method.equals("main")) {
                deadMethods.add(method);
            }
        }
        return deadMethods;
    }
}