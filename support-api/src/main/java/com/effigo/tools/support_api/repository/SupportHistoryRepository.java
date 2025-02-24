package com.effigo.tools.support_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.effigo.tools.support_api.model.SupportHistory;
import java.util.List;

public interface SupportHistoryRepository extends JpaRepository<SupportHistory, Long> {
    List<SupportHistory> findByUserId(Long userId);
}
