package com.sequenceiq.cloudbreak.structuredevent.repository;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
@Transactional(TxType.REQUIRED)
public interface CDPResourceRepository<T extends AccountAwareResource, ID extends Serializable> extends CrudRepository<T, ID> {

    Set<T> findAllByAccount(String accountId);

    Optional<T> findByNameAndAccountId(String name, String accountId);

    Set<T> findByNamesInAndAccountId(Set<String> names, String accountId);

    Optional<T> findByCrnAndAccountId(String crn, String accountId);

    Set<T> findByCrnsInAndAccountId(Set<String> crn, String accountId);
}
