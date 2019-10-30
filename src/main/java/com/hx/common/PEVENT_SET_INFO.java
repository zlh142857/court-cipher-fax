package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/15 14:13
 *@功能:
 */

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class PEVENT_SET_INFO extends Structure{
    public int dwWorkMode=1;
    public Pointer lpHandlerParam;
    public int dwOutCondition;
    public int dwOutParamVal;
    public int dwUser;
    public static class ByReference extends PEVENT_SET_INFO implements Structure.ByReference {}
    public static class ByValue extends PEVENT_SET_INFO implements Structure.ByValue {}
    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[]{"dwWorkMode", "lpHandlerParam", "dwOutCondition","dwOutParamVal","dwUser"});
    }
}
