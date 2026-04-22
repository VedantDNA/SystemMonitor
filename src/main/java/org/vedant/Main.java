package org.vedant;

import org.vedant.service.CpuService;
import org.vedant.service.HardwareService;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class Main {
    public static void main (String[] args) throws InterruptedException {
        SystemInfo sys = new SystemInfo();
        HardwareAbstractionLayer hal = sys.getHardware();
        CentralProcessor processor = hal.getProcessor();
        GlobalMemory memory = hal.getMemory();
    }
}