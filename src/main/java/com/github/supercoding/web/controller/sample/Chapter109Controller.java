package com.github.supercoding.web.controller.sample;

import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.items.Item;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//

@RestController
@RequestMapping("/api/sample")
@RequiredArgsConstructor
@Slf4j
public class Chapter109Controller {

    private final ElectronicStoreItemService electronicStoreItemService;

    @GetMapping("/items-prices")
    @Operation(summary = "Item price 조회(query문)")
    public List<Item> findItemByPrices(HttpServletRequest httpServletRequest) {
        Integer maxPrice = Integer.valueOf(httpServletRequest.getParameter("max"));
        return electronicStoreItemService.findItemsOrderByPrices(maxPrice);
    }

    // id 삭제
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Item 삭제")
    public void deleteItemByPathId(@PathVariable String id, HttpServletResponse httpServletResponse) throws IOException {
        electronicStoreItemService.deleteItem(id);
        String responseMessage = "Object with id = " + id + " deleted";
        httpServletResponse.getOutputStream().println(responseMessage);
    }
}

