package com.route.compiler.builders;

import com.route.compiler.configuration.Configuration;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

import static javax.lang.model.element.Modifier.PUBLIC;

public class GroupBuilder extends RouteBuilder {
    public GroupBuilder(Messager messager, Elements elements) {
        super(messager, elements);
    }

    public TypeSpec.Builder build() {
        return TypeSpec.classBuilder(Configuration.GROUP_CLASS)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(createParameter(), "groups", PUBLIC).build())
                .addMethod(initMethod().build())
                .addMethod(getMethod().build());
    }

    public MethodSpec.Builder initMethod() {
        // Build method : ArrayList init().
        return MethodSpec.methodBuilder(Configuration.METHOD_INIT)
                .addModifiers(PUBLIC)
                .addStatement("if (groups == null) { groups = new ArrayList(); }");
    }

    public MethodSpec.Builder getMethod() {
        // Build method : ArrayList get().
        return MethodSpec.methodBuilder(Configuration.METHOD_GET)
                .addModifiers(PUBLIC)
                .addStatement("return groups")
                .returns(ArrayList.class);
    }

    public MethodSpec.Builder loadMethod() {
        // Build method : void load().
        return MethodSpec.methodBuilder(Configuration.METHOD_LOAD)
                .addModifiers(PUBLIC)
                .addStatement("init()");
    }

    public void addStatement(MethodSpec.Builder builder, String group) {
        builder.addStatement("groups.add($S)", group);
    }

    private ParameterizedTypeName createParameter() {
        return ParameterizedTypeName.get(
                ClassName.get(ArrayList.class),
                ClassName.get(String.class)
        );
    }
}
