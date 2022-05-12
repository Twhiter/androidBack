package com.springtest.demo.dto;


import com.springtest.demo.enums.State;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserJoinMerchant {


    public Integer userId;
    public String phoneNumber;
    public String firstName;
    public String lastName;
    public String passportNumber;
    public String country;
    public String email;
    public String passportPhoto;
    public String password;
    public String paymentPassword;
    public State state;
    public BigDecimal moneyAmount;
    public BigDecimal frozenMoney;
    public String avatar;


    //merchant
    public Integer merchantId;
    public Integer merchantUserId;
    public String merchantName;
    public String merchantLicense;
    public String merchantLicensePhoto;
    public String merchantPhoneNumber;
    public String merchantLogo;
    public String merchantEmail;
    public BigDecimal merchantFrozenMoney;
    public BigDecimal merchantMoneyAmount;
    public State merchantState;
}
