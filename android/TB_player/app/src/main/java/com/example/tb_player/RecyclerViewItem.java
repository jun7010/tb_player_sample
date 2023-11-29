package com.example.tb_player;

public class RecyclerViewItem {
    private String mImgName;
    private String mMainText;
    private String mSubText;
    private String mLinkUrl;

    public String getImgName() {
        return mImgName;
    }

    public void setImgName(String imgName) {
        this.mImgName = imgName;
    }

    public String getMainText() {
        return mMainText;
    }

    public void setMainText(String mainText) {
        this.mMainText = mainText;
    }

    public String getSubText() {
        return mSubText;
    }

    public void setSubText(String subText) {
        this.mSubText = subText;
    }

    public String getmLinkUrl() {
        return mLinkUrl;
    }

    public void setmLinkUrl(String mLinkUrl) {
        this.mLinkUrl = mLinkUrl;
    }
}