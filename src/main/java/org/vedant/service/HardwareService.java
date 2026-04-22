package org.vedant.service;

import org.vedant.model.HardwareInfo;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class HardwareService {

    private final SystemInfo systemInfo;

    public HardwareService (SystemInfo systemInfo){
        this.systemInfo = systemInfo;
    }

    public HardwareInfo fetchStaticReport(){
        try {
            HardwareAbstractionLayer hal = systemInfo.getHardware();
            CentralProcessor cpu = hal.getProcessor();
            GlobalMemory memory = hal.getMemory();
            OperatingSystem os = systemInfo.getOperatingSystem();

            return new HardwareInfo(
                    cpu.getProcessorIdentifier().getVendor(),
                    hal.getComputerSystem().getModel(),
                    hal.getComputerSystem().getSerialNumber(),
                    cpu.getProcessorIdentifier().getName(),
                    cpu.getPhysicalProcessorCount(),
                    cpu.getLogicalProcessorCount(),
                    cpu.getProcessorIdentifier().getMicroarchitecture(),
                    Math.max(cpu.getMaxFreq(),cpu.getProcessorIdentifier().getVendorFreq()),
                    memory.getTotal(),
                    os.toString(),
                    os.getBitness()
            );

        }catch (Exception e){
            System.err.println("Failed to Fetch Hardware Report: " + e.getMessage());
            return HardwareInfo.EMPTY;
        }
    }
}
