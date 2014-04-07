package org.n52.gsoc.challenge;

import static org.testng.Assert.assertEquals;

import java.util.List;

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
}
