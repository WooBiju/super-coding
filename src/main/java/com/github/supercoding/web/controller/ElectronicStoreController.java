package com.github.supercoding.web.controller;



import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.items.BuyOrder;
import com.github.supercoding.web.dto.items.Item;
import com.github.supercoding.web.dto.items.ItemBody;
import com.github.supercoding.web.dto.items.StoreInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Item Store")
public class ElectronicStoreController {


    private final ElectronicStoreItemService electronicStoreItemService;


    private static int serialItemId = 1;


    @GetMapping("/items")
    @Operation(summary = "전체 Item 조회")
    public List<Item> findAllItem() {
        log.info("GET /items 요청이 들어왔습니다.");
        List<Item> items= electronicStoreItemService.findAllItem();
        log.info("GET /items 응답: " + items);
        return items;
    }

    // 직접 등록
    @PostMapping("/items")
    @Operation(summary = "Item 등록")
    // @RequestBody : ItemBody 에 있는 정보를 직접 조회
    public String registerItem(@RequestBody ItemBody itemBody) {
        Integer itemId = electronicStoreItemService.saveItem(itemBody);
        return "ID: " + itemId;
    }

    // path 로 id 조회
    @GetMapping("/items/{id}")
    @Operation(summary = "Item 조회(path문)")
    public Item findItemById(@PathVariable Integer id) {
        return electronicStoreItemService.findItemById(id);
    }

    // 쿼리로 id 조회
    @GetMapping("/items-query")
    @Operation(summary = "Item 조회(query문)")
    public Item findItemByQueryId(@RequestParam("id") Integer id) {
        return electronicStoreItemService.findItemById(id);
    }

    // 여러개 id 가져오기
    @GetMapping("/items-queries")
    @Operation(summary = "여러 Item 조회(query문)")
    public List<Item> findItemByQueryIds(@RequestParam("id") List<String> ids) {
        log.info("/items-queries 요청 ids: " + ids);
        List<Item> items = electronicStoreItemService.findItemsByIds(ids);
        log.info("/items-queries 응답: " + ids);
        return items;
    }

    // id 삭제
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Item 삭제")
    public String deleteItemByPathId(@PathVariable String id) {
        electronicStoreItemService.deleteItem(id);
        return "Object with id = " + id + " deleted";
    }

    // 수정
    @PutMapping("/items/{id}")
    @Operation(summary = "Item 수정")
    public Item updateItem(@PathVariable String id,@RequestBody ItemBody itemBody) {
        return electronicStoreItemService.updateItem(id,itemBody);
    }

    @PostMapping("/items/buy")
    @Operation(summary = "Item 구매")
    public String buyItem(@RequestBody BuyOrder buyOrder) {
        Integer orderItemNums = electronicStoreItemService.buyItems(buyOrder);
        return "요청하신 Item 중 " + orderItemNums + "개를 구매 하였습니다.";
    }

    @GetMapping("/items-types")
    @Operation(summary = "여러 Item type 조회(query문)")
    public List<Item> findItemByTypes(@RequestParam("type") List<String> types) {
        log.info("/items-types 요청 ids: " + types);
        List<Item> items = electronicStoreItemService.findItemsByTypes(types);
        log.info("/items-types 응답: " + types);
        return items;
    }
    @GetMapping("/items-prices")
    @Operation(summary = "Item price 조회(query문)")
    public List<Item> findItemByPrices(@RequestParam("max") Integer maxValue) {
        return electronicStoreItemService.findItemsOrderByPrices(maxValue);
    }

    @GetMapping("/items-page")
    @Operation(summary = "pagination 지원")
    public Page<Item> findItemPagination(Pageable pageable) {
        return electronicStoreItemService.findAllWithPageable(pageable);

    }

    @GetMapping("/items-types-page")
    @Operation(summary = "pagination 지원2")
    public Page<Item> findItemPagination(@RequestParam("type") List<String> types,Pageable pageable) {
        return electronicStoreItemService.findAllWithPageable(types,pageable);

    }

    @Operation(summary = "전체 stores 정보 검색")
    @GetMapping("/stores")
    public List<StoreInfo> findAllStoreInfo() {
        return electronicStoreItemService.findAllStoreInfo();
    }
}

