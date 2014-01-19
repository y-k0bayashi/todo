package jp.co.newral.sukima.todo.dao.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DAOが{@link SQLiteDatabase}にアクセスする手段。
 */
public class Session {
	private final SQLiteOpenHelper openHelper;

	public Session(SQLiteOpenHelper openHelper) {
		this.openHelper = openHelper;
	}

	public SQLiteDatabase getReadable() {
		return openHelper.getReadableDatabase();
	}

	public SQLiteDatabase getWritable() {
		return openHelper.getWritableDatabase();
	}
}
