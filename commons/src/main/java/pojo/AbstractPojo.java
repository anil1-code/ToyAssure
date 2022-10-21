package pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Date;

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