package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysResult implements Serializable{
	private static final long serialVersionUID = 3139734292806133766L;
	private Integer status;//定义状态嘛,200代表正常,201代表失败
	private String msg;//服务其返回客户端消息
	private Object data;//服务器返回客户端数据
	
	public static SysResult success() {
		return new SysResult(200,null,null);
	}
	
	public static SysResult success(Object object) {
		return new SysResult(200,null,object);
	}
	
	public static SysResult success(String msg,Object object) {
		return new SysResult(200,msg,object);
	}
	
	public static SysResult fail() {
		return new SysResult(201,null,null);
	}
	
	public static SysResult fail(String msg) {
		return new SysResult(201,msg,null);
	}
}
