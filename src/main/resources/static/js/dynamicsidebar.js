
/*
	This is the working java script for the menu close and open keeping the active menu highlightd
	Created dt  01/04/2026
*/

/* This java scripts highlight only the active child */

/*
document.addEventListener("DOMContentLoaded", function () {
    const menuItems = document.querySelectorAll(".menu-item");
    const currentUrl = window.location.pathname; // current page path

    menuItems.forEach(item => {
        const parentLink = item.querySelector(".menu-link");
        const childLinks = item.querySelectorAll(".submenu li a");

        let isActive = false;

        // Highlight child if URL matches
        childLinks.forEach(link => {
            if (link.getAttribute("href") === currentUrl) {
                link.classList.add("active");
                isActive = true;
            }
        });

        // Highlight parent if URL matches (single link menu)
        if (parentLink && parentLink.getAttribute("href") === currentUrl) {
            parentLink.classList.add("active");
            isActive = true;
        }

        // Open parent if any child is active
        if (isActive) {
            item.classList.add("open");
        }

        // Optional: Toggle submenu on parent click
        if (parentLink && item.querySelector(".submenu")) {
            parentLink.addEventListener("click", () => {
                const isOpen = item.classList.contains("open");
                menuItems.forEach(i => i.classList.remove("open")); // close others
                if (!isOpen) item.classList.add("open");
            });
        }
    });
});

*/

/* This java code will highlight both parent menu and child menu which is active for the moment */

/*
document.addEventListener("DOMContentLoaded", function () {
    const menuItems = document.querySelectorAll(".menu-item");
    const currentUrl = window.location.pathname; // current page

    // 1️⃣ Find the active link based on URL
    let activeLink = null;
    menuItems.forEach(parent => {
        const childLinks = parent.querySelectorAll(".submenu li a");
        childLinks.forEach(link => {
            if (link.getAttribute("href") === currentUrl) {
                activeLink = link;
            }
        });

        const parentLink = parent.querySelector(".menu-link.single-link");
        if (parentLink && parentLink.getAttribute("href") === currentUrl) {
            activeLink = parentLink;
        }
    });

    // 2️⃣ Highlight active link and open parent hierarchy
    if (activeLink) {
        activeLink.classList.add("active");

        let parentMenu = activeLink.closest(".menu-item");
        while (parentMenu) {
            parentMenu.classList.add("open", "active"); // open & highlight parent
            parentMenu = parentMenu.parentElement.closest(".menu-item");
        }
    }

    // 3️⃣ Parent menu toggle (accordion)
    menuItems.forEach(item => {
        const parentLink = item.querySelector(".menu-link");
        if (parentLink && item.querySelector(".submenu")) {
            parentLink.addEventListener("click", () => {
                const isOpen = item.classList.contains("open");
                
                // Close all other parents
                menuItems.forEach(i => i.classList.remove("open"));

                // Toggle clicked parent
                if (!isOpen) item.classList.add("open");
            });
        }
    });
});

*/

document.addEventListener("DOMContentLoaded", function () {

    const menuItems = document.querySelectorAll(".menu-item");
    const currentUrl = window.location.pathname;

    let activeLink = null;



    // 🔹 1. Find active link (child or single menu)
    menuItems.forEach(item => {

        // Check child links
        const childLinks = item.querySelectorAll(":scope .submenu li a");
        childLinks.forEach(link => {
            if (link.getAttribute("href") === currentUrl) {
                activeLink = link;
            }
        });

        // Check single (no submenu)
        const singleLink = item.querySelector(":scope > .menu-link.single-link");
        if (singleLink && singleLink.getAttribute("href") === currentUrl) {
            activeLink = singleLink;
        }
    });

    // 🔹 2. Highlight and open parent hierarchy
    if (activeLink) {
        activeLink.classList.add("active");

        let parentMenu = activeLink.closest(".menu-item");
        while (parentMenu) {
            parentMenu.classList.add("open", "active");
            parentMenu = parentMenu.parentElement.closest(".menu-item");
        }
    }

    // 🔹 3. Accordion toggle (FIXED)
    menuItems.forEach(item => {

        const parentLink = item.querySelector(":scope > .menu-link");

        // Only apply to menus that have submenu
        if (parentLink && item.querySelector(":scope > .submenu")) {

            parentLink.addEventListener("click", function (e) {
                e.preventDefault();

                const isOpen = item.classList.contains("open");

                // 👉 Close ONLY siblings (same level)
                const siblings = item.parentElement.children;

                Array.from(siblings).forEach(sibling => {
                    if (sibling !== item) {
                        sibling.classList.remove("open");
                    }
                });

                // 👉 Toggle current menu
                if (!isOpen) {
                    item.classList.add("open");
                } else {
                    item.classList.remove("open");
                }
            });
        }
    });

});