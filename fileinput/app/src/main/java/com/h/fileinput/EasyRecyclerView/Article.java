package com.h.fileinput.EasyRecyclerView;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class Article {
    private String url;
    private String title;
    private String content;

    public Article(String url,String title,String content) {
        this.setUrl(url);
        this.setTitle(title);
        this.setContent(content);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
