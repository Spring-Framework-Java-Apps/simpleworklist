package org.woehlke.beachbox.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.beachbox.entities.Vinyl;
import org.woehlke.beachbox.repository.VinylRepository;

import javax.inject.Inject;

/**
 * Created by Fert on 27.03.2014.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class VinylServiceImpl implements VinylService {

    @Inject
    private VinylRepository vinylRepository;

    @Override
    public Page<Vinyl> findAll(Pageable pageable) {
        return vinylRepository.findAll(pageable);
    }

    @Override
    public Page<Vinyl> search(String searchString, Pageable pageable) {
        return vinylRepository.findBySearchStrings(searchString, pageable);
    }

    @Override
    public Vinyl findById(long id) {
        return vinylRepository.findOne(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Vinyl save(Vinyl vinyl) {
        return vinylRepository.saveAndFlush(vinyl);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteById(long id) {
        vinylRepository.delete(id);
    }
}
