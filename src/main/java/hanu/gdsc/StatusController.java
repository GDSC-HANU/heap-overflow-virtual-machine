package hanu.gdsc;

import com.sun.management.OperatingSystemMXBean;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;


@RestController
public class StatusController {
    @Builder
    public static class Output {
        public int cpuCores;
        public double cpuUsage;
        public long ram;
        public long ramUsage;
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        Output output = Output.builder()
                .cpuCores(Runtime.getRuntime().availableProcessors())
                .cpuUsage(osBean.getProcessCpuLoad())
                .ram(Runtime.getRuntime().maxMemory())
                .ramUsage(Runtime.getRuntime().totalMemory())
                .build();
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}