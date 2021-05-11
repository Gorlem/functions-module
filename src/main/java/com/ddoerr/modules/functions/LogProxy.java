package com.ddoerr.modules.functions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.eq2online.macros.scripting.api.IMacro;

public class LogProxy implements InvocationHandler {
	private final Object object;
	
	public LogProxy(Object macro) {
		this.object = macro;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(object.getClass().getName() + " -> " + method.getName());
		return method.invoke(object, args);
	}

	@SuppressWarnings("unchecked")
	public static <T> T create(T object) {
		return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), new LogProxy(object));
	}
}
