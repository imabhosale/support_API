package com.effigo.tools.support_api.controller;

import com.effigo.tools.support_api.model.SupportHistory;
import com.effigo.tools.support_api.service.SupportHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support-history")
public class SupportHistoryController {

    @Autowired
    private SupportHistoryService supportHistoryService;

    @GetMapping("/user/{userId}")
    public List<SupportHistory> getHistoryByUserId(@PathVariable Long userId) {
        return supportHistoryService.getHistoryByUserId(userId);
    }
    

    @GetMapping()
    public List<SupportHistory> getAllHistories() {
        return supportHistoryService.getAllHistory();
    }
    
}
