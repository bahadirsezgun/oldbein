package tr.com.abasus.general.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import tr.com.abasus.general.exception.NoColumnFoundFromClassVariablesException;
import tr.com.abasus.general.exception.NoTableOrSynonymFoundFromClassNameException;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.util.OhbeUtil;

public class SqlDaoMysql extends AbaxJdbcDaoSupport implements SqlDao {

	//private String user = null;
	private List<String> tableList;
	//private List<String> viewList;
	private HashMap<String,Integer> typeMap = new HashMap<String, Integer>();
	
	private static final String DATE_TYPE="datetime";
	private static final String TIMESTAMP_TYPE="timestamp";
	private static final String NDATE_TYPE="date";
	
	private String scheme;
	
	public SqlDaoMysql() throws Exception{
		//user = getUser();
		
		//viewList = getViewList();
		typeMap.put("java.lang.String",java.sql.Types.VARCHAR);
		typeMap.put("java.lang.Short",java.sql.Types.NUMERIC);
		typeMap.put("java.lang.Integer",java.sql.Types.NUMERIC);
		typeMap.put("java.lang.Long",java.sql.Types.NUMERIC);
		typeMap.put("java.lang.Float",java.sql.Types.NUMERIC);
		typeMap.put("java.lang.Double",java.sql.Types.NUMERIC);
		typeMap.put("java.math.BigDecimal",java.sql.Types.NUMERIC);
		typeMap.put("boolean",java.sql.Types.BIT);
		typeMap.put("byte",java.sql.Types.TINYINT);
		typeMap.put("short",java.sql.Types.NUMERIC);
		typeMap.put("int",java.sql.Types.NUMERIC);
		typeMap.put("long",java.sql.Types.NUMERIC);
		typeMap.put("float",java.sql.Types.NUMERIC);
		typeMap.put("double",java.sql.Types.NUMERIC);
		typeMap.put("java.sql.Date",java.sql.Types.TIMESTAMP);
		typeMap.put("java.sql.Time",java.sql.Types.TIMESTAMP);
		typeMap.put("java.sql.Timestamp",java.sql.Types.TIMESTAMP);
		typeMap.put("java.util.Date",java.sql.Types.TIMESTAMP);
		typeMap.put("java.math.BigInteger",java.sql.Types.NUMERIC);
		//typeMap.put("java.util.Time",java.sql.Types.TIMESTAMP);
		//typeMap.put("java.util.Timestamp",java.sql.Types.TIMESTAMP);
	}
	
