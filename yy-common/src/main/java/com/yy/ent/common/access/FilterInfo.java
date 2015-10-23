package com.yy.ent.common.access;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/23
 * Time: 18:37
 * To change this template use File | Settings | File Templates.
 */
public class FilterInfo {

    //目标对象 key
    private Object targetKey;

    //首次访问时间
    private Date firstAccessTime = new Date();

    //访问次数
    private AtomicInteger hitCount = new AtomicInteger(1);

    //末次访问时间
    private Date lastAccessTime = new Date();

    public FilterInfo(Object targetKey)
    {
        this.targetKey = targetKey;
    }

    public Date getFirstAccessTime()
    {
        return firstAccessTime;
    }

    public void setFirstAccessTime(Date firstAccessTime)
    {
        this.firstAccessTime = firstAccessTime;
    }

    public AtomicInteger getHitCount() {
        return hitCount;
    }

    public void setHitCount(AtomicInteger hitCount) {
        this.hitCount = hitCount;
    }

    public Object getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(Object targetKey) {
        this.targetKey = targetKey;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" targetKey:");
        sb.append(this.getTargetKey());
        sb.append(" hitCount:");
        sb.append(this.getHitCount().get());
        sb.append(" firstAccessTime:");
        sb.append(this.getFirstAccessTime());
        sb.append(" lastAccessTime:");
        sb.append(this.getLastAccessTime());
        return sb.toString();
    }
}
