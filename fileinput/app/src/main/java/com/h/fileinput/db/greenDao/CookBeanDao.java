package com.h.fileinput.db.greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.h.fileinput.db.greenBean.CookBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COOK_BEAN".
*/
public class CookBeanDao extends AbstractDao<CookBean, Long> {

    public static final String TABLENAME = "COOK_BEAN";

    /**
     * Properties of entity CookBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property ImgPath = new Property(3, String.class, "imgPath", false, "IMG_PATH");
        public final static Property CreateUser = new Property(4, String.class, "createUser", false, "CREATE_USER");
        public final static Property UserUrl = new Property(5, String.class, "userUrl", false, "USER_URL");
        public final static Property CateName = new Property(6, String.class, "cateName", false, "CATE_NAME");
    };


    public CookBeanDao(DaoConfig config) {
        super(config);
    }
    
    public CookBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COOK_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"URL\" TEXT," + // 2: url
                "\"IMG_PATH\" TEXT," + // 3: imgPath
                "\"CREATE_USER\" TEXT," + // 4: createUser
                "\"USER_URL\" TEXT," + // 5: userUrl
                "\"CATE_NAME\" TEXT);"); // 6: cateName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COOK_BEAN\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CookBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
 
        String imgPath = entity.getImgPath();
        if (imgPath != null) {
            stmt.bindString(4, imgPath);
        }
 
        String createUser = entity.getCreateUser();
        if (createUser != null) {
            stmt.bindString(5, createUser);
        }
 
        String userUrl = entity.getUserUrl();
        if (userUrl != null) {
            stmt.bindString(6, userUrl);
        }
 
        String cateName = entity.getCateName();
        if (cateName != null) {
            stmt.bindString(7, cateName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CookBean readEntity(Cursor cursor, int offset) {
        CookBean entity = new CookBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // url
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // imgPath
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // createUser
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // userUrl
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // cateName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CookBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImgPath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCreateUser(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCateName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CookBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CookBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
