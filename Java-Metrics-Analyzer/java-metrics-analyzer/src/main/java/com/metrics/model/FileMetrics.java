package com.metrics.model;

import java.util.ArrayList;
import java.util.List;

public class FileMetrics {
    private String filePath;
    private String fileName;
    private int totalLoc;
    private int totalLines;
    private int commentLines;
    private int blankLines;
    private int classCount;
    private double averageComplexity;
    private double averageMaintainabilityIndex;
    private List<MethodMetrics> methods;
    private List<String> deadMethods;
    private List<String> codeSmells;

    public FileMetrics() {
        this.methods = new ArrayList<>();
        this.deadMethods = new ArrayList<>();
        this.codeSmells = new ArrayList<>();
    }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public int getTotalLoc() { return totalLoc; }
    public void setTotalLoc(int totalLoc) { this.totalLoc = totalLoc; }

    public int getTotalLines() { return totalLines; }
    public void setTotalLines(int totalLines) { this.totalLines = totalLines; }

    public int getCommentLines() { return commentLines; }
    public void setCommentLines(int commentLines) { this.commentLines = commentLines; }

    public int getBlankLines() { return blankLines; }
    public void setBlankLines(int blankLines) { this.blankLines = blankLines; }

    public int getClassCount() { return classCount; }
    public void setClassCount(int classCount) { this.classCount = classCount; }

    public double getAverageComplexity() { return averageComplexity; }
    public void setAverageComplexity(double averageComplexity) { this.averageComplexity = averageComplexity; }

    public double getAverageMaintainabilityIndex() { return averageMaintainabilityIndex; }
    public void setAverageMaintainabilityIndex(double averageMaintainabilityIndex) { this.averageMaintainabilityIndex = averageMaintainabilityIndex; }

    public List<MethodMetrics> getMethods() { return methods; }
    public void setMethods(List<MethodMetrics> methods) { this.methods = methods; }
    public void addMethod(MethodMetrics method) { this.methods.add(method); }

    public List<String> getDeadMethods() { return deadMethods; }
    public void setDeadMethods(List<String> deadMethods) { this.deadMethods = deadMethods; }
    public void addDeadMethod(String deadMethod) { this.deadMethods.add(deadMethod); }

    public List<String> getCodeSmells() { return codeSmells; }
    public void setCodeSmells(List<String> codeSmells) { this.codeSmells = codeSmells; }
    public void addCodeSmell(String smell) { this.codeSmells.add(smell); }
}