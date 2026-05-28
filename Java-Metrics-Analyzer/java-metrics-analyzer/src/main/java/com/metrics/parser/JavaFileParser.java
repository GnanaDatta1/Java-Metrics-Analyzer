package com.metrics.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.metrics.model.FileMetrics;
import com.metrics.model.MethodMetrics;
import com.metrics.analyzer.MethodAnalyzer;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class JavaFileParser {

    private final JavaParser javaParser;

    public JavaFileParser() {
        this.javaParser = new JavaParser();
    }

    public FileMetrics parseFile(Path filePath, String content, List<String> allMethodSignatures) {
        FileMetrics fileMetrics = new FileMetrics();
        fileMetrics.setFilePath(filePath.toString());
        fileMetrics.setFileName(filePath.getFileName().toString());

        try {
            ParseResult<CompilationUnit> parseResult = javaParser.parse(content);

            if (parseResult.getProblems().isEmpty() && parseResult.getResult().isPresent()) {
                CompilationUnit cu = parseResult.getResult().get();

                MethodAnalyzer analyzer = new MethodAnalyzer(filePath, content, allMethodSignatures);
                analyzer.analyze(cu);

                fileMetrics = analyzer.getFileMetrics();
            }
        } catch (Exception e) {
            System.err.println("Failed to parse " + filePath + ": " + e.getMessage());
            fileMetrics.setTotalLoc(0);
            fileMetrics.setTotalLines(0);
        }

        return fileMetrics;
    }
}