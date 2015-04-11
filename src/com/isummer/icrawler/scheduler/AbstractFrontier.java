package com.isummer.icrawler.scheduler;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;

public abstract class AbstractFrontier {

	private Environment env;
	protected Database catalogdatabase;
	protected Database database;
	
	public void close() throws DatabaseException {
		database.close();
		env.close();
	}
	
	protected abstract void put(Object key, Object value);
	
	protected abstract Object get(Object key);
	
	protected abstract Object delete(Object key);
}
