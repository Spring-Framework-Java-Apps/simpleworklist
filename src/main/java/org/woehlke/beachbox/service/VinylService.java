package org.woehlke.beachbox.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.beachbox.entities.Vinyl;

/**
 * Created by Fert on 27.03.2014.
 */
public interface VinylService {

    Page<Vinyl> findAll(Pageable pageable);

    Page<Vinyl> search(String searchString, Pageable pageable);

    Vinyl findById(long id);

    Vinyl save(Vinyl vinyl);
}
