package com.A10.instablind.Model;

import java.util.Date;

public class Comment {
    private String commentId;
    private String content;
    private String publisher;
    private Date publishDate; //TODO look up correct class when needed

    public Comment() {
    }

    public Comment(String commentId, String content, String publisher, Date publishDate) {
        this.commentId = commentId;
        this.content = content;
        this.publisher = publisher;
        this.publishDate = publishDate;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
