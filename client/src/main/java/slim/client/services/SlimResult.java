package slim.client.services;

/**
 * Container class to wrap a response from the webservice.
 * 
 * @author Robert Wolfinger
 * @param <T> content class
 */
public class SlimResult<T> {
    
    private T mResultContent;
    private int mResultStatus;

    public SlimResult(int status, T content) {
        mResultStatus = status;
        mResultContent = content;
    }
    
    public SlimResult(T content){
        
    }

    public void setResultContent(T resultContent) {
        mResultContent = resultContent;
    }

    public void setStatus(int status) {
        mResultStatus = status;
    }

    public T getResultContent() {
        return mResultContent;
    }

    public int getResultStatus() {
        return mResultStatus;
    }
}
