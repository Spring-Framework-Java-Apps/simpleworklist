package org.woehlke.java.simpleworklist.domain.db.search.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.java.simpleworklist.domain.db.search.SearchRequest;

@Repository
public interface SearchRequestRepository extends JpaRepository<SearchRequest, Long> {
}
