package com.metrics.model;

import java.util.ArrayList;
import java.util.List;

public class MethodMetrics {
    private String methodName;
    private String className;
    private int lineNumber;
    private int loc;
    private int cyclomaticComplexity;
    private int parameterCount;
    private int halsteadVocabulary;
    private int halsteadLength;
    private double halsteadVolume;
    private double maintainabilityIndex;
    private boolean isDeadCode;
    private List<String> smells;
    private boolean calledExternally;

    public MethodMetrics() {
        this.smells = new ArrayList<>();
        this.cyclomaticComplexity = 1;
    }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }

    public int getLoc() { return loc; }
    public void setLoc(int loc) { this.loc = loc; }

    public int getCyclomaticComplexity() { return cyclomaticComplexity; }
    public void setCyclomaticComplexity(int cyclomaticComplexity) { this.cyclomaticComplexity = cyclomaticComplexity; }

    public int getParameterCount() { return parameterCount; }
    public void setParameterCount(int parameterCount) { this.parameterCount = parameterCount; }

    public int getHalsteadVocabulary() { return halsteadVocabulary; }
    public void setHalsteadVocabulary(int halsteadVocabulary) { this.halsteadVocabulary = halsteadVocabulary; }

    public int getHalsteadLength() { return halsteadLength; }
    public void setHalsteadLength(int halsteadLength) { this.halsteadLength = halsteadLength; }

    public double getHalsteadVolume() { return halsteadVolume; }
    public void setHalsteadVolume(double halsteadVolume) { this.halsteadVolume = halsteadVolume; }

    public double getMaintainabilityIndex() { return maintainabilityIndex; }
    public void setMaintainabilityIndex(double maintainabilityIndex) { this.maintainabilityIndex = maintainabilityIndex; }

    public boolean isDeadCode() { return isDeadCode; }
    public void setDeadCode(boolean deadCode) { isDeadCode = deadCode; }

    public List<String> getSmells() { return smells; }
    public void setSmells(List<String> smells) { this.smells = smells; }
    public void addSmell(String smell) { this.smells.add(smell); }

    public boolean isCalledExternally() { return calledExternally; }
    public void setCalledExternally(boolean calledExternally) { this.calledExternally = calledExternally; }
}