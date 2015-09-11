/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client.services;

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
 *
 * @author Robert
 */
public class SlimService {

    protected static final String ERROR_IO = "IO Exception while fetching: ";
    protected static final String ERROR_JAXB = "Could not unmarshall: ";

    protected final String mServiceBaseURI;
    protected final HttpClient mHttpClient;
    protected MediaType mMediaType = MediaType.XML;

    public SlimService(String serviceUrl, MediaType type) {
        mServiceBaseURI = serviceUrl;
        mHttpClient = new HttpClient();
        mMediaType = type;
    }

    public void setmMediaType(MediaType type) {
        mMediaType = type;
    }

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

    protected Object unmarshall(String content, Class clazz) throws JAXBException {
        switch(mMediaType){
            case XML:
                return unmarshallFromXML(new StringReader(content), JAXBContext.newInstance(clazz));
            case JSON:
                return unmarshallFromJson(new StringReader(content), JAXBContext.newInstance(clazz), clazz);
            default:
                throw new UnsupportedOperationException("Unmarshalling for the given MediaType is not supported!");
        }
    }

    private Object unmarshallFromXML(Reader in, JAXBContext jaxbc) throws JAXBException {
        Unmarshaller unmarshaller = jaxbc.createUnmarshaller();
        return unmarshaller.unmarshal(in);
    }
    
    private Object unmarshallFromJson(Reader in, JAXBContext jaxbc, Class clazz) throws JAXBException{
        Unmarshaller unmarshaller = jaxbc.createUnmarshaller();
        unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
        return unmarshaller.unmarshal(new StreamSource(in), clazz).getValue();
    }

    protected void whatAmIDoing(SlimResult result, HttpMethodBase httpMethod) {
        if (httpMethod == null) {
            System.out.println("I am doing nothing....");
            return;
        }

        System.out.println();
        System.out.println("----------- SERVICE CALL -----------");
        System.out.println("Executed " + httpMethod.getPath());
        System.out.println("Type " + httpMethod.getName());
        System.out.println("Mediatype: " + mMediaType);
        if (result != null) {
            System.out.println("Response: " + result.getmResultStatus());
            System.out.println("Result: " + result.getmResultContent());
        } else {
            System.out.println("The result was null....");
        }
        System.out.println("--------- SERVICE CALL END ---------");
    }
}
