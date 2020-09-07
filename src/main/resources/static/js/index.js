$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");

    // 获取标题和内容
    let title = $("#recipient-name").val();
    let content = $("#message-text").val();
    // 发送异步请求
    $.post(
        "/discuss/insert",
        {"title": title, "content": content},
        function (data) {
            data = $.parseJSON(data);
            // 在提示框中显示返回的信息
            $("#hintBody").text(data.msg);
            // 显示提示框
            $("#hintModal").modal("show");
            // 2 秒后自动隐藏
            setTimeout(function () {
                $("#hintModal").modal("hide");
                // 发布成功，刷新页面显示
                if (data.code == 0) {
                    window.location.reload();
                }
            }, 2000);
        }
    )
}