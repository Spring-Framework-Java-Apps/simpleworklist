package org.woehlke.simpleworklist.model;

import java.io.Serializable;

/**
 * Created by tw on 13.03.16.
 */
public class UserSessionBean implements Serializable {

    private long areaId;

    public UserSessionBean(){
        areaId=0;
    }

    public UserSessionBean(long areaId){
        this.areaId=areaId;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSessionBean that = (UserSessionBean) o;

        return areaId == that.areaId;
    }

    @Override
    public int hashCode() {
        return (int) (areaId ^ (areaId >>> 32));
    }

    @Override
    public String toString() {
        return "UserSessionBean{" +
                "areaId=" + areaId +
                '}';
    }
}
