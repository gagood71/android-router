package com.route.compiler.builders;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public class RouteBuilder {
    protected Messager messager;
    protected Elements elementsUtils;

    public RouteBuilder(Messager message, Elements elements) {
        messager = message;
        elementsUtils = elements;
    }

    protected void write(String packageName, TypeSpec.Builder builder, Filer filer) {
        try {
            JavaFile.builder(packageName, builder.build())
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }
}
