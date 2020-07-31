package com.beyond.cloud.tools.mybatis.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

/**
 * @author Raven
 */
public class ExtendedIntrospectedTable extends IntrospectedTableMyBatis3Impl {

    @Override
    protected void calculateXmlMapperGenerator(final AbstractJavaClientGenerator javaClientGenerator,
                                               final List<String> warnings,
                                               final ProgressCallback progressCallback) {
        xmlMapperGenerator = new ExtendedXmlMapperGenerator();
        this.initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

    @Override
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        return new ExtendedJavaMapperGenerator(this.getClientProject());
    }

}
