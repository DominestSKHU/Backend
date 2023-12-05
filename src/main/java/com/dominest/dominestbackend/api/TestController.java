package com.dominest.dominestbackend.api;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
public class TestController {

    @Autowired
    public TestController(@Value("${script.backup-filename}") String dbBackupScriptFileName) {
        this.DB_BACKUP_SCRIPT_FILE_NAME = dbBackupScriptFileName;
    }
    private final String DB_BACKUP_SCRIPT_FILE_NAME;

    @GetMapping("/health")
    public String getRoom(){
        return LocalDateTime.now().toString();
    }

    @GetMapping("/test/back-up-db")
    public void runBatFile() {
        try {
            // 외부 파일이므로 JVM이 아닌 독립적인 프로세스에서 실행
            Process process = Runtime.getRuntime().exec(DB_BACKUP_SCRIPT_FILE_NAME);
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
