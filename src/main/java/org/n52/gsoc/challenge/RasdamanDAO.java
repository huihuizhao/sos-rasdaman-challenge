
package org.n52.gsoc.challenge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.n52.sos.ds.AbstractGetObservationDAO;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.GetObservationRequest;
import org.n52.sos.response.GetObservationResponse;
import org.odmg.DBag;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.OQLQuery;
import org.odmg.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rasj.RasException;
import rasj.RasGMArray;
import rasj.RasImplementation;
import rasj.RasPoint;

public class RasdamanDAO extends AbstractGetObservationDAO {

    private static Logger log = LoggerFactory.getLogger(RasdamanDAO.class);

    private String server = "localhost";

    private String base = "RASBASE";

    private String coll = "rgb";

    private String port = "7001";

    private String user = "rasadmin";

    private String passwd = "rasadmin";

    public RasdamanDAO(String service) {
        super(service);

        log.info("NEW {}", this);
    }

    public RasdamanDAO(String service, String server, String port, String user, String password) {
        this(service);
        this.server = server;
        this.port = port;
        this.user = user;
        this.passwd = password;
    }

    @Override
    public String toString() {
        return "RasdamanDAO [server=" + server + ", base=" + base + ", coll=" + coll + ", port=" + port + ", user="
                + user + ", passwd=" + passwd + "]";
    }

    @Override
    public GetObservationResponse getObservation(GetObservationRequest request) throws OwsExceptionReport {
        log.debug("Serving request {}", request);

        List<String> offerings = request.getOfferings();

        List<OmObservation> observationCollection = new ArrayList<>();
        observationCollection = makeRequest();

        GetObservationResponse obsResp = new GetObservationResponse();
        obsResp.setObservationCollection(observationCollection);

        log.debug("Returning response: {}", obsResp);
        return obsResp;
    }

    private List<OmObservation> makeRequest() {
        List<OmObservation> observationCollection = new ArrayList<>();

        DBag resultBag = null;
        RasGMArray result = null;
        Transaction myTa = null;
        Database myDb = null;
        OQLQuery myQu = null;

        try {
            Implementation myApp = new RasImplementation("http://" + server + ":" + port);
            ((RasImplementation) myApp).setUserIdentification(user, passwd);
            myDb = myApp.newDatabase();

            log.debug("Opening database connection to {}", myApp);
            myDb.open(base, Database.OPEN_READ_ONLY);

            log.debug("Starting transaction ...");
            myTa = myApp.newTransaction();
            myTa.begin();

            log.debug("Retrieving MDDs ...");
            myQu = myApp.newOQLQuery();
            myQu.create("select img from " + coll + " as img");
            resultBag = (DBag) myQu.execute();

            if (resultBag != null) {
                Iterator iter = resultBag.iterator();
                while (iter.hasNext()) {
                    result = (RasGMArray) iter.next();
                    
                    log.debug("Processing current array: {}", result);
                    
                    if (result.getTypeLength() != 3)
                        log.warn("skipping image because of non-RGB cell type");
                    else {
                        for (long i = result.spatialDomain().item(0).low(); i <= result.spatialDomain().item(0).high(); i++) {
                            for (long j = result.spatialDomain().item(1).low(); j <= result.spatialDomain().item(1).high(); j++) {
                                RasPoint p = new RasPoint(i, j);

                                // Add observation to observationCollection
                                OmObservation observation = new OmObservation();
                                RasImgObservationValue myObs = new RasImgObservationValue();
                                RasImgValue myValue = new RasImgValue();
                                RasImg rasImg = new RasImg(result.getCell(p));
                                myValue.setValue(rasImg);
                                myObs.setValue(myValue);
                                observation.setValue(myObs);
                                observationCollection.add(observation);
                            }
                        }
                    }
                }
            }

            log.debug("Committing transaction ...");
            myTa.commit();

            log.debug("Closing database ...");
            myDb.close();
        }
        catch (RasException rase) {
            log.error("An exception has occurred.", rase);
        }
        catch (org.odmg.ODMGException e) {
            log.error("An exception has occurred", e);
            
            log.debug("Try to abort the transaction ...");
            if (myTa != null)
                myTa.abort();

            try {
                log.debug("Try to close the database ...");
                if (myDb != null)
                    myDb.close();
            }
            catch (org.odmg.ODMGException e2) {
                log.error("Could not close the database!", e2);
            }
        }
        
        log.debug("Done, resulting collection: {}, size: {}", observationCollection, observationCollection.size());
        
        return observationCollection;
    }

}
