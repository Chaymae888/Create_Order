package org.example.create_order.controllers;

import org.example.create_order.client.OrderClient;
import org.example.create_order.exceptions.ProductNotFoundException;
import org.example.create_order.models.Order;
import org.example.create_order.models.Product;
import org.example.create_order.models.ProductResponse;
import org.example.create_order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderClient orderClient;

    @GetMapping(produces = { "application/vnd.myapi.v1+json", "application/vnd.myapi.v1+xml" })
    public ResponseEntity<PagedModel<EntityModel<Order>>> getAllOrders(Pageable pageable) {

        Page<Order> ordersPage = orderService.allOrders(pageable);
        List<EntityModel<Order>> orderModels = ordersPage.getContent().stream()
                .map(this::createOrderEntityModel)
                .collect(Collectors.toList());
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                ordersPage.getSize(),
                ordersPage.getNumber(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages());
        Link linkToAllOrders = linkTo(methodOn(OrderController.class).getAllOrders(pageable)).withSelfRel();

        PagedModel<EntityModel<Order>> pagedModel = PagedModel.of(orderModels, pageMetadata, linkToAllOrders);

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache());
        return ResponseEntity.ok().headers(headers).body(pagedModel);
    }

    private EntityModel<Order> createOrderEntityModel(Order order) {
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getAllOrders(null)).withSelfRel()
        );
    }
    @GetMapping(value = "/{id}", produces = { "application/vnd.myapi.v1+json", "application/vnd.myapi.v1+xml" })
    public ResponseEntity<EntityModel<Order>> getSingleOrder(@PathVariable String id) {
        return orderService.singleOrder(id)
                .map(this::createOrderEntityModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Order>> createOrder(@RequestBody Order order) {
        try {
            ProductResponse productResponse = orderClient.getProductById(order.getProductId());

            Product product = new Product();
            product.setId(productResponse.getProductId());
            product.setName(productResponse.getName());
            product.setDescription(productResponse.getDescription());
            product.setPrice(productResponse.getPrice());
            product.setQuantity(productResponse.getQuantity());

            Order createdOrder = orderService.createOrder(product, order);

            return ResponseEntity.ok(EntityModel.of(createdOrder,
                    linkTo(methodOn(OrderController.class).getSingleOrder(createdOrder.getId())).withSelfRel()));
        }catch(ProductNotFoundException ex){
            throw ex;
        }}


    }