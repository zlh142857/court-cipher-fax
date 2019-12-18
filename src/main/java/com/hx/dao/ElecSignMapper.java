package com.hx.dao;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/11 16:57
 *@功能:
 */

import com.hx.modle.Elec_Sign;

import java.util.List;

public interface ElecSignMapper {

    void insertElecSign(Elec_Sign elec_sign);

    int deleteSignById(Integer id);

    List<Elec_Sign> selectElecSign();

    List<Elec_Sign> selectElecSignName();

    String selectElecSignById(Integer id);
}
