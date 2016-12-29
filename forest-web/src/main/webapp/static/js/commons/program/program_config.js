var BASE_URI='/mgr/program/';
var API_URL = BASE_URI+'listProgramConfig.action';
var DEL_API= BASE_URI+'delProgramConfig.action';
var SAVE_OR_UPDATE_API=BASE_URI+'saveOrUpdateProgramConfig.action';

var $table = $('#table').bootstrapTable({url: API_URL}),
    $modal = $('#modal').modal({show: false}),
    $alert = $('.alert').hide();

$(function () {
    $('.create').click(function () {
    	$modal.data('id', null);
    	$("#appid").val(null);
    	$("#topcid").val(null);
    	$("#subcid").val(null);
    	$("#uids").val(null);
    	$("#beginTime").val(null);
    	$("#endTime").val(null);
    	$("#descri").val(null);
    	showModal($(this).text());
    });

    $modal.find('.submit').click(function () {
        var row = {};

        $modal.find('input[name]').each(function () {
        	if(($(this).attr('name') == "beginTime") || ($(this).attr('name') == "endTime")){
        		var srcValue = $(this).val();
        		var targetValue = Date.parse(srcValue) / 1000;
        		row[$(this).attr('name')] =targetValue;
        	}else{
        		row[$(this).attr('name')] = $(this).val();
        	}
        });

        row['id']=$modal.data('id');
        var data =JSON.stringify(row);
        
        var jsonData = JSON.parse(data);
    	if(jsonData.appid==null || jsonData.appid==""){
    		alert("appid不能为空！");
    		return false;
    	}
    	if(jsonData.topcid==null || jsonData.topcid==""){
    		alert("顶级频道ID不能为空！");
    		return false;
    	}
    	if(jsonData.subcid==null || jsonData.subcid==""){
    		alert("子频道ID不能为空！");
    		return false;
    	}
    	if(jsonData.beginTime==null || jsonData.beginTime==""){
    		alert("开播时间不能为空！");
    		return false;
    	}
    	if(jsonData.endTime==null || jsonData.endTime==""){
    		alert("结束时间不能为空！");
    		return false;
    	}
    	if(jsonData.uids==null || jsonData.uids==""){
    		alert("开播UID不能为空！");
    		return false;
    	}
    	if( parseInt(jsonData.beginTime) >= parseInt(jsonData.endTime) ){
    		alert("结束时间应大于开播时间！");
    		return false;
    	}

        $.ajax({
            url: SAVE_OR_UPDATE_API,
            type: "POST",
            data:JSON.parse(data),
            success: function (data) {
                data=JSON.parse(data);
                $modal.modal('hide');
                if(data.data>0){
                    $table.bootstrapTable('refresh');
                    showAlert(($modal.data('id') ? '更新' : '新增') + ' 成功!', 'success');
                }else if(data.data==0){
                	showAlert("操作失败！", 'danger');
                }else if(data.data==-1) {
                    showAlert("已结束节目不能修改！", 'danger');
                }

            },
            error: function () {
                $modal.modal('hide');
                showAlert(($modal.data('id') ? 'Update' : 'Create') + ' item error!', 'danger');
            }
        });
    });
});

function queryParams(params) {
    return {};
}

function actionFormatter(value) {
    return [
        '<a class="update" href="javascript:" title="更新"><button class="btn btn-success">更新</button></a>&nbsp;&nbsp;',
        '<a class="remove" href="javascript:" title="删除"><button class="btn btn-danger">删除</button></a>&nbsp;&nbsp;',
    ].join('');
}

function dateFormatter(value) {
	var dateT = new Date(value*1000);
	return dateT.format("yyyy-MM-dd HH:mm:ss");
}

// update and delete events
window.actionEvents = {
    'click .update': function (e, value, row) {
        showModal($(this).attr('title'), row);
    },
    'click .remove': function (e, value, row) {
        if (confirm('确认删除?')) {
            $.ajax({
                url: DEL_API+"?id=" + row.id,
                success: function (data) {
                	data=JSON.parse(data);
                    if(data.data>0){
                    	$table.bootstrapTable('refresh');
                    	showAlert('成功删除!', 'success');
                    }else if(data.data==0){
                    	showAlert('删除失败，请刷新页面重新操作!', 'danger');
                    }else if(data.data==-1) {
                        showAlert("已结束节目不能删除！", 'danger');
                    }
                },
                error: function () {
                    showAlert('删除错误!', 'danger');
                }
            })
        }
    }
};

function showModal(title, row) {
    row = row || {
        name: '',
        stargazers_count: 0,
        forks_count: 0,
        description: ''
    }; // default row value

    $modal.data('id', row.id);
    $modal.find('.modal-title').text(title);
    for (var name in row) {
    	if(name=="beginTime" || name=="endTime"){
    		$modal.find('input[name="' + name + '"]').val(dateFormatter(row[name]));
    	}else{
    		$modal.find('input[name="' + name + '"]').val(row[name]);
    	}
    }
    $modal.modal('show');
}

function showAlert(title, type) {
    $alert.attr('class', 'alert alert-' + type || 'success')
        .html('<i class="glyphicon glyphicon-check"></i> ' + title).show();
    setTimeout(function () {
        $alert.hide();
    }, 3000);
}

Date.prototype.format = function(fmt)   
{   
	var o = {           
		    "M+" : this.getMonth()+1, //月份           
		    "d+" : this.getDate(), //日           
		    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时           
		    "H+" : this.getHours(), //小时           
		    "m+" : this.getMinutes(), //分           
		    "s+" : this.getSeconds(), //秒           
		    "q+" : Math.floor((this.getMonth()+3)/3), //季度           
		    "S" : this.getMilliseconds() //毫秒           
    };           
    var week = {           
    "0" : "/u65e5",           
    "1" : "/u4e00",           
    "2" : "/u4e8c",           
    "3" : "/u4e09",           
    "4" : "/u56db",           
    "5" : "/u4e94",           
    "6" : "/u516d"          
    };           
    if(/(y+)/.test(fmt)){           
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));           
    }           
    if(/(E+)/.test(fmt)){           
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);           
    }           
    for(var k in o){           
        if(new RegExp("("+ k +")").test(fmt)){           
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));           
        }           
    }           
    return fmt;
}