package com.dominest.dominestbackend.global.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Slf4j
@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Autowired
    public SchedulerConfig(@Value("${script.backup-db}") String dbBackupScriptFile) {
        this.dbBackupScriptFile = dbBackupScriptFile;
    }
    private final String dbBackupScriptFile;

    @Scheduled(cron = "0 0 9-18 * * *")
    public void runDbBackupBatFile() {
        try {
            String filePath = dbBackupScriptFile;
            // 외부 파일이므로 JVM이 아닌 독립적인 프로세스에서 실행
            Process process = Runtime.getRuntime().exec(filePath);

            // 프로세스의 수행이 끝날 때까지 대기
            process.waitFor();
        } catch (IOException e) {
            log.error("IOException occurred while running bat file:", e);
        } catch (InterruptedException e) { // 대기 중 서버 종료, 타임아웃 등의 이유로 다른 스레드에서 중단 요청 발생 시
            log.warn("Interrupted while running bat file:", e);
            // 현재 thread의 interrupted status 재설정
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Exception occurred while running bat file:", e);
        }
    }
}
