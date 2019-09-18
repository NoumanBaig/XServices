package com.hss.xservices.models;

public class Photo {

    private Integer svcPhotoId;
    private String photoFileName;
    private String photoDispName;

    public Integer getSvcPhotoId() {
        return svcPhotoId;
    }

    public void setSvcPhotoId(Integer svcPhotoId) {
        this.svcPhotoId = svcPhotoId;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public String getPhotoDispName() {
        return photoDispName;
    }

    public void setPhotoDispName(String photoDispName) {
        this.photoDispName = photoDispName;
    }
}
