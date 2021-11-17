package com.chuang.rbac2.crud.entity.api;

/**
 * 树结构的Model
 * @author ATH
 */
public interface TreeModel {
    /**
     * 获取节点id
     * @return 节点id
     */
    Integer getId();

    /**
     * 获取父节点id
     * @return  父节点id
     */
    Integer getParentId();

    /**
     * 设置父节点id
     * @param parentId  父节点id
     */
    void setParentId(Integer parentId);

    /**
     * 设置节点路径
     * @param parents 节点路径
     */
    void setParents(String parents);

    /**
     * 获取节点路径
     * @return 节点路径
     */
    String getParents();

    /**
     * 获取节点排序
     * @return 节点排序
     */
    Integer getSortRank();

    /**
     * 设置节点排序
     * @param sortRank 节点排序
     */
    void setSortRank(Integer sortRank);
}
