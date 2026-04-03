
document.addEventListener("DOMContentLoaded", function () {

    const menuLinks = document.querySelectorAll(".menu-link");

    menuLinks.forEach(link => {
        link.addEventListener("click", function () {

            const parentLi = this.parentElement;

            // Toggle open class
            parentLi.classList.toggle("open");

            // Optional: redirect if no submenu
            const submenu = parentLi.querySelector(".submenu");
            const url = this.getAttribute("data-url");

            if (!submenu && url) {
                window.location.href = url;
            }
        });
    });

});
