package com.dempe.ketty.common.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/23
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
public class AccessPolicy {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccessPolicy.class);

    private static final int SECOND = 1 * 1000;

    private static final int MINUT = 1 * 60 * 1000;

    /**
     * 统计上限(在指定时段内，达到上限，则后续的访问将被拒绝)
     */
    private int maxLimit;

    /**
     * 统计时长,单位为：毫秒(比如 1秒内,即 1)
     */
    private int countInterval;

    /**
     * 重置时长,单位为: 分钟 (比如 5分钟后，则为5)
     */
    private int recyleInterval;

    /**
     * 是否启用"清除过期访问记录"定时器  true:启用   false:不启用  默认是启用
     */
    private boolean isStartCleanExpireTimer;

    /**
     * 访问统计信息map
     */
    private Map<String, FilterInfo> filterInfoMap = new ConcurrentHashMap<String, FilterInfo>();

    /**
     * 构造方法:初始化访问控制策略
     *
     * @param maxLimit                统计上限(在指定时段内，达到上限，则后续的访问将被拒绝)
     * @param countInterval           统计时长,单位为：毫秒(比如 1秒内,即 1)
     * @param recyleInterval          重试时长,单位为:分钟 (比如 5分钟后，则为5)
     * @param isStartCleanExpireTimer true:启用  false:不启用
     */
    public AccessPolicy(int maxLimit, int countInterval, int recyleInterval, boolean isStartCleanExpireTimer) {
        this.maxLimit = maxLimit;
        this.countInterval = countInterval;
        this.recyleInterval = recyleInterval;
        this.isStartCleanExpireTimer = isStartCleanExpireTimer;
    }

    /**
     * 构造方法:初始化访问控制策略
     *
     * @param maxLimit       统计上限(在指定时段内，达到上限，则后续的访问将被拒绝)
     * @param countInterval  统计时长,单位为：毫秒(比如 1秒内,即 1)
     * @param recyleInterval 重试时长,单位为:分钟 (比如 5分钟后，则为5)
     */
    public AccessPolicy(int maxLimit, int countInterval, int recyleInterval) {
        this(maxLimit, countInterval, recyleInterval, true);
    }

    /**
     * 构造方法: 默认是countInterval内最多允许访问上限maxLimit; 5分钟后重复检测
     *
     * @param maxLimit      统计上限(在指定时段内，达到上限，则上续的访问将被拒绝)
     * @param countInterval 统计时长,单位为：毫秒(比如 1秒内)
     */
    public AccessPolicy(int maxLimit, int countInterval) {
        this(maxLimit, countInterval, 5 * MINUT, true);
    }

    /**
     * 构造方法: 默认是1s内最多允许访问上限maxLimit; 5分钟后重复检测
     *
     * @param maxLimit 统计上限(在指定时段内，达到上限，则上续的访问将被拒绝)
     */
    public AccessPolicy(int maxLimit) {
        this(maxLimit, 1 * SECOND, 5 * MINUT, true);
    }

    /**
     * 构造方法: 默认是1s内最多允许访问上限20次; 5分钟后重复检测
     */
    public AccessPolicy() {
        this(20, 1 * SECOND, 5 * MINUT, true);
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public int getCountInterval() {
        return countInterval;
    }

    public int getRecyleInterval() {
        return recyleInterval;
    }

    public boolean isStartCleanExpireTimer() {
        return isStartCleanExpireTimer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" maxLimit:");
        sb.append(this.getMaxLimit());
        sb.append(" countInterval:");
        sb.append(this.getCountInterval());
        sb.append("[毫秒] recyleInterval:");
        sb.append(this.getRecyleInterval());
        sb.append("[毫秒] isUseCleanTimer:");
        sb.append(this.isStartCleanExpireTimer());
        return sb.toString();
    }


    /**
     * 日志打印访问控制的信息
     *
     * @param targetKey 访问IP
     * @param isPass    是否通过  true:通过  false:拒绝
     */
    public void logFilterInfo(String targetKey, boolean isPass) {
        if (null == targetKey || targetKey.length() == 0) {
            return;
        }
        FilterInfo filterInfo = filterInfoMap.get(targetKey);
        if (!isPass) {
            System.err.println(filterInfo.toString());
        } else {
            System.out.println(filterInfo.toString());
        }
    }

    /**
     * 清除统计的目标(当时间大于统计周期、同时未达到访问限制的上限时)
     *
     * @param targetKey 统计的目标key
     */
    public void removeTargetKey(String targetKey) {
        filterInfoMap.remove(targetKey);
    }

    /**
     * 访问命中
     *
     * @param targetKey 访问统计key(ip、sid、方法名,...)
     * @return 返回目标key的统计对象
     * @throws Exception
     */
    public FilterInfo addHit(String targetKey) throws Exception {
        synchronized (targetKey.intern()) {
            FilterInfo filterInfo = filterInfoMap.get(targetKey);
            if (null == filterInfo) {
                filterInfo = new FilterInfo(targetKey);
                filterInfoMap.put(targetKey, filterInfo);
                return filterInfo;
            } else {
                filterInfo.getHitCount().incrementAndGet();
                filterInfo.setLastAccessTime(new Date());
                return filterInfo;
            }
        }
    }


    /**
     * 检查本次访问频率是否合法
     *
     * @param targetKey 访问统计key(ip、sid、方法名,...)
     * @return 合法：true    非法：false
     * @throws Exception
     */
    public boolean getFilterResult(String targetKey) throws Exception {
        FilterInfo filterInfo = getFilterInfo(targetKey);
        if (filterInfo == null) {
            return true;
        }
        Date time = filterInfo.getFirstAccessTime();
        long timeInterval = System.currentTimeMillis() - time.getTime();
        if (timeInterval <= this.getCountInterval()) //时间小于统计周期时(1)
        {
            if (filterInfo.getHitCount().get() > this.getMaxLimit()) {
                logFilterInfo(targetKey, false);
                return false;
            } else {
                logFilterInfo(targetKey, true);
                return true;
            }
        } else //时间大于统计周期后(2)
        {
            if (filterInfo.getHitCount().get() <= this.getMaxLimit()) { //未达到访问控制的上限,则清空原来的值
                removeTargetKey(targetKey);
                return true;
            } else {
                if (timeInterval >= this.getRecyleInterval()) { //看时间如果到了重置时长，则清空统计目标重试一次
                    removeTargetKey(targetKey);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * 取得目标的访问控制信息
     *
     * @param targetKey 统计目标key
     * @return
     */
    public FilterInfo getFilterInfo(String targetKey) {
        if (null == targetKey || targetKey.length() == 0) {
            return null;
        }
        return filterInfoMap.get(targetKey);
    }


    /**
     * 清除记录
     */
    public void removeExpireFilterInfo() {
        try {
            if (filterInfoMap.size() == 0) {
                return;
            }
            Iterator<String> inter = filterInfoMap.keySet().iterator();
            while (inter.hasNext()) {
                String key = inter.next();
                FilterInfo value = filterInfoMap.get(key);
                if (null == value) {
                    continue;
                }
                long timeLen = System.currentTimeMillis() - value.getFirstAccessTime().getTime();
                if (timeLen >= this.getRecyleInterval()) {
                    inter.remove();
                    //filterInfoMap.remove(key);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}

