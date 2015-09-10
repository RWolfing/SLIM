/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client;

import org.apache.commons.httpclient.HttpClient;

/**
 *
 * @author Robert
 */
public class SlimService {
    private static final String mServiceBaseURI = "http://localhost:8080";
    protected static final HttpClient client = new HttpClient();
}
