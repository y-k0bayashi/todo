package jp.co.newral.sukima.todo.logic;

import jp.co.newral.sukima.todo.entity.Todo;

/**
 * Todoに関する処理。
 */
public interface TodoLogic {
	long create(Todo entity);
	Todo read(long id);
	int update(Todo entity);
	int delete(Todo entity);
	Todo[] findAll();
}
