function setThumbnail(event) {
  const reader = new FileReader();
  reader.onload = function (event) {
    const imgBox=$('#img-box');
    imgBox.empty();
    const img = document.createElement("img");
    img.setAttribute("src", event.target.result);
    img.setAttribute("class","profile-img")
    imgBox.append(img);
  };
  reader.readAsDataURL(event.target.files[0]);
}