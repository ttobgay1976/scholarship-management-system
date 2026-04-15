
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



/*
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

*/

/** Todays changes */
/*

document.addEventListener("DOMContentLoaded", function () {
    const menuContainer = document.getElementById("menus");
    if (!menuContainer) return;

    const currentUrl = window.location.pathname;

    function clearMenuState() {
        menuContainer.querySelectorAll(".menu-item, .menu-link, .submenu-link").forEach(el => {
            el.classList.remove("open", "active");
        });
    }

    function applyActiveMenu() {
        clearMenuState();

        let activeLink = null;

        const menuItems = menuContainer.querySelectorAll(".menu-item");

        menuItems.forEach(item => {
            const childLinks = item.querySelectorAll(":scope .submenu li a");
            childLinks.forEach(link => {
                const href = link.getAttribute("href");
                if (href && currentUrl.startsWith(href)) {
                    activeLink = link;
                }
            });

            const singleLink = item.querySelector(":scope > .menu-link.single-link");
            if (singleLink) {
                const href = singleLink.getAttribute("href");
                if (href && currentUrl.startsWith(href)) {
                    activeLink = singleLink;
                }
            }
        });

        if (activeLink) {
            activeLink.classList.add("active");

            let parentMenu = activeLink.closest(".menu-item");
            while (parentMenu) {
                parentMenu.classList.add("open", "active");
                parentMenu = parentMenu.parentElement.closest(".menu-item");
            }
        }
    }

    menuContainer.addEventListener("click", function (e) {
        const parentLink = e.target.closest(".menu-item > .menu-link");
        if (!parentLink) return;

        const item = parentLink.closest(".menu-item");
        const submenu = item.querySelector(":scope > .submenu");

        if (submenu) {
            e.preventDefault();

            const isOpen = item.classList.contains("open");

            Array.from(item.parentElement.children).forEach(sibling => {
                if (sibling !== item) {
                    sibling.classList.remove("open");
                }
            });

            if (isOpen) {
                item.classList.remove("open");
            } else {
                item.classList.add("open");
            }
        }
    });

    applyActiveMenu();
});

*/

/**Added today */


document.addEventListener("DOMContentLoaded", function () {

    /**
     * ==============================
     * 1. LOAD PAGE INTO CONTENT AREA
     * ==============================
     */
    function attachLoadEvents() {

        document.querySelectorAll(".load-page").forEach(link => {

            link.removeEventListener("click", handleLoadClick);
            link.addEventListener("click", handleLoadClick);
        });
    }

    function handleLoadClick(e) {
        e.preventDefault();

        const url = this.getAttribute("data-url");
        if (!url) return;

        // loading indicator
        document.getElementById("section").innerHTML =
            "<h4>Loading...</h4>";

        fetch(url)
            .then(res => res.text())
            .then(html => {
                document.getElementById("section").innerHTML = html;

                // active child highlight
                document.querySelectorAll(".load-page")
                    .forEach(m => m.classList.remove("active"));

                this.classList.add("active");

                // 🔥 highlight parent also
                const parentMenu = this.closest(".menu-item").querySelector(".menu-link");
                if (parentMenu) {
                    document.querySelectorAll(".menu-link")
                        .forEach(m => m.classList.remove("active-parent"));

                    parentMenu.classList.add("active-parent");
                }

                // re-init events if new content has JS
                attachLoadEvents();
            })
            .catch(() => {
                document.getElementById("section").innerHTML =
                    "<h3>Failed to load page</h3>";
            });
    }


    /**
     * ==============================
     * 2. MENU EXPAND / COLLAPSE (ACCORDION FIXED)
     * ==============================
     */
    document.querySelectorAll(".menu-link").forEach(menu => {

        menu.addEventListener("click", function () {

            const submenu = this.nextElementSibling;

            // skip if no submenu (single menu)
            if (!submenu || !submenu.classList.contains("submenu")) return;

		const arrow = this.querySelector(".arrow");
            const isOpen = submenu.classList.contains("open");
            
            // 🔴 CLOSE ALL MENUS
            document.querySelectorAll(".submenu").forEach(sub => {
                sub.classList.remove("open");
            });

/** 
            document.querySelectorAll(".menu-link").forEach(m => {
                m.classList.remove("active-parent");
            });
            
*/
            document.querySelectorAll(".arrow").forEach(a => {
                a.classList.remove("rotated");
            });

            // TOGGLE CURRENT
		if (!isOpen) {
		    submenu.classList.add("open");
		    this.classList.add("active-parent");
		
		    if (arrow) {
		        arrow.classList.add("rotated");
		    }
		}

        });

    });


    /**
     * ==============================
     * INIT
     * ==============================
     */
    attachLoadEvents();

});