package com.example.pai_demo.utils;


import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.model.vo.ReturnPageVO;
import com.github.pagehelper.Page;
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

    /**
     * 获取当前线程 PageHelper 分页的起始偏移量（供 ES 等非 MyBatis 查询复用）
     */
    public static int pageFrom() {
        Page<?> local = PageHelper.getLocalPage();
        int pageNum = local != null ? local.getPageNum() : 1;
        int pageSize = local != null ? local.getPageSize() : PAGE_SIZE_LIMIT;
        if (pageSize < 1) {
            pageSize = PAGE_SIZE_LIMIT;
        }
        return (pageNum - 1) * pageSize;
    }

    /**
     * 获取当前线程 PageHelper 分页的页大小
     */
    public static int pageSize() {
        Page<?> local = PageHelper.getLocalPage();
        int pageSize = local != null ? local.getPageSize() : PAGE_SIZE_LIMIT;
        return pageSize < 1 ? PAGE_SIZE_LIMIT : pageSize;
    }

    /**
     * 获取当前线程 PageHelper 排序的首个字段名（orderBy 已为 snake_case，可直接作为 ES 字段）
     */
    public static String orderField() {
        String orderBy = orderBy();
        if (orderBy == null) {
            return "update_time";
        }
        String[] parts = orderBy.split(",")[0].trim().split("\\s+");
        return parts[0];
    }

    /**
     * 当前线程 PageHelper 排序是否升序
     */
    public static boolean orderAsc() {
        String orderBy = orderBy();
        if (orderBy == null) {
            return false;
        }
        String[] parts = orderBy.split(",")[0].trim().split("\\s+");
        return parts.length > 1 && "asc".equalsIgnoreCase(parts[1]);
    }

    private static String orderBy() {
        Page<?> local = PageHelper.getLocalPage();
        String orderBy = local != null ? local.getOrderBy() : null;
        return (orderBy == null || orderBy.trim().isEmpty()) ? null : orderBy.trim();
    }

    /**
     * 清除当前线程的 PageHelper 分页，避免后续 MyBatis 查询（如标签查询）被误分页。
     * ES 路径读不到 MyBatis 的首个查询来消费分页信息，需要手动清理。
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }
}
