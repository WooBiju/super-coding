package com.github.supercoding.service;

import com.github.supercoding.repository.Items.ElectronicStoreItemJpaRepository;
import com.github.supercoding.repository.Items.ElectronicStoreItemRepository;
import com.github.supercoding.repository.Items.ItemEntity;
import com.github.supercoding.repository.storeSales.StoreSales;
import com.github.supercoding.repository.storeSales.StoreSalesJpaRepository;
import com.github.supercoding.repository.storeSales.StoreSalesRepository;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.service.mapper.ItemMapper;
import com.github.supercoding.web.dto.items.BuyOrder;
import com.github.supercoding.web.dto.items.Item;
import com.github.supercoding.web.dto.items.ItemBody;
import com.github.supercoding.web.dto.items.StoreInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreItemService {

    private final ElectronicStoreItemJpaRepository electronicStoreItemJpaRepository;

    private final StoreSalesJpaRepository storeSalesJpaRepository;



    public List<Item> findAllItem() {
        // Repository 가 dto 반환하는 것은 좋지 않음 -> entity 반환하는것이 일반적
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();

        // item 없을때 예외발생
        if(itemEntities.isEmpty()) throw new NotFoundException("아무런 item들을 찾을 수 없습니다.");

        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());   // 반환
    }

    public Integer saveItem(ItemBody itemBody) {
        ItemEntity itemEntity = ItemMapper.INSTANCE.idAndItemBodyToItemEntity(null, itemBody);
        ItemEntity itemEntityCreated;

        try {
            itemEntityCreated = electronicStoreItemJpaRepository.save(itemEntity);
        } catch (RuntimeException e) {
            throw new NotAcceptException("Item을 저장하는 도중에 Error가 발생하였습니다.") ;
        }
        return itemEntityCreated.getId();
    }

    public Item findItemById(Integer id) {
        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당되는 id : " +id+ "의 Item을 찾을 수 없습니다."));

        Item item = ItemMapper.INSTANCE.itemEntityToItem(itemEntity);
        return item;
    }

    public List<Item> findItemsByIds(List<String> ids) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();
        if(itemEntities.isEmpty()) throw new NotFoundException("아무 Item 들을 찾을 수 없습니다.");

        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem)
                                    .filter((item) ->ids.contains(item.getId()))
                                     .collect(Collectors.toList());

    }

    public void deleteItem(String id) {
        Integer itemId = Integer.parseInt(id);
        if (!electronicStoreItemJpaRepository.existsById(itemId)) {  // id 존재하는지 확인
            throw new NotFoundException("삭제하려는 Item 의 id 가 존재하지 않습니다.");
        }
        electronicStoreItemJpaRepository.deleteById(itemId);
    }

    @Transactional(transactionManager = "tmJpa1")
    public Item updateItem(String id, ItemBody itemBody) {
        Integer idInt = Integer.valueOf(id);
        // 1. id를 먼저 찾음
        ItemEntity itemEntityUpdated = electronicStoreItemJpaRepository.findById(idInt)
                .orElseThrow(() -> new NotFoundException("해당되는 id : " +id+ "의 Item을 찾을 수 없습니다."));
        // 2. setItemBody 메서드 만들어서 넣어주면 됨
        itemEntityUpdated.setItemBody(itemBody);


        return ItemMapper.INSTANCE.itemEntityToItem(itemEntityUpdated);
    }

    @Transactional(transactionManager = "tmJpa1")
    public Integer buyItems(BuyOrder buyOrder) {
        // 1. buyOrder 에서 상품 ID 와 수량을 얻어낸다
        // 2. 상품을 조회하여 수량이 얼마나 있는지 확인한다
        // 3. 상품의 수량과 가격을 가지고 계산하여 총 가격을 구한다
        // 4. 상품의 재고에 기존 계산한 재고를 구매하는 수량을 뺀다
        // 5. 상품 구매하는 수량 * 가격 만큼 가계 매상으로 올린다
        // 단, 재고가 아예 없거나 매장을 찾을 수 없으면 살 수 없다

        Integer itemId = buyOrder.getItemId();
        Integer itemNums = buyOrder.getItemNums();

        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("해당 이름 Item 을 찾을 수 없습니다."));

        log.info("===============동작 확인 로그1 =================");

        if(itemEntity.getStoreSales() == null) throw new NotFoundException ("매장을 찾을 수 없습니다.");
        if(itemEntity.getStock() <= 0) throw new NotAcceptException("상품의 재고가 없습니다.");

        Integer successByItemNums;
        if(itemNums >= itemEntity.getStock() ) successByItemNums = itemEntity.getStock();
        else successByItemNums = itemNums;

        Integer totalPrice = successByItemNums * itemEntity.getPrice();

        // Item 재고 감소
        itemEntity.setStock(itemEntity.getStock()-successByItemNums);

        // 트랜잭션 로직
        if(successByItemNums == 4) {
            log.error("4개를 구매하는건 허락하지 않습니다.");
            throw new NotAcceptException("4개를 구매하는건 허락하지 않습니다.");
        }

        log.info("===============동작 확인 로그2 =================");

        // 매장 매상 추가
        StoreSales storeSales =  itemEntity.getStoreSales()
                .orElseThrow(() -> new NotFoundException("요청하신 Store에 해당하는 storeSales 를 찾을수 없습니다."));

        storeSales.setAmount(storeSales.getAmount() + totalPrice);

        return successByItemNums;

    }

    // 자동 메서드 적용
    public List<Item> findItemsByTypes(List<String> types) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByTypeIn(types);
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    public List<Item> findItemsOrderByPrices(Integer maxValue) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByPriceIsLessThanEqualOrderByPriceAsc(maxValue);
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    // pagination 적용
    public Page<Item> findAllWithPageable(Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll(pageable);
        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);

    }

    // pagination 적용
    public Page<Item> findAllWithPageable(List<String> types, Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAllByTypeIn(types, pageable);
        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }

    @Transactional(transactionManager = "tmJpa1")
    public List<StoreInfo> findAllStoreInfo() {
        List<StoreSales> storeSales = storeSalesJpaRepository.findAllFetchJoin();
        return storeSales.stream().map(StoreInfo::new).collect(Collectors.toList());
    }
}
