package com.route.compiler.builders;

import com.route.annotation.Route;
import com.route.compiler.configuration.Configuration;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static javax.lang.model.element.Modifier.PUBLIC;

public class RouteGroupBuilder extends RouteBuilder {
    public RouteGroupBuilder(Messager messager, Elements elements) {
        super(messager, elements);
    }

    public TypeSpec.Builder build() {
        // TypeSpec.classBuilder(...)，Enter class name and create class builder.
        // FieldSpec.builder(createRouteGroup(), "routes", PUBLIC).build() => Map<String, Class<? extends AppCompatActivity>>
        return TypeSpec.classBuilder(Configuration.ROUTE_GROUP_CLASS)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(createParameter(), "routes", PUBLIC).build())
                .addMethod(initMethod().build())
                .addMethod(getMethod().build());
    }

    public MethodSpec.Builder initMethod() {
        // Build method : Map init().
        return MethodSpec.methodBuilder(Configuration.METHOD_INIT)
                .addModifiers(PUBLIC)
                .addStatement("if (routes == null) { routes = new HashMap(); }")
                .addStatement("return new HashMap()")
                .returns(HashMap.class);
    }

    public MethodSpec.Builder getMethod() {
        // Build method : Map get().
        return MethodSpec.methodBuilder(Configuration.METHOD_GET)
                .addModifiers(PUBLIC)
                .addStatement("return routes")
                .returns(Map.class);
    }

    public MethodSpec.Builder loadMethod() {
        // Build method : void load().
        return MethodSpec.methodBuilder(Configuration.METHOD_LOAD)
                .addModifiers(PUBLIC)
                .addStatement("init()");
    }

    public MethodSpec.Builder addStatement(MethodSpec.Builder builder, Route route) {
        builder.addStatement("routes.put($S, $T.class)", route.path(), ClassName.get(route.namespace(), route.activity()));

        return builder;
    }

    private ParameterizedTypeName createParameter() {
        // Build input type，format as : Map<String, Class<? extends AppCompatActivity>>.
        TypeElement activity = elementsUtils.getTypeElement(Configuration.APP_COMPAT_ACTIVITY);

        return ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(activity))
                )
        );
    }
}
