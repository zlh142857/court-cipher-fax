package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/24 14:58
 *@功能:设备设置表,电话线路设置
 */

import java.io.Serializable;

public class Device_Setting implements Serializable {
    private static final long serialVersionUID = 2412734508087829096L;
    private Integer id; //id
    private Integer ch;  //通道编号
    private String prefix;  //前缀
    private String areaCode;  //区号
    private String seatNumber; //座机号
    private Integer chType;  //通道类型,仅接受1,仅发送2,或者是接收发送都有0,默认为0
    private String deviceName;//设备名称
    private boolean localTelLine;//市话类型 默认1,true
    private boolean longLine; //长途线路默认true
    private boolean internationalLine;//国际线路默认true
    private boolean checkDialTone; //拨号前检查拨号音(检测不到拨号音将停止拨号)
    private int ringNum; //自动应答前响铃次数
    private String localPrefix; //市话前缀
    private String longPrefix;//拨国内长途前缀
    private String internationalPrefix; //拨国际长途前缀
    private boolean delPrefix; //删除来电中的固话前缀
    private int sendSpeedId;//发送速度id
    private int receiveSpeedId;//接收速率id
    private boolean openEcm; //启用Ecm传输模式
    private boolean refuseFax;//拒绝接收没有拨分机号码的公共传真
    private boolean open2D;//启用2D传真压缩编码
    private String sendPointRule;//发送提示规则
    private boolean openInterPoint;//启用国际长途拨号音
    private String sendFaxPath;//发送传真提示音
    private String internationalPath;//国际长途提示音
    private String receivePointRule;//接收提示规则
    private String receiveFaxPath;//接收传真提示音
    private String wrongPath;//分机号错提示音
    private boolean editFlag;
    private int sendSpeed;
    private int receiveSpeed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCh() {
        return ch;
    }

    public void setCh(Integer ch) {
        this.ch = ch;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getChType() {
        return chType;
    }

    public void setChType(Integer chType) {
        this.chType = chType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isLocalTelLine() {
        return localTelLine;
    }

    public void setLocalTelLine(boolean localTelLine) {
        this.localTelLine = localTelLine;
    }

    public boolean isLongLine() {
        return longLine;
    }

    public void setLongLine(boolean longLine) {
        this.longLine = longLine;
    }

    public boolean isInternationalLine() {
        return internationalLine;
    }

    public void setInternationalLine(boolean internationalLine) {
        this.internationalLine = internationalLine;
    }

    public boolean isCheckDialTone() {
        return checkDialTone;
    }

    public void setCheckDialTone(boolean checkDialTone) {
        this.checkDialTone = checkDialTone;
    }

    public int getRingNum() {
        return ringNum;
    }

    public void setRingNum(int ringNum) {
        this.ringNum = ringNum;
    }

    public String getLocalPrefix() {
        return localPrefix;
    }

    public void setLocalPrefix(String localPrefix) {
        this.localPrefix = localPrefix;
    }

    public String getLongPrefix() {
        return longPrefix;
    }

    public void setLongPrefix(String longPrefix) {
        this.longPrefix = longPrefix;
    }

    public String getInternationalPrefix() {
        return internationalPrefix;
    }

    public void setInternationalPrefix(String internationalPrefix) {
        this.internationalPrefix = internationalPrefix;
    }

    public boolean isDelPrefix() {
        return delPrefix;
    }

    public void setDelPrefix(boolean delPrefix) {
        this.delPrefix = delPrefix;
    }

    public int getSendSpeedId() {
        return sendSpeedId;
    }

    public void setSendSpeedId(int sendSpeedId) {
        this.sendSpeedId = sendSpeedId;
    }

    public int getReceiveSpeedId() {
        return receiveSpeedId;
    }

    public void setReceiveSpeedId(int receiveSpeedId) {
        this.receiveSpeedId = receiveSpeedId;
    }

    public boolean isOpenEcm() {
        return openEcm;
    }

    public void setOpenEcm(boolean openEcm) {
        this.openEcm = openEcm;
    }

    public boolean isRefuseFax() {
        return refuseFax;
    }

    public void setRefuseFax(boolean refuseFax) {
        this.refuseFax = refuseFax;
    }

    public boolean isOpen2D() {
        return open2D;
    }

    public void setOpen2D(boolean open2D) {
        this.open2D = open2D;
    }

    public String getSendPointRule() {
        return sendPointRule;
    }

    public void setSendPointRule(String sendPointRule) {
        this.sendPointRule = sendPointRule;
    }

    public boolean isOpenInterPoint() {
        return openInterPoint;
    }

    public void setOpenInterPoint(boolean openInterPoint) {
        this.openInterPoint = openInterPoint;
    }

    public String getSendFaxPath() {
        return sendFaxPath;
    }

    public void setSendFaxPath(String sendFaxPath) {
        this.sendFaxPath = sendFaxPath;
    }

    public String getInternationalPath() {
        return internationalPath;
    }

    public void setInternationalPath(String internationalPath) {
        this.internationalPath = internationalPath;
    }

    public String getReceivePointRule() {
        return receivePointRule;
    }

    public void setReceivePointRule(String receivePointRule) {
        this.receivePointRule = receivePointRule;
    }

    public String getReceiveFaxPath() {
        return receiveFaxPath;
    }

    public void setReceiveFaxPath(String receiveFaxPath) {
        this.receiveFaxPath = receiveFaxPath;
    }

    public String getWrongPath() {
        return wrongPath;
    }

    public void setWrongPath(String wrongPath) {
        this.wrongPath = wrongPath;
    }

    public boolean isEditFlag() {
        return editFlag;
    }

    public void setEditFlag(boolean editFlag) {
        this.editFlag = editFlag;
    }

    public int getSendSpeed() {
        return sendSpeed;
    }

    public void setSendSpeed(int sendSpeed) {
        this.sendSpeed = sendSpeed;
    }

    public int getReceiveSpeed() {
        return receiveSpeed;
    }

    public void setReceiveSpeed(int receiveSpeed) {
        this.receiveSpeed = receiveSpeed;
    }

    @Override
    public String toString() {
        return "Device_Setting{" +
                "id=" + id +
                ", ch=" + ch +
                ", prefix='" + prefix + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", chType=" + chType +
                ", deviceName='" + deviceName + '\'' +
                ", localTelLine=" + localTelLine +
                ", longLine=" + longLine +
                ", internationalLine=" + internationalLine +
                ", checkDialTone=" + checkDialTone +
                ", ringNum='" + ringNum + '\'' +
                ", localPrefix='" + localPrefix + '\'' +
                ", longPrefix='" + longPrefix + '\'' +
                ", internationalPrefix='" + internationalPrefix + '\'' +
                ", delPrefix=" + delPrefix +
                ", sendSpeedId='" + sendSpeedId + '\'' +
                ", receiveSpeedId='" + receiveSpeedId + '\'' +
                ", openEcm=" + openEcm +
                ", refuseFax=" + refuseFax +
                ", open2D=" + open2D +
                ", sendPointRule='" + sendPointRule + '\'' +
                ", openInterPoint=" + openInterPoint +
                ", sendFaxPath='" + sendFaxPath + '\'' +
                ", internationalPath='" + internationalPath + '\'' +
                ", receivePointRule='" + receivePointRule + '\'' +
                ", receiveFaxPath='" + receiveFaxPath + '\'' +
                ", wrongPath='" + wrongPath + '\'' +
                ", editFlag=" + editFlag +
                ", sendSpeed='" + sendSpeed + '\'' +
                ", receiveSpeed='" + receiveSpeed + '\'' +
                '}';
    }
}
