package com.oup.eac.common.utils.activationcode;

import java.util.Set;

public interface ActivationCodeGenerator {
    Set<String> createActivationCodes(final String prefix, final int numTokens);
    String createActivationCode(final String prefix);
}
