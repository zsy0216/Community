package com.tassel.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.io.Serializable;

/**
 * (DiscussPost)实体类
 *
 * @author Ep流苏
 * @since 2020-06-14 09:43:07
 */
@Data
@Document(indexName = "discusspost", type = "_doc", shards = 6, replicas = 3)
public class DiscussPost implements Serializable {
	private static final long serialVersionUID = -90583991338816189L;

	@Id
	private Integer id;

	@Field(type = FieldType.Integer)
	private Integer userId;

	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
	private String title;

	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
	private String content;

	/**
	 * 0-普通; 1-置顶;
	 */
	@Field(type = FieldType.Integer)
	private Integer type;

	/**
	 * 0-正常; 1-精华; 2-拉黑;
	 */
	@Field(type = FieldType.Integer)
	private Integer status;

    @Field(type = FieldType.Date)
	private Date createTime;

    @Field(type = FieldType.Integer)
	private Integer commentCount;

    @Field(type = FieldType.Double)
	private Double score;
}