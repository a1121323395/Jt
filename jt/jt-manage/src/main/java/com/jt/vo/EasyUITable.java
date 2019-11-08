package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyUITable {
	/**
	 * 数据转化为JSON串时调用属性的get方法
	 * getTotal() ---get去掉---首字母小写生成key
	 * value:利用get方法获取的值
	 * 
	 * JSON转化为对象,调用对象的set方法
	 */
	private Integer total;
	private List<?> rows;
}
