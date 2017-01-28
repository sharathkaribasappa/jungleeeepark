package com.amigosphire_poc.demo.adapter;

/**
 * Created by skaribasappa on 1/28/17.
 */

public class WhiteListHolder {
    private String mName;
    private String mPhoneNumber;

    public WhiteListHolder(String name, String phonenumber) {
        mName = name;
        mPhoneNumber = phonenumber;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getName() {
        return mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }
}
