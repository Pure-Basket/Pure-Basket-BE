package com.example.purebasketbe.domain.product;

import com.example.purebasketbe.domain.product.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteAllByProductId(Long productId);

    List<Image> findAllByProductId(Long productId);
}
