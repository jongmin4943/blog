package com.min.testapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/min/api")
@Slf4j
public class TestController {

    @GetMapping("/test/{no}")
    public ResponseEntity<Long> testWithPathVariable(@PathVariable Long no) throws InterruptedException {
        log.info("testWithPathVariable 호출 no : {}", no);
        Thread.sleep(1000);
        return ResponseEntity.ok(no);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testWithParam(@RequestParam Map<String, Object> data) throws InterruptedException {
        log.info("testWithParam 호출 data : {}", data);
        Thread.sleep(1000);
        return ResponseEntity.ok(data);
    }

}