package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/30 13:27
 *@功能:
 */

import java.io.Serializable;
import java.util.Date;

public class Program_Setting implements Serializable {
    private static final long serialVersionUID = -8605147854312074502L;
    private int id;
    private String courtName;
    private int isPrint;
    private String printService;
    private int startTime;
    private int endTime;
    private String courtAddress; //本地法院地址
    private String courtNumber;  //本地法院传真号码,默认为空
    private String countryCode;  //国家地区代码
    private String nationCode;   //地区代码
    private String serverComName; //服务器计算机名称
    private String serverName;    //服务器名称
    private String ipAddress;     //Ip地址
    private String ipPort;       //端口
    private boolean isLoginWindows;//登录Windows后自动运行传真服务器管理程序,默认为1
    private boolean isLogin;       //传真服务器启动后自动登录
    private boolean isServer;      //以服务模式运行服务器
    private int choiceCh;    //1表示自动选择空闲线路发送   2表示负载均衡分配方式选择线路
    private String insideNum;//拨号时,传真号码位数小于等于3时,做为内线传真
    private boolean isAgain;  //传真发送失败时是否自动重拨
    private String timeTen; //拨自动总计+分级号码传真时,拨分级号码前自动延时
    private boolean openHeader;//启用传真页眉功能  默认为0
    private String maxNum; //发送传真时,播放语音提示音最大超时次数为
    private boolean autoAgain;  //发送失败时自动重新发送  默认为1
    private String getFileType; //接收传真时保存格式
    private String voiceMaxTime; //应答来电时,播放语音欢迎词最大超时时间
    private boolean setGetFileDate; //接收传真打印时,页面左上角打印接收时间  默认为0
    private boolean isAutoClose;  //是否启用自动关机 默认为0
    private Date autoCloseDate; //每日关机时间
    private boolean isToAdmin; //所有新收到的外部传真语音拨号通知管理员
    private String adminPhone; //通知号码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public int getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(int isPrint) {
        this.isPrint = isPrint;
    }

    public String getPrintService() {
        return printService;
    }

    public void setPrintService(String printService) {
        this.printService = printService;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getCourtAddress() {
        return courtAddress;
    }

    public void setCourtAddress(String courtAddress) {
        this.courtAddress = courtAddress;
    }

    public String getCourtNumber() {
        return courtNumber;
    }

    public void setCourtNumber(String courtNumber) {
        this.courtNumber = courtNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getServerComName() {
        return serverComName;
    }

    public void setServerComName(String serverComName) {
        this.serverComName = serverComName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public boolean isLoginWindows() {
        return isLoginWindows;
    }

    public void setLoginWindows(boolean loginWindows) {
        isLoginWindows = loginWindows;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public int getChoiceCh() {
        return choiceCh;
    }

    public void setChoiceCh(int choiceCh) {
        this.choiceCh = choiceCh;
    }

    public String getInsideNum() {
        return insideNum;
    }

    public void setInsideNum(String insideNum) {
        this.insideNum = insideNum;
    }

    public boolean isAgain() {
        return isAgain;
    }

    public void setAgain(boolean again) {
        isAgain = again;
    }

    public String getTimeTen() {
        return timeTen;
    }

    public void setTimeTen(String timeTen) {
        this.timeTen = timeTen;
    }

    public boolean isOpenHeader() {
        return openHeader;
    }

    public void setOpenHeader(boolean openHeader) {
        this.openHeader = openHeader;
    }

    public String getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(String maxNum) {
        this.maxNum = maxNum;
    }

    public boolean isAutoAgain() {
        return autoAgain;
    }

    public void setAutoAgain(boolean autoAgain) {
        this.autoAgain = autoAgain;
    }

    public String getGetFileType() {
        return getFileType;
    }

    public void setGetFileType(String getFileType) {
        this.getFileType = getFileType;
    }

    public String getVoiceMaxTime() {
        return voiceMaxTime;
    }

    public void setVoiceMaxTime(String voiceMaxTime) {
        this.voiceMaxTime = voiceMaxTime;
    }

    public boolean isSetGetFileDate() {
        return setGetFileDate;
    }

    public void setSetGetFileDate(boolean setGetFileDate) {
        this.setGetFileDate = setGetFileDate;
    }

    public boolean isAutoClose() {
        return isAutoClose;
    }

    public void setAutoClose(boolean autoClose) {
        isAutoClose = autoClose;
    }

    public Date getAutoCloseDate() {
        return autoCloseDate;
    }

    public void setAutoCloseDate(Date autoCloseDate) {
        this.autoCloseDate = autoCloseDate;
    }

    public boolean isToAdmin() {
        return isToAdmin;
    }

    public void setToAdmin(boolean toAdmin) {
        isToAdmin = toAdmin;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    @Override
    public String toString() {
        return "Program_Setting{" +
                "id=" + id +
                ", courtName='" + courtName + '\'' +
                ", isPrint=" + isPrint +
                ", printService='" + printService + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", courtAddress='" + courtAddress + '\'' +
                ", courtNumber='" + courtNumber + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", nationCode='" + nationCode + '\'' +
                ", serverComName='" + serverComName + '\'' +
                ", serverName='" + serverName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", ipPort='" + ipPort + '\'' +
                ", isLoginWindows=" + isLoginWindows +
                ", isLogin=" + isLogin +
                ", isServer=" + isServer +
                ", choiceCh=" + choiceCh +
                ", insideNum='" + insideNum + '\'' +
                ", isAgain=" + isAgain +
                ", timeTen='" + timeTen + '\'' +
                ", openHeader=" + openHeader +
                ", maxNum='" + maxNum + '\'' +
                ", autoAgain=" + autoAgain +
                ", getFileType='" + getFileType + '\'' +
                ", voiceMaxTime='" + voiceMaxTime + '\'' +
                ", setGetFileDate=" + setGetFileDate +
                ", isAutoClose=" + isAutoClose +
                ", autoCloseDate=" + autoCloseDate +
                ", isToAdmin=" + isToAdmin +
                ", adminPhone='" + adminPhone + '\'' +
                '}';
    }
}
