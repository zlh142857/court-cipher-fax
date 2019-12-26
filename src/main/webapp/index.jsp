<!DOCTYPE html>
<html>

<head>
    <%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
    <title>111</title>
    <meta charset="utf-8" />
    <script src="./js/jquery.js"></script>
    <script src="./js/jquery.form.js"></script>
</head>

<body>
    <button onclick="clickBtn3()">inbox</button>
    <button onclick="clickBtn7()">outbox</button>
    <button onclick="clickBtn8()">send</button>
    <button onclick="clickBtn9()">return</button>
    <button onclick="clickBtn()">测试发送和接收</button>


<br>
    <form id="uploadForm" enctype="multipart/form-data">
        文件:<input type="file" name="file" multiple="multiple" /><br>
    </form>
    <img src="http://localhost:8080/fileView.do?photoUrl=C:\Users\zlh\Desktop\201912061556176360.jpg"/>
    <button id="upload">上传文件</button>
</body>
<script src="./jquery.2.14.js"></script>
<script src="./jquery.form.js"></script>
<script type="text/javascript">
    //在表单上追加input hidden元素 存放其他参数
    ///court-cipher-fax

    function clickBtn3() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/eee.do",
            success: function (str) {
                console.log(str)
            },
            error: function(str){
            }
        });
    }
    function clickBtn7() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/outboxTest.do",
            success: function (str) {
            },
            error: function(str){
            }
        });
    }
    function clickBtn8() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/sendTest.do",
            success: function (str) {
            },
            error: function(str){
            }
        });
    }
    function clickBtn9() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/returnTest.do",
            success: function (str) {
            },
            error: function(str){
            }
        });
    }
    function clickBtn() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/court-cipher-fax/sendFax.do",
            dataType:'json',
            success: function (str) {
                console.log(str);
            },
            error: function(str){
            }
        });
    }

    $(function () {
        $("#upload").click(function () {
            var formData = new FormData($('#uploadForm')[0]);
            $.ajax({
                type: 'post',
                url: "http://localhost:8080/changeFileSend.do",
                data: formData,
                cache: false,
                processData: false,
                contentType: false,
            }).success(function (data) {
                console.log(data);
            }).error(function (data) {
                alert("上传失败");
            });
        });
    });

</script>
</html>