package com.metrics.analyzer;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;

public class CyclomaticComplexityCalculator {

    public int calculate(MethodDeclaration md) {
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

    public String getComplexityLabel(int complexity) {
        if (complexity <= 5) return "Low";
        if (complexity <= 10) return "Medium";
        return "High";
    }

    public String getComplexityColor(int complexity) {
        if (complexity <= 5) return "#28a745";
        if (complexity <= 10) return "#ffc107";
        return "#dc3545";
    }
}