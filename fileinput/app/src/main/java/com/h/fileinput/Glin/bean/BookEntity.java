package com.h.fileinput.Glin.bean;

import java.util.List;

/**
 * Created by wangchm on 2016/9/13 0013.
 * 图书返回数据
 */
public class BookEntity {
    private int count;
    private int start;
    private int total;
    private List<Book> books;

    public BookEntity(int count, int start, int total, List<Book> books) {
        this.setCount(count);
        this.setStart(start);
        this.setTotal(total);
        this.setBooks(books);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
