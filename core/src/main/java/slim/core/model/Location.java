/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.model;

/**
 *
 * @author Robert
 */
public class Location {
    private long mLattitude;
    private long mLongitude;
    
    public Location(){
        
    }
    
    public Location(long lattitude, long longitude){
        mLattitude = lattitude;
        mLongitude = longitude;
    }

    public long getmLattitude() {
        return mLattitude;
    }

    public void setmLattitude(long mLattitude) {
        this.mLattitude = mLattitude;
    }

    public long getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(long mLongitude) {
        this.mLongitude = mLongitude;
    }
}
