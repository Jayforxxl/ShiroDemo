<%@page contentType="text/html; UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1>系统主页</h1>
    <h2>当前用户：<shiro:principal/></h2>
    <h2><shiro:authenticated>认证之后才展示的内容！</shiro:authenticated></h2>
    <h2><shiro:notAuthenticated>没有认证才展示的内容！</shiro:notAuthenticated></h2>

    <a href="${pageContext.request.contextPath}/user/logout">退出用户</a>
    <ul>
        <shiro:hasAnyRoles name="user,admin">
            <li>
                <a href="">用户管理</a>
                <ul>
                    <shiro:hasPermission name="user:*:*">
                        <li><a href="">添加用户</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="user:delete:*">
                        <li><a href="">删除用户</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="user:update:*">
                        <li><a href="">修改用户</a></li>
                    </shiro:hasPermission>
                    <li><a href="">查询用户</a></li>
                </ul>
            </li>
        </shiro:hasAnyRoles>
        <shiro:hasRole name="admin">
            <li><a href="">商品管理</a></li>
            <li><a href="">订单管理</a></li>
            <li><a href="">物流管理</a></li>
        </shiro:hasRole>
    </ul>
</body>
</html>
