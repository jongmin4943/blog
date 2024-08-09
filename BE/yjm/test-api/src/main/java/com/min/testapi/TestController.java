package com.min.testapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/min/api")
@Slf4j
public class TestController {
    private final TestService testService;

    public TestController(final TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testBigData() {
        return ResponseEntity.ok(testService.getBigData());
    }

    @GetMapping("/test-no-zip")
    public ResponseEntity<Map<String, String>> testBigDataWithoutZip() {
        return ResponseEntity.ok(testService.getBigData());
    }

    @GzipResponse(level = 6)
    @GetMapping("/test-annotation-level6")
    public ResponseEntity<Map<String, String>> testBigDataWithAnnotationLevel6() {
        return ResponseEntity.ok(testService.getBigData());
    }

    @GzipResponse
    @GetMapping("/test-annotation-level1")
    public ResponseEntity<Map<String, String>> testBigDataWithAnnotationLevel1() {
        return ResponseEntity.ok(testService.getBigData());
    }

}
