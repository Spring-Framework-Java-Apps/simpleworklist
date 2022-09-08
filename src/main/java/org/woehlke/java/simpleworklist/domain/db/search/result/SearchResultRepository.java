package org.woehlke.java.simpleworklist.domain.db.search.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.java.simpleworklist.domain.db.search.SearchResult;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

}
