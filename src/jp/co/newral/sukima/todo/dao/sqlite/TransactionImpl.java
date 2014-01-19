package jp.co.newral.sukima.todo.dao.sqlite;

import android.database.sqlite.SQLiteDatabase;
import jp.co.newral.sukima.dao.Transaction;

public class TransactionImpl implements Transaction {
	private SQLiteDatabase database;

	public TransactionImpl(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public void begin() {
		database.beginTransaction();
	}

	@Override
	public void success() {
		database.setTransactionSuccessful();
	}

	@Override
	public void end() {
		database.endTransaction();
	}
}
