package com.chuang.rbac2.crud.policy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 系统配置策略
 * @author ath
 */
public interface SystemConfigPolicy {

    /**
     * 清理缓存
     */
    void clearCached();

    /**
     * 是否包含key
     * @param key 配置key
     * @return 是否包含
     */
    boolean contains(String key);

    /**
     * 根据key获取value
     * @param key 配置key
     * @return 配置value
     */
    Optional<String> get(String key);

    default <T extends Collection<String>> T getCollection(String key, Supplier<T> creator) {
        T coll = creator.get();
        Optional<String> str = get(key);
        str.ifPresent(s -> coll.addAll(Arrays.asList(s.split(","))));
        return coll;
    }

    void set(String key, String value, String regex);


    default Optional<Boolean> getBoolean(String key) {
        return getter(key, Boolean::parseBoolean);
    }

    default Optional<BigDecimal> getBigDecimal(String key) {
        return getter(key, BigDecimal::new);
    }

    default Optional<Integer> getInteger(String key) {
        return getter(key, Integer::valueOf);
    }

    default Optional<Double> getDouble(String key) {
        return getter(key, Double::valueOf);
    }

    default Optional<Float> getFloat(String key) {
        return getter(key, Float::valueOf);
    }

    default Optional<Long> getLong(String key) {
        return getter(key, Long::valueOf);
    }

    default <T> Optional<T> getter(String key, Function<String, T> parse) {
        return get(key).map(parse);
    }
}
