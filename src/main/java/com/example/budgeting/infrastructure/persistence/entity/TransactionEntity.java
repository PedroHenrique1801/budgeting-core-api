package com.example.budgeting.infrastructure.persistence.entity;

import com.example.budgeting.domain.Category;
import com.example.budgeting.domain.Transaction;
import com.example.budgeting.domain.TransactionId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // LIGA O VIGIA: Avisa o Spring para monitorar esta entidade
public class TransactionEntity {

    @Id
    private UUID id;

    private String description;

    private long amount;

    @Enumerated(EnumType.STRING)
    private Category category;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static TransactionEntity from (Transaction transaction) {

        TransactionEntity entity = new TransactionEntity();
        entity.setId(transaction.getId().uuid());
        entity.setDescription(transaction.getDescription());
        entity.setAmount(transaction.getAmount());
        entity.setCategory(transaction.getCategory());

        return entity;
    }

    public Transaction toDomain() {
        return new Transaction(
                new TransactionId(this.id),
                this.description,
                this.amount,
                this.category
        );
    }
}