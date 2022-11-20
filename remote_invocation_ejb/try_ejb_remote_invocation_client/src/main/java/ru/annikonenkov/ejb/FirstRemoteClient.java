package ru.annikonenkov.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstRemoteClient {

	public static void main(String[] args) throws Exception {
		invokeStatelessBean();
	}

	private static void invokeStatelessBean() throws NamingException {
		final FirstRemoteInterface statelessRemoteCalculator = lookup(FirstRemoteInterface.class);
		System.out.println("Obtained a remote stateless calculator for invocation");
		int first =12;
		int	second = 14;
		int firstInvoce = statelessRemoteCalculator.plus(first, second);
		System.out.println(String.format("The result %d + %d = %d", first, second, firstInvoce));
		
		first = 20;
		second = 30;
		int secondInvoce = statelessRemoteCalculator.plus(first, second);
		System.out.println(String.format("The result %d + %d = %d", first, second, secondInvoce));
		
		MyOwnClass moc = statelessRemoteCalculator.getMyOwnClass();
		int res = moc.multi(firstInvoce, secondInvoce);
		System.out.println(String.format("The result %d * %d = %d", firstInvoce, secondInvoce, res));
	}

	@SuppressWarnings("unchecked")
	private static <T> T lookup(Class<T> clazz) throws NamingException {
		final Hashtable<String, String> jndiProperties = new Hashtable<>();
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");

		jndiProperties.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
		// jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");

		final Context context = new InitialContext(jndiProperties);

		// return (FirstRemoteInterface) context.lookup("ejb:/ejb-remote-server-side/FirstRemoteInterface!"+FirstRemoteInterface.class.getName());
		return (T) context.lookup("ejb:/try-remote-call-ejb-server/FirstRemoteBean!ru.annikonenkov.ejb.FirstRemoteInterface");
	}
}
