package com.dempe.forest.client.cluster;

import com.dempe.forest.codec.Message;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
public interface Caller {

    Object call(Message request);
}
