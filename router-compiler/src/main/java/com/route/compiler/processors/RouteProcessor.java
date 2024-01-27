package com.route.compiler.processors;

import com.route.annotation.Route;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public abstract class RouteProcessor extends Processor {
    protected ArrayList<String> groups;

    protected Map<String, TypeSpec.Builder> classBuilders;

    protected Map<String, MethodSpec.Builder> methodBuilders;

    public RouteProcessor() {
        super();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        groups = new ArrayList<>();
        classBuilders = new HashMap<>();
        methodBuilders = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        super.process(set, roundEnvironment);

        if (set.isEmpty()) {
            return false;
        }

        Set<? extends Element> bindRouteElements = roundEnvironment.getElementsAnnotatedWith(Route.class);

        for (Element element : bindRouteElements) {
            processGroup(element);
            processElement(element);
        }

        processGroupBuilder();
        processElementBuilders();

        return true;
    }

    protected void processGroup(Element element) {
        Route route = element.getAnnotation(Route.class);

        if (!groups.contains(route.group())) {
            groups.add(route.group());
            classBuilders.put(route.group(), routeGroupBuilder.build());
            methodBuilders.put(route.group(), routeGroupBuilder.loadMethod());
        }
    }

    protected void processElement(Element element) {
        Route route = element.getAnnotation(Route.class);

        MethodSpec.Builder builder = methodBuilders.get(route.group());

        methodBuilders.put(route.group(), routeGroupBuilder.addStatement(builder, route));
    }

    protected void processElementBuilders() {
        messager.printMessage(Diagnostic.Kind.NOTE, "Groups: " + groups.toString());
        messager.printMessage(Diagnostic.Kind.NOTE, "Class builders: " + classBuilders.toString());
        messager.printMessage(Diagnostic.Kind.NOTE, "Method builders: " + methodBuilders.toString());

        for (String group : groups) {
            TypeSpec.Builder classBuilder = classBuilders.get(group);
            MethodSpec.Builder methodBuilder = methodBuilders.get(group);

            classBuilder.addMethod(methodBuilder.build());

            processJavaFileBuilder(getPackageName(group), classBuilder);
        }
    }

    protected void processGroupBuilder() {
        TypeSpec.Builder classBuilder = groupBuilder.build();
        MethodSpec.Builder methodBuilder = groupBuilder.loadMethod();

        for (String group : groups) {
            groupBuilder.addStatement(methodBuilder, group);

            classBuilder.addMethod(methodBuilder.build());

            processJavaFileBuilder(getPackageName(group), classBuilder);
        }
    }
}
