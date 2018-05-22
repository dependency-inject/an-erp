<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">

    <!-- CSRF Token -->
    <%--<meta name="csrf-token" content="{{ csrf_token() }}">--%>

    <title>电子光电码盘生产管理系统</title>

    <!-- Icon -->
    <!-- <link rel="shortcut icon" href="{{ config('blog.default_icon') }}"> -->

    <link rel="stylesheet" href="../../static/css/iview.css">
    <link rel="stylesheet" href="../../static/css/app.css">

    <script>
        // window.Laravel = {
        //     csrfToken: "{{ csrf_token() }}"
        // }

        window.Admin = ${ loginAdmin }
    </script>
</head>
<body>
    <div id="app"></div>

    <script src="../../static/js/app.js"></script>
</body>
</html>
