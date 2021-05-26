/**
 * 모집 상태 dropdown 클릭 이벤트
 */
function boardStatusClick(status){
  var statusInput=$("#status");
  statusInput.val(status);
  if(status=='READY'){
    $("#dropdownMenuButton").text("모집중")
  }else {
    $("#dropdownMenuButton").text("모집 마감")
  }
}