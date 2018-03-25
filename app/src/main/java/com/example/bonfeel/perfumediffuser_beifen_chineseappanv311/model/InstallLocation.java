package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

/**
 *
 * Author:FunFun
 * Function:设备的安装地址
 * Date:2016/4/12 8:54
 */
public class InstallLocation {
    private String country;// 国家
    private String province;// 省份
    private String city;// 城市
    private String county;// 县区
    private String street;// 街道
    private String detail;// 详细地址

    /**
     * 构造方法名称：	InstallLocation
     * 创建时间：		2015-2-1上午11:22:28
     * 方法描述：		TODO
     */
    public InstallLocation() {
        super();
        this.country = "中国";
        this.province = "";
        this.city = "";
        this.county = "";
        this.street = "";
        this.detail = "";
    }

    /**
     * 返回值： country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 参数设置： country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 返回值： province
     */
    public String getProvince() {
        return province;
    }

    /**
     * 参数设置： province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 返回值： city
     */
    public String getCity() {
        return city;
    }

    /**
     * 参数设置： city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 返回值： county
     */
    public String getCounty() {
        return county;
    }

    /**
     * 参数设置： county to set
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * 返回值： street
     */
    public String getStreet() {
        return street;
    }

    /**
     * 参数设置： street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * 返回值： detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 参数设置： detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
