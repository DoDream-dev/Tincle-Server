package tinqle.tinqleServer.common.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    protected String createdAt;
    @LastModifiedDate
    protected String modifiedAt;
    protected boolean visibility = true;

    @PrePersist
    protected void onPrePersist() {
        createdAt = customFormat();
        modifiedAt = createdAt;
    }

    @PreUpdate
    protected void onPreUpdate() {
        modifiedAt = customFormat();
    }
    private String customFormat() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public void softDelete() {
        visibility = false;
    }

    public void setVisible() {
        visibility = true;
    }
}
