package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {
}
