package jp.co.newral.sukima.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link @Transactional}を付けたメソッドの開始・終了時に、トランザクションを制御するハンドラ。
 */
public class TransactionHandler implements InvocationHandler {

	private Object target;
	private Class<?> annotatedType;
	private Transaction transaction;

	public <I, T extends I, A extends I> TransactionHandler(T target, Class<A> annotatedType, Transaction transaction) {
		this.target = target;
		this.annotatedType = annotatedType;
		this.transaction = transaction;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result;
		boolean transactional = annotatedType.getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Transactional.class);
		if(transactional) transaction.begin();
		try {
			result = method.invoke(target, args);
			if(transactional) transaction.success();
		}
		catch(InvocationTargetException e) {
			throw e.getTargetException();
		}
		finally {
			if(transactional) transaction.end();
		}
		return result;
	}
}
