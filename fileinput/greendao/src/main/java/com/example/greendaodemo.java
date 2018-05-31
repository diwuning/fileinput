package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class greendaodemo {
    public static void main(String[] args) throws Exception{
        //bean生成目录
        Schema schema = new Schema(1,"com.h.fileinput.db.greenBean");
        // master、session、dao生成目录
        schema.setDefaultJavaPackageTest("com.h.fileinput.db.greenDao");
        schema.setDefaultJavaPackageDao("com.h.fileinput.db.greenDao");
        schema.enableKeepSectionsByDefault();

        addCook(schema);

        new DaoGenerator().generateAll(schema,"app/src/main/java");
    }

    private static void addCook(Schema schema){
        Entity bean = schema.addEntity("CookBean");
        bean.addIdProperty().primaryKey().autoincrement();
        bean.addStringProperty("title");
        bean.addStringProperty("url");
        bean.addStringProperty("imgPath");
        bean.addStringProperty("createUser");
        bean.addStringProperty("userUrl");
        bean.addStringProperty("cateName");

    }
}
