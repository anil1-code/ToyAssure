package com.increff.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class AbstractPojo {

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    protected Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    protected Date updatedOn;

    @Version
    protected Long version;
}