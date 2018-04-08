package org.solteh.repository;

import org.solteh.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

	@Query(value = "SELECT * FROM news ORDER BY createDate DESC LIMIT 5", nativeQuery = true)
	List<News> findTopNews();

	@Override
	Optional<News> findById(Long aLong);
}
