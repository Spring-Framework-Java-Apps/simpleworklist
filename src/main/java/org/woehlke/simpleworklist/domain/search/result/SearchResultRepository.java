package org.woehlke.simpleworklist.domain.search.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.domain.search.result.SearchResult;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

}
