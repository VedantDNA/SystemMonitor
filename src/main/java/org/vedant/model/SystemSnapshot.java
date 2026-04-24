package org.vedant.model;

import oshi.software.os.OSProcess;

import java.util.List;

public record SystemSnapshot(
        long timeStamp,
        CpuMetrics cpuMetrics,
        MemoryMetrics memoryMetrics,
        HardwareInfo hardwareInfo,
        List<OSProcess> osProcesses
) {}
