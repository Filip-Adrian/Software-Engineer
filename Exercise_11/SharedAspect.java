package Aspects;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SharedAspect {
	// metodo attach
	public static <T> T attach(T target) {
		// controllo che target non sia NULL
		if (target == null) {
			throw new IllegalArgumentException("taget == null");
		}
		// <?> viene utilizzato quando si desidera lavorare con un tipo generico, ma non
		// si conosce il tipo specifico in fase
		// di compilazione.Viene dedotto a run-time
		// ottengo la classe (ottenendone il nome completo, es. "class java.util.LinkedList")
		Class<?> targetClass = target.getClass();
		
	    // dobbiamo ottenere tutte le interfacce
		// perchè il proxy dovrà avere tutte le interfacce dell'oggetto target
		Class<?>[] targetInterfaces = targetClass.getInterfaces();

		Object proxy = Proxy.newProxyInstance(targetClass.getClassLoader(), targetInterfaces,
				new InnerInvocationHandler(target));

		@SuppressWarnings("unchecked")
		T result = (T) proxy;
		return result;
	}

	private static class InnerInvocationHandler implements InvocationHandler {
		private Object lock;
		private Object target;

		private InnerInvocationHandler(Object target) {
			this.target = target;

			this.lock = new Object();
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
			try {
				synchronized (lock) {
					Object result = method.invoke(target, arguments);

					return result;
				}
			} catch (InvocationTargetException exception) {
				throw exception.getCause();
			} catch (Throwable throwable) {
				throw throwable;
			}
		}
	}
}