	private Object getBigDecimalObjectForNumericValues(Field field,Object value){
		if(value==null) return null;
		String valueType=field.getType().getName();
		if(typeMap.get(valueType)==java.sql.Types.NUMERIC
				&& !(value instanceof java.math.BigDecimal)){
			if(value instanceof Number){
				if(value instanceof Short)
					value = new BigDecimal(((Short)value));
				else if(value instanceof Integer)
					value = new BigDecimal(((Integer)value));
				else if(value instanceof Float)
					value = new BigDecimal(((Float)value));
				else if(value instanceof Double)
					value = new BigDecimal(((Double)value));
				else if(value instanceof Long)
					value = new BigDecimal(((Long)value));
				else if(value instanceof BigInteger)
					value = new BigDecimal(((BigInteger)value));
			} else
				value = new BigDecimal(value.toString());
		}
			
		
		return value;
	}

	
	public <T> T selectNotReturnNull(T row) throws SqlErrorException{
		try {
			T obj = select(row);
			if(obj == null)
				return row;
			else
				return obj;
		} catch (Exception e) {
			throw new SqlErrorException("Sorgulama isleminde Hata:"+e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T select(T row) throws SqlErrorException {
		try {
			//String tableName=getRealTableNameFromClassName(row.getClass().getSimpleName());
			String tableName=getRealTableNameFromClassName(row);
			List<String> pkList = getPrimaryKeyColumnList(tableName);
			if(pkList==null||pkList.size()==0)return null;

			HashMap<Field, Column> columnMap = getColumnMap(tableName, row.getClass());
			Set<Field> keySet= columnMap.keySet();

			HashMap<Field,Object> valueMap = new HashMap<Field, Object>();
			for (Field field : keySet) {
				
				Object value = getBigDecimalObjectForNumericValues(field,field.get(row));
				
				if(value!=null){
					String columnType=columnMap.get(field).getDataType();
					valueMap.put(field, convertDateOrTimestampToString(columnType, value));
				}
			}
			
			String sql = "select * from "+tableName;
			sql+=" where ";
			boolean f=true;
			HashMap<String,Object> map = new HashMap<String, Object>();
			for (String pk : pkList) {
				for (Field field : keySet) {
					String columnType=columnMap.get(field).getDataType();
					if(columnMap.get(field).getColumnName().equals(pk)){
						if(f){
							if(columnType.equalsIgnoreCase(DATE_TYPE)){
								sql+=(pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
							}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
								sql+=(pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
							}else
							sql+=(pk + " = :"+field.getName());
							f=false;
						}else{
							if(columnType.equalsIgnoreCase(DATE_TYPE)){
								sql+=(" AND "+pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
							}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
								sql+=(" AND "+pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
							}else
							sql+=(" AND "+pk + " = :"+field.getName());
						}
						if(valueMap.get(field)==null)return null;
						map.put(field.getName(), valueMap.get(field));
						break;
					}
				}
			}
			
			
			
			try {
				T obj = (T)findEntityForObject(sql, map, row.getClass());
				return obj;
			} catch (EmptyResultDataAccessException e) {
				//e.printStackTrace();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SqlErrorException("Sorgulama isleminde Hata : "+e.toString());
		}
		
	}

	@Override
	public void insertUpdate(Object row) throws SqlErrorException {
		try {
			
			
			//String tableName=getRealTableNameFromClassName(row.getClass().getSimpleName());
			String tableName=getRealTableNameFromClassName(row);
			
			if(tableName==null)
				throw new NoTableOrSynonymFoundFromClassNameException("class ismi : "+row.getClass().getSimpleName());
			HashMap<Field, Column> columnMap = getColumnMap(tableName, row.getClass()); 
			try {
				insert(tableName,columnMap,row);
			} catch (DataIntegrityViolationException e) {
				update(tableName,columnMap,row);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SqlErrorException("Insert ya da Update isleminde Hata:"+e.toString());
		
		}
	}

	@Override
	public void delete(Object row) throws SqlErrorException {
		try {
			//String tableName=getRealTableNameFromClassName(row.getClass().getSimpleName());
			String tableName=getRealTableNameFromClassName(row);
			HashMap<Field, Column> columnMap = getColumnMap(tableName, row.getClass());
			delete(tableName,columnMap,row);
		} catch (Exception e) {
			throw new SqlErrorException("Delete isleminde Hata:"+e.toString());
		}

	}
	
	
	private void insert(String tableName,HashMap<Field, Column> columnMap,Object row)throws Exception{
		String sql = "insert into "+tableName+" (";
		Set<Field> keySet= columnMap.keySet();
		HashMap<Field,Object> valueMap = new HashMap<Field, Object>();
		
		for (Field field : keySet) {
			Object value = getBigDecimalObjectForNumericValues(field,field.get(row));
			if(value!=null)
				valueMap.put(field, value);
		}
		
		boolean f=true;
		for (Field field : keySet) {
			if(f){ sql+=columnMap.get(field).getColumnName(); f=false;}
			else sql+=(","+columnMap.get(field).getColumnName());
		}
		sql+=") values (";
		f=true;
		for (Field field : keySet) {
			if(f){ sql+=(":"+field.getName()); f=false;}
			else sql+=(",:"+field.getName());
		}
		sql+=")";
		MapSqlParameterSource map = new MapSqlParameterSource();
		for (Field field : keySet) {
			map.addValue(field.getName(), valueMap.get(field),  typeMap.get(field.getType().getName()));
		}
		
		
		update(sql, map);
		
	}
	
	private void update(String tableName,HashMap<Field, Column> columnMap,Object row)throws Exception{
		List<String> pkList = getPrimaryKeyColumnList(tableName);
		if(pkList==null||pkList.size()==0)return;

		String sql = "update "+tableName+" set ";
		Set<Field> keySet= columnMap.keySet();
		HashMap<Field,Object> valueMap = new HashMap<Field, Object>();
		
		for (Field field : keySet) {
			
			Object value = getBigDecimalObjectForNumericValues(field,field.get(row));

			if(value!=null){
				String columnType=columnMap.get(field).getDataType();
				valueMap.put(field, convertDateOrTimestampToString(columnType, value));
			}
		}
		
		boolean f=true;
		for (Field field : keySet) {
			String columnType=columnMap.get(field).getDataType();
			
			
			
			if(f){
				if(columnType.equalsIgnoreCase(DATE_TYPE)){
					sql+=(columnMap.get(field).getColumnName()+"= STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')"); 
				}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
					sql+=(columnMap.get(field).getColumnName()+"= STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
				}else if(columnType.equalsIgnoreCase(NDATE_TYPE)){
					sql+=(columnMap.get(field).getColumnName()+"= STR_TO_DATE(:"+field.getName()+",'%Y%m%d')");
				}else
					sql+=(columnMap.get(field).getColumnName()+"= :"+field.getName()); 
				f=false;
			}else{
				if(columnType.equalsIgnoreCase(DATE_TYPE)){
					sql+=(","+columnMap.get(field).getColumnName()+"= STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')"); 
				}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
					sql+=(","+columnMap.get(field).getColumnName()+"= STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
				}else if(columnType.equalsIgnoreCase(NDATE_TYPE)){
					sql+=(","+columnMap.get(field).getColumnName()+"= STR_TO_DATE(:"+field.getName()+",'%Y%m%d')");
				}else
					sql+=(","+columnMap.get(field).getColumnName()+"= :"+field.getName());
			}
			
		}

		sql+=" where ";
		f=true;
		for (String pk : pkList) {
			for (Field field : keySet) {
				String columnType=columnMap.get(field).getDataType();
				if(columnMap.get(field).getColumnName().equals(pk)){
					if(valueMap.get(field)==null) return;
					if(f){
						
						if(columnType.equalsIgnoreCase(DATE_TYPE)){
							sql+=(pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')"); 
						}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
							sql+=(pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
						}else if(columnType.equalsIgnoreCase(NDATE_TYPE)){
							sql+=(pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d')");
						}else
							sql+=(pk + " = :"+field.getName());
						f=false;
					}else{
						if(columnType.equalsIgnoreCase(DATE_TYPE)){
							sql+=(" AND "+pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')"); 
						}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
							sql+=(" AND "+pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
						}else if(columnType.equalsIgnoreCase(NDATE_TYPE)){
							sql+=(" AND "+pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d')");
						}else
							sql+=(" AND "+pk + " = :"+field.getName());
					}
					break;
				}
			}
		}
		
		MapSqlParameterSource map = new MapSqlParameterSource();
		for (Field field : keySet) {
			Object val= valueMap.get(field);
			Integer typ = typeMap.get(field.getType().getName());
			if(typ==java.sql.Types.TIMESTAMP)
				typ=java.sql.Types.VARCHAR;
			map.addValue(field.getName(), val, typ);
		}
		
		update(sql, map);
	}
	
	private void delete(String tableName,HashMap<Field, Column> columnMap,Object row)throws Exception{
		List<String> pkList = getPrimaryKeyColumnList(tableName);
		if(pkList==null||pkList.size()==0)return;

		String sql = "delete from "+tableName+" where ";
		Set<Field> keySet= columnMap.keySet();
		HashMap<Field,Object> valueMap = new HashMap<Field, Object>();
		
		for (Field field : keySet) {
			Object value = getBigDecimalObjectForNumericValues(field,field.get(row));

			if(value!=null){
				String columnType=columnMap.get(field).getDataType();
				valueMap.put(field, convertDateOrTimestampToString(columnType, value));
			}
		}
		
		boolean f=true;
		//MapSqlParameterSource map = new MapSqlParameterSource();
		HashMap<String,Object> map = new HashMap<String, Object>();
		for (String pk : pkList) {
			for (Field field : keySet) {
				String columnType=columnMap.get(field).getDataType();
				if(columnMap.get(field).getColumnName().equals(pk)){
					if(valueMap.get(field)==null) return;
					if(f){
						if(columnType.equalsIgnoreCase(DATE_TYPE)){
							sql+=(pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')"); 
						}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
							sql+=(pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
						}else if(columnType.equalsIgnoreCase(NDATE_TYPE)){
							sql+=(pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d')");
						}else
							sql+=(pk + " = :"+field.getName());
						f=false;
					}else{
						if(columnType.equalsIgnoreCase(DATE_TYPE)){
							sql+=(" AND "+pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')"); 
						}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
							sql+=(" AND "+pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d%k%i%s')");
						}else if(columnType.equalsIgnoreCase(NDATE_TYPE)){
							sql+=(" AND "+pk + " = STR_TO_DATE(:"+field.getName()+",'%Y%m%d')");
						}else
							sql+=(" AND "+pk + " = :"+field.getName());
					}
					//map.addValue(field.getName(), valueMap.get(field), typeMap.get(field.getType().getName()));
					map.put(field.getName(), valueMap.get(field));
					break;
				}
			}
		}
		
		update(sql, map);
	}
	
	/*private String getUser() throws Exception{
		String sql="select user from dual";
		return findEntityForObject(sql, null, String.class);
	}*/

	private List<String> getTableList() throws Exception{
		String sql="select C.`TABLE_NAME` from INFORMATION_SCHEMA.`TABLES` C where C.`TABLE_SCHEMA`='"+getScheme()+"'";
		//////////System.out.println(sql);
		
		return findEntityList(sql, null, String.class);
	}

	
	
	/*private List<String> getViewList() throws Exception{
		String sql="select view_name from user_views";
		return findEntityList(sql, null, String.class);
	}*/
/*
	private String getRealTableNameFromClassName(String className) throws Exception{
		if(tableList==null)tableList = getTableList();
		for (String name : tableList) {
			if(className.equalsIgnoreCase(name)) return name;
			if(className.equalsIgnoreCase(name.replaceAll("_", ""))) return name;
		}
		
		return null;
	}
	*/
	private String getRealTableNameFromClassName(Object row) throws Exception{
		if(tableList==null)tableList = getTableList();
		String className=row.getClass().getSimpleName();
		
		Method[] declaredMethods= row.getClass().getSuperclass().getDeclaredMethods();
		
		for (Method method : declaredMethods) {
			if(method.getName().equals("getTableName")){
				className=row.getClass().getSuperclass().getSimpleName();
			}
		}
		
		for (String name : tableList) {
			if(className.equalsIgnoreCase(name)) return name;
			if(className.equalsIgnoreCase(name.replaceAll("_", ""))) return name;
		}
		
		return null;
	}

	private List<Column> getColumnList(String tableName) throws Exception{
		/*String sql="select column_name columnName,data_type columnType from all_tab_columns t " +
				   "where t.owner in ('PUBLIC',(select user from dual)) " +
				   "and t.TABLE_NAME = :tableName";*/
		String sql= "SELECT C.`COLUMN_NAME` ,C.`DATA_TYPE`  " +
					"FROM INFORMATION_SCHEMA.`COLUMNS` C " +
					"where C.`TABLE_NAME`= :tableName " +
					"and C.`TABLE_SCHEMA`='"+getScheme()+"'";
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", tableName);
		return findEntityList(sql, paramMap, Column.class);
	}

	private List<String> getPrimaryKeyColumnList(String tableName) throws Exception{
		/*String sql="SELECT cols.column_name " +
					"FROM all_constraints cons, all_cons_columns cols " +
					"WHERE cols.table_name = :tableName " +
					"AND cons.constraint_type = 'P' " +
					"AND cons.constraint_name = cols.constraint_name " +
					"AND cons.owner = cols.owner " +
					"AND cons.owner = (select user from dual) ";*/
		String sql= "SELECT C.`COLUMN_NAME` " +
				"FROM INFORMATION_SCHEMA.`COLUMNS` C " +
				"where C.`TABLE_NAME`= :tableName " +
				"and C.`TABLE_SCHEMA`='"+getScheme()+"'" +
				"and C.`COLUMN_KEY`='PRI'";
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", tableName);
		//@SuppressWarnings({ "rawtypes", "unused" })
		//List list=getJdbcTemplate().queryForList(sql,new Object[]{tableName});
		return findEntityList(sql, paramMap, String.class);
	}

	private Column getRealColumnNameFromClassVariable(String classVariableName,List<Column> columnList){
		for (Column column : columnList) { 
			String realColumnName=column.getColumnName();
			if(realColumnName.equalsIgnoreCase(classVariableName)) return column;
			String str=realColumnName.replaceAll("_", "");
			if(str.equalsIgnoreCase(classVariableName)) return column;
		}
		return null;
	}
	
	private HashMap<Field,Column> getColumnMap(String tableName,Class<?> rowClass) throws Exception{
		HashMap<Field,Column> columnMap=new HashMap<Field,Column>();
		List<Column> columnList = getColumnList(tableName);
		
		Field[] fields=getDeclaredFields(rowClass);
		String fieldList="";
		for (Field field : fields) {
			int m=field.getModifiers();
			if(isFinal(m)||isStatic(m)) continue;
			String classVariableName = field.getName();
			fieldList+=(classVariableName+",");
			Column realColumn = getRealColumnNameFromClassVariable(classVariableName,columnList);
			field.setAccessible(true);
			if(realColumn!=null)
			columnMap.put(field, realColumn);
		}
		
		if(columnMap.size()<=0)
			throw new NoColumnFoundFromClassVariablesException("table column list:"+columnList+" class field list:"+fieldList);
		return columnMap;
	}
	
	private boolean isFinal(int modifier){
		return !((modifier&0x10)==0);
	}
	
	private boolean isStatic(int modifier){
		return !((modifier&0x08)==0);
	}
	
	private Object convertDateOrTimestampToString(String columnType,Object dateOrTime){
		String format=null;
		if(columnType.equalsIgnoreCase(DATE_TYPE)){
			format="yyyyMMddHHmmss";
		}else if(columnType.equalsIgnoreCase(TIMESTAMP_TYPE)){
			format="yyyyMMddHHmmss";
		}else if(columnType.equalsIgnoreCase(NDATE_TYPE)){
			format="yyyyMMdd";
		}else return dateOrTime;
		
		if(dateOrTime instanceof java.sql.Timestamp){
			return OhbeUtil.getTimeByFormat((java.sql.Timestamp)dateOrTime, format);
		}else if(dateOrTime instanceof java.sql.Date){
			return OhbeUtil.getDateStrByFormat((java.sql.Date)dateOrTime, format);
		}else if(dateOrTime instanceof java.util.Date){
			return OhbeUtil.getDateStrByFormat((java.util.Date)dateOrTime, format);
		}else return dateOrTime;
	}
	
	public static class Column{
		
		private String columnName;
		
		private String dataType;
		
		public Column() {
			
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}
		
		@Override
		public String toString() {
			return "Column:"+columnName+"-"+dataType;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private Field[] getDeclaredFields(Class rowClass){
		ArrayList<Field> fieldList = new ArrayList<Field>();
		while(true){
			Field[] fields=rowClass.getDeclaredFields();
			fieldList.addAll(Arrays.asList(fields));
			if(rowClass.getSuperclass().getName().equals("java.lang.Object"))
				break;
			else
				rowClass=rowClass.getSuperclass();
		}
		return fieldList.toArray(new Field[fieldList.size()]);
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	
	
}

