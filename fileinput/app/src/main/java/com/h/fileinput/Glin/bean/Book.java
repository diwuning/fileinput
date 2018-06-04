package com.h.fileinput.Glin.bean;

import java.util.List;

/**
 * Created by wangchm on 2016/9/13 0013.
 * 图书详细
 */
public class Book {
    private RatingMusic rating;
    private String subtitle;
    private String[] author;
    private String pubdate;
    private List<TagMusic> tags;
    private String origin_title;
    private String image;
    private String binding;
    private String[] translator;
    private String catalog;
    private String pages;
    private AvatarsBody images;
    private String alt;
    private String id;
    private String publisher;
    private String isbn10;
    private String isbn13;
    private String title;
    private String url;
    private String alt_title;
    private String author_intro;
    private String price;

    public Book(){}
    public Book(RatingMusic rating, String subtitle, String[] author, String pubdate, List<TagMusic> tags, String origin_title,
                String image, String binding, String[] translator, String catalog, String pages, AvatarsBody images, String alt,
                String id, String publisher, String isbn10, String isbn13, String title, String url, String alt_title,
                String author_intro, String price) {
        this.setRating(rating);
        this.setSubtitle(subtitle);
        this.setAuthor(author);
        this.setPubdate(pubdate);
        this.setTags(tags);
        this.setOrigin_title(origin_title);
        this.setImage(image);
        this.setBinding(binding);
        this.setTranslator(translator);
        this.setCatalog(catalog);
        this.setPages(pages);
        this.setImages(images);
        this.setAlt(alt);
        this.setId(id);
        this.setPublisher(publisher);
        this.setIsbn10(isbn10);
        this.setIsbn13(isbn13);
        this.setTitle(title);
        this.setUrl(url);
        this.setAlt_title(alt_title);
        this.setAuthor_intro(author_intro);
        this.setPrice(price);
    }

    public RatingMusic getRating() {
        return rating;
    }

    public void setRating(RatingMusic rating) {
        this.rating = rating;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public List<TagMusic> getTags() {
        return tags;
    }

    public void setTags(List<TagMusic> tags) {
        this.tags = tags;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String[] getTranslator() {
        return translator;
    }

    public void setTranslator(String[] translator) {
        this.translator = translator;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public AvatarsBody getImages() {
        return images;
    }

    public void setImages(AvatarsBody images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt_title() {
        return alt_title;
    }

    public void setAlt_title(String alt_title) {
        this.alt_title = alt_title;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
