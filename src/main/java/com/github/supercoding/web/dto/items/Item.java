package com.github.supercoding.web.dto.items;

import com.github.supercoding.repository.Items.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class Item {

    private String id;
    private String name;
    private String type;
    private Integer price;
    private Spec spec;


}
