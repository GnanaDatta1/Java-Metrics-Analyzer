package com.metrics.scanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryScanner {

    public List<Path> scanForJavaFiles(String directoryPath) throws IOException {
        Path startPath = Paths.get(directoryPath);

        if (!Files.exists(startPath)) {
            throw new IOException("Directory does not exist: " + directoryPath);
        }

        if (!Files.isDirectory(startPath)) {
            throw new IOException("Path is not a directory: " + directoryPath);
        }

        List<Path> javaFiles = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(startPath)) {
            javaFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().toLowerCase().endsWith(".java"))
                    .collect(Collectors.toList());
        }

        return javaFiles;
    }

    public String readFileContent(Path filePath) throws IOException {
        return Files.readString(filePath);
    }
}