package com.example.memo;

/**
 * @author Created by wulei
 * @date 2020/12/10, 010
 * @description
 */
public class MemoBean {
    private int id;
    private String content;
    private int category;
    private String imgs;
    private long time;

    public MemoBean(int id, String content, int category, String imgs, long time) {
        this.id = id;
        this.content = content;
        this.category = category;
        this.imgs = imgs;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
