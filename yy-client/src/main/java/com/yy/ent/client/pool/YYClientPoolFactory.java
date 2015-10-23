package com.yy.ent.client.pool;

import com.yy.ent.client.YYClient;
import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * 类说明：YYPClient连接池工场类;
 *
 * @create:创建时间：2013-3-26 下午3:01:06
 * @author:<a href="mailto:chenxu@yy.com">陈顼</a>
 * @version:v1.00
 */
public class YYClientPoolFactory extends BasePoolableObjectFactory<YYClient> {
    private String m_ip;

    private int m_port;

    private int timeOut = 10 * 1000;// 默认读取超时为10s

    /**
     * 设置超时时间
     *
     * @param millisecond
     * @throws Exception
     */
    public void setSoTimeout(int millisecond)
            throws Exception {
        timeOut = millisecond;
    }

    public YYClientPoolFactory(String ip, int port) {
        m_ip = ip;
        m_port = port;
    }

    @Override
    public void destroyObject(YYClient o)
            throws Exception {
        o.close();
    }

    @Override
    public YYClient makeObject()
            throws Exception {
        YYClient ret = new YYClient();
        return ret;
    }

    @Override
    public boolean validateObject(YYClient o) {
        if (o instanceof YYClient)
            return true;

        return false;
    }
}