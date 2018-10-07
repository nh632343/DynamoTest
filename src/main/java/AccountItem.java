import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

/**
 * 注　：not null代表数据库字段不能为空
 * 　　　null代表数据库字段为空
 */

@DynamoDBTable(tableName = "account")
public class AccountItem implements Serializable {

    private static final long serialVersionUID = 8823025867674655664L;
    /**
     * 主键，用户名(not null)
     */
    @DynamoDBHashKey(attributeName = "account_name")
    private String accountName;

    /**
     * 唯一标识(not null)
     */
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "open_id-index")
    @DynamoDBAttribute(attributeName = "open_id")
    private String openId;

    /**
     * 账户类型(1、匿名 2、手机 3、邮箱 4、自定义用户名)
     * 5: ‘google’, 6: ‘facebook’, 7: ‘twitter’,
     * 8:’opposingle’,9: ‘wan_ba’
     * (not null)
     */
    @DynamoDBAttribute(attributeName = "account_type")
    private Integer accountType;


    @DynamoDBAttribute(attributeName = "mobile")
    private String mobile;


    /**
     * 账户注册时间(not null)
     */
    @DynamoDBAttribute(attributeName = "register_time")
    private Long registerTime;

    /**
     * 密码(null)
     */
    @DynamoDBAttribute(attributeName = "password")
    private String pwd;


    @DynamoDBAttribute(attributeName = "plamform_id")
    private Integer plamformId;


    @DynamoDBAttribute(attributeName = "channel_id")
    private Integer channelId;




    @DynamoDBAttribute(attributeName = "app_id_1_register_time")
    private Integer appId1RegisterTime;


    @DynamoDBAttribute(attributeName = "app_id_2_register_time")
    private Integer appId2RegisterTime;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccountName() {
        return accountName;
    }

    public AccountItem setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public AccountItem setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public AccountItem setAccountType(Integer accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public AccountItem setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public AccountItem setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
        return this;
    }

    public String getPwd() {
        return pwd;
    }

    public AccountItem setPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }

    public Integer getPlamformId() {
        return plamformId;
    }

    public AccountItem setPlamformId(Integer plamformId) {
        this.plamformId = plamformId;
        return this;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public AccountItem setChannelId(Integer channelId) {
        this.channelId = channelId;
        return this;
    }

    @DynamoDBAttribute(attributeName = "app_id_3_register_time")
    private Integer appId3RegisterTime;


    @DynamoDBAttribute(attributeName = "app_id_4_register_time")
    private Integer appId4RegisterTime;



    @DynamoDBAttribute(attributeName = "app_id_5_register_time")
    private Integer appId5RegisterTime;




}
