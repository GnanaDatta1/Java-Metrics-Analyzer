package com.metrics.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectReport {
    private String projectName;
    private String scanTimestamp;
    private int totalFiles;
    private int totalClasses;
    private int totalMethods;
    private int totalLoc;
    private double averageComplexity;
    private double averageMaintainabilityIndex;
    private List<FileMetrics> files;
    private List<String> globalDeadMethods;
    private List<String> globalCodeSmells;
    private int highComplexityCount;
    private int mediumComplexityCount;
    private int lowComplexityCount;

    public ProjectReport() {
        this.files = new ArrayList<>();
        this.globalDeadMethods = new ArrayList<>();
        this.globalCodeSmells = new ArrayList<>();
    }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getScanTimestamp() { return scanTimestamp; }
    public void setScanTimestamp(String scanTimestamp) { this.scanTimestamp = scanTimestamp; }

    public int getTotalFiles() { return totalFiles; }
    public void setTotalFiles(int totalFiles) { this.totalFiles = totalFiles; }

    public int getTotalClasses() { return totalClasses; }
    public void setTotalClasses(int totalClasses) { this.totalClasses = totalClasses; }

    public int getTotalMethods() { return totalMethods; }
    public void setTotalMethods(int totalMethods) { this.totalMethods = totalMethods; }

    public int getTotalLoc() { return totalLoc; }
    public void setTotalLoc(int totalLoc) { this.totalLoc = totalLoc; }

    public double getAverageComplexity() { return averageComplexity; }
    public void setAverageComplexity(double averageComplexity) { this.averageComplexity = averageComplexity; }

    public double getAverageMaintainabilityIndex() { return averageMaintainabilityIndex; }
    public void setAverageMaintainabilityIndex(double averageMaintainabilityIndex) { this.averageMaintainabilityIndex = averageMaintainabilityIndex; }

    public List<FileMetrics> getFiles() { return files; }
    public void setFiles(List<FileMetrics> files) { this.files = files; }
    public void addFile(FileMetrics file) { this.files.add(file); }

    public List<String> getGlobalDeadMethods() { return globalDeadMethods; }
    public void setGlobalDeadMethods(List<String> globalDeadMethods) { this.globalDeadMethods = globalDeadMethods; }
    public void addGlobalDeadMethod(String method) { this.globalDeadMethods.add(method); }

    public List<String> getGlobalCodeSmells() { return globalCodeSmells; }
    public void setGlobalCodeSmells(List<String> globalCodeSmells) { this.globalCodeSmells = globalCodeSmells; }
    public void addGlobalCodeSmell(String smell) { this.globalCodeSmells.add(smell); }

    public int getHighComplexityCount() { return highComplexityCount; }
    public void setHighComplexityCount(int highComplexityCount) { this.highComplexityCount = highComplexityCount; }

    public int getMediumComplexityCount() { return mediumComplexityCount; }
    public void setMediumComplexityCount(int mediumComplexityCount) { this.mediumComplexityCount = mediumComplexityCount; }

    public int getLowComplexityCount() { return lowComplexityCount; }
    public void setLowComplexityCount(int lowComplexityCount) { this.lowComplexityCount = lowComplexityCount; }
}