package jp.co.newral.sukima.todo.logic.impl;

import jp.co.newral.sukima.dao.Transactional;
import jp.co.newral.sukima.todo.dao.TodoDao;
import jp.co.newral.sukima.todo.entity.Todo;
import jp.co.newral.sukima.todo.logic.TodoLogic;


/**
 * Todoに関する処理。
 */
public class TodoLogicImpl implements TodoLogic {
	private final TodoDao todoDao;

	public TodoLogicImpl(TodoDao todoDao) {
		this.todoDao = todoDao;
	}

	@Transactional
	public long create(Todo entity) {
		return todoDao.create(entity);
	}

	public Todo read(long id) {
		return todoDao.read(id);
	}

	@Transactional
	public int update(Todo entity) {
		return todoDao.update(entity);
	}

	@Transactional
	public int delete(Todo entity) {
		return todoDao.delete(entity);
	}

	public Todo[] findAll() {
		return todoDao.find("done=0", null, "_id desc");
	}
}
