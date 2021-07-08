package com.oup.eac.dto;

/**
 * Used to transfer 'external customer id' information from the WebServicesApp to the service layer.
 * @author David Hay
 */
public class ExternalIdDto {

    private final String type;
    private final String id;
    private final String systemId;

    public ExternalIdDto(String systemId, String type, String id) {
        super();
        this.type = type;
        this.id = id;
        this.systemId = systemId;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getSystemId() {
        return systemId;
    }
    
    

}
