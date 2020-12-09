package com.example.memo;

/**
 * @date 12/9/20
 * @description
 */
class MemoTypeBean {
    private int id;
    private String name;
    private boolean showDel;

    public MemoTypeBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowDel() {
        return showDel;
    }

    public void setShowDel(boolean showDel) {
        this.showDel = showDel;
    }
}
