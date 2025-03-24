document.addEventListener("DOMContentLoaded", () => {
    const wrapper = document.querySelector('.wrapper');
    const loginLink = document.querySelector('.login-link');
    const registerLink = document.querySelector('.register-link');
    const btnPopup = document.querySelector('.btnlogin-popup');
    const iconClose = document.querySelector('.icon-close');

    if (registerLink) {
        registerLink.addEventListener('click', () => {
            wrapper.classList.add('active');
        });
    }

    if (loginLink) {
        loginLink.addEventListener('click', () => {
            wrapper.classList.remove('active');
        });
    }

    if (btnPopup) {
        btnPopup.addEventListener('click', () => {
            wrapper.classList.add('active-popup');
        });
    }

    if (iconClose) {
        iconClose.addEventListener('click', () => {
            wrapper.classList.remove('active-popup');
            // Reset the form state when closing
            wrapper.classList.remove('active');
        });
    }

    // Close modal when clicking outside
    window.addEventListener('click', function(e) {
        if (e.target === wrapper) {
            wrapper.classList.remove('active-popup');
            // Reset the form state when clicking outside
            wrapper.classList.remove('active');
        }
    });
});
