package org.n52.gsoc.challenge;

import org.n52.sos.ogc.gml.time.Time;
import org.n52.sos.ogc.om.ObservationValue;
import org.n52.sos.ogc.om.values.Value;

public class RasdamanObservationValue implements ObservationValue<Value<RasdamanData>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Value<RasdamanData> value = null;
	
	@Override
	public Time getPhenomenonTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<RasdamanData> getValue() {
		return this.value;
	}

	@Override
	public void setPhenomenonTime(Time arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Value<RasdamanData> rasImgValue) {
		this.value = rasImgValue;
	}

}
