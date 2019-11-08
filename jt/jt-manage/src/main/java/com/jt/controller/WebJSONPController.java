package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebJSONPController {
	@SuppressWarnings("unused")
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(10086L).setItemDesc("跨越访问");
		String json = ObjectMapperUtil.toJSON(itemDesc);
		return new JSONPObject(callback, itemDesc);
	}
}
