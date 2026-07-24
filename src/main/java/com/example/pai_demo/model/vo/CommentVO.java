package com.example.pai_demo.model.vo;

import com.example.pai_demo.model.Comment;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GilbertYoung
 * @date 2026/07/24 14:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CommentVO", description = "CommentVO")
public class CommentVO extends Comment {
    /**
     * 评论点赞数
     */
    private Long likeCount;
}
