(() => {
    const link = document.getElementById("download-link");
    const revealItems = document.querySelectorAll(".reveal");

    if (revealItems.length > 0) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    entry.target.classList.add("visible");
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.12 });

        revealItems.forEach((item) => observer.observe(item));
    }

    if (link) {
        link.addEventListener("click", () => {
            link.textContent = "Starting download...";
            setTimeout(() => {
                link.textContent = "Download for Windows";
            }, 1400);
        });
    }
})();
