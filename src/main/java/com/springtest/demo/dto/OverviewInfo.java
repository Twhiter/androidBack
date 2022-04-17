package com.springtest.demo.dto;


import com.springtest.demo.entity.Merchant;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverviewInfo {


    public Integer id;
    public UserType type;
    public String avatar;
    public String name;
    public String phoneNumber;
    public String email;

    static public OverviewInfo fromUser(User user) {
        return new OverviewInfo(user.userId, UserType.user,user.avatar,
                String.format("%s %s",user.firstName,user.lastName), user.phoneNumber,user.email);
    }

    static public OverviewInfo fromMerchant(Merchant merchant) {
        return new OverviewInfo(merchant.merchantId,UserType.merchant,merchant.merchantLogo,merchant.merchantName,
                merchant.merchantPhoneNumber, merchant.merchantEmail);
    }



}
