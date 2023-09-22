package tinqle.tinqleServer.common.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    protected LocalDateTime createdAt;
    @LastModifiedDate
    protected String modifiedAt;
    protected boolean visibility = true;

    public void softDelete() {
        visibility = false;
    }

    public void setVisible() {
        visibility = true;
    }
}
