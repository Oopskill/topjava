const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

function enabled(checkbox, id){
    //console.log(checkbox.parentNode.parentNode);
    let enable = checkbox.checked;

    $.post(userAjaxUrl + id, {'enable' : enable})
        .done(function () {
            let tr = checkbox.parentNode.parentNode;
            $(tr).toggleClass('table-dark');
            successNoty((enable ? "Enabled" : "Disabled") + " - id: " + id);
        })
        .fail(function () {
            checkbox.checked = !enable;
        });
}

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