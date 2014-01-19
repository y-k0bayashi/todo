package jp.co.newral.sukima.todo.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import jp.co.newral.sukima.dao.Transaction;
import jp.co.newral.sukima.dao.TransactionHandler;
import jp.co.newral.sukima.todo.BuildConfig;

import android.util.Log;

/**
 * 動的プロキシを生成。
 */
class Proxies {

	private Proxies() {}

	@SuppressWarnings("unchecked")
	public static <I, T extends I> I newProxy(T target, Class<I> type, InvocationHandler handler) {
		return (I)Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class<?>[] { type }, handler);
	}

	public static <I, T extends I> I newLogging(T target, Class<I> type) {
		if(!BuildConfig.DEBUG) return target;
		return newProxy(target, type, new LoggingHandler(target));
	}

	public static <I, T extends I, A extends I> I newTransaction(T target, Class<I> type, Class<A> annotatedType, Transaction transaction) {
		return newProxy(target, type, new TransactionHandler(target, annotatedType, transaction));
	}
}

/**
 * メソッドの開始・終了時に、引数・戻り値をログ出力するハンドラ。
 */
class LoggingHandler implements InvocationHandler {

	private Object target;

	public LoggingHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result;
		String tag = target.getClass().getSimpleName() + "#" + method.getName();
		Log.v(tag, String.valueOf("args: " + Arrays.asList(args)));
		try {
			result = method.invoke(target, args);
		}
		catch(InvocationTargetException e) {
			Throwable cause = e.getTargetException();
			Log.v(tag, "catch: " + String.valueOf(cause));
			throw cause;
		}
		Log.v(tag, "result: " + String.valueOf(result));
		return result;
	}
}
