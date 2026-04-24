package org.vedant.service;

import org.vedant.model.CpuMetrics;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;
import java.util.List;

public class CpuService {

    private CentralProcessor processor;
    private OperatingSystem os;
    private long[] prevSystemTicks;
    private long[][] prevProcessorTicks;

    public CpuService(SystemInfo systemInfo){
        this.processor = systemInfo.getHardware().getProcessor();
        this.os = systemInfo.getOperatingSystem();
        this.prevSystemTicks = processor.getSystemCpuLoadTicks();
        this.prevProcessorTicks = processor.getProcessorCpuLoadTicks();
    }

    public CpuMetrics fetch(){
        try {
            double totalUsage = Math.round(processor.getSystemCpuLoadBetweenTicks(prevSystemTicks) * 10000.0)/100.0;
            double[] coreLoads = processor.getProcessorCpuLoadBetweenTicks(prevProcessorTicks);

            for(int i  = 0; i < coreLoads.length; i++){
                coreLoads[i] = Math.round(coreLoads[i] * 10000.0) / 100.0;
            }
            this.prevSystemTicks = processor.getSystemCpuLoadTicks();
            this.prevProcessorTicks = processor.getProcessorCpuLoadTicks();

            int threadCount = os.getThreadCount();
            return new CpuMetrics(
                    totalUsage,
                    coreLoads,
                    threadCount,
                    processor.getProcessorIdentifier().getMicroarchitecture());
        }catch (Exception e){
            System.err.println("Unable to Capture Data: " + e.getMessage());
            return CpuMetrics.EMPTY;
        }
    }

    public List<OSProcess> getTopProcesses(){
        return os.getProcesses(OperatingSystem.ProcessFiltering.ALL_PROCESSES,OperatingSystem.ProcessSorting.CPU_DESC,20);
    }

}
