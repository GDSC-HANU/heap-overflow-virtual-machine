package hanu.gdsc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class VirtualMachineStarter {
    private Logger logger = LoggerFactory.getLogger(VirtualMachineStarter.class);

    @EventListener(ApplicationReadyEvent.class)
    public void start() throws IOException, InterruptedException {
        run("rm -rf judge0-v1.13.0");
        run("rm -rf judge0-v1.13.0.zip");
        run("rm -rf docker-compose.yml");
        run("wget https://github.com/judge0/judge0/releases/download/v1.13.0/judge0-v1.13.0.zip");
        run("unzip judge0-v1.13.0.zip");
        move("judge0-v1.13.0/docker-compose.yml", "docker-compose.yml");
        run("docker stop $(docker ps -a -q)");
        run("docker rm $(docker ps -a -q)");
        run("docker-compose up", false);
        logger.info("Virtual machine started successfully.");
    }

    public void move(String src, String tar) throws IOException {
        Files.move(Paths.get(src), Paths.get(tar), StandardCopyOption.ATOMIC_MOVE);
    }


    public void run(String command) throws IOException, InterruptedException {
        run(command, true);
    }

    public void run(String command, boolean wait) throws IOException, InterruptedException {
        final Process process = Runtime.getRuntime().exec(command);
        logger.info("Start running command: " + command);
        if (wait) {
            process.waitFor();
            logger.info("Finish running command: " + command);
        }
    }

    @PreDestroy
    public void destroy() throws IOException, InterruptedException {
        run("rm -rf judge0-v1.13.0");
        run("rm -rf judge0-v1.13.0.zip");
        run("rm -rf docker-compose.yml");
    }
}
