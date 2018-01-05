package tr.com.abasus.general.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;

//public class AbaxMapper implements ParameterizedRowMapper<Object> {
public class AbaxMapper implements RowMapper<Object> {
	@SuppressWarnings("rawtypes")
	Class cls;
	ClassInfo classInfo;

	@SuppressWarnings("rawtypes")
	public AbaxMapper(Class cls) {
		this.cls = cls;
		this.classInfo=getClassInfo(cls);
	}

	@Override
	public Object mapRow(ResultSet resultSet, int i) throws SQLException {
		
		/*
		Object obj = null;
		try {
			obj = cls.newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		final Method mtd[] = cls.getDeclaredMethods();
		ResultSetMetaData md = resultSet.getMetaData();
		
		for (int column = 1; column <= md.getColumnCount(); column++) {
			for (int k = 0; k < mtd.length; k++) {
				Class[] params = mtd[k].getParameterTypes();
                if (mtd[k].getName().toUpperCase().replaceAll("�", "I").equals("SET" + md.getColumnName(column).replaceAll("_", ""))) {
					try {
						if(params[0].getName().equals("long"))
							mtd[k].invoke(obj, resultSet.getLong(md.getColumnName(column)));
						else
							mtd[k].invoke(obj, params[0].cast(resultSet.getObject(md.getColumnName(column))));
					} catch (final Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		return obj;
		*/
		ResultSetMetaData md = resultSet.getMetaData();
		for (int column = 1; column <= md.getColumnCount(); column++) {
			String columnName  = md.getColumnName(column).replaceAll("_", "");
			Object columnValue = resultSet.getObject(md.getColumnName(column));
			if(columnValue instanceof java.sql.Date)
				columnValue=resultSet.getTimestamp(column);
			setValue(this.classInfo, columnName, columnValue);
		}
		Object obj=this.classInfo.clazzObj;
		setNullClassInfoObj(this.classInfo);
		return obj;
	}
	
	private void setNullClassInfoObj(ClassInfo clsInfo){
		if(clsInfo==null)return;
		clsInfo.clazzObj=null;
		setNullClassInfoObj(clsInfo.embedIdField);
		ArrayList<ClassInfo> manyToOneFieldList = clsInfo.manyToOneFieldList;
		for (ClassInfo cInfo : manyToOneFieldList) {
			setNullClassInfoObj(cInfo);
		}
	}
	
