package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	/**
	 *restFul风格,根据请求方式处理请求:
	 * 请求方式4种:
	 * get    查询操作@GetMapping 
	 * post   入库操作@PostMapping 
	 * put    更新操作@PutMapping 
	 * delete 删除操作@DeleteMapping 
	 * @param moduleName
	 * @return
	 */
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		return moduleName;
	}
}
