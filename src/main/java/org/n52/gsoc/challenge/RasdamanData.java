package org.n52.gsoc.challenge;

public class RasdamanData {
    
	public byte[] values = null;
	
	public RasdamanData() {
		
	}
	
	public RasdamanData(byte[] values) {
		if (values == null)
			this.values = values;
		this.values = new byte[values.length];
		for (int i = 0; i < values.length; i++) {
			this.values[i] = values[i];
		}
	}
	
	public byte[] getValues() {
		
		return values;
	}

	public void setValues(byte[] values) {
		this.values = values;
	}
	
	@Override
	public String toString() {
		String returnVal = new String();
		if (values == null)
			return null;
		
		returnVal += values[0];
		for (int i = 1; i < this.values.length; i++) {
			returnVal += ", " + values[i];
		}
		
		return returnVal;
	}
}