	private Object setValue(ClassInfo clsInfo,String columnName,Object columnValue){
		
		try {
		
			if(clsInfo.clazz.getName().indexOf("java.")==0){
				if(clsInfo.clazz.getName().equals("java.lang.Integer")&&columnValue.getClass().getName().equals("java.math.BigDecimal")){
					clsInfo.clazzObj=((BigDecimal)columnValue).intValue();
				}else if(clsInfo.clazz.getName().equals("java.lang.Long")&&columnValue.getClass().getName().equals("java.math.BigDecimal")){
					clsInfo.clazzObj=((BigDecimal)columnValue).longValue();
				}else if(clsInfo.clazz.getName().equals("java.lang.Float")&&columnValue.getClass().getName().equals("java.math.BigDecimal")){
					clsInfo.clazzObj=((BigDecimal)columnValue).floatValue();
				}else if(clsInfo.clazz.getName().equals("java.lang.Double")&&columnValue.getClass().getName().equals("java.math.BigDecimal")){
					clsInfo.clazzObj=((BigDecimal)columnValue).doubleValue();
				}else{
					clsInfo.clazzObj=columnValue;
				}
				
				return clsInfo.clazzObj;
			}
			
			if(clsInfo.clazzObj==null){
				clsInfo.clazzObj=clsInfo.clazz.newInstance();
			}

			ArrayList<Field> normalFieldList=clsInfo.normalFieldList;
			for (Field field : normalFieldList) {
				if(field.getName().toUpperCase(Locale.ENGLISH).equals(columnName.toUpperCase(Locale.ENGLISH))){
					field.setAccessible(true);
					////////////System.out.println(field.getName()+":"+field.getType().getName()+":"+columnValue.getClass().getName()+":"+columnValue);
					
					if(field.getType().getName().equals("short")){
						if(columnValue!=null && columnValue instanceof BigDecimal )
							field.set(clsInfo.clazzObj, ((BigDecimal)columnValue).shortValue());
						else if(columnValue!=null )
							field.set(clsInfo.clazzObj, Short.parseShort(String.valueOf(columnValue).trim()));
					
					
					}else if(field.getType().getName().equals("int")){
						if(columnValue!=null && columnValue instanceof BigDecimal )
							field.set(clsInfo.clazzObj, ((BigDecimal)columnValue).intValue());
						else if(columnValue!=null )
							field.set(clsInfo.clazzObj, Integer.parseInt(String.valueOf(columnValue).trim()));
					
					
					}else if(field.getType().getName().equals("long")){
						if(columnValue!=null && columnValue instanceof BigDecimal )
							field.set(clsInfo.clazzObj, ((BigDecimal)columnValue).longValue());
						else if(columnValue!=null )
							field.set(clsInfo.clazzObj, Long.parseLong(String.valueOf(columnValue).trim()));
					
					
					}else if(field.getType().getName().equals("float")){
						if(columnValue!=null && columnValue instanceof BigDecimal )
							field.set(clsInfo.clazzObj, ((BigDecimal)columnValue).floatValue());
						else if(columnValue!=null )
							field.set(clsInfo.clazzObj, Float.parseFloat(String.valueOf(columnValue).trim()));
					
					
					}else if(field.getType().getName().equals("double")){
						if(columnValue!=null && columnValue instanceof BigDecimal )
							field.set(clsInfo.clazzObj, ((BigDecimal)columnValue).doubleValue());
						else if(columnValue!=null )
							field.set(clsInfo.clazzObj, Double.parseDouble(String.valueOf(columnValue).trim()));
					
					
					}else if(  field.getType().getName().equals("java.util.Date")
							 &&columnValue instanceof java.sql.Timestamp){
					
						field.set(clsInfo.clazzObj, new java.util.Date(((java.sql.Timestamp)columnValue).getTime()));
					
					
					
					}else if(  field.getType().getName().equals("java.util.Date")
							 &&columnValue instanceof java.sql.Date){
						
						field.set(clsInfo.clazzObj, new java.util.Date(((java.sql.Date)columnValue).getTime()));
					
					
					
					}else if(  field.getType().getName().equals("java.sql.Date")
							 &&columnValue instanceof java.util.Date){
						field.set(clsInfo.clazzObj, new java.sql.Date(((java.util.Date)columnValue).getTime()));
					
					
					}else if(  field.getType().getName().equals("java.sql.Date")
							 &&columnValue instanceof java.sql.Timestamp){
						field.set(clsInfo.clazzObj, new java.sql.Date(((java.sql.Timestamp)columnValue).getTime()));
					
					
					}else if(  field.getType().getName().equals("java.sql.Timestamp")
							 &&columnValue instanceof java.sql.Date){
						field.set(clsInfo.clazzObj, new java.sql.Timestamp(((java.sql.Date)columnValue).getTime()));
					
					
					}else if(  field.getType().getName().equals("java.sql.Timestamp")
							 &&columnValue instanceof java.util.Date){
						field.set(clsInfo.clazzObj, new java.sql.Timestamp(((java.util.Date)columnValue).getTime()));
					
					
					}else
						field.set(clsInfo.clazzObj, columnValue);
					break;
				}
			}
			
			if(clsInfo.embedIdField!=null){
				clsInfo.embedId.setAccessible(true);
				clsInfo.embedId.set(clsInfo.clazzObj, setValue(clsInfo.embedIdField,columnName,columnValue));
			}
			
			ArrayList<ClassInfo> manyToOneFieldList = clsInfo.manyToOneFieldList;
			ArrayList<Field> manyToOneList = clsInfo.manyToOneList;
			
			for (int i = 0; i < manyToOneFieldList.size(); i++) {
				ClassInfo clazzInfo=manyToOneFieldList.get(i);
				Field field = manyToOneList.get(i);
				field.setAccessible(true);
				field.set(clsInfo.clazzObj,setValue(clazzInfo,columnName,columnValue));
			}
			
			/*for (ClassInfo clazzInfo : manyToOneFieldList) {
				setValue(clazzInfo,columnName,columnValue);
			}*/
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return clsInfo.clazzObj;
	}
	
	
	
	
	
	@SuppressWarnings("rawtypes")
	private ClassInfo getClassInfo(Class clazz){
		ClassInfo classInfo=new ClassInfo();
		classInfo.clazz=clazz;
		Field[] fields=getDeclaredFields(clazz);//clazz.getDeclaredFields();
		for (Field field : fields) {
			
			int m=field.getModifiers();
			if(isFinal(m)||isStatic(m)) continue;
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if(annotation.annotationType().equals(javax.persistence.EmbeddedId.class)){
					classInfo.embedId=field;
					classInfo.embedIdField=getClassInfo(field.getType());
					break;
				}else if(annotation.annotationType().equals(javax.persistence.ManyToOne.class)){
					classInfo.manyToOneList.add(field);
					classInfo.manyToOneFieldList.add(getClassInfo(field.getType()));
					break;
				}else if(annotation.annotationType().equals(javax.persistence.OneToMany.class)){
					break;
				}else if(annotation.annotationType().equals(javax.persistence.Id.class)){
					classInfo.normalFieldList.add(field);
					break;
				}else{
					classInfo.normalFieldList.add(field);
					break;
				}
			}
			if(annotations==null||annotations.length==0){
				classInfo.normalFieldList.add(field);
			}
		}
		return classInfo;
	}
	
