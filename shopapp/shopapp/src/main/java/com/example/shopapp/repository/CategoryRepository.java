package com.example.shopapp.repository;

import com.example.shopapp.model.Category;
import com.example.shopapp.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {

}
