package com.github.supercoding.repository.storeSales;

import com.github.supercoding.repository.Items.ItemEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Objects;

// store_sales 1:1 매핑 엔티티
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Builder
@Entity
@Table(name = "store_sales")
public class StoreSales {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "store_name",length = 30)
    private String storeName;

    @Column(name = "amount",nullable = false,columnDefinition = "DEFAULT 0 CHECK(amount) >=0")
    private Integer amount;

    @OneToMany(mappedBy = "storeSales")
    private List<ItemEntity> itemEntities;

}
