$(".day-butt").click(function () {
    var id = $(this).attr("id");
    var dayArr = ["월", "화", "수", "목", "금", "토", "일"];
    var dayIndex = Number(id[4]);
    $("#md-day-span").text(dayArr[dayIndex]);
    $("#md-day-input").val(dayIndex);
    $("#end-time-input").val("");
    $("#start-time-input").val("");
    $("#timeFormModal").modal("show");
});

$("#time-delete-butt").click(function () {
    var dayIndex = $("#md-day-input").val();
    var targetTimeId = "time-" + dayIndex;
    var targetDayId = "day-" + dayIndex;
    $("#" + targetTimeId).empty();
    $("#" + targetDayId).css("background-color", "rgb(239, 239, 239)");
    $("#timeFormModal").modal("hide");
});

$("#time-register-butt").click(function () {
    var startTime = $("#start-time-input").val();
    var endTime = $("#end-time-input").val();
    var html = "<p>" + startTime + "</p>" + "<p>~</p>" + "<p>" + endTime + "</p>";
    var dayIndex = $("#md-day-input").val();
    var targetTimeId = "time-" + dayIndex;
    $("#" + targetTimeId).empty();
    $("#" + targetTimeId).append(html);

    var targetDayId = "day-" + dayIndex;
    $("#" + targetDayId).css("background-color", "#42F9CD");
    console.log(startTime + "," + endTime);
    $("#timeFormModal").modal("hide");
});
