package org.n52.gsoc.challenge;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.coding.CodingRepository;
import org.n52.sos.config.SettingsManager;
import org.n52.sos.encode.sos.v2.GetObservationResponseEncoder;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.GetObservationRequest;
import org.n52.sos.response.GetObservationResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.thoughtworks.xstream.XStream;

public class RasdamanIT {

	private RasdamanDAO dao;
	
	@BeforeClass
	public final static void initDecoders() {
		CodingRepository.getInstance();
	}

	@BeforeTest
	public void prepareDAO() {
		// rasguest:rasguest@kahlua.eecs.jacobs-university.de:7001 
		//        this.dao = new RasdamanDAO(null, "kahlua.eecs.jacobs-university.de", "7001", "rasguest", "rasguest");
		this.dao = new RasdamanDAO(null, "localhost", "7001", "rasadmin", "rasadmin");
	}

	@Test
	public void allImageDataFromDBIsContained() throws OwsExceptionReport {
		GetObservationRequest request = new GetObservationRequest(); 
		List<String> offs = new ArrayList<>();
		offs.add("Test");
		request.setOfferings(offs);
		GetObservationResponse imgResp = this.dao.getObservation(request);
		List<OmObservation> observationCollection = imgResp.getObservationCollection();

		assertEquals(observationCollection.size(), 137600);
	}

	public void encodeTest() throws OwsExceptionReport {
		GetObservationResponse imgResp = this.dao.getObservation(null);
		List<OmObservation> observationCollection = imgResp.getObservationCollection();

		XStream xstream = new XStream();
		xstream.alias("imgval", RasdamanData.class);
		xstream.alias("value", RasdamanValue.class);
		xstream.alias("observation", RasdamanObservationValue.class);
		xstream.alias("omobservation", OmObservation.class);
		xstream.registerConverter(new ByteArrayConverter());
		String xmlString = xstream.toXML(observationCollection);

		System.out.println(xmlString);
	}

	@Test
	public void encodeRasdamanObservation() throws OwsExceptionReport {
	    GetObservationResponseEncoder enc = new GetObservationResponseEncoder();
		GetObservationRequest request = new GetObservationRequest(); 
		List<String> offs = new ArrayList<>();
		offs.add("Test");
		request.setOfferings(offs);
		RasdamanDAO getRasImg = new RasdamanDAO(null);
		GetObservationResponse response = getRasImg.getObservation(request);
		XmlObject encoded = enc.encode(response);

		System.out.println(encoded);

		// TODO add assert for correct XMLBeans class

		// TODO could use XmlUnit to compare the created response against a previously created one (loaded from src/test/resources)
	}
}
