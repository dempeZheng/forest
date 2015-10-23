package com.yy.ent.halb.listener;

import java.util.EventListener;

/**
 * 
 * 类说明： 高可用监听器接口;
 *
 * @create:创建时间：2013-4-24 上午11:11:42
 * @author <a href="mailto:kuanglingxuan@chinaduo.com">匡凌轩</a> V1
 * @author <a href="mailto:chenxu@yy.com">陈顼</a>V2
 * @version:v1.00
 */
public interface HAListener extends EventListener{
	void handleEvent(HAEvent event)throws Exception;
}