package com.example.covac;

public class adminsignupdataholder {
    String fullname,phonenumber,email,registrationno,password;

    public adminsignupdataholder(String fullname, String phonenumber, String email, String registrationno, String password) {
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.registrationno = registrationno;
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationno() {
        return registrationno;
    }

    public void setRegistrationno(String registrationno) {
        this.registrationno = registrationno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
