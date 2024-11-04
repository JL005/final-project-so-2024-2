package com.so2042.finalproject.global;

import com.so2042.finalproject.serial.service.SerialProcessing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping()
public class ProcessingController {

    private final SerialProcessing serialProcessing;

    public ProcessingController(SerialProcessing serialProcessing) {
        this.serialProcessing = serialProcessing;
    }


    @PostMapping("/serial")
    public ResponseEntity<String> processSerial(@RequestBody DTO dto) throws FileNotFoundException {
        System.out.println(dto.getFilePath());
        return new ResponseEntity<>(this.serialProcessing.execute(dto.getFilePath()), HttpStatus.OK);
    }

}
