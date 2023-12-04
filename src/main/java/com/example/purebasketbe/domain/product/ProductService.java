package com.example.purebasketbe.domain.product;

import com.example.purebasketbe.domain.product.dto.ProductListResponseDto;
import com.example.purebasketbe.domain.product.dto.ProductRequestDto;
import com.example.purebasketbe.domain.product.dto.ProductResponseDto;
import com.example.purebasketbe.domain.product.entity.Event;
import com.example.purebasketbe.domain.product.entity.Image;
import com.example.purebasketbe.domain.product.entity.Product;
import com.example.purebasketbe.global.exception.CustomException;
import com.example.purebasketbe.global.exception.ErrorCode;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final S3Template s3Template;

    @Value("${products.page.size}")
    private int pageSize;
    @Value("${aws.bucket.name}")
    private String bucket;
    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Transactional(readOnly = true)
    public ProductListResponseDto getProducts(int eventPage, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedAt");

        Pageable eventPageable = PageRequest.of(eventPage, pageSize, sort);
        Slice<Product> eventProducts = productRepository.findAllByDeletedAndEvent(false, Event.DISCOUNT, eventPageable);

        List<ProductResponseDto> responseDtoList = new ArrayList<>();
        for (Product product : eventProducts) {
            responseDtoList.add(pageToDto(product));
        }

        Page<ProductResponseDto> eventPageResponse = new PageImpl<>(responseDtoList);

    }

    private ProductResponseDto pageToDto(Product product) {
        List<URL> imageUrlList = imageRepository.findAllByProductId(product.getId())
                .stream()
                .map(Image::getImgUrl)
                .map((string) -> {
                    try {
                        return new URL(string);
                    } catch (MalformedURLException e) {
                        throw new CustomException(ErrorCode.INVALID_IMAGE);
                    }
                })
                .toList();
        return ProductResponseDto.of(product, imageUrlList);
    }

    @Transactional
    public void registerProduct(ProductRequestDto requestDto, List<MultipartFile> files) {
        checkExistProductByName(requestDto.getName());
        Product newProduct = Product.from(requestDto);
        productRepository.save(newProduct);

        for (MultipartFile file : files) {
            saveAndUploadImage(file, newProduct);
        }
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequestDto requestDto, List<MultipartFile> files) {
        Product product = findProduct(productId);
        product.update(requestDto);

        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                saveAndUploadImage(file, product);
            }
        }
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = findProduct(productId);
        productRepository.delete(product);
        imageRepository.deleteAllByProductId(productId);
    }

    private void saveAndUploadImage(MultipartFile file, Product product) {
        String originalFileName = file.getOriginalFilename();
        String key = UUID.randomUUID() + "_" + originalFileName;
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INVALID_IMAGE);
        }
        ObjectMetadata metadata = ObjectMetadata.builder().contentType("text/plain").build();

        String publicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
        Image newimage = Image.of(originalFileName, publicUrl, product);
        imageRepository.save(newimage);

        s3Template.upload(bucket, key, inputStream, metadata);
    }

    private void checkExistProductByName(String name) {
        if (productRepository.existsByName(name))
            throw new CustomException(ErrorCode.PRODUCT_ALREADY_EXISTS);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );
    }
}
