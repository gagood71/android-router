package com.route.compiler.processors;

import com.route.annotation.Route;
import com.route.compiler.builders.GroupBuilder;
import com.route.compiler.builders.RouteGroupBuilder;
import com.route.compiler.configuration.Configuration;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public abstract class Processor extends AbstractProcessor {
    protected GroupBuilder groupBuilder;
    protected RouteGroupBuilder routeGroupBuilder;
    protected TypeSpec.Builder classBuilder;
    protected MethodSpec.Builder methodBuilder;
    protected Elements elements;
    protected Filer filer;
    protected Types types;
    protected Messager messager;

    public Processor() {
        super();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elements = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        messager = processingEnv.getMessager();

        initBuilder();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> bindRouteElements = roundEnvironment.getElementsAnnotatedWith(Route.class);

        messager.printMessage(Diagnostic.Kind.NOTE, "Elements is " + bindRouteElements.toString());

        return false;
    }

    protected void initBuilder() {
        groupBuilder = new GroupBuilder(messager, elements);
        routeGroupBuilder = new RouteGroupBuilder(messager, elements);
        classBuilder = routeGroupBuilder.build();
        methodBuilder = routeGroupBuilder.loadMethod();
    }

    protected void processElement(Element element) {
        messager.printMessage(Diagnostic.Kind.NOTE, "Element is " + element);
        messager.printMessage(Diagnostic.Kind.NOTE, "Element's asType is " + element.asType());
        messager.printMessage(Diagnostic.Kind.NOTE, "Element's simple name is " + element.getSimpleName().toString());
    }

    /**
     * JavaFile.builder(XXX, XXX.build()).build().writeTo(XXX), write file.
     */
    protected void processJavaFileBuilder(String packageName, TypeSpec.Builder builder) {
        try {
            JavaFile.builder(packageName, builder.build())
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    protected String getPackageName() {
        return Configuration.COMPILER_PACKAGE;
    }

    protected String getPackageName(String group) {
        return getPackageName() + "." + group;
    }
}
