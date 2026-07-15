package com.example.pai_demo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GilbertYoung
 * @date 2026/7/9 10:03
 * 分页插件返回类
 */
@Data
@ApiModel(value = "分页插件返回类", description = "分页插件返回类")
public class ReturnPageVO<T> {
    /**
     * 分页、排序完的数据
     */
    @ApiModelProperty(value = "分页、排序完的数据", name = "data", required = true)
    private List<T> data;
    /**
     * 数据总数（未分页的）
     */
    @ApiModelProperty(value = "数据总数（未分页的）", name = "total", required = true)
    private Long total;
    /**
     * 每页容量
     */
    @ApiModelProperty(value = "每页容量", name = "pageSize", required = true)
    private Integer pageSize;
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", name = "current", required = true)
    private Integer current;
}
