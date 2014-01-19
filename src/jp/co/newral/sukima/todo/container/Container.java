package jp.co.newral.sukima.todo.container;

import jp.co.newral.sukima.dao.Transaction;
import jp.co.newral.sukima.todo.MainActivity;
import jp.co.newral.sukima.todo.dao.TodoDao;
import jp.co.newral.sukima.todo.dao.sqlite.OpenHelper;
import jp.co.newral.sukima.todo.dao.sqlite.Session;
import jp.co.newral.sukima.todo.dao.sqlite.TodoDaoImpl;
import jp.co.newral.sukima.todo.dao.sqlite.TransactionImpl;
import jp.co.newral.sukima.todo.logic.TodoLogic;
import jp.co.newral.sukima.todo.logic.impl.TodoLogicImpl;


import android.content.Context;

/**
 * 当アプリを構成するコンポーネントを管理。
 */
public class Container {

	private static Container instance;

	private final Context context;

	private OpenHelper openHelper;
	private Transaction transaction;
	private Session session;

	private TodoDao todoDao;
	private TodoLogic todoLogic;

	synchronized public static Container getInstance(MainActivity context) {
		if(context == null) throw new NullPointerException("context");
		if(instance == null) instance = new Container(context);
		return instance;
	}

	synchronized public static Container getInstance() {
		if(instance == null) throw new IllegalStateException();
		return instance;
	}

	private Container(Context context) {
		this.context = context;
	}

	synchronized public void destroy() {
		getOpenHelper().close();
	}

	private OpenHelper newOpenHelper() {
		return new OpenHelper(context);
	}

	synchronized public OpenHelper getOpenHelper() {
		if(openHelper == null) openHelper = newOpenHelper();
		return openHelper;
	}

	private Transaction newTransaction() {
		return new TransactionImpl(getOpenHelper().getWritableDatabase());
	}

	synchronized public Transaction getTransaction() {
		if(transaction == null) transaction = newTransaction();
		return transaction;
	}

	private Session newSession() {
		return new Session(getOpenHelper());
	}

	synchronized public Session getSession() {
		if(session == null) session = newSession();
		return session;
	}

	private TodoDao newTodoDao() {
		return new TodoDaoImpl(getSession());
	}

	synchronized public TodoDao getTodoDao() {
		if(todoDao == null) todoDao = newTodoDao();
		return todoDao;
	}

	private TodoLogic newTodoLogic() {
		TodoLogicImpl impl = new TodoLogicImpl(getTodoDao());
		TodoLogic target = Proxies.newLogging(impl, TodoLogic.class);
		target = Proxies.newTransaction(target, TodoLogic.class, impl.getClass(), getTransaction());
		return target;
	}

	synchronized public TodoLogic getTodoLogic() {
		if(todoLogic == null) todoLogic = newTodoLogic();
		return todoLogic;
	}
}
