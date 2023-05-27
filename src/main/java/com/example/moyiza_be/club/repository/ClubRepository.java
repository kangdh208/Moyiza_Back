package com.example.moyiza_be.club.repository;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.CategoryEnum;
import jdk.jfr.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Page<Club> findByCategoryAndTitleContaining(Pageable pageable, CategoryEnum category, String q);

    Page<Club> findByCategory(Pageable pageable, CategoryEnum category);

    Page<Club> findByTitleContaining(Pageable pageable, String q);

    Integer countByOwnerIdAndIsDeletedFalse(Long userId);

    Boolean existsByIdAndOwnerIdEquals(Long clubId, Long userId);
    Optional<Club> findByIdAndIsDeletedFalse(Long clubId);

    List<Club> findByOwnerId(Long userId);
}
