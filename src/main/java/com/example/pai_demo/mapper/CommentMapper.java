package com.example.pai_demo.mapper;

import com.example.pai_demo.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/22 13:59
 */
@Mapper
public interface CommentMapper {
    /**
     * 创建评论
     *
     * @param comment 评论
     * @return 是否创建成功
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into comment (create_time, update_time, article_id, user_id, content, top_comment_id, parent_comment_id, is_delete) " +
            "values (#{comment.createTime},#{comment.updateTime},#{comment.articleId},#{comment.userId}," +
            "#{comment.content},#{comment.topCommentId},#{comment.parentCommentId},#{comment.isDelete})")
    Boolean createComment(@Param("comment") Comment comment);

    /**
     * 增量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    Long updateCommentSelective(@Param("comment") Comment comment);

    /**
     * 全量更新评论
     *
     * @param comment 评论
     * @return 影响的行数
     */
    @Update("update comment set update_time=#{comment.updateTime}," +
            "article_id=#{comment.articleId},user_id=#{comment.userId}," +
            "content=#{comment.content},top_comment_id=#{comment.topCommentId}," +
            "parent_comment_id=#{comment.parentCommentId},is_delete=#{comment.isDelete} where id=#{comment.id} and is_delete=0")
    Long updateCommentAll(@Param("comment") Comment comment);

    /**
     * 根据id获取评论
     *
     * @param id 评论id
     * @return 评论信息
     */
    @Select("select * from comment where id=#{id} and is_delete=0 LIMIT 1")
    Comment getCommentById(@Param("id") Integer id);

    /**
     * 根据文章id获取评论列表
     *
     * @param articleId 文章id
     * @param keyword   关键词
     * @return 评论列表
     */
    @Select("select * from comment where article_id=#{articleId} and is_delete=0 " +
            "and content like CONCAT('%',#{keyword},'%')")
    List<Comment> getCommentListByArticleId(@Param("articleId") Integer articleId, @Param("keyword") String keyword);

    /**
     * 根据顶级评论id获取子评论列表
     *
     * @param topCommentId 顶级评论id
     * @param keyword      关键词
     * @return 评论列表
     */
    @Select("select * from comment where top_comment_id=#{topCommentId} and is_delete=0 " +
            "and content like CONCAT('%',#{keyword},'%')")
    List<Comment> getCommentListByTopCommentId(@Param("topCommentId") Integer topCommentId, @Param("keyword") String keyword);

    /**
     * 根据顶级评论id删除其下所有子评论（软删除）
     *
     * @param topCommentId 顶级评论id
     * @return 影响的行数
     */
    @Update("update comment set is_delete=1 where top_comment_id=#{topCommentId} and is_delete=0")
    Long deleteCommentsByTopCommentId(@Param("topCommentId") Integer topCommentId);
}
