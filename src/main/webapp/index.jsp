<!DOCTYPE html>
<html>

<head>
    <%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
    <title>111</title>
    <meta charset="utf-8" />
    <script src="./js/jquery.js"></script>

</head>

<body>
    <button onclick="clickBtn()">test水电费</button>
    <button onclick="clickBtn2()">图片</button>
    <button onclick="clickBtn3()">直接打开扫描</button>
    <button onclick="clickBtn4()">pdf</button>
</body>
<script type="text/javascript">
    //在表单上追加input hidden元素 存放其他参数
    ///court-cipher-fax
    function clickBtn() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/court-cipher-fax/print.do",
            dataType : "json",
            success: function (str) {
                alert(str);
            },
            error: function(str){
                alert("失败");
            }
        });
    }
    function clickBtn2() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/court-cipher-fax/printT.do",
            dataType : "json",
            success: function (str) {
                alert(str);
            },
            error: function(str){
                alert("失败");
            }
        });
    }
    function clickBtn4() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/court-cipher-fax/printW.do",
            dataType : "json",
            success: function (str) {
                alert(str);
            },
            error: function(str){
                alert("失败");
            }
        });
    }
    function clickBtn3() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/court-cipher-fax/printD.do",
            success: function (str) {
            },
            error: function(str){
            }
        });
    }

</script>
</html>