<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<aside class="main-sidebar">
    <section class="sidebar">
        <ul class="sidebar-menu">
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-dashboard"></i> <span>服务发现</span> <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="/discovery/index"><i class="fa fa-circle-o"></i>服务列表</a></li>
                    <li><a href="/mgr/chat/bulletLibIndex.action"><i class="fa fa-circle-o"></i></a></li>
                </ul>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-dashboard"></i> <span>系统监控</span> <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="/mgr/trans/globalConfig.action"><i class="fa fa-circle-o"></i></a></li>
                </ul>

            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-dashboard"></i> <span>系统配置</span> <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="/mgr/program/programConfigIndex.action"><i class="fa fa-circle-o"></i>用户配置</a></li>
                </ul>
            </li>

            <li>
                <a href="#">
                    <i class="fa fa-envelope"></i> <span>设置</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="/mgr/shiro/userPermissionsIndex.action"><i class="fa fa-circle-o"></i>用户权限配置</a></li>
                </ul>
            </li>


        </ul>
    </section>
</aside>