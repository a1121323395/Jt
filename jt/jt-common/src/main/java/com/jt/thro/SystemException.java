package com.jt.thro;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.jt.vo.SysResult;

@RestControllerAdvice //对controller层的异常生效,并返回json格式数据
@Slf4j
public class SystemException {
	//只对运行时异常有效
	@ExceptionHandler(RuntimeException.class)
	public SysResult exception(Throwable throwable) {
		throwable.printStackTrace();
		log.info(throwable.getMessage());
		return SysResult.fail("调用失败");
	}
}
