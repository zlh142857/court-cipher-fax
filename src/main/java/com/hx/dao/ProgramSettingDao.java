package com.hx.dao;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/30 13:30
 *@功能:
 */

import com.hx.modle.Program_Setting;
import org.apache.ibatis.annotations.Param;

public interface ProgramSettingDao {

    Program_Setting selectProgramSetting();

    int updatePrintService(Program_Setting programSetting);

    void updateServerWindowsName(@Param( "address" ) String address,@Param( "ip" ) String ip);

    Program_Setting selectProgramSettingAll();

    String selectTelVoicePath();
}
