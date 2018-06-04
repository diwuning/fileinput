package com.h.fileinput.Glin;

import com.h.fileinput.Glin.bean.Book;
import com.h.fileinput.Glin.bean.BookEntity;

import org.loader.glin.annotation.Arg;
import org.loader.glin.annotation.GET;
import org.loader.glin.annotation.POST;
import org.loader.glin.annotation.ShouldCache;
import org.loader.glin.call.Call;

import java.util.List;

/**
 * Created by pc on 2018/6/4.
 */

public interface UserApi {
    //@POST("")
    // use @ShouldCache, Glin will cache the lastest response
    @ShouldCache
    @GET("book/search")
    Call<List<Book>> list(@Arg("tag") String tag);

    @GET("book/search")
    Call<BookEntity> one(@Arg("tag") String tag);
}
