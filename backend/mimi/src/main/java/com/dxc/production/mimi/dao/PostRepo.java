package com.dxc.production.mimi.dao;

import com.dxc.production.mimi.model.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepo extends JpaRepository<PostEntity, Long> {

    @Query("SELECT p FROM PostEntity p WHERE p.deleteFlag = 0 ORDER BY p.id DESC") // 0 = not deleted
    Page<PostEntity> findAllActivePostOrderByIdDesc(Pageable pageable);

    @Query("SELECT p FROM PostEntity p ORDER BY p.id DESC")
    Page<PostEntity> findAllPostOrderByIdDesc(Pageable pageable);

    PostEntity findById(long id);

    @Modifying
    @Query("UPDATE PostEntity p SET p.deleteFlag = :deleteFlag WHERE p.username = :username")
    void updatePostStatusByUsername(String username, int deleteFlag);
}
