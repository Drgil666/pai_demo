package com.example.pai_demo.model.elastic_search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * Elasticsearch 中 comment 索引对应的文档，字段与 MySQL 表列一一对应。
 * 数据由 Canal 负责 MySQL -> ES 同步，本类仅用于查询结果映射。
 */
@Data
@Document(indexName = "comment")
public class CommentDocument {
    @Id
    private Integer id;

    @Field(name = "article_id", type = FieldType.Integer)
    private Integer articleId;

    @Field(name = "user_id", type = FieldType.Integer)
    private Integer userId;

    @Field(name = "content", type = FieldType.Keyword)
    private String content;

    @Field(name = "top_comment_id", type = FieldType.Integer)
    private Integer topCommentId;

    @Field(name = "parent_comment_id", type = FieldType.Integer)
    private Integer parentCommentId;

    @Field(name = "is_delete", type = FieldType.Integer)
    private Integer isDelete;

    @Field(name = "create_time", type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date createTime;

    @Field(name = "update_time", type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date updateTime;
}
