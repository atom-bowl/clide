(() => {
    const link = document.getElementById("download-link");
    if (!link) return;

    link.addEventListener("click", () => {
        link.textContent = "Starting download...";
        setTimeout(() => {
            link.textContent = "Download for Windows";
        }, 1500);
    });
})();
