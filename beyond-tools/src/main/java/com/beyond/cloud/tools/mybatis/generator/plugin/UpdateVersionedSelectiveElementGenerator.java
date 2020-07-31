package com.beyond.cloud.tools.mybatis.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author Raven
 */
public class UpdateVersionedSelectiveElementGenerator extends AbstractXmlElementGenerator {

    private static final String METHOD_NAME = "updateVersionedSelective";
    private static final String VERSION_COLUMN_NAME = "row_version";
    private static final String VERSION_COLUMN_NAME2 = "rowversion";

    @Override
    public void addElements(final XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute("id", METHOD_NAME));

        String parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set");
        answer.addElement(dynamicElement);

        List<IntrospectedColumn> columns = ListUtilities.removeGeneratedAlwaysColumns(
            introspectedTable.getNonPrimaryKeyColumns());

        IntrospectedColumn versionColumn = null;

        for (IntrospectedColumn introspectedColumn : columns) {

            String loweredColumnName = introspectedColumn.getActualColumnName().toLowerCase();
            if (VERSION_COLUMN_NAME.equals(loweredColumnName) || VERSION_COLUMN_NAME2.equals(loweredColumnName)) {
                versionColumn = introspectedColumn;
                continue;
            }

            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            XmlElement isNotNullElement = new XmlElement("if");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append(',');

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        if (versionColumn != null) {
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(versionColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(versionColumn));
            sb.append(" + 1");
            dynamicElement.addElement(new TextElement(sb.toString()));
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
            .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if (versionColumn != null) {
            sb.setLength(0);
            sb.append(" and ");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(versionColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(versionColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if (context.getPlugins().sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

}
