package com.beyond.cloud.tools.mybatis.generator.plugin;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

/**
 * @author Raven
 */
public class UpdateVersionedMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private static final String METHOD_NAME = "updateByPrimaryKeyVersioned";

    @Override
    public void addInterfaceElements(final Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        FullyQualifiedJavaType parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }

        importedTypes.add(parameterType);

        Method method = new Method(METHOD_NAME);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(METHOD_NAME);
        method.addParameter(new Parameter(parameterType, "record"));
        method.setAbstract(true);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        addMapperAnnotations(method);

        if (context.getPlugins()
            .clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    protected void addMapperAnnotations(final Method method) {
    }

    protected void addExtraImports(final Interface interfaze) {
    }

}
