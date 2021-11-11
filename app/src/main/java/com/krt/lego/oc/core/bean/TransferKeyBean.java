package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * @author hyj
 * @time 2020/9/25 10:56
 * @class describe
 */
public class TransferKeyBean {

    /**
     *         {
     *           "key": "data%krt_0%krt_region",
     *           "transferType": [
     *             "1",
     *             "2"
     *           ],
     *           "variableName": "phoneNumber",
     *           "storageName": "phoneNumber"
     *         }
     */

    private String key;
    private List<String> transferType;
    private String variableName;
    private String storageName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getTransferType() {
        return transferType;
    }

    public void setTransferType(List<String> transferType) {
        this.transferType = transferType;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }
}
