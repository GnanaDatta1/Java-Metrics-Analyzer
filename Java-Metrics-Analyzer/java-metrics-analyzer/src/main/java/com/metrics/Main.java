package com.metrics;

import com.metrics.exporter.HtmlReportExporter;
import com.metrics.model.FileMetrics;
import com.metrics.model.MethodMetrics;
import com.metrics.model.ProjectReport;
import com.metrics.parser.JavaFileParser;
import com.metrics.scanner.DirectoryScanner;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar java-metrics-analyzer.jar <source-directory> [output-file]");
            System.out.println("  <source-directory>  Path to the directory containing Java files");
            System.out.println("  [output-file]       Path for the generated HTML report (default: report.html)");
            System.exit(1);
        }

        String sourceDir = args[0];
        String outputFile = args.length > 1 ? args[1] : "report.html";

        try {
            System.out.println("Analyzing Java files in: " + sourceDir);
            ProjectReport report = analyze(sourceDir);
            HtmlReportExporter exporter = new HtmlReportExporter();
            exporter.export(report, outputFile);
            System.out.println("Analysis complete!");
            System.out.println("  Files analyzed: " + report.getTotalFiles());
            System.out.println("  Total LOC: " + report.getTotalLoc());
            System.out.println("  Avg Complexity: " + String.format("%.2f", report.getAverageComplexity()));
            System.out.println("  Avg Maintainability Index: " + String.format("%.2f", report.getAverageMaintainabilityIndex()));
            System.out.println("  Dead methods found: " + report.getGlobalDeadMethods().size());
            System.out.println("  Code smells found: " + report.getGlobalCodeSmells().size());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    public static ProjectReport analyze(String sourceDir) throws IOException {
        DirectoryScanner scanner = new DirectoryScanner();
        JavaFileParser parser = new JavaFileParser();
        List<Path> javaFiles = scanner.scanForJavaFiles(sourceDir);

        ProjectReport report = new ProjectReport();
        report.setProjectName(sourceDir);
        report.setScanTimestamp(LocalDateTime.now().format(FORMATTER));
        report.setTotalFiles(javaFiles.size());

        List<String> allMethodSignatures = new ArrayList<>();
        List<String> allFileContents = new ArrayList<>();

        for (Path file : javaFiles) {
            String content = scanner.readFileContent(file);
            allFileContents.add(content);
        }

        for (int i = 0; i < javaFiles.size(); i++) {
            String content = allFileContents.get(i);
            FileMetrics fm = parser.parseFile(javaFiles.get(i), content, allMethodSignatures);
            if (fm != null) {
                report.addFile(fm);
            }
        }

        aggregateResults(report);
        return report;
    }

    private static void aggregateResults(ProjectReport report) {
        List<FileMetrics> allFiles = report.getFiles();
        int totalLoc = 0;
        int totalClasses = 0;
        int totalMethods = 0;
        double totalComplexity = 0;
        double totalMi = 0;
        int highComplexityCount = 0;
        int mediumComplexityCount = 0;
        int lowComplexityCount = 0;

        for (FileMetrics file : allFiles) {
            totalLoc += file.getTotalLoc();
            totalClasses += file.getClassCount();
            totalMethods += file.getMethods().size();

            for (MethodMetrics mm : file.getMethods()) {
                totalComplexity += mm.getCyclomaticComplexity();
                totalMi += mm.getMaintainabilityIndex();

                if (mm.isDeadCode()) {
                    report.addGlobalDeadMethod(file.getFileName() + "::" + mm.getMethodName());
                }
                for (String smell : mm.getSmells()) {
                    report.addGlobalCodeSmell(file.getFileName() + "::" + mm.getMethodName() + " — " + smell);
                }

                int cc = mm.getCyclomaticComplexity();
                if (cc > 10) {
                    highComplexityCount++;
                } else if (cc > 5) {
                    mediumComplexityCount++;
                } else {
                    lowComplexityCount++;
                }
            }
        }

        report.setTotalLoc(totalLoc);
        report.setTotalClasses(totalClasses);
        report.setTotalMethods(totalMethods);
        report.setAverageComplexity(totalMethods > 0 ? totalComplexity / totalMethods : 0);
        report.setAverageMaintainabilityIndex(totalMethods > 0 ? totalMi / totalMethods : 0);
        report.setHighComplexityCount(highComplexityCount);
        report.setMediumComplexityCount(mediumComplexityCount);
        report.setLowComplexityCount(lowComplexityCount);
    }
}