package jp.co.newral.sukima.todo.dao;

import jp.co.newral.sukima.dao.Crud;
import jp.co.newral.sukima.dao.Query;
import jp.co.newral.sukima.todo.entity.Todo;

/**
 * Todo表のDAO。
 */
public interface TodoDao extends Crud<Todo>, Query<Todo> {
}
