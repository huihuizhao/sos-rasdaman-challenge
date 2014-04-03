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

import rasj.RasException;
import rasj.RasGMArray;
import rasj.RasImplementation;
import rasj.RasPoint;


public class GetRasdamanImage extends AbstractGetObservationDAO {

	public GetRasdamanImage(String service) {
		super(service);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GetObservationResponse getObservation(GetObservationRequest arg0)
			throws OwsExceptionReport {
		List<OmObservation> observationCollection = new ArrayList<>();
		observationCollection = makeRequest();
		
		GetObservationResponse obsResp = new GetObservationResponse();
		obsResp.setObservationCollection(observationCollection);
		return obsResp;
	}
	
	public List<OmObservation> makeRequest() {
		
		List<OmObservation> observationCollection = new ArrayList<>();
		
		String server = "localhost";
		String base = "RASBASE";
		String coll = "rgb";
		String port = "7001";
		String user = "rasadmin";
		String passwd = "rasadmin";

		long sum = 0;
		long pixelcount = 0;

		//System.out.println(server+base+coll+port+user+passwd);

		DBag resultBag = null;
		RasGMArray result = null;
		Transaction myTa = null;
		Database myDb = null;
		OQLQuery myQu = null;

		try
		{
			Implementation myApp = new RasImplementation("http://"+server+":"+port);
			((RasImplementation)myApp).setUserIdentification(user, passwd);
			myDb = myApp.newDatabase();

			System.out.println("Opening database ...");
			myDb.open(base, Database.OPEN_READ_ONLY);

			System.out.println("Starting transaction ...");
			myTa = myApp.newTransaction();
			myTa.begin();

			System.out.println("Retrieving MDDs ...");
			myQu = myApp.newOQLQuery();
			myQu.create("select img from "+ coll+" as img");
			resultBag = (DBag)myQu.execute();


			//System.out.println(resultBag);
			if (resultBag != null)
			{
				Iterator iter = resultBag.iterator();
				while (iter.hasNext())
				{
					result = (RasGMArray)iter.next();
					System.out.println(result);
					if(result.getTypeLength() != 3)
						System.out.println("skipping image because of non-RGB cell type");
					else
					{
						sum = 0;
						pixelcount = 0;

						for(long i = result.spatialDomain().item(0).low();
								i <= result.spatialDomain().item(0).high(); i++)
						{
							for(long j = result.spatialDomain().item(1).low();
									j <= result.spatialDomain().item(1).high(); j++)
							{
								RasPoint p = new RasPoint(i,j);
								
								//Add observation to observationCollection
								OmObservation observation = new OmObservation();
								RasImgObservationValue myObs = new RasImgObservationValue();
								RasImgValue myValue = new RasImgValue();
								RasImg rasImg = new RasImg(result.getCell(p));
								myValue.setValue(rasImg);
								myObs.setValue(myValue);
								observation.setValue(myObs);
								observationCollection.add(observation);
								
								//System.out.println(p);
								sum += result.getCell(p)[0];

							}
						}
						
						pixelcount =  (result.spatialDomain().item(0).high() -
								result.spatialDomain().item(0).low() + 1) *
								(result.spatialDomain().item(1).high() -
										result.spatialDomain().item(1).low() + 1);
						System.out.println("Average over "+pixelcount+" red pixels is "+
								(sum/pixelcount));
						
					}
				}
				System.out.println("All results");
			}

			System.out.println( "Committing transaction ..." );
			myTa.commit();

			System.out.println( "Closing database ..." );
			myDb.close();
		}
		catch (RasException rase)
		{
			System.out.println("An exception has occurred: " + rase.getMessage());
		}
		catch (org.odmg.ODMGException e)
		{
			System.out.println("An exception has occurred: " + e.getMessage());
			System.out.println("Try to abort the transaction ...");
			if(myTa != null) myTa.abort();

			try
			{
				System.out.println("Try to close the database ...");
				if(myDb != null) myDb.close();
			}
			catch ( org.odmg.ODMGException exp )
			{
				System.err.println("Could not close the database: " + exp.getMessage());
			}
		}
		System.out.println( "Done." );
		System.out.println(observationCollection.size() == pixelcount);
		return observationCollection;
	}

}
