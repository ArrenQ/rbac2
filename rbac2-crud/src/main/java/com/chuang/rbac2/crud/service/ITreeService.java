package com.chuang.rbac2.crud.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.chuang.rbac2.crud.entity.api.TreeModel;
import com.chuang.tauceti.rowquery.IRowQueryService;
import com.chuang.tauceti.support.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 树结构服务
 * @author ath
 */
public interface ITreeService<T extends TreeModel> extends IRowQueryService<T> {
    /** 默认排序 */
    int DEFAULT_RANK = 100;

    /**
     * 将 fromId 节点 移动到 toId 节点的后面
     * @param fromId 被移动的节点
     * @param toId 目标节点
     * @return 是否移动成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean moveAfter(int fromId, int toId) {
        T to = findById(toId).orElseThrow(() -> new BusinessException("节点{}不存在", toId));
        T from = findById(fromId).orElseThrow(() -> new BusinessException("节点{}不存在", fromId));

        from.setParentId(to.getParentId());
        from.setParents(to.getParents());
        from.setSortRank(to.getSortRank() + 1);

        List<T> greatList = list(
                Wrappers.lambdaQuery(getEntityClass())
                        .eq(T::getParentId, to.getParentId())
                        .gt(T::getSortRank, to.getSortRank())
        );

        if(!greatList.isEmpty()) {
            greatList.forEach(t -> t.setSortRank(t.getSortRank() + 1));
            updateBatchById(greatList);
        }

        return updateById(from);
    }

    /**
     * 将 fromId 节点 移动到 toId 节点的前面
     * @param fromId 被移动的节点
     * @param toId 目标节点
     * @return 是否移动成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean moveBefore(int fromId, int toId) {
        T to = findById(toId).orElseThrow(() -> new BusinessException("节点{}不存在", toId));
        T from = findById(fromId).orElseThrow(() -> new BusinessException("节点{}不存在", fromId));

        from.setParentId(to.getParentId());
        from.setParents(to.getParents());
        from.setSortRank(to.getSortRank() - 1);


        List<T> lessList = list(
                Wrappers.lambdaQuery(getEntityClass())
                        .eq(T::getParentId, to.getParentId())
                        .lt(T::getSortRank, to.getSortRank())
        );

        if(!lessList.isEmpty()) {
            lessList.forEach(t -> t.setSortRank(t.getSortRank() - 1));
            updateBatchById(lessList);
        }


        return updateById(from);
    }

    /**
     * 将 fromId 节点 移动到 toId 节点的里面
     * @param fromId 被移动的节点
     * @param toId 目标节点
     * @return 是否移动成功
     */
    default boolean moveInner(int fromId, int toId) {
        T to = findById(toId).orElseThrow(() -> new BusinessException("节点{}不存在", toId));
        T from = findById(fromId).orElseThrow(() -> new BusinessException("节点{}不存在", fromId));

        from.setParentId(to.getId());
        from.setParents(to.getParents() + to.getId());

        // 获取 to 内部最后一个菜单的rank值，并对其加1，如果没有，则直接去默认rank
        // 放在最后
        int rank = findOne(
                Wrappers.lambdaQuery(getEntityClass())
                    .select(T::getSortRank)
                    .eq(T::getParentId, to.getId())
                    .orderByDesc(T::getSortRank)
                    .last(" limit 1 ")
        )
                .map(T::getSortRank)
                .map(i -> ++i)
                .orElse(DEFAULT_RANK);
        from.setSortRank(rank);
        return updateById(from);
    }

    /**
     * 将 fromId 节点 向 toId 节点移动
     * @param fromId 被移动的节点
     * @param toId 目标节点
     * @param pos 移动方式， 大于0表示移动到后面，小于0表示移动到前面，等于1表示移动到里面
     * @return 是否移动成功
     */
    default boolean move(int fromId, int toId, int pos) {
        if (pos < 0) {
            // 移动到前面
            return moveBefore(fromId, toId);
        } else if (pos > 0) {
            // 移动到后面
            return moveAfter(fromId, toId);
        } else {
            // 移动到里面
            return moveInner(fromId, toId);
        }
    }

    /**
     * 保存一个树对象
     * @param tree 树对象
     * @return 是否存储成功
     */
    @Override
    default boolean save(T tree) {

        String parentPath;
        if(tree.getParentId() == 0) {
            // 表示是根目录
            parentPath = "0/";
        } else {
            T parent = findById(tree.getParentId())
                    .orElseThrow(() -> new BusinessException("父级节点无法找到"));
            parentPath = parent.getParents() + parent.getId() + "/";
        }

        tree.setParents(parentPath);

        if(Optional.ofNullable(tree.getId()).map(id -> 0 == id).orElse(true)) {
            //是否不存在id
            return SqlHelper.retBool(getBaseMapper().insert(tree));
        } else {
            return updateById(tree);
        }
    }
}
