package com.hss.xservices.models;

import java.util.List;

public class ServicesCategory {

    private Integer svcId;
    private Integer svcCatg;
    private Integer svcType;
    private String svcTitle;
    private String svcDescription;
    private String hourRate;
    private String operCommission;
    private Integer minSku;
    private String svcSlaHrs;
    private String svcSla;
    private Integer vehicleCategory;
    private Integer status;
    private String editOn;
    private List<Photo> photos = null;
//    private Category category;

    public Integer getSvcId() {
        return svcId;
    }

    public void setSvcId(Integer svcId) {
        this.svcId = svcId;
    }

    public Integer getSvcCatg() {
        return svcCatg;
    }

    public void setSvcCatg(Integer svcCatg) {
        this.svcCatg = svcCatg;
    }

    public Integer getSvcType() {
        return svcType;
    }

    public void setSvcType(Integer svcType) {
        this.svcType = svcType;
    }

    public String getSvcTitle() {
        return svcTitle;
    }

    public void setSvcTitle(String svcTitle) {
        this.svcTitle = svcTitle;
    }

    public String getSvcDescription() {
        return svcDescription;
    }

    public void setSvcDescription(String svcDescription) {
        this.svcDescription = svcDescription;
    }

    public String getHourRate() {
        return hourRate;
    }

    public void setHourRate(String hourRate) {
        this.hourRate = hourRate;
    }

    public String getOperCommission() {
        return operCommission;
    }

    public void setOperCommission(String operCommission) {
        this.operCommission = operCommission;
    }

    public Integer getMinSku() {
        return minSku;
    }

    public void setMinSku(Integer minSku) {
        this.minSku = minSku;
    }

    public String getSvcSlaHrs() {
        return svcSlaHrs;
    }

    public void setSvcSlaHrs(String svcSlaHrs) {
        this.svcSlaHrs = svcSlaHrs;
    }

    public String getSvcSla() {
        return svcSla;
    }

    public void setSvcSla(String svcSla) {
        this.svcSla = svcSla;
    }

    public Integer getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(Integer vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEditOn() {
        return editOn;
    }

    public void setEditOn(String editOn) {
        this.editOn = editOn;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

//    public Category getCategory() {
//        return category;
//    }
//
//    public void setCategory(Category category) {
//        this.category = category;
//    }
}
