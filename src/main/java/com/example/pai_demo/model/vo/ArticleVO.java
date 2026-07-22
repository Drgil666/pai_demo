package com.example.pai_demo.model.vo;

import com.example.pai_demo.model.Article;
import com.example.pai_demo.model.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/07/22 10:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ArticleVO", description = "ArticleVO")
public class ArticleVO extends Article {
    /**
     * 文章标签
     */
    @ApiModelProperty(value = "文章标签")
    private List<Tag> articleTag;
    /**
     * 文章评论
     */
    @ApiModelProperty(value = "文章评论")
    private Integer commentCount;
    /**
     * 文章收藏数
     */
    @ApiModelProperty(value = "文章收藏数")
    private Integer favoriteCount;
}