	private int tabIndex=-1;
	public String printClassInfo(ClassInfo classInfo){
		if(classInfo==null)return "";
		tabIndex++;
		StringBuffer buf = new StringBuffer();
		buf.append(println(""));
		buf.append(println("========================================="));
		buf.append(println(classInfo.clazz.getName()));
		buf.append(println("========================================="));
		
		buf.append(println("******************"));
		buf.append(println("*  NORMAL FIELDS *"));
		buf.append(println("******************"));
		ArrayList<Field> normalFieldList=classInfo.normalFieldList;
		for (Field field : normalFieldList) {
			buf.append(println(field.getName()+":"+field.getType().getSimpleName()+":"+field.getType().getModifiers()));
		}
		
		buf.append(println("**********************"));
		buf.append(println("*  EMBEDED ID FIELD  *"));
		buf.append(println("**********************"));
		ClassInfo embedIdField=classInfo.embedIdField;
		if(embedIdField!=null){
			buf.append(printClassInfo(embedIdField));
		}
		buf.append(println("**********************"));
		buf.append(println("*  ManyToOne FIELDS  *"));
		buf.append(println("**********************"));
		ArrayList<ClassInfo> manyToOneFieldList=classInfo.manyToOneFieldList;
		for (ClassInfo classInfo2 : manyToOneFieldList) {
			buf.append(printClassInfo(classInfo2));
		}
		buf.append(println("========================================="));
		buf.append(println(""));
		tabIndex--;
		return buf.toString();
	}
	
	private String println(String str){
		return repeat(" ",tabIndex*2)+"|"+str+"\n";
	}
	
	private String repeat(String str,int num){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < num; i++) {
			buf.append(str);
		}
		return buf.toString();
	}
	
	@Override
	public String toString() {
		return printClassInfo(this.classInfo);
	}
	
	public class ClassInfo{
		
		@SuppressWarnings("rawtypes")
		Class clazz=null;
		Object clazzObj=null;
		Field embedId=null;
		ClassInfo embedIdField=null;
		ArrayList<Field> normalFieldList=new ArrayList<Field>();
		ArrayList<Field> manyToOneList=new ArrayList<Field>();
		ArrayList<ClassInfo> manyToOneFieldList=new ArrayList<ClassInfo>();
		
		public ClassInfo() {
			
		}
		
	}
	
	private boolean isFinal(int modifier){
		return !((modifier&0x10)==0);
	}
	
	private boolean isStatic(int modifier){
		return !((modifier&0x08)==0);
	}
	
	@SuppressWarnings({ "rawtypes" })
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
	
	
}

