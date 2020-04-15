package org.woehlke.simpleworklist.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.search.SearchResult;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

}
