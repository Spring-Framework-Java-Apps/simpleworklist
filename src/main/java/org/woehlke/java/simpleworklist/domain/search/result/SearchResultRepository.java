package org.woehlke.java.simpleworklist.domain.search.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

}
