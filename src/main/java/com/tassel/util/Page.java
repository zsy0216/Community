package com.tassel.util;

/**
 * @author Ep流苏
 * @Date: 2020/6/14 11:17
 * @Description: 封装分页
 */
public class Page {

    /**
     * 当前页码
     */
    private Integer current = 1;
    /**
     * 页记录上限
     */
    private Integer limit = 10;
    /**
     * 数据总数（用于计算总页数）
     */
    private Integer rows;
    /**
     * 查询路径：用于复用分页路径
     */
    private String path;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 数据起始行 current * limit - limit
     */
    public Integer getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 总页数 rows / limit [+ 1]
     */
    public Integer getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 起始页码
     */
    public Integer getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    /**
     * 终止页码
     */
    public Integer getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }
}
