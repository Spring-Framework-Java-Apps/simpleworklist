package org.woehlke.simpleworklist.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;

public interface DataRepository extends JpaRepository<Data,Long> {
	
	List<Data> findByCategoryIsNull();
	
	List<Data> findByCategory(Category thisCategory);
	
	Page<Data> findByCategoryIsNull(Pageable pageable);
	
	Page<Data> findByCategory(Category thisCategory,Pageable pageable);
}
