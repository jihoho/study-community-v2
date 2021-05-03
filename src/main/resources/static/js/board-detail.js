function commentReplyClick(e) {
  var commentDiv = $(e).parents("div.board-comment-list");

  var nickname = $(commentDiv).find(".board-comment-nick").find(
      "a span").text();
  var imageUrl = $(commentDiv).find(".board-comment-img").children("img").attr(
      "src");
  var superCommentId = $(commentDiv).find(".comment-id").val();
  var content = $(commentDiv).find(".board-comment-p").text();
  var modifiedDate = $(commentDiv).find(".board-comment-nick").children(
      ":last").text();
  console.log(commentDiv.attr("class"));
  console.log(nickname);
  console.log(imageUrl);
  console.log(superCommentId);
  console.log(content);
  console.log(modifiedDate);

  $("#comment-md-img").attr("src", imageUrl);
  $("#comment-md-nick").text(nickname);
  $("#comment-md-date").text(modifiedDate);
  $("#comment-md-content").text(content);
  $("#comment-md-super-id").val(superCommentId);
  $("#commentFormModal").modal("show");
}

function commentUpdateClick(e) {
  var commentDiv = $(e).parents("div.board-comment-list");

  var nickname = $(commentDiv).find(".board-comment-nick").find(
      "a span").text();
  var imageUrl = $(commentDiv).find(".board-comment-img").children("img").attr(
      "src");
  var commentId = $(commentDiv).find(".comment-id").val();
  var content = $(commentDiv).find(".board-comment-p").text();
  var modifiedDate = $(commentDiv).find(".board-comment-nick").children(":last").text();

  $("#update-comment-id").val(commentId);
  $("#update-comment-img").attr("src", imageUrl);
  $("#update-comment-nickname").text(nickname);
  $("#update-comment-content").text(content);
  $("#update-comment-modified-date").text(modifiedDate);
  $("#commentUpdateFormModal").modal("show");
}

function commentDeleteConfirm(e) {
  var parentDiv = $(e).parents("div.board-comment-header");
  var targetCommentId = $(parentDiv).find(".comment-id").val();
  $("#delete-comment-id").val(targetCommentId);
  $("#deleteConfirmModal").modal("show");
}

function commentDeleteClick(targetCommentId) {
  console.log(targetCommentId);
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  console.log(token + ", " + header);
  $.ajax({
    url: "/comments/" + targetCommentId,
    type: "DELETE",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    success: function () {
      location.reload();
    },
    error: function () {
      alert("삭제 요청 에러");
    },
  });
}