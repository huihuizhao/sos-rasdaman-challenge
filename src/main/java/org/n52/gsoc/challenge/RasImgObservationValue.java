package org.n52.gsoc.challenge;

import org.n52.sos.ogc.gml.time.Time;
import org.n52.sos.ogc.om.ObservationValue;
import org.n52.sos.ogc.om.values.Value;

import rasj.RasPoint;

public class RasImgObservationValue implements ObservationValue<Value<RasImg>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Value<RasImg> value = null;
	
	@Override
	public Time getPhenomenonTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<RasImg> getValue() {
		return this.value;
	}

	@Override
	public void setPhenomenonTime(Time arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Value<RasImg> rasImgValue) {
		this.value = rasImgValue;
	}

}
