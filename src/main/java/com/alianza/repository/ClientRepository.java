package com.alianza.repository;

import com.alianza.model.Client;
import com.alianza.model.QClient;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, QuerydslPredicateExecutor<Client> , QuerydslBinderCustomizer<QClient> {

   @Override
   default void customize(QuerydslBindings bindings, QClient root) {
      bindings.bind(String.class).first(
              (StringPath path, String value) -> path.containsIgnoreCase(value));
      bindings.excluding(root.id);
   }

   boolean existsBySharedKey(String shared);

   Integer countBySharedKeyContains(String shared);
}
