package com.exemplo.service;

import com.exemplo.model.SistemaInfo;
import com.exemplo.model.SistemaInfo.DiscoInfo;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

@Service
public class SistemaService {

    public SistemaInfo getSistemaInfo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        // CPU
        CentralProcessor processor = hal.getProcessor();
        long[] oldTicks = processor.getSystemCpuLoadTicks();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double usoCPU = processor.getSystemCpuLoadBetweenTicks(oldTicks) * 100;

        // Memória
        GlobalMemory memory = hal.getMemory();
        long memoriaTotal = memory.getTotal();
        long memoriaDisponivel = memory.getAvailable();
        double usoMemoria = ((double) (memoriaTotal - memoriaDisponivel) / memoriaTotal) * 100;

        // Disco
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fileStores = fileSystem.getFileStores().toArray(new OSFileStore[0]);
        long discoTotal = 0;
        long discoUsado = 0;

        SistemaInfo sistemaInfo = new SistemaInfo(usoCPU, usoMemoria, 0,
                                                 memoriaTotal, memoriaDisponivel,
                                                 0, 0);

        // Coleta informações de cada disco
        for (OSFileStore store : fileStores) {
            long total = store.getTotalSpace();
            long disponivel = store.getUsableSpace();
            long usado = total - disponivel;
            
            discoTotal += total;
            discoUsado += usado;

            DiscoInfo discoInfo = new DiscoInfo(
                store.getName(),
                store.getMount(),
                total,
                usado,
                disponivel
            );
            sistemaInfo.getDiscos().add(discoInfo);
        }

        double usoDisco = ((double) discoUsado / discoTotal) * 100;
        sistemaInfo.setUsoDisco(usoDisco);
        sistemaInfo.setDiscoTotal(discoTotal);
        sistemaInfo.setDiscoUsado(discoUsado);

        return sistemaInfo;
    }
} 