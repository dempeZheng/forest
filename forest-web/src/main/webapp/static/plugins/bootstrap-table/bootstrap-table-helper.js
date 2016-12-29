TableHelper = {
    idstr: null,
    // 是否有且只选择了一项
    isSelectOne: function(idstr){
        if($(idstr).bootstrapTable('getSelections').length==1){
            return true;
        }
        return false;
    },
    //是否选择了至少一项
    hasSelectAny: function(idstr){
        if($(idstr).bootstrapTable('getSelections').length>0){
            return true;
        }
        return false;
    },
    // 获取选择的一项
    getOneSelectItem: function(idstr){
    	return  $(idstr).bootstrapTable('getSelections')[0];
    },
    // 已经选择的记录
    getAllSelectItems: function(idstr){
        return $(idstr).bootstrapTable('getSelections');
    },
    getRowByUniqueId: function(idstr,id){
        return $(idstr).bootstrapTable('getRowByUniqueId',id);
    },
    // 已选择的项数量
    selectLength: function(idstr){
        return $(idstr).bootstrapTable('getSelections').length;
    },
    // 刷新
    doRefresh: function(idstr){
        $(idstr).bootstrapTable('refresh');
    },
    unCheckAll: function(idstr){
        $(idstr).bootstrapTable('uncheckAll');
    }
}