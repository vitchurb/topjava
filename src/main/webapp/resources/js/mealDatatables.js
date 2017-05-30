var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize(),
        success: updateTableByData
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    $(".datetimepicker").datetimepicker({
        format:'Y-m-d H:i'
    });

    $(".datepicker").datetimepicker({
        timepicker:false,
        format:'Y-m-d'
    });

    $(".timepicker").datetimepicker({
        datepicker:false,
        format:'H:i'
    });

    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (dateTime, type, row) {
                    if (type === 'display') {
                        if (dateTime) {
                            return dateTime.replace('T', ' ').slice(0, -3);
                        }
                    }
                    return dateTime;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderEditBtn
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).addClass(data.exceed ? "exceeded" : "normal")
        },
        "initComplete": makeEditable
    });
});