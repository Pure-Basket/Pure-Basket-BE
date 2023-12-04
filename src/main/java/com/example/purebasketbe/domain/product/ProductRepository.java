package com.example.purebasketbe.domain.product;

import com.example.purebasketbe.domain.product.entity.Event;
import com.example.purebasketbe.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Query("select p.id, p.name, p.price, p.stock, p.info, p.category, p.event, p.modifiedAt, i.imgUrl from Product p " +
            "left join fetch Image i " +
            "on i.product.id = p.id " +
            "where p.deleted =:isDeleted AND p.event = :event")
    Page<Product> findAllByDeletedAndEvent(boolean isDeleted, Event event, Pageable pageable);

    List<Product> findByIdIn(List<Long> requestIds);
}
