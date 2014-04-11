package org.n52.gsoc.challenge;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.encode.sos.v2.GetObservationResponseEncoder;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.response.GetObservationResponse;
import org.testng.annotations.Test;

public class RasImgTest {
	/**
	 * Test if the GetObservationResponse contains all the image data from rasdaman
	 * @throws OwsExceptionReport
	 */
	@Test
	public static void getRasImgObservation() throws OwsExceptionReport {
		GetRasdamanImage getRasImg = new GetRasdamanImage(null);
		GetObservationResponse imgResp = getRasImg.getObservation(null);
		List<OmObservation> observationCollection = imgResp.getObservationCollection();
		
		assertEquals(observationCollection.size(), 137600);
	}
	
	@Test
	public static void encodeRasdamanObservation() throws OwsExceptionReport {
	    GetObservationResponseEncoder enc = new GetObservationResponseEncoder();
	    GetRasdamanImage getRasImg = new GetRasdamanImage(null);
	    GetObservationResponse response = getRasImg.getObservation(null);
	    XmlObject encoded = enc.encode(response);
	    
	    System.out.println(encoded);
	    
	    // TODO add assert for correct XMLBeans class
	    
	    // TODO could use XmlUnit to compare the created response against a previously created one (loaded from src/test/resources)
	}
}
