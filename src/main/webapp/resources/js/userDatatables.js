var ajaxUrl = 'ajax/admin/users/';
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $('#datatable').DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});

function toggleUserEnabled(checkboxItem) {
    var tr = $(checkboxItem).closest("tr");
    $.ajax({
        url: ajaxUrl + tr.attr("itemId") + '/toggleEnabled',
        type: 'POST',
        data: {enabled : $(checkboxItem).prop('checked')},
        success: function (data) {
            $(checkboxItem).prop( "checked", data);
            if (data) {
                successNoty('User enabled');
                tr.removeClass("disabledUser");
            } else {
                successNoty('User disabled');
                tr.addClass("disabledUser");
            }
        }
    });
}

function updateTable() {
    $.get({
        url: ajaxUrl,
        cache: false
    }, function (data) {
        datatableApi.clear();
        $.each(data, function (key, item) {
            datatableApi.row.add(item);
        });
        datatableApi.draw();
    });
}
