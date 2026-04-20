
function loadPage(url) {

    console.log("➡ Loading page:", url);

    const section = document.getElementById("section");

    if (!section) {
        console.error("❌ #section not found in DOM");
        return;
    }

    // Show loading state
    section.innerHTML = `
        <div style="padding:10px;">
            ⏳ Loading...
        </div>
    `;

    fetch(url, {
        method: "GET",
        headers: {
            "X-Requested-With": "XMLHttpRequest"
        }
    })
    .then(res => {

        console.log("📡 Response status:", res.status);

        if (!res.ok) {
            throw new Error("HTTP error " + res.status);
        }

        return res.text();
    })
    .then(html => {

        console.log("📄 HTML received (preview):");
        console.log(html.substring(0, 200));

        // Direct injection (simple + stable)
        section.innerHTML = html;

        console.log("✅ Page loaded successfully into #section");
    })
    .catch(err => {

        console.error("❌ Load error:", err);

        section.innerHTML = `
            <div style="color:red; padding:10px;">
                ❌ Failed to load page
            </div>
        `;
    });
}

document.addEventListener("click", function (e) {

    const el = e.target.closest(".load-page");

    if (!el) return;

    e.preventDefault();

    const url = el.getAttribute("data-url");

    if (!url) {
        console.error("❌ No URL found in menu");
        return;
    }

    loadPage(url);
});