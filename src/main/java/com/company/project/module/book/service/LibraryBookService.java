package com.company.project.module.book.service;

import java.util.List;

import com.company.project.module.book.model.Book;
import com.company.project.module.book.dto.AppointExecution;

/**
 * 业务接口：站在"使用者"角度设计接口 三个方面：方法定义粒度，参数，返回类型（return 类型/异常）
 */
public interface LibraryBookService {

	/**
	 * 查询一本图书
	 * 
	 * @param bookId
	 * @return
	 */
	Book getById(long bookId);

	/**
	 * 查询所有图书
	 * 
	 * @return
	 */
	List<Book> getList();

	/**
	 * 预约图书
	 * 
	 * @param bookId
	 * @param studentId
	 * @return
	 * @throws Exception 
	 */
	AppointExecution appoint(long bookId, long studentId) throws Exception;

}
