package es.upm.miw.functionaltests;

import java.util.Arrays;
import java.util.List;

public enum Scope {
    ADMIN, MANAGER, OPERATOR, CUSTOMER, URL_TOKEN, ANONYMOUS, AUTHENTICATED;

    public static final String PREFIX = "SCOPE_";
    public static final String ROLE_PREFIX = "ROLE_";

    public static List<String> allValues() {
        return Arrays.stream(Scope.values())
                .map(Scope::value)
                .toList();
    }

    public static Scope of(String withPrefix) {
        return Scope.valueOf(withPrefix
                .replace(PREFIX, "")
                .replace(ROLE_PREFIX, "")
                .toUpperCase());
    }

    public String scopeValue() {
        return PREFIX + this.value();
    }

    public String value() {
        return this.name().toLowerCase();
    }

}
