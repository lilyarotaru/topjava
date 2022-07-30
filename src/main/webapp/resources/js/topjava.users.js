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

function enable(checked){
    if (!checked) {
        ctx.datatableApi.closest('tr').attr("disabled", true); //присвоить строке столбца свойство disabled (в css стиле)
    } else {
        ctx.datatableApi.closest('tr').attr("disabled", false);
    }

    let number = ctx.datatableApi.closest('tr').attr("id");      //как захватить id строки???
    $.ajax({
        url: "rest/"+userAjaxUrl+"enable",
        type: "POST",
        data: {id:number, enabled:checked}
    });
}