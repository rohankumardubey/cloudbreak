package com.sequenceiq.cloudbreak.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sequenceiq.cloudbreak.common.metrics.status.StackCountByStatusView;
import com.sequenceiq.cloudbreak.domain.stack.StackStatus;
import com.sequenceiq.cloudbreak.workspace.repository.EntityType;
import com.sequenceiq.common.api.type.Tunnel;

@EntityType(entityClass = StackStatus.class)
@Transactional(TxType.REQUIRED)
public interface StackStatusRepository extends CrudRepository<StackStatus, Long> {

    Optional<StackStatus> findFirstByStackIdOrderByCreatedDesc(long stackId);

    List<StackStatus> findAllByStackIdOrderByCreatedAsc(long stackId);

    List<StackStatus> findAllByStackIdAndCreatedGreaterThanEqualOrderByCreatedDesc(long stackId, long created);

    @Query("SELECT COUNT(status) as count, st.status as status " +
            "FROM StackStatus st " +
            "WHERE st.id IN (SELECT s.stackStatus.id FROM Stack s WHERE s.terminated IS NULL AND s.type != 'TEMPLATE' AND s.cloudPlatform = :cloudPlatform) " +
            "GROUP BY (st.status)")
    List<StackCountByStatusView> countStacksByStatusAndCloudPlatform(String cloudPlatform);

    @Query("SELECT COUNT(status) as count, st.status as status " +
            "FROM StackStatus st " +
            "WHERE st.id IN (SELECT s.stackStatus.id FROM Stack s WHERE s.terminated IS NULL AND s.type != 'TEMPLATE' AND s.tunnel = :tunnel) " +
            "GROUP BY (st.status)")
    List<StackCountByStatusView> countStacksByStatusAndTunnel(Tunnel tunnel);
}
