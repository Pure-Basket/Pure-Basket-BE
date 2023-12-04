package com.example.purebasketbe.domain.product;

import com.example.purebasketbe.domain.product.dto.ProductListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductListResponseDto> getProducts(
            @RequestParam(defaultValue = "1", required = false) int eventPage,
            @RequestParam(defaultValue = "1", required = false) int page) {
        ProductListResponseDto responseBody = productService.getProducts(eventPage - 1, page - 1);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
