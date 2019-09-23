package com.hss.xservices.models;

import java.util.List;

public class Profile {
    private Integer userId;
    private String userCode;
    private Integer userType;
    private Integer userRole;
    private String mobile;
    private String email;
    private String password;
    private String title;
    private Object firstName;
    private Object middleName;
    private Object lastName;
    private Object nickName;
    private Integer gender;
    private Object dob;
    private Object photo;
    private String registerDate;
    private String updatedOn;
    private Integer status;
    private Object addBy;
    private String addOn;
    private Object editBy;
    private String editOn;
    private List<Addresses> addressess = null;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getNickName() {
        return nickName;
    }

    public void setNickName(Object nickName) {
        this.nickName = nickName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getAddBy() {
        return addBy;
    }

    public void setAddBy(Object addBy) {
        this.addBy = addBy;
    }

    public String getAddOn() {
        return addOn;
    }

    public void setAddOn(String addOn) {
        this.addOn = addOn;
    }

    public Object getEditBy() {
        return editBy;
    }

    public void setEditBy(Object editBy) {
        this.editBy = editBy;
    }

    public String getEditOn() {
        return editOn;
    }

    public void setEditOn(String editOn) {
        this.editOn = editOn;
    }

    public List<Addresses> getAddressess() {
        return addressess;
    }

    public void setAddressess(List<Addresses> addressess) {
        this.addressess = addressess;
    }

}
