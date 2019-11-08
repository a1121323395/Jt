package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyUITree implements Serializable{
	private static final long serialVersionUID = 7230860897896512345L;
	
	private Long id;          //节点ID
	private String text;     //节点名称
	private String state;   //节点状态 open/closed
}
