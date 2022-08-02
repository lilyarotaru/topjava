const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
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
        })
    );
});

function enable(element) {
    let checked = element.checked;
    let row = element.closest('tr');
    let id = row.getAttribute("id");
    $.ajax({
        url: userAjaxUrl + id,
        type: "PATCH",
        contentType: "application/json",
        data: JSON.stringify(checked)
    }).done(function () {
        row.setAttribute('disabled',!checked);
        successNoty(checked? "Запись активирована": "Запись деактивирована");
    }).fail(function () {
        element.checked = !checked;
    });
}