package com.effigo.tools.support_api.service;

import com.effigo.tools.support_api.model.SupportHistory;
import java.util.List;

public interface SupportHistoryService {
    void logEvent(Long userId, String eventType, String eventDescription, String createdBy);
    List<SupportHistory> getHistoryByUserId(Long userId);
    List<SupportHistory> getAllHistory();

}
