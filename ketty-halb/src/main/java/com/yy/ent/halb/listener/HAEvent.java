package com.yy.ent.halb.listener;

import java.util.EventObject;

/**
 * 
 * 类说明：;
 *
 * @create:创建时间：2013-4-24 下午2:52:21
 * @author <a href="mailto:kuanglingxuan@chinaduo.com">匡凌轩</a> V1
 * @author <a href="mailto:chenxu@yy.com">陈顼</a>V2
 * @version:v1.00
 */
public class HAEvent extends EventObject{

	private static final long serialVersionUID = 7282959056243872418L;
	
	private Object data;
	
	public HAEvent(Object source)
	{
		super(source);
	}
	
	public HAEvent(Object source,Object data)
	{
		super(source);
		this.data = data;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}
}
