var BASE_URI='/mgr/shiro/';
var API_URL = BASE_URI+'listUserRoles.action';
var DEL_API= BASE_URI+'delUserRole.action';
var SAVE_OR_UPDATE_API=BASE_URI+'saveOrUpdateUserRole.action';

var $table = $('#table').bootstrapTable({url: API_URL}),
    $modal = $('#modal').modal({show: false}),
    $alert = $('.alert').hide();

$(function () {
    // create event
    $('.create').click(function () {
        showModal($(this).text());
    });

    $modal.find('.submit').click(function () {
        var row = {};

        $modal.find('input[name]').each(function () {
            row[$(this).attr('name')] = $(this).val();
        });

        row['id']=$modal.data('id');
        var data =JSON.stringify(row);
        $.ajax({
            url: SAVE_OR_UPDATE_API,
            type: "POST",
            data:JSON.parse(data),
            success: function () {
                $modal.modal('hide');
                $table.bootstrapTable('refresh');
                showAlert(($modal.data('id') ? 'Update' : 'Create') + ' item successful!', 'success');
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
        '<a class="update" href="javascript:" title="Update Item"><button class="btn">更新</button></a>&nbsp;&nbsp;',
        '<a class="remove" href="javascript:" title="Delete Item"><button class="btn btn-danger">删除</button></a>',
    ].join('');
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
                success: function () {
                    $table.bootstrapTable('refresh');
                    showAlert('成功删除!', 'success');
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
        $modal.find('input[name="' + name + '"]').val(row[name]);
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