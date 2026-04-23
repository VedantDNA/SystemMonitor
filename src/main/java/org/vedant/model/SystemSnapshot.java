package org.vedant.model;

public record SystemSnapshot(
        long timeStamp,
        CpuMetrics cpuMetrics,
        MemoryMetrics memoryMetrics,
        HardwareInfo hardwareInfo
) {}
