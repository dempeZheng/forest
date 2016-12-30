<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Forest管理后台</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css">
    <!-- jvectormap -->
    <link rel="stylesheet" href="/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
    <!-- Font Awesome -->
    <%--<link rel="stylesheet" href="/libs/css/font-awesome.min.css">--%>
    <%--<!-- Ionicons -->--%>
    <link rel="stylesheet" href="/libs/css/ionicons.min.css">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="//cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.css">
    <%--<!-- Ionicons -->--%>
    <%--<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">--%>
    <!-- Theme style -->
    <link rel="stylesheet" href="/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="/dist/css/skins/_all-skins.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    <jsp:include page="../header.jsp"/>
    <jsp:include page="../siderbar.jsp"/>
    <div class="content-wrapper">
        <%--<div id="container" class="col-md-12">--%>
            <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box  box-default">
                            <div class="box-header with-border">
                                <h3 class="box-title">服务列表</h3>
                                <div class="box-tools pull-right">
                                </div><!-- /.box-tools -->
                            </div>
                            <div class="box-body">
                                <table id="table">
                                    <div id="toolbar">

                                        <div class="form-inline" role="form">
                                            <div class="form-group extend_query_choice">
                                                <span>服务名:</span>
                                                <select id="serviceName" class="form-control">
                                                    <c:forEach var="name" items="${names}">
                                                        <option value="${name}">${name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <button id="ok" type="submit" class=" form-control btn btn-default">查询
                                            </button>
                                        </div>

                                    </div>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box  box-default">
                            <div class="box-body">
                                <div id="timeDelayContainer"
                                     style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box  box-default">
                            <div class="box-body">
                                <div id="totalCountContainer"
                                     style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box  box-default">
                            <div class="box-body">
                                <div id="timeDelayDistributionContainer"
                                     style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>

    </div>
    <jsp:include page="../footer.jsp"/>
    <jsp:include page="../control-siderbar.jsp"/>

    <div class="control-sidebar-bg"></div>
</div>

<!-- jQuery 2.2.0 -->
<script src="/plugins/jQuery/jQuery-2.2.0.min.js"></script>
<!-- Bootstrap 3.3.5 -->
<script src="/bootstrap/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="/plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="/dist/js/app.min.js"></script>


<!-- AdminLTE for demo purposes -->
<script src="/dist/js/demo.js"></script>
<%--<script src="/dist/js/pages/dashboard.js"></script>--%>
<jsp:include page="../common/script.jsp"/>
<script>

    $('#ok').click(function () {
        $('#table').bootstrapTable('refresh');
    });
    $(function () {
        Highcharts.chart('timeDelayContainer', {
            title: {
                text: '时延',
                x: -20 //center
            },
            subtitle: {
                text: '单位：毫秒',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Temperature (°C)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: '°C'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [{
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]
        });
    });

    $(function () {
        Highcharts.chart('totalCountContainer', {
            title: {
                text: '请求数',
                x: -20 //center
            },
            subtitle: {
                text: '单位：次',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Temperature (°C)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: '°C'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [{
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]
        });
    });

    $(function () {
        // Create the chart
        Highcharts.chart('timeDelayDistributionContainer', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Browser market shares. January, 2015 to May, 2015'
            },
            subtitle: {
                text: 'Click the columns to view versions. Source: <a href="http://netmarketshare.com">netmarketshare.com</a>.'
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Total percent market share'
                }

            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.1f}%'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
            },

            series: [{
                name: 'Brands',
                colorByPoint: true,
                data: [{
                    name: 'Microsoft Internet Explorer',
                    y: 56.33,
                    drilldown: 'Microsoft Internet Explorer'
                }, {
                    name: 'Chrome',
                    y: 24.03,
                    drilldown: 'Chrome'
                }, {
                    name: 'Firefox',
                    y: 10.38,
                    drilldown: 'Firefox'
                }, {
                    name: 'Safari',
                    y: 4.77,
                    drilldown: 'Safari'
                }, {
                    name: 'Opera',
                    y: 0.91,
                    drilldown: 'Opera'
                }, {
                    name: 'Proprietary or Undetectable',
                    y: 0.2,
                    drilldown: null
                }]
            }],
            drilldown: {
                series: [{
                    name: 'Microsoft Internet Explorer',
                    id: 'Microsoft Internet Explorer',
                    data: [
                        [
                            'v11.0',
                            24.13
                        ],
                        [
                            'v8.0',
                            17.2
                        ],
                        [
                            'v9.0',
                            8.11
                        ],
                        [
                            'v10.0',
                            5.33
                        ],
                        [
                            'v6.0',
                            1.06
                        ],
                        [
                            'v7.0',
                            0.5
                        ]
                    ]
                }, {
                    name: 'Chrome',
                    id: 'Chrome',
                    data: [
                        [
                            'v40.0',
                            5
                        ],
                        [
                            'v41.0',
                            4.32
                        ],
                        [
                            'v42.0',
                            3.68
                        ],
                        [
                            'v39.0',
                            2.96
                        ],
                        [
                            'v36.0',
                            2.53
                        ],
                        [
                            'v43.0',
                            1.45
                        ],
                        [
                            'v31.0',
                            1.24
                        ],
                        [
                            'v35.0',
                            0.85
                        ],
                        [
                            'v38.0',
                            0.6
                        ],
                        [
                            'v32.0',
                            0.55
                        ],
                        [
                            'v37.0',
                            0.38
                        ],
                        [
                            'v33.0',
                            0.19
                        ],
                        [
                            'v34.0',
                            0.14
                        ],
                        [
                            'v30.0',
                            0.14
                        ]
                    ]
                }, {
                    name: 'Firefox',
                    id: 'Firefox',
                    data: [
                        [
                            'v35',
                            2.76
                        ],
                        [
                            'v36',
                            2.32
                        ],
                        [
                            'v37',
                            2.31
                        ],
                        [
                            'v34',
                            1.27
                        ],
                        [
                            'v38',
                            1.02
                        ],
                        [
                            'v31',
                            0.33
                        ],
                        [
                            'v33',
                            0.22
                        ],
                        [
                            'v32',
                            0.15
                        ]
                    ]
                }, {
                    name: 'Safari',
                    id: 'Safari',
                    data: [
                        [
                            'v8.0',
                            2.56
                        ],
                        [
                            'v7.1',
                            0.77
                        ],
                        [
                            'v5.1',
                            0.42
                        ],
                        [
                            'v5.0',
                            0.3
                        ],
                        [
                            'v6.1',
                            0.29
                        ],
                        [
                            'v7.0',
                            0.26
                        ],
                        [
                            'v6.2',
                            0.17
                        ]
                    ]
                }, {
                    name: 'Opera',
                    id: 'Opera',
                    data: [
                        [
                            'v12.x',
                            0.34
                        ],
                        [
                            'v28',
                            0.24
                        ],
                        [
                            'v27',
                            0.17
                        ],
                        [
                            'v29',
                            0.16
                        ]
                    ]
                }]
            }
        });
    });

    $(function () {
        window.initHandle = {
            // 编辑
            editModel: function (id) {
                $.ajax({
                    type: "post",
                    url: "/discovery/query",
                    data: {vuid: id},
                    dateType: "json",
                    success: function (json) {
                        var data = JSON.parse(json).data;
                        $("#uid").attr("readonly", true)
                        $("#edit").val(1);
                        if (data) {
                            $("#uid").val(data.uid);
                            $("#topSid").val(data.topSid);
                            $("#modal").modal('show');
                        }

                    }
                });
            },
            initTable: function () {
                $('#table').bootstrapTable('destroy');
                $('#table').bootstrapTable({
                    columns: [
                        {field: 'id', title: 'id', sortable: 'true', align: 'center', width: '10%'},
                        {
                            field: 'name',
                            title: '服务名',
                            sortable: 'true',
                            visible: false,
                            align: 'center',
                            width: '15%'
                        },
                        {
                            field: 'address',
                            title: 'host',
                            sortable: 'true',
                            visible: true,
                            align: 'center',
                            width: '15%'
                        },
                        {field: 'port', title: 'port', sortable: 'true', align: 'center', width: '15%'},
                        {field: 'serviceType', title: '服务类型', sortable: 'true', align: 'center', width: '15%'},
                        {field: 'registrationTimeUTC', title: '注册时间', sortable: 'true', align: 'center', width: '15%'},
                        {
                            field: 'id',
                            title: '操作',
                            align: 'center',
                            width: '20%',
                            formatter: function (val, row, index) {
                                return [
                                    '<button class="btn btn-primary" onclick="initHandle.editModel(\'' + row.name + '\')"><i class="glyphicon glyphicon-edit"></i>监控</button>&nbsp;&nbsp;',
                                    '<button class="btn btn-danger" onclick="initHandle.deleteModel(\'' + row.id + '\')">隔离</button>',
                                ].join('');
                            }
                        },
                    ],
                    cache: false,
                    striped: true,
                    showRefresh: true,
                    showToggle: true,
                    showColumns: true,
                    pagination: true,
                    pageList: [5, 10, 20, 30, 50],
                    search: true,
                    sidePagination: "client",
                    toolbar: '#toolbar',
                    url: '/discovery/queryByName',
                    queryParams: function queryParams(params) {   //设置查询参数
                        var name = $("#serviceName" + " option:selected").val();
                        var param = {
                            name: name
                        };
                        return param;
                    }
                });

            }
        };

        // 初始化table
        initHandle.initTable();

        // 设置校验ui
        $('#form').validationEngine('attach', {
            promptPosition: 'centerRight',
            scroll: false
        });


        $("#save").click(function () {
            var edit = $("#edit").val();
            var id = $("#uid").val();
            var topSid = $("#topSid").val();
            var subSid = $("#subSid").val();
            var url = "/mgr/trans/saveTailorVideoBlackList.action";
            if (edit == '1') {
                url = "/mgr/trans/updateTailorVideoBlackList.action";
            }
            if ($("#form").validationEngine('validate')) {
                $.ajax({
                    type: "post",
                    url: url,
                    //data: $('#form').serialize(),
                    data: {vuid: id, topSid: topSid, subSid: subSid},
                    dataType: "json",
                    success: function (json) {
                        $("#modal").modal('hide');
                        TableHelper.doRefresh("#table");
                        if (json.data >= 0) {
                            $("#tipMsg").text("保存成功");
                            $("#tipModal").modal('show');
                        } else if (json.data == -2) {
                            // 已经存在
                            $("#tipMsg").text("此主播存在，请勿重复添加!");
                            $("#tipModal").modal('show');
                        } else {
                            console.log("return code:" + json.data);
                            $("#tipMsg").text("保存失败");
                            $("#tipModal").modal('show');
                        }
                    }
                });
            }
        });

    });


</script>

</body>
</html>
