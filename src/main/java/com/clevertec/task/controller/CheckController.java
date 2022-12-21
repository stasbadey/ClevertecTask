package com.clevertec.task.controller;

import com.clevertec.task.CheckGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/check")
@RequiredArgsConstructor
public class CheckController {

    private final CheckGenerator checkGenerator;

    @GetMapping
    public ResponseEntity<Resource> generateCheck(@RequestParam("itemId") List<Integer> itemId) throws IOException {
        Map<Integer, Integer> items = itemId.stream()
                .collect(Collectors.toMap(Integer::intValue, s -> 1, Integer::sum));

        List<String> argsList = new ArrayList<>();
        items.forEach(
                (k, v) -> argsList.add(k + "-" + v)
        );
        String[] args = argsList.toArray(new String[0]);

        checkGenerator.generate(args);
        Path path = Path.of(Paths.get("src", "main", "resources") + "\\output.txt");
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
