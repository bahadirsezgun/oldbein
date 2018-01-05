package tr.com.abasus.ptboss.result.entity;

import java.io.Serializable;
import java.util.List;

public class HmiResultObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	private String resultMessage;
	
	private int resultStatu;

	private int loadedValue;

	private Object obj;

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public int getResultStatu() {
		return resultStatu;
	}

	public void setResultStatu(int resultStatu) {
		this.resultStatu = resultStatu;
	}

	public int getLoadedValue() {
		return loadedValue;
	}

	public void setLoadedValue(int loadedValue) {
		this.loadedValue = loadedValue;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
	
	
}
