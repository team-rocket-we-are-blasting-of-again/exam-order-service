package com.teamrocket.orderservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewOrderItem {
    private int menuItemId;
    private int quantity;

    public NewOrderItem(OrderItemDTO item) {
        this.menuItemId = item.getMenuItemId();
        this.quantity = item.getAmount();
    }

    public static List<NewOrderItem> fromItemDTOList(List<OrderItemDTO> items) {
        List<NewOrderItem> list = new ArrayList<>();
        for (OrderItemDTO item: items
             ) {
            list.add(new NewOrderItem(item));
        }
        return list;
    }
}
