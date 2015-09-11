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
import org.apache.commons.httpclient.HttpClient;
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
    
    protected Object unmarshall(String content, Class clazz) throws JAXBException{
        return unmarshallFromXML(new StringReader(content), JAXBContext.newInstance(clazz));
    }
    
    private Object unmarshallFromXML(Reader in, JAXBContext jaxbc) throws JAXBException{
        Unmarshaller unmarshaller = jaxbc.createUnmarshaller();
        return unmarshaller.unmarshal(in);
    }
}
