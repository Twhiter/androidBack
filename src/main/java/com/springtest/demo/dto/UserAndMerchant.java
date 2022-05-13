package com.springtest.demo.dto;

import com.springtest.demo.entity.Merchant;
import com.springtest.demo.entity.User;
import lombok.Data;


@Data
public class UserAndMerchant {

    public User user;
    public Merchant merchant;


    public static UserAndMerchant fromUserJoinMerchant(UserJoinMerchant userJoinMerchant) {

        UserAndMerchant um = new UserAndMerchant();
        um.user = new User();
        um.merchant = new Merchant();


        um.user.userId = userJoinMerchant.userId;
        um.user.phoneNumber = userJoinMerchant.phoneNumber;
        um.user.firstName = userJoinMerchant.firstName;
        um.user.lastName = userJoinMerchant.lastName;
        um.user.passportNumber = userJoinMerchant.passportNumber;
        um.user.country = userJoinMerchant.country;
        um.user.email = userJoinMerchant.email;
        um.user.password = userJoinMerchant.password;
        um.user.paymentPassword = userJoinMerchant.paymentPassword;
        um.user.passportPhoto = userJoinMerchant.passportPhoto;
        um.user.state = userJoinMerchant.state;
        um.user.moneyAmount = userJoinMerchant.moneyAmount;
        um.user.frozenMoney = userJoinMerchant.frozenMoney;
        um.user.avatar = userJoinMerchant.avatar;


        um.merchant.merchantId = userJoinMerchant.merchantId;
        um.merchant.merchantUserId = userJoinMerchant.merchantUserId;
        um.merchant.merchantLicense = userJoinMerchant.merchantLicense;
        um.merchant.merchantLicensePhoto = userJoinMerchant.merchantLicensePhoto;
        um.merchant.merchantPhoneNumber = userJoinMerchant.merchantPhoneNumber;
        um.merchant.merchantLogo = userJoinMerchant.merchantLogo;
        um.merchant.merchantEmail = userJoinMerchant.merchantEmail;
        um.merchant.frozenMoney = userJoinMerchant.merchantFrozenMoney;
        um.merchant.moneyAmount = userJoinMerchant.merchantMoneyAmount;
        um.merchant.state = userJoinMerchant.merchantState;


        return um;
    }
}
