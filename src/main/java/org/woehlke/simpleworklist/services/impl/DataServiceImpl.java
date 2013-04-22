package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;
import org.woehlke.simpleworklist.repository.CategoryRepository;
import org.woehlke.simpleworklist.repository.DataRepository;
import org.woehlke.simpleworklist.services.DataService;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
public class DataServiceImpl implements DataService {
	
	@Inject
	private CategoryRepository categoryNodeRepository;
	
	@Inject
	private DataRepository dataLeafRepository;

	@Override
	public Page<Data> findByCategory(Category thisCategory,
			Pageable request) {
		return dataLeafRepository.findByCategory(thisCategory, request);
	}

	@Override
	public Page<Data> findByCategoryIsNull(Pageable request) {
		return dataLeafRepository.findByCategoryIsNull(request);
	}

	@Override
	public Data findOne(long dataId) {
		return dataLeafRepository.findOne(dataId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public Data saveAndFlush(Data entity) {
		return dataLeafRepository.saveAndFlush(entity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void delete(Data data) {
		dataLeafRepository.delete(data);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void deleteAll() {
		dataLeafRepository.deleteAll();
	}
	
}
