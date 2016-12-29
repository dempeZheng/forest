<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<header class="main-header">
    <!-- Logo -->
    <a class="logo">
        <span class="logo-mini"><b>F</b>orest</span>
        <span class="logo-lg"><b>Forest管理后台</b></span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>

        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <!-- Messages: style can be found in dropdown.less-->

                <!-- User Account: style can be found in dropdown.less -->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img src="
                        <c:choose>
                           <c:when test="${userInfo.im_logo==null||userInfo.im_logo==''}">
                           http://yyweb.yystatic.com/pc/images/portrait/person/1.jpg
                           </c:when>
                           <c:otherwise>
                               ${userInfo.im_logo}
                           </c:otherwise>
                        </c:choose>"
                             class="user-image">
                        <span class="hidden-xs">${userInfo.nick}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <li class="user-header">
                            <img src="
                            <c:choose>
                           <c:when test="${userInfo.im_logo==null||userInfo.im_logo==''}">
                           http://yyweb.yystatic.com/pc/images/portrait/person/1.jpg
                           </c:when>
                           <c:otherwise>
                               ${userInfo.im_logo}
                           </c:otherwise>
                        </c:choose>" class="img-circle" alt="User Image">

                            <p>
                                ${roles}
                                <%--<small>Member since Nov. 2012</small>--%>
                            </p>
                        </li>
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-left">
                                <a href="#" class="btn btn-default btn-flat">Profile</a>
                            </div>
                            <div class="pull-right">
                                <a onclick="" class="btn btn-default btn-flat">Sign out</a>
                            </div>
                        </li>
                    </ul>
                </li>


            </ul>
        </div>
    </nav>
</header>



</script>