package com.effigo.tools.support_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Table(name = "support_history")
@Data
public class SupportHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_support_history_user"))
    private User user;  // Reference to the User entity

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;  // Type of event (e.g., CREATED, UPDATED, DELETED)

    @Column(name = "event_description", columnDefinition = "TEXT")
    private String eventDescription; // Event details

    @Column(name = "event_timestamp", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp eventTimestamp; // Time of event

    @Column(name = "created_by", length = 50)
    private String createdBy;  // User who triggered the event

    @PrePersist
    protected void onCreate() {
        if (eventTimestamp == null) {
            eventTimestamp = new Timestamp(System.currentTimeMillis());
        }
    }
}
