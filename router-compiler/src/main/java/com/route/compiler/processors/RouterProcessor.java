package com.route.compiler.processors;

import com.google.auto.service.AutoService;
import com.route.compiler.configuration.Configuration;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@AutoService(Processor.class)
@SupportedAnnotationTypes({Configuration.ANNOTATION_ROUTE})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RouterProcessor extends RouteProcessor {
}