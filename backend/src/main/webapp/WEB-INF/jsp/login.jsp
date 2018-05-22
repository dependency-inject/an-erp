<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">

    <!-- CSRF Token -->
    <%--<meta name="csrf-token" content="{{ csrf_token() }}">--%>

    <title>请登录</title>

    <!-- Icon -->
    <!-- <link rel="shortcut icon" href="{{ config('blog.default_icon') }}"> -->

    <link rel="stylesheet" href="../../static/css/app.css">

</head>
<body>
<div class="di-login">
    <div class="title">电子光电码盘生产管理系统</div>
    <div class="login-form">
        <div class="header">快速登录</div>
        <div class="content">
            <form method="post">
                <%--{{ csrf_field() }}--%>
                <div><span>登录名</span><input type="text" name="loginName" placeholder="请输入登录名" /></div>
                <div><span>密码</span><input type="password" name="password" placeholder="请输入密码" /></div>
                <button type="submit">登录</button>
                <div class="help-text">${ error }</div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
