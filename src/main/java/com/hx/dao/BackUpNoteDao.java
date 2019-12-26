package com.hx.dao;/*
 */

import java.util.Date;

public interface BackUpNoteDao {
    int selectIsDateNull();

    void insertDate(Date date);

    int selectDaysByTime();
}
