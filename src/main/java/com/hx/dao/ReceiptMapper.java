package com.hx.dao;

        import com.hx.modle.Return_Receipt;
        import org.apache.ibatis.annotations.Param;

        import java.util.List;

/**
 * @author 范聪敏
 * @date 2019/10/16 17:20
 * @desc
 */
public interface ReceiptMapper {
    List<Return_Receipt> queryReturn();

    void insertReceipt(@Param( "returnReceipt" ) Return_Receipt returnReceipt);
}
