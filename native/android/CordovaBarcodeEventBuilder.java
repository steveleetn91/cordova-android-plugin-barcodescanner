package org.apache.cordova;

import org.json.JSONObject;

public class CordovaBarcodeEventBuilder {
    private String eventName;
    private String jsonData;

    public CordovaBarcodeEventBuilder(String eventName) {
        this.eventName = eventName;
    }

    public CordovaBarcodeEventBuilder withData(String data) {
        this.jsonData = data;
        return this;
    }

    public CordovaBarcodeEventBuilder withData(JSONObject jsonObj) {
        if (jsonObj == null) {
            return withData("");
        }
        return withData(jsonObj.toString());
    }

    public String build() {
        StringBuilder js = new StringBuilder();
        js.append("javascript:cordova.fireDocumentEvent('");
        js.append(eventName);
        js.append("'");
        if (jsonData != null && !"".equals(jsonData)) {
            js.append(",");
            js.append(jsonData);
        }
        js.append(");");
        return js.toString();
    }
}