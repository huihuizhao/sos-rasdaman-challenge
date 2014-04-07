package org.n52.gsoc.challenge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.xmlbeans.impl.common.XmlObjectList;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.response.GetObservationResponse;

import org.testng.annotations.Test;

import com.thoughtworks.xstream.XStream;

import static org.testng.Assert.assertEquals;

public class Main {

	public static String getXmlObservationResponse() throws OwsExceptionReport {
		GetRasdamanImage getRasImg = new GetRasdamanImage(null);
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
		return null;
	}

	public static void main(String[] args) throws OwsExceptionReport {

		getXmlObservationResponse();
	}
}
