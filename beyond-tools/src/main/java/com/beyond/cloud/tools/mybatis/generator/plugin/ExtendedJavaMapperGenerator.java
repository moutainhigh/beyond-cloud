package com.beyond.cloud.tools.mybatis.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

/**
 * @author Raven
 */
public class ExtendedJavaMapperGenerator extends JavaMapperGenerator {

    public ExtendedJavaMapperGenerator(final String project) {
        super(project);
    }

    public ExtendedJavaMapperGenerator(final String project, final boolean requiresMatchedXMLGenerator) {
        super(project, requiresMatchedXMLGenerator);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        List<CompilationUnit> units = super.getCompilationUnits();

        units.stream()
            .filter(x -> x instanceof Interface)
            .map(x -> (Interface) x)
            .filter(x -> x.getType().equals(type))
            .findFirst()
            .ifPresent(face -> {
                this.addUpdateVersionedMethod(face);
                this.addUpdateVersionedSelectiveMethod(face);
            });

        return units;
    }

    protected void addUpdateVersionedMethod(final Interface face) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateVersionedMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, face);
        }
    }

    protected void addUpdateVersionedSelectiveMethod(final Interface face) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateVersionedSelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, face);
        }
    }

}
