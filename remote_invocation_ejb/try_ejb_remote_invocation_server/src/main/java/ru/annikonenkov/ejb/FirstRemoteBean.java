package ru.annikonenkov.ejb;

import org.jboss.logging.Logger;

import jakarta.annotation.Resource;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;

@Stateless
@Remote(FirstRemoteInterface.class)
public class FirstRemoteBean implements FirstRemoteInterface {

	// @Inject
	// Logger log;
	private final Logger log = Logger.getLogger(FirstRemoteBean.class);

	@Resource
	private SessionContext context;

	@Override
	public int plus(int i, int y) {
		int z = i + y;
		log.info(String.format("The result of the pus %d + %d = %d", i, y, z));
		return z;
	}

	@Override
	public MyOwnClass getMyOwnClass() {
		return new MyOwnClass();
	}

}
