package org.vedant.model;

public record CpuMetrics(
        double totalUsage,
        double[] perCoreLoad,
        int threadCount,
        String architecture
) {}
