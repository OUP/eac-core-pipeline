package com.oup.eac.dto.licence;

import java.util.Locale;

public interface LicenceDescriptionGeneratorSource {

    public LicenceDescriptionGenerator getLicenceDescriptionGenerator(Locale locale, String timeZone, boolean generateHtml);
    
}
