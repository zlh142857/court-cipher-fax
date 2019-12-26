package com.hx.modle;/*
 *//*
 *@作者:张立恒
 *@时间:2019/12/25 9:40
 *@功能:
 */

import java.io.Serializable;
import java.sql.Date;

public class Backup_note implements Serializable {
    private static final long serialVersionUID = -5581670770145938492L;
    private int id;
    private Date backUpDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getBackUpDate() {
        return backUpDate;
    }

    public void setBackUpDate(Date backUpDate) {
        this.backUpDate = backUpDate;
    }

    @Override
    public String toString() {
        return "Backup_note{" +
                "id=" + id +
                ", backUpDate=" + backUpDate +
                '}';
    }
}
