package org.woehlke.beachbox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.woehlke.beachbox.entities.Vinyl;

/**
 * Created by Fert on 27.03.2014.
 */
public interface VinylRepository extends JpaRepository<Vinyl, Long> {

    @Query("select v from Vinyl v where v.interpret like %?1% or v.song like %?1% or v.name like %?1%")
    Page<Vinyl> findBySearchStrings( String searchInterpret, Pageable pageable);
}
