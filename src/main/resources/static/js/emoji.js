document.addEventListener("DOMContentLoaded", function () {
    const emojiButton = document.getElementById("emojiButton");
    const emojiPanel = document.getElementById("emojiPanel");
    const messageContent = document.getElementById("messageContent");
    const attachFileButton = document.getElementById("attachFileButton");
    const fileInput = document.getElementById("fileInput");
    const filePreview = document.getElementById("filePreview");
    const fileName = document.getElementById("fileName");
    const removeFileButton = document.getElementById("removeFileButton");

    emojiButton.addEventListener("click", () => {
        emojiPanel.classList.toggle("d-none");
    });

    emojiPanel.addEventListener("click", (e) => {
        if (e.target.classList.contains("emoji")) {
            messageContent.value += e.target.textContent;
            emojiPanel.classList.add("d-none");
        }
    });

    attachFileButton.addEventListener("click", () => {
        fileInput.click();
    });

    fileInput.addEventListener("change", () => {
        if (fileInput.files.length > 0) {
            const file = fileInput.files[0];
            fileName.textContent = file.name;
            filePreview.classList.remove("d-none");
        }
    });

    removeFileButton.addEventListener("click", () => {
        fileInput.value = "";
        fileName.textContent = "";
        filePreview.classList.add("d-none");
    });
});