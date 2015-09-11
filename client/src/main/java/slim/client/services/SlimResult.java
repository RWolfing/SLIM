/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client.services;

/**
 *
 * @author Robert
 * @param <T>
 */
public class SlimResult<T> {
    
    private final T mResultContent;
    private final int mResultStatus;
    
    public SlimResult(int status, T content){
        mResultStatus = status;
        mResultContent = content;
    }

    public T getmResultContent() {
        return mResultContent;
    }

    public int getmResultStatus() {
        return mResultStatus;
    }
}
