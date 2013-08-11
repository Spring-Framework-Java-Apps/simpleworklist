package org.woehlke.simpleworklist.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;

public interface DataService {

	Page<Data> findByCategory(Category thisCategory, Pageable request);

	Page<Data> findByCategoryIsNull(Pageable request);

	Data findOne(long dataId);

	Data saveAndFlush(Data persistentData);

	void delete(Data data);
	
	void deleteAll();

    boolean hasNoData(Category category);

}
