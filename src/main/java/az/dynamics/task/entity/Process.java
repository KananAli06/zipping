package az.dynamics.task.entity;

import az.dynamics.task.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

import static az.dynamics.task.model.Status.IN_PROGRESS;

/**
 * @author Kanan
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime modifiedDate;

    @OneToOne(mappedBy = "process")
    private Task task;

    @PrePersist
    void createdAt() {
        this.status = IN_PROGRESS;
        this.createdDate = LocalDateTime.now();
    }
}
