package org.n52.gsoc.challenge;

import org.n52.sos.ogc.om.values.Value;

import rasj.RasPoint;

public class RasdamanValue implements Value<RasdamanData> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RasdamanData rasImg = null;
	
	@Override
	public String getUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RasdamanData getValue() {
		// TODO Auto-generated method stub
		return this.rasImg;
	}

	@Override
	public boolean isSetUnit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSetValue() {
		// TODO Auto-generated method stub
		return this.rasImg != null;
	}

	@Override
	public void setUnit(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(RasdamanData rasImg) {
		// TODO Auto-generated method stub
		this.rasImg = rasImg;
	}

}
