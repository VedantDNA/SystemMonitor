package org.vedant.service;

import org.vedant.model.HardwareInfo;
import org.vedant.model.SystemSnapshot;
import java.time.Instant;

public class MonitoringOrchestrator {
    private final CpuService cpuService;
    private final MemoryService memoryService;
    private final HardwareInfo staticHardwareReport;

    public MonitoringOrchestrator(HardwareService hardwareService, CpuService cpuService, MemoryService memoryService){
        this.cpuService = cpuService;
        this.memoryService = memoryService;

        staticHardwareReport = hardwareService.fetchStaticReport();
    }

    public SystemSnapshot takeSnapShot(){
        return new SystemSnapshot(
                Instant.now().toEpochMilli(),
                cpuService.fetch(),
                memoryService.fetch(),
                staticHardwareReport
        );
    }

    public HardwareInfo getStaticHardwareReport() {
        return staticHardwareReport;
    }
}
