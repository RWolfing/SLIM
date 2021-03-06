package slim.client.services;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import slim.core.model.Location;

/**
 * Service for all location related communication
 * The methods are almost exactly the same as the API from the webservice so no extra documentation will be added.
 * For more @see LocationResourceImpl
 * 
 * Every Method builds the appropriate HttpMethod with the given parameters and executes it.
 * The response is going to be wrapped in a {@link SlimResult} which contains the status code
 * and also the response object if one exists.
 * 
 * @author Robert Wolfinger
 */
public class LocationService extends SlimService {

    private static final String ERROR_JAXB_LOCATION = ERROR_JAXB + Location.class.getName();

    public LocationService(String serviceUrl, MediaType type) {
        super(serviceUrl + "/locations", type);
    }

    public SlimResult<Location> createLocation(String name, long lattitude, long longitude) {
        SlimResult result = new SlimResult(null);
        PostMethod postMethod = new PostMethod(mServiceBaseURI);
        postMethod.addParameter("name", name);
        postMethod.addParameter("lattitude", String.valueOf(lattitude));
        postMethod.addParameter("longitude", String.valueOf(longitude));
        postMethod.addRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(postMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_CREATED) {
                Location location = (Location) unmarshall(postMethod.getResponseBodyAsString(), Location.class);
                result.setResultContent(location);
            }
        } catch (IOException ex) {
            Logger.getLogger(LocationService.class.getName()).log(Level.SEVERE, ERROR_IO + "create location", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(LocationService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
        whatAmIDoing(result, postMethod);
        return result;
    }

    public SlimResult<Location> updateLocation(int locationId, String name) {
        SlimResult result = new SlimResult(null);
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + locationId);
        NameValuePair pairName = new NameValuePair("name", name);
        putMethod.setQueryString(new NameValuePair[]{pairName});
        putMethod.setRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(putMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Location location = (Location) unmarshall(putMethod.getResponseBodyAsString(), Location.class);
                result.setResultContent(location);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "update location", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
        whatAmIDoing(result, putMethod);
        return result;
    }

    public SlimResult deleteLocation(int locationId) {
        SlimResult result = new SlimResult(null);
        DeleteMethod deleteMethod = new DeleteMethod(mServiceBaseURI + "/" + locationId);
        try {
            int status = mHttpClient.executeMethod(deleteMethod);
            result.setStatus(status);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        }
        whatAmIDoing(result, deleteMethod);
        return result;
    }

    public SlimResult<Location> fetchLocationById(int locationId) {
        SlimResult result = new SlimResult(null);
        GetMethod getMethod = new GetMethod(mServiceBaseURI + "/" + locationId);
        getMethod.setRequestHeader("Accept", mMediaType.getValue());
        try {
            int status = mHttpClient.executeMethod(getMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Location location = (Location) unmarshall(getMethod.getResponseBodyAsString(), Location.class);
                result.setResultContent(location);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "fetch location", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
        whatAmIDoing(result, getMethod);
        return result;
    }

    public SlimResult<Location> fetchLocationLatLong(long lattitude, long longitude) {
        SlimResult result = new SlimResult(null);
        GetMethod getMethod = new GetMethod(mServiceBaseURI);
        NameValuePair pairLatt = new NameValuePair("lattitude", String.valueOf(lattitude));
        NameValuePair pairLong = new NameValuePair("longitude", String.valueOf(longitude));
        getMethod.setQueryString(new NameValuePair[]{pairLatt, pairLong});
        getMethod.setRequestHeader("Accept", mMediaType.getValue());
        try {
            int status = mHttpClient.executeMethod(getMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Location location = (Location) unmarshall(getMethod.getResponseBodyAsString(), Location.class);
                result.setResultContent(location);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "fetch location by long/lat", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
        whatAmIDoing(result, getMethod);
        return result;
    }
}
