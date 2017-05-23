var ajaxUrl = 'ajax/meals/';
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $('#datatable').DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
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
    makeEditableMeals();
});

function makeEditableMeals() {
    $('#filterForm').submit(function () {
        updateTable();
        return false;
    });
}

function resetForm(formElement) {
    formElement.form.reset();
}

function updateTable() {
    var form = $('#filterForm');
    $.get({
        url: ajaxUrl + 'filter',
        cache: false,
        data: form.serialize(),
        success: function (data) {
            datatableApi.clear();
            $.each(data, function (key, item) {
                datatableApi.row.add(item);
            });
            datatableApi.draw();
        }
    });
}
