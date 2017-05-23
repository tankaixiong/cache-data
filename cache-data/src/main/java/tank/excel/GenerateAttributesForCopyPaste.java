/*
 * Copyright 2002-2016 XianYu Game Co. Ltd, The Inuyasha Project
 */

package tank.excel;

import org.apache.commons.io.FileUtils;
import tank.excel.entity.ExcelEntityDemo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by gukaitong(29283212@qq.com) on 2015/12/1.
 *
 * @author gukaitong
 * @since 1.0
 */
public class GenerateAttributesForCopyPaste {

    public static void main(String[] args) throws IOException {

        writeJavaCode(ExcelEntityDemo.class);
    }

    private static void writeJavaCode(Class clazz) throws IOException {


        String path = GenerateAttributesForCopyPaste.class.getResource("/").getPath();
        File file = new File(path);

        String pack = clazz.getPackage().getName().replaceAll("\\.", "\\/");

        path = file.getParentFile().getParentFile().getParentFile().getAbsolutePath() + "/src/main/java/" + pack;
        path = path + "/" + clazz.getSimpleName() + "_.java";
        System.out.println(path);

        // System.out.println(AttributeSourceGenerator.generateAttributesForPastingIntoTargetClass(TestTemplate.class));
        //String code = AttributeSourceGenerator.generateAttributesForPastingIntoTargetClass(clazz);
        String code = TemplateAttributeSourceGenerator.generateAttributesForPastingIntoTargetClass(clazz);


        String tmplPath = GenerateAttributesForCopyPaste.class.getResource("/template/").getPath();

        String temp = FileUtils.readFileToString(new File(tmplPath + "template.txt"), Charset.forName("utf-8"));


        temp = temp.replaceAll("#<clazzName>", clazz.getSimpleName()).replaceAll("#<code>", code).replaceAll("#<package>",clazz.getPackage().getName());


        FileUtils.writeStringToFile(new File(path), temp, Charset.forName("utf-8"));

        System.out.println("生成完成");
    }
}