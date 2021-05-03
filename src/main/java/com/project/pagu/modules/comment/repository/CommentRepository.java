package com.project.pagu.modules.comment.repository;

import com.project.pagu.modules.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-30 Time: 오후 3:04
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // @Query("SELECT c FROM Comment c "
    //         + "left join Board b "
    //         + "WHERE b.id = :boardId "
    //         + "ORDER BY c.superComment.id asc nulls first, c.createdDate asc ")
    // List<Comment> findCommentByBoardId(@Param("boardId") Long boardId);
}
