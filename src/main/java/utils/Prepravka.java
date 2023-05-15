package utils;

import tfidf.CrawledPage;

import java.util.ArrayList;

public class Prepravka {
    int key;
    String val;
    CrawledPage el;

    public Prepravka(int key, String val, CrawledPage el) {
        this.key = key;
        this.val = val;
        this.el = el;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public CrawledPage getEl() {
        return el;
    }

    public void setEl(CrawledPage el) {
        this.el = el;
    }

    @Override
    public String toString() {
        return getEl().getTitle();
    }
}
