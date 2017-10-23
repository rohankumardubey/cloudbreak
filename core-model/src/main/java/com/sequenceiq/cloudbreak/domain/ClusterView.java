package com.sequenceiq.cloudbreak.domain;

import static com.sequenceiq.cloudbreak.api.model.Status.REQUESTED;
import static com.sequenceiq.cloudbreak.api.model.Status.START_REQUESTED;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sequenceiq.cloudbreak.api.model.Status;

@Entity
@Table(name = "Cluster", uniqueConstraints = @UniqueConstraint(columnNames = {"account", "name"}))
public class ClusterView implements ProvisionEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private StackView stack;

    private String name;

    private String owner;

    private String ambariIp;

    private Boolean emailNeeded;

    private String emailTo;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StackView getStack() {
        return stack;
    }

    public void setStack(StackView stack) {
        this.stack = stack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAmbariIp() {
        return ambariIp;
    }

    public void setAmbariIp(String ambariIp) {
        this.ambariIp = ambariIp;
    }

    public Boolean getEmailNeeded() {
        return emailNeeded;
    }

    public void setEmailNeeded(Boolean emailNeeded) {
        this.emailNeeded = emailNeeded;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isRequested() {
        return REQUESTED.equals(status);
    }

    public boolean isStartRequested() {
        return START_REQUESTED.equals(status);
    }
}
