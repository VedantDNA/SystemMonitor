package org.vedant.service;

import oshi.hardware.CentralProcessor;

public class CpuService {

    private CentralProcessor processor;

    public CpuService(CentralProcessor processor){
        this.processor = processor;
    }



    @Override
    public String toString() {
        return "CpuService{" +
                "processor=" + processor +
                '}';
    }
}
