package tr.com.abasus.general.dao;


import tr.com.abasus.general.exception.SqlErrorException;

public interface SqlDao {

	public void insertUpdate(Object row) throws SqlErrorException;
	public void delete(Object row) throws SqlErrorException;
	public <T> T select(T row) throws SqlErrorException;
	public <T> T selectNotReturnNull(T row) throws SqlErrorException;
}