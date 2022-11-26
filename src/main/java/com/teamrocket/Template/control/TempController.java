package com.teamrocket.Template.control;

import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.dto.OrderStatusDTO;
import com.teamrocket.Template.service.OrderServiceImpl;
import com.teamrocket.Template.service.TemplateService;
import com.teamrocket.Template.dto.TemplateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/tmpl", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class TempController {

    private final OrderServiceImpl orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int id) {
        OrderDTO dto = orderService.getOrderById(id);
        return ResponseEntity.status(201).body(dto);
    }

    /*@PostMapping
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO dto = orderService.saveOrder(orderDTO);
        return ResponseEntity.status(201).body(dto);
    }*/

    /*@PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@RequestBody OrderStatusDTO status, @PathVariable Long id) {
        OrderDTO dto = orderService.updateOrderStatus(id, status.getStatus());
        return ResponseEntity.status(201).body(dto);
    }*/
}
