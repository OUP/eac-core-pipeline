package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetAllPlatformsResponse extends AbstractPlatformResponse implements
        Serializable {
	private List<OupPlatform> platforms = null;
	private static final long serialVersionUID = 12343L;

	public List<OupPlatform> getPlatforms() {
		if(platforms == null ) platforms = new ArrayList<OupPlatform>();
		return platforms;
	}

	public void setPlatforms(List<OupPlatform> platforms) {
		this.platforms = platforms;
	}
}
