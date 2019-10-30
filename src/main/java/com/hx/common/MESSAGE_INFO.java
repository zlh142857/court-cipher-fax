package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/12 15:28
 *@功能:
 */

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class MESSAGE_INFO extends Structure {
    public short wEvent;//事件编码
    public int nReference;//参考值
    public int dwParam;//输出参数

    public static class ByReference extends MESSAGE_INFO implements Structure.ByReference {}
    public static class ByValue extends MESSAGE_INFO implements Structure.ByValue {}
    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[]{"wEvent", "nReference", "dwParam"});
    }
}
