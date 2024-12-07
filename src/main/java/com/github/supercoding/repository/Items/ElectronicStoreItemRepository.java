package com.github.supercoding.repository.Items;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElectronicStoreItemRepository {

    List<ItemEntity> findAllItems();

    Integer saveItem(ItemEntity itemEntity);

    ItemEntity updateItemEntity(Integer idInt, ItemEntity itemEntity);

    ItemEntity findPathId(Integer id);


    List<ItemEntity> findItemByIds(List<String> ids);

    boolean existsById(String id);

    void deleteById(String id);

    void updateItemStock(Integer itemId, Integer i);
}
