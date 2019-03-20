package com.example.demo.repositories;

import com.example.demo.beans.Cafe;
import org.springframework.data.repository.CrudRepository;

public interface CafeRepository extends CrudRepository<Cafe, Long> {
}
