package jp.co.newral.sukima.todo.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 当アプリ用の{@link SQLiteOpenHelper}。
 */
public class OpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "SukimaTodo.db";
	private static final int DB_VERSION = 1;

	public OpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists Todo(" +
			"_id integer primary key autoincrement not null," +
			"subject text not null," +
			"due_date text," +
			"done integer not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
