package slim.client.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.eclipse.persistence.jaxb.MarshallerProperties;

/**
 * Base class for every service that communicates with the webservice
 *
 * @author Robert Wolfinger
 */
public class SlimService {

    public enum MediaType {

        JSON("application/json"), XML("application/xml");

        private final String mType;

        private MediaType(String type) {
            mType = type;
        }

        public String getValue() {
            return mType;
        }
    }

    protected static final String ERROR_IO = "IO Exception while fetching: ";
    protected static final String ERROR_JAXB = "Could not unmarshall: ";

    protected final String mServiceBaseURI;
    protected final HttpClient mHttpClient;
    protected MediaType mMediaType = MediaType.XML;
    private boolean mLoggingEnabled = false;

    /**
     * Default constructor Creates the service from the given url, with the
     * given media type
     *
     * @param serviceUrl base url of the service
     * @param type media type
     */
    public SlimService(String serviceUrl, MediaType type) {
        mServiceBaseURI = serviceUrl;
        mHttpClient = new HttpClient();
        mMediaType = type;
    }

    public void setMediaType(MediaType type) {
        mMediaType = type;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        this.mLoggingEnabled = loggingEnabled;
    }

    /**
     * Unmarshalls the given content to an object of the given clazz
     *
     * @param content content as a string
     * @param clazz content class
     * @return the unmarshalled object
     * @throws JAXBException
     */
    protected Object unmarshall(String content, Class clazz) throws JAXBException {
        //Switch the unmarshaller depending on the content type
        switch (mMediaType) {
            case XML:
                return unmarshallFromXML(new StringReader(content), JAXBContext.newInstance(clazz));
            case JSON:
                return unmarshallFromJson(new StringReader(content), JAXBContext.newInstance(clazz), clazz);
            default:
                throw new UnsupportedOperationException("Unmarshalling for the given MediaType is not supported!");
        }
    }

    /**
     * Unmarshalls an object from xml
     *
     * @param in in reader
     * @param jaxbc jaxbc context
     * @return the unmarshalled object
     * @throws JAXBException
     */
    private Object unmarshallFromXML(Reader in, JAXBContext jaxbc) throws JAXBException {
        Unmarshaller unmarshaller = jaxbc.createUnmarshaller();
        return unmarshaller.unmarshal(in);
    }

    /**
     * Unmarshalls the object from json
     *
     * @param in in reader
     * @param jaxbc jaxbc context
     * @param clazz clazz of the content object
     * @return the unmarshalled object
     * @throws JAXBException
     */
    private Object unmarshallFromJson(Reader in, JAXBContext jaxbc, Class clazz) throws JAXBException {
        Unmarshaller unmarshaller = jaxbc.createUnmarshaller();
        unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
        return unmarshaller.unmarshal(new StreamSource(in), clazz).getValue();
    }

    /**
     * Helper method to log the executed service method, with url, response
     * etc...
     *
     * @param result the result of the executed service
     * @param httpMethod the used httpmethod
     */
    protected void whatAmIDoing(SlimResult result, HttpMethodBase httpMethod) {
        if (mLoggingEnabled) {
            if (httpMethod == null) {
                System.out.println("I am doing nothing....");
                return;
            }

            System.out.println("----------- SERVICE CALL -----------");
            System.out.println("Executed " + httpMethod.getPath());
            System.out.println("QueryParams " + httpMethod.getQueryString());
            System.out.println("Type " + httpMethod.getName());
            System.out.println("Mediatype: " + mMediaType);
            if (result != null) {
                System.out.println("Response: " + result.getResultStatus());
                System.out.println("Result: " + result.getResultContent());
            } else {
                System.out.println("The result was null....");
            }
            if (result.getResultStatus() == 200 || result.getResultStatus() == 201) {
                try {
                    System.out.println("Response Raw: " + httpMethod.getResponseBodyAsString());
                } catch (IOException ex) {
                    System.out.println("Response Raw: " + "IO Exception could not retrieve...");
                }
            }
            System.out.println("--------- SERVICE CALL END ---------");
        }
    }
}
