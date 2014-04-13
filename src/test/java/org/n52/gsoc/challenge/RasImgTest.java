package org.n52.gsoc.challenge;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.encode.sos.v2.GetObservationResponseEncoder;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.response.GetObservationResponse;
import org.testng.annotations.Test;

import com.thoughtworks.xstream.XStream;

public class RasImgTest {
	/**
	 * Test if the GetObservationResponse contains all the image data from rasdaman
	 * @throws OwsExceptionReport
	 */
	@Test
	public void getRasImgObservation() throws OwsExceptionReport {
		RasdamanDAO getRasImg = new RasdamanDAO(null);
		GetObservationResponse imgResp = getRasImg.getObservation(null);
		List<OmObservation> observationCollection = imgResp.getObservationCollection();
		
		assertEquals(observationCollection.size(), 137600);
	}
	
	public void encodeTest() {
	    RasdamanDAO getRasImg = new RasdamanDAO(null);
        GetObservationResponse imgResp = getRasImg.getObservation(null);
        List<OmObservation> observationCollection = imgResp.getObservationCollection();

        XStream xstream = new XStream();
        xstream.alias("imgval", RasImg.class);
        xstream.alias("value", RasImgValue.class);
        xstream.alias("observation", RasImgObservationValue.class);
        xstream.alias("omobservation", OmObservation.class);
        xstream.registerConverter(new ByteArrayConverter());
        String xmlString = xstream.toXML(observationCollection);

        System.out.println(xmlString);
	}
	
	@Test
	public void encodeRasdamanObservation() throws OwsExceptionReport {
	    GetObservationResponseEncoder enc = new GetObservationResponseEncoder();
	    RasdamanDAO getRasImg = new RasdamanDAO(null);
	    GetObservationResponse response = getRasImg.getObservation(null);
	    XmlObject encoded = enc.encode(response);
	    
	    System.out.println(encoded);
	    
	    // TODO add assert for correct XMLBeans class
	    
	    // TODO could use XmlUnit to compare the created response against a previously created one (loaded from src/test/resources)
	}
}
