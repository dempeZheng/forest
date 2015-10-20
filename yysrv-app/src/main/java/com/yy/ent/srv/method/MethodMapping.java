package com.yy.ent.srv.method;

import com.yy.ent.srv.exception.JCodecException;
import com.yy.ent.srv.exception.JServerException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/15
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public interface MethodMapping {

    /**
     * 根据uri查找对应的class
     *
     * @param uri
     * @return
     */
    public Class<?> getUri(Long uri);

    /**
     * 根据uri查找消息处理器
     *
     * @param uri
     * @return
     */
    public ActionMethod getAction(Long uri);

    /**
     * @param uri
     * @return
     */
    public ActionMethod removeAction(Long uri);

    /**
     * @param uri
     * @return
     */
    public Class<?> removeClass(Long uri);

    /**
     * 添加消息处理的对象
     *
     * @param messageActions
     */
    public void addAction(Object... messageActions) throws JServerException;

    /**
     * 添加协议映射的消息对象
     *
     * @throws JCodecException
     */
    public void addClass(Class<?>... messageClasses) throws JCodecException;
}
