package org.n52.gsoc.challenge;

import java.util.List;

import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.response.GetObservationResponse;

public class Main {
    public static void main(String[] args) throws OwsExceptionReport {
        /*
         * GetObservationRasdaman getObs = new GetObservationRasdaman(null); GetObservationResponse obsResp =
         * getObs.getObservation(null); System.out.println("\nGetObservationResponse:\n" +
         * obsResp.getObservationCollection().get(0).getValue().getValue().getValue());
         */

        GetRasdamanImage getRasImg = new GetRasdamanImage(null);
        GetObservationResponse imgResp = getRasImg.getObservation(null);
        List<OmObservation> observationCollection = imgResp.getObservationCollection();
        for (OmObservation observation : observationCollection) {
            System.out.println("Response: " + observation.getValue().getValue().getValue());
        }
    }
}
