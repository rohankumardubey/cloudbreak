package com.sequenceiq.freeipa.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sequenceiq.cloudbreak.quartz.metric.statusmetric.StackCountByStatusView;
import com.sequenceiq.cloudbreak.workspace.repository.EntityType;
import com.sequenceiq.common.api.type.Tunnel;
import com.sequenceiq.freeipa.entity.StackStatus;

@EntityType(entityClass = StackStatus.class)
@Transactional(TxType.REQUIRED)
public interface StackStatusRepository extends CrudRepository<StackStatus, Long> {

    Optional<StackStatus> findFirstByStackIdOrderByCreatedDesc(long stackId);

    @Query("SELECT COUNT(status) as count, st.status as status " +
            "FROM StackStatus st " +
            "WHERE st.id IN (SELECT s.stackStatus.id FROM Stack s WHERE s.terminated < 0 AND s.cloudPlatform = :cloudPlatform) " +
            "GROUP BY (st.status)")
    List<StackCountByStatusView> countStacksByStatusAndCloudPlatform(String cloudPlatform);

    @Query("SELECT COUNT(status) as count, st.status as status " +
            "FROM StackStatus st " +
            "WHERE st.id IN (SELECT s.stackStatus.id FROM Stack s WHERE s.terminated < 0 AND s.tunnel = :tunnel) " +
            "GROUP BY (st.status)")
    List<StackCountByStatusView> countStacksByStatusAndTunnel(Tunnel tunnel);
}


