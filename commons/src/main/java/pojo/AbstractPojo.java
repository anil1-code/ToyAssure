package pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
public class AbstractPojo {

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    protected ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    protected ZonedDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    protected Long version;
}