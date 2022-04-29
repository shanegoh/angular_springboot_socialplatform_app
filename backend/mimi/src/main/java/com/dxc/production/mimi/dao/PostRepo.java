package com.dxc.production.mimi.dao;

import com.dxc.production.mimi.model.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<PostEntity, Long> {

    @Query("SELECT p FROM PostEntity p WHERE p.deleteFlag = 0 ORDER BY p.id DESC") // 0 = not deleted
    Page<PostEntity> findAllActivePostOrderByIdDesc(Pageable pageable);

    PostEntity findById(long id);

    @Query("SELECT p FROM PostEntity p WHERE p.deleteFlag = 0 AND id = :id ORDER BY p.id DESC") // 0 = not deleted
    PostEntity findActivePostOrderByIdDesc(long id);
}
