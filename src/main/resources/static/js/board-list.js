$(document).ready(function () {
  var searchType = $("#searchType").val();
  var keyword = $("#keyword-input").val();
  console.log("searchType: " + searchType + ", keyword: " + keyword);
  if (searchType == "TITLE") {
    $("#search-dropdown-menu").text("제목");
    changeInputElement("input");
  } else if (searchType == "TAG") {
    $("#search-dropdown-menu").text("주제+기술");
    changeInputElement("input");
  } else if (searchType == "STATUS") {
    $("#search-dropdown-menu").text("상태");
    changeInputElement("radio");
    if (keyword == "FINISH") {
      $("#inlineRadio1").attr("checked", false);
      $("#inlineRadio2").attr("checked", true);
    } else {
      $("#inlineRadio1").attr("checked", true);
      $("#inlineRadio2").attr("checked", false);
    }
  }
  console.log("ready!");
});

/**
 * 검색 타입 변경
 */
function changeSearchType(type) {
  $("#searchType").val(type);
  if (type == "TITLE") {
    $("#search-dropdown-menu").text("제목");
    changeInputElement("input");
  } else if (type == "TAG") {
    $("#search-dropdown-menu").text("주제+기술");
    changeInputElement("input");
  } else if (type == "STATUS") {
    $("#search-dropdown-menu").text("상태");
    changeInputElement("radio");
  }
}

function changeInputElement(element) {
  if (element == "input") {
    $("#search-input-div").css("display", "flex");
    $("#board-status-radio").css("display", "none");
    $("#keyword-input").attr("disabled", false);
    $("#inlineRadio1").attr("disabled", true);
    $("#inlineRadio2").attr("disabled", true);
  } else if (element == "radio") {
    $("#search-input-div").css("display", "none");
    $("#board-status-radio").css("display", "block");
    $("#keyword-input").attr("disabled", true);
    $("#inlineRadio1").attr("disabled", false);
    $("#inlineRadio2").attr("disabled", false);
    $("#keyword-input").val("");
  } else {
    console.log("invalid action!");
  }
}