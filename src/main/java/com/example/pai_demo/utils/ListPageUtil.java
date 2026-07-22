package com.example.pai_demo.utils;


import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import static com.example.pai_demo.constants.errorDict.CURRENT_ERROR;
import static com.example.pai_demo.constants.errorDict.PAGESIZE_ERROR;

/**
 * @author Gilbert
 * @date 2020/11/14 16:48
 */
@Data
public class ListPageUtil {
    /**
     * 分页上限
     */
    public static Integer PAGE_SIZE_LIMIT = 1000;

    /**
     * 创建分页
     *
     * @param current  当前页面
     * @param pageSize 页面大小
     * @param sorter   排序方式
     */
    public static void paging(Integer current, Integer pageSize, String sorter) {
        if (current == null && pageSize == null) {
            current = 1;
            pageSize = PAGE_SIZE_LIMIT;
        }
        AssertionUtil.notNull(current, ErrorCode.BIZ_PARAM_ILLEGAL, CURRENT_ERROR);
        AssertionUtil.notNull(pageSize, ErrorCode.BIZ_PARAM_ILLEGAL, PAGESIZE_ERROR);
        if (sorter != null) {
            sorter = OrderToolUtil.toOrderString(sorter);
            PageHelper.startPage(current, pageSize, sorter);
        } else {
            PageHelper.startPage(current, pageSize);
        }
    }

    /**
     * 封装页面
     *
     * @param data 要封装的数据
     * @return 封装好的页面类
     */
    public static <T> ReturnPageVO<T> returnPage(PageInfo<T> data) {
        ReturnPageVO<T> returnPageVO = new ReturnPageVO<>();
        returnPageVO.setPageSize(data.getPageSize());
        returnPageVO.setCurrent(data.getPageNum());
        returnPageVO.setTotal(data.getTotal());
        returnPageVO.setData(data.getList());
        return returnPageVO;
    }
}
