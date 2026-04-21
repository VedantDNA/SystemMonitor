package org.vedant;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class Main {
    public static void main(String[] args) {
        SystemInfo sys = new SystemInfo();
        HardwareAbstractionLayer hal = sys.getHardware();
        CentralProcessor processor = hal.getProcessor();
        System.out.println(hal.getGraphicsCards());
        GlobalMemory memory = hal.getMemory();
    }
}