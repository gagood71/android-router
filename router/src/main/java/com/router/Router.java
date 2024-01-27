package com.router;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Router {
    protected static ArrayList<String> GROUPS;

    protected static Map<String, Class<? extends AppCompatActivity>> ROUTES;

    protected ArrayList<Class<?>> ROUTE_GROUP_CLASS_LIST;

    protected ArrayList<Object> ROUTE_GROUP_CLASS_OBJECT_LIST;

    public Router(ArrayList<String> groups) {
        ROUTE_GROUP_CLASS_LIST = new ArrayList<>();
        ROUTE_GROUP_CLASS_OBJECT_LIST = new ArrayList<>();
        GROUPS = groups;
        ROUTES = new HashMap<>();

        init();
    }

    protected void init() {
        initGroups();
        initRouteGroups();
        initRoutes();
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    protected void initGroups() {
        try {
            for (String group : GROUPS) {
                Class<?> $class = Class.forName(Configuration.parseGroupPackage(group));
                Object object = $class.newInstance();

                $class.getMethod(Configuration.METHOD_LOAD).invoke(object);

                if (GROUPS == null || GROUPS.isEmpty()) {
                    GROUPS.addAll((ArrayList<String>) $class.getMethod(Configuration.METHOD_GET).invoke(object));
                }
            }

            Log.e(getClass().getName(), GROUPS.toString());
        } catch (ClassNotFoundException
                 | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    protected void initRoutes() {
        if (ROUTE_GROUP_CLASS_LIST != null) {
            for (Class<?> $class : ROUTE_GROUP_CLASS_LIST) {
                try {
                    Object object = $class.newInstance();

                    $class.getMethod(Configuration.METHOD_LOAD).invoke(object);
                    ROUTES.putAll((Map<? extends String, ? extends Class<? extends AppCompatActivity>>) $class.getMethod(Configuration.METHOD_GET).invoke(object));

                    Log.e(getClass().getName(), ROUTES.toString());
                } catch (IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void initRouteGroups() {
        try {
            for (String group : GROUPS) {
                Class<?> $class = Class.forName(Configuration.parseRouteGroupPackage(group));

                ROUTE_GROUP_CLASS_LIST.add($class);
                ROUTE_GROUP_CLASS_OBJECT_LIST.add($class.newInstance());
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();

            Log.e(getClass().getName(), e.getMessage());
        }
    }
}
