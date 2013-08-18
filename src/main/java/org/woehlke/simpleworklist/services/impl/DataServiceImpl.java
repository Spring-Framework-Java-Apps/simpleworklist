package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;
import org.woehlke.simpleworklist.repository.DataRepository;
import org.woehlke.simpleworklist.services.DataService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class DataServiceImpl implements DataService {

    @Inject
    private DataRepository dataRepository;

    @Override
    public Page<Data> findByCategory(Category thisCategory,
                                     Pageable request) {
        return dataRepository.findByCategory(thisCategory, request);
    }

    @Override
    public Page<Data> findByRootCategory(Pageable request) {
        return dataRepository.findByCategoryIsNull(request);
    }

    @Override
    public Data findOne(long dataId) {
        return dataRepository.findOne(dataId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Data saveAndFlush(Data entity) {
        return dataRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Data data) {
        dataRepository.delete(data);
    }

    @Override
    public boolean categoryHasNoData(Category category) {
        return dataRepository.findByCategory(category).isEmpty();
    }

}
