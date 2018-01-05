package tr.com.abasus.general.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;



public class AbaxJdbcDaoSupport extends NamedParameterJdbcDaoSupport {
	
	
	
	public int update(String sql, Object[] args, int[] argTypes)throws Exception{
		int i=0;
		//assert sql!=null : "SQL COMMAND IS NULL";
		if(args!=null && argTypes!=null)
			i= getJdbcTemplate().update(sql,args,argTypes);
		if(args==null && argTypes==null)
			i= getJdbcTemplate().update(sql);
		if(args!=null && argTypes==null)
			i= getJdbcTemplate().update(sql,args);
		
		if(sql==null) throw new Exception("SQL cumlesi NULL olamaz");
		
		
		return i;
	}
	
	//***update delete insert sql vith paramname
	public int update(String sql, Map<String,?> paramMap )throws Exception{
		//assert sql!=null : "SQL COMMAND IS NULL";
		return getNamedParameterJdbcTemplate().update(sql,paramMap);	
	}
	
	//***update delete insert sql vith paramname
	public int update(String sql, MapSqlParameterSource paramMap )throws Exception{
		//assert sql!=null : "SQL COMMAND IS NULL";
		return getNamedParameterJdbcTemplate().update(sql,paramMap);	
	}
	
	
		
		
	
	
	
	
	
	/* ES3Mapper ile sorgulama */
	
	@SuppressWarnings("unchecked")
	public <T> T findEntityForObject(String sql,Map<String,?> paramMap, Class<T> type)throws Exception{
		RowMapper rowMapper = new AbaxMapper(type);
		return (T)getNamedParameterJdbcTemplate().queryForObject(sql, paramMap, rowMapper);
	}
	
	//select with array param and rowmapper
	@SuppressWarnings("unchecked")
	public <T> T findEntityForObject(String sql,Object[] args,int[] argTypes,Class<T> type)throws Exception{
		RowMapper rowMapper = new AbaxMapper(type);
		Object o = null;
		if(args!=null && argTypes!=null)
			o= getJdbcTemplate().queryForObject(sql, args, argTypes,rowMapper);
		if(args==null && argTypes==null)
			o= getJdbcTemplate().queryForObject(sql,rowMapper);
		if(args!=null && argTypes==null)
			o= getJdbcTemplate().queryForObject(sql,args,rowMapper);
		
		if(sql==null) throw new Exception("SQL cumlesi NULL olamaz");
				
		return (T)o;
	}
	
	
	//select with array param and rowmapper
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntityList(String sql,Object[] args,int[] argTypes,Class<T> type)throws Exception{
		RowMapper rowMapper = new AbaxMapper(type);
		List<T> l= null;
		if(args!=null && argTypes!=null)
			l= getJdbcTemplate().query(sql, args, argTypes,rowMapper);
		if(args==null && argTypes==null)
			l= getJdbcTemplate().query(sql,rowMapper);
		if(args!=null && argTypes==null)
			l= getJdbcTemplate().query(sql,args,rowMapper);
		
		if(sql==null) throw new Exception("SQL cumlesi NULL olamaz");
		
		return l;
		
	
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntityList(String sql,Map<String,?> paramMap,Class<T> type)throws Exception{
		RowMapper rowMapper = new AbaxMapper(type);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper);
	}
}
