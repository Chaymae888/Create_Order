package org.example.create_order.controllers;

import org.example.create_order.models.Product;
import org.example.create_order.services.ProductService;
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
@RequestMapping("/v1/Products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(produces = { "application/vnd.myapi.v1+json", "application/vnd.myapi.v1+xml" })
    public ResponseEntity<PagedModel<EntityModel<Product>>> getAllProducts(Pageable pageable) {

        Page<Product> ProductsPage = productService.allProducts(pageable);
        List<EntityModel<Product>> ProductModels = ProductsPage.getContent().stream()
                .map(this::createProductEntityModel)
                .collect(Collectors.toList());
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                ProductsPage.getSize(),
                ProductsPage.getNumber(),
                ProductsPage.getTotalElements(),
                ProductsPage.getTotalPages());
        Link linkToAllProducts = linkTo(methodOn(ProductController.class).getAllProducts(pageable)).withSelfRel();

        PagedModel<EntityModel<Product>> pagedModel = PagedModel.of(ProductModels, pageMetadata, linkToAllProducts);

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache());
        return ResponseEntity.ok().headers(headers).body(pagedModel);
    }

    private EntityModel<Product> createProductEntityModel(Product Product) {
        return EntityModel.of(Product,
                linkTo(methodOn(ProductController.class).getAllProducts(null)).withSelfRel()
        );
    }
    @GetMapping(value = "/{id}", produces = { "application/vnd.myapi.v1+json", "application/vnd.myapi.v1+xml" })
    public ResponseEntity<EntityModel<Product>> getSingleProduct(@PathVariable String id) {
        return productService.singleProduct(id)
                .map(this::createProductEntityModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping(value = "/search",produces = { "application/vnd.myapi.v1+json", "application/vnd.myapi.v1+xml" })
    public ResponseEntity<Page<Product>> searchProducts(@RequestParam String query, Pageable pageable) {
        Page<Product> Products = productService.searchProducts(query, pageable);
        return ResponseEntity.ok(Products);
    }

    @PostMapping(produces = { "application/vnd.myapi.v1+json", "application/vnd.myapi.v1+xml" })
    public ResponseEntity<Product> createProduct(@RequestBody Product Product){
        return ResponseEntity.ok(productService.createProduct(Product));
    }

    @PutMapping(value = "/{id}",produces = { "application/vnd.myapi.v1+json", "application/vnd.myapi.v1+xml" })
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product Product){
        return ResponseEntity.ok(productService.updateProduct(id, Product));
    }

    @DeleteMapping(value = "/{id}",produces = { "application/vnd.myapi.v1+json", "application/vnd.myapi.v1+xml" })
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(id);
    }
}