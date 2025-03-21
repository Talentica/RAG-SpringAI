package com.openAi.touchpoint.controllers;


import com.openAi.touchpoint.service.TouchpointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/touchpoint")
@RequiredArgsConstructor
public class TouchpointController {
    private final TouchpointService service;
    @GetMapping("/status-update")
    public String updateTouchPointStatusForTeam(@RequestParam Integer customerId) {
        String resp = service.updateTouchPointStatusForTeam(customerId);
        return resp;
    }
}
