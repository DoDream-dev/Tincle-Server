package tinqle.tinqleServer.domain.policy.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Policy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long id;

    private String name;
    private String title;

    @Column(columnDefinition = "TEXT", length = 30000)
    private String content;
}
