package com.effigo.tools.support_api.service.serviceImpl;

import com.effigo.tools.support_api.model.SupportHistory;
import com.effigo.tools.support_api.model.User;
import com.effigo.tools.support_api.repository.SupportHistoryRepository;
import com.effigo.tools.support_api.repository.UserRepository;
import com.effigo.tools.support_api.service.SupportHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportHistoryServiceImpl implements SupportHistoryService {

    @Autowired
    private SupportHistoryRepository supportHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void logEvent(Long userId, String eventType, String eventDescription, String createdBy) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        SupportHistory history = new SupportHistory();
        history.setUser(user);
        history.setEventType(eventType);
        history.setEventDescription(eventDescription);
        history.setCreatedBy(createdBy);

        supportHistoryRepository.save(history);
    }

    @Override
    public List<SupportHistory> getHistoryByUserId(Long userId) {
        return supportHistoryRepository.findByUserId(userId);
    }
    
    @Override
    public List<SupportHistory> getAllHistory() {
    	// TODO Auto-generated method stub
    	return supportHistoryRepository.findAll();
    }
}
