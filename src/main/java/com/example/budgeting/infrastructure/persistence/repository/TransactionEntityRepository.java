package com.example.budgeting.infrastructure.persistence.repository;

import com.example.budgeting.domain.Category;
import com.example.budgeting.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionEntityRepository extends CrudRepository<TransactionEntity, UUID> {


    List<TransactionEntity> findAllByCategory(Category category);
    @Query("SELECT SUM(t.amount) FROM TransactionEntity t WHERE t.category = :category")
    Long sumAmountByCategory(@Param("category") Category category);
}