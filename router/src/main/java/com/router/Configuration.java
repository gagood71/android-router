package com.router;

public class Configuration {
    public static final String PACKAGE = "com.route.compiler";
    public static final String $_PACKAGE = "$package";

    public static final String GROUP_CLASS = ".$Group";

    public static final String ROUTE_GROUP_CLASS = ".$RouteGroup";

    public static final String METHOD_INIT = "init";
    public static final String METHOD_GET = "get";
    public static final String METHOD_LOAD = "load";

    public static String parseGroupPackage(String group) {
        return PACKAGE + "." + group + GROUP_CLASS;
    }

    public static String parseRouteGroupPackage(String group) {
        return PACKAGE + "." + group + ROUTE_GROUP_CLASS;
    }
}
