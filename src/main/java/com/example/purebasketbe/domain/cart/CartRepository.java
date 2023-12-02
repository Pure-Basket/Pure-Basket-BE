package com.example.purebasketbe.domain.cart;

import com.example.purebasketbe.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository <Cart,Long> {

}
