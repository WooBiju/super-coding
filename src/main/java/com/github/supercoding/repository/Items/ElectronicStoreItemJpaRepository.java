package com.github.supercoding.repository.Items;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectronicStoreItemJpaRepository extends JpaRepository<ItemEntity,Integer> {
    // 자동메서드
    List<ItemEntity> findItemEntitiesByTypeIn(List<String> types);

    List<ItemEntity> findItemEntitiesByPriceIsLessThanEqualOrderByPriceAsc(Integer maxValue);

    Page<ItemEntity> findAllByTypeIn(List<String> types, Pageable pageable);
}
