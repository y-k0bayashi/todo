package jp.co.newral.sukima.dao;

/**
 * ビューへの要求。
 */
public interface Query<Entity> {
	Entity[] find(String selection, String[] selectionArgs, String orderBy);
}
