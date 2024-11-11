document.addEventListener("DOMContentLoaded", function () {
    const confirmationForms = document.querySelectorAll("form[data-confirm]");

    confirmationForms.forEach(form => {
        form.addEventListener("submit", function (e) {
            const confirmMessage = this.getAttribute("data-confirm");
            if (!confirm(confirmMessage)) {
                e.preventDefault(); 
            }
        });
    });
});
