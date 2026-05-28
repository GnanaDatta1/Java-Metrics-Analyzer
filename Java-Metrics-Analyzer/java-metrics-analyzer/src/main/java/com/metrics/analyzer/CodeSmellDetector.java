package com.metrics.analyzer;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.metrics.model.MethodMetrics;

import java.util.ArrayList;
import java.util.List;

public class CodeSmellDetector {

    public List<String> detectSmells(MethodMetrics mm, MethodDeclaration md) {
        List<String> smells = new ArrayList<>();

        if (mm.getParameterCount() > 4) {
            smells.add("Too many parameters (" + mm.getParameterCount() + ")");
        }

        if (mm.getCyclomaticComplexity() > 10) {
            smells.add("High complexity (" + mm.getCyclomaticComplexity() + ")");
        }

        if (mm.getLoc() > 50) {
            smells.add("Long method (" + mm.getLoc() + " lines)");
        }

        if (mm.getMaintainabilityIndex() < 40) {
            smells.add("Low maintainability (" + String.format("%.1f", mm.getMaintainabilityIndex()) + ")");
        }

        return smells;
    }
}