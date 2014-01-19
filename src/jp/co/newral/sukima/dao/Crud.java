package jp.co.newral.sukima.dao;

/**
 * 単一表のCRUD操作。
 */
public interface Crud<Entity> {
	long create(Entity entity);
	Entity read(long id);
	int update(Entity entity);
	int delete(Entity entity);
}
