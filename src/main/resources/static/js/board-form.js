/**
 * 요일의 스케줄 등록 시 modal창에 요일 정보를 보내고 modal창을 띄움
 *
 */
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

/**
 * schedule 등록 modal창에서 삭제 버튼 클릭 시
 * 해당 요일의 스케줄 제거
 */
$("#time-delete-butt").click(function () {
  var dayIndex = $("#md-day-input").val();
  var targetTimeId = "time-" + dayIndex;
  var targetDayId = "day-" + dayIndex;
  $("#" + targetTimeId).empty();
  $("#" + targetDayId).css("background-color", "rgb(239, 239, 239)");
  $("#timeFormModal").modal("hide");
});

/**
 * schedule 등록 modal창에서 등록 버튼 클릭 시
 * 시작 시간과 종료 시간 정보가 해당 요일 아래에 동적으로 생성 됨
 */
$("#time-register-butt").click(function () {
  var startTime = $("#start-time-input").val();
  var endTime = $("#end-time-input").val();
  var dayIndex = $("#md-day-input").val();
  var html = "<div class='day-schedule'>" + "<input type='hidden' class='day-key' value='" + dayIndex + "'/>" + "<p class='start-time'>" + startTime + "</p>" + "<p>~</p>" + "<p class='end-time'>" + endTime + "</p>" + "</div>";
  var targetTimeId = "time-" + dayIndex;
  $("#" + targetTimeId).empty();
  $("#" + targetTimeId).append(html);

  var targetDayId = "day-" + dayIndex;
  $("#" + targetDayId).css("background-color", "#42F9CD");
  console.log(startTime + "," + endTime);
  $("#timeFormModal").modal("hide");
});

/**
 * 공고 등록 버튼 클릭 시
 * 스케줄 정보 input태그로 동적 생성 및 submit
 */
$("#board-create-butt").click(function () {
  var scheduleInput = $("#schedule-input");
  var form = $("#board-create-form");
  scheduleInput.empty();
  var schduleList = document.getElementsByClassName("day-schedule");
  for (var i = 0; i < schduleList.length; i++) {
    var startTime = schduleList[i].getElementsByClassName("start-time")[0].innerText;
    var endTime = schduleList[i].getElementsByClassName("end-time")[0].innerText;
    var dayKey = schduleList[i].getElementsByClassName("day-key")[0].value;
    var inputDay = "<input type='hidden' name='boardSchedules[" + i + "].dayKey' value='" + dayKey + "'>";
    var inputStartTime = "<input type='hidden' name='boardSchedules[" + i + "].startTime' value='" + startTime + "'/>";
    var inputEndTime = "<input type='hidden' name='boardSchedules[" + i + "].endTime' value='" + endTime + "'/>";
    console.log("index :" + i + ", day: " + dayKey + ", startTime: " + startTime + ", endTime: " + endTime);
    scheduleInput.append(inputDay);
    scheduleInput.append(inputStartTime);
    scheduleInput.append(inputEndTime);
  }
  form.submit();
});

// typeahead 관련 script
var substringMatcher = function (strs) {
  return function findMatches(q, cb) {
    var matches, substringRegex;

    // an array that will be populated with substring matches
    matches = [];

    // regex used to determine if a string contains the substring `q`
    substrRegex = new RegExp(q, "i");

    // iterate through the pool of strings and for any string that
    // contains the substring `q`, add it to the `matches` array
    $.each(strs, function (i, str) {
      if (substrRegex.test(str)) {
        matches.push(str);
      }
    });

    cb(matches);
  };
};

var subjects = ["Web", "Backend", "Frontent", "Embeded", "DataEngineering", "DataAnalysis", "Android", "IOS", "System", "DevOps"];

var techStacks = ["Spring", "JPA", "Flutter", "Docker", "HTML", "JavaScript", "CSS", "React", "Vue.js", "MyBatis", "struts", "Java", "C++", "C", "Python"];

/**
 * window.onload와 다르게 태그 로드만을 감지
 * (즉 DOM 트리의 로드만을 감지, 이미지, 영상의 로드 X)
 * 페이지 로드 후 동적으로 태그 데이터 호출
 */
$(document).ready(function () {
  $.ajax({
    type: "get",
    url: "/subjects",
    success: function (jsonArr) {
      console.log("subjects response: " + jsonArr);
      subjects = [];
      jsonArr.forEach(function (json, idx) {
        subjects.push(json["name"]);
      });
      console.log("subjects: " + subjects);
    },
    error: function () {
      alert("주제 관련 태그 로딩 실패!");
    },
  });

  $.ajax({
    type: "get",
    url: "/tech-stacks",
    success: function (jsonArr) {
      console.log("techstacks response: " + jsonArr);
      techStacks = [];
      jsonArr.forEach(function (json, idx) {
        techStacks.push(json["name"]);
      });
      console.log("techStacks: " + techStacks);
    },
    error: function () {
      alert("기술 관련 태그 로딩 실패!");
    },
  });
});

$(document).ajaxComplete(function () {
  $("#subject-input").tagsinput({
    typeaheadjs: {
      name: "subjects",
      source: substringMatcher(subjects),
    },
  });

  $("#tech-input").tagsinput({
    typeaheadjs: {
      name: "techStachs",
      source: substringMatcher(techStacks),
    },
  });
});
