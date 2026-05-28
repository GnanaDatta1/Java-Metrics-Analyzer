package com.metrics.analyzer;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.HashSet;
import java.util.Set;

public class HalsteadMetricsCalculator {

    private static final String TOKEN_SPLIT = "\\s+|(?=[{}();,.+\\-*/%&|^~!<>=])|(?<=[{}();,.+\\-*/%&|^~!<>=])";

    public int calculateVocabulary(MethodDeclaration md) {
        String body = getBodyText(md);
        String[] tokens = body.split(TOKEN_SPLIT);
        Set<String> unique = new HashSet<>();

        for (String token : tokens) {
            String t = token.trim();
            if (!t.isEmpty() && !t.equals("\n") && !t.equals("\r")) {
                unique.add(t);
            }
        }
        return unique.size();
    }

    public int calculateLength(MethodDeclaration md) {
        String body = getBodyText(md);
        String[] tokens = body.split(TOKEN_SPLIT);
        int count = 0;

        for (String token : tokens) {
            String t = token.trim();
            if (!t.isEmpty() && !t.equals("\n") && !t.equals("\r")) {
                count++;
            }
        }
        return count;
    }

    public double calculateVolume(int vocabulary, int length) {
        if (vocabulary == 0) return 0;
        return length * (Math.log(vocabulary) / Math.log(2));
    }

    private String getBodyText(MethodDeclaration md) {
        return md.getBody().map(b -> b.toString()).orElse("");
    }
}