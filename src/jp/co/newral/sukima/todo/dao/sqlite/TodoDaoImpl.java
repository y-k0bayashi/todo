package jp.co.newral.sukima.todo.dao.sqlite;

import java.text.SimpleDateFormat;

import jp.co.newral.sukima.todo.dao.TodoDao;
import jp.co.newral.sukima.todo.entity.Todo;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Todo表のDAO。
 */
public class TodoDaoImpl implements TodoDao {
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private final Session session;

	public TodoDaoImpl(Session session) {
		this.session = session;
	}

	@Override
	public long create(Todo entity) {
		ContentValues values = new ContentValues();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		values.put("subject", entity.getSubject());
		if(entity.getDueDate() != null) values.put("due_date", sdf.format(entity.getDueDate()));
		values.put("done", entity.isDone() ? 1 : 0);
		return session.getWritable().insert("Todo", null, values);
	}

	@Override
	public Todo read(long id) {
		Todo[] result = find("_id=?", new String[] { String.valueOf(id) }, null);
		return result.length > 0 ? result[0] : null;
	}

	@Override
	public int update(Todo entity) {
		ContentValues values = new ContentValues();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		values.put("subject", entity.getSubject());
		values.put("due_date", entity.getDueDate() != null ? sdf.format(entity.getDueDate()) : null);
		values.put("done", entity.isDone() ? 1 : 0);
		return session.getWritable().update("Todo", values, "_id=?", new String[] { String.valueOf(entity.getId()) });
	}

	@Override
	public int delete(Todo entity) {
		return session.getWritable().delete("Todo", "_id=?", new String[] { String.valueOf(entity.getId()) });
	}

	@Override
	public Todo[] find(String selection, String[] selectionArgs, String orderBy) {
		StringBuilder sql = new StringBuilder();
		sql.append("select _id,subject,due_date,done from Todo");
		if(selection != null) sql.append(" where ").append(selection);
		if(orderBy != null) sql.append(" order by ").append(orderBy);

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		SQLiteDatabase db = session.getReadable();
		Cursor c = db.rawQuery(sql.toString(), selectionArgs);
		try {
			c.moveToFirst();
			Todo[] result = new Todo[c.getCount()];
			for(int i = 0; i < result.length; ++i) {
				Todo todo = new Todo();
				todo.setId(c.getLong(0));
				todo.setSubject(c.getString(1));
				try { todo.setDueDate(sdf.parse(c.getString(2))); }
				catch(Exception e) {}
				todo.setDone(c.getLong(3) != 0);
				result[i] = todo;
				c.moveToNext();
			}
			return result;
		}
		finally {
			c.close();
		}
	}
}
