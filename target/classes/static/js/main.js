// main.js - Primary JavaScript file for the Vaccination Management System

document.addEventListener("DOMContentLoaded", function () {
  // Mobile menu toggle
  const mobileMenuToggle = document.querySelector(".mobile-menu-toggle");
  const mainNav = document.querySelector(".main-nav");

  if (mobileMenuToggle && mainNav) {
    mobileMenuToggle.addEventListener("click", function () {
      mainNav.classList.toggle("active");
    });
  }

  // Dropdown toggle for mobile
  const dropdownToggles = document.querySelectorAll(".dropdown-toggle");

  dropdownToggles.forEach((toggle) => {
    toggle.addEventListener("click", function (e) {
      if (window.innerWidth < 992) {
        e.preventDefault();
        this.parentElement.classList.toggle("active");
      }
    });
  });

  // Service tabs
  const tabButtons = document.querySelectorAll(".tab-btn");

  if (tabButtons.length > 0) {
    tabButtons.forEach((button) => {
      button.addEventListener("click", function () {
        // Remove active class from all buttons and panes
        tabButtons.forEach((btn) => btn.classList.remove("active"));
        document
          .querySelectorAll(".tab-pane")
          .forEach((pane) => pane.classList.remove("active"));

        // Add active class to current button and corresponding pane
        this.classList.add("active");
        const tabId = this.getAttribute("data-tab");
        document.getElementById(tabId).classList.add("active");
      });
    });
  }

  // Testimonial slider
  const testimonialSlider = document.querySelector(".testimonial-slider");

  if (testimonialSlider) {
    let isDown = false;
    let startX;
    let scrollLeft;

    testimonialSlider.addEventListener("mousedown", (e) => {
      isDown = true;
      startX = e.pageX - testimonialSlider.offsetLeft;
      scrollLeft = testimonialSlider.scrollLeft;
    });

    testimonialSlider.addEventListener("mouseleave", () => {
      isDown = false;
    });

    testimonialSlider.addEventListener("mouseup", () => {
      isDown = false;
    });

    testimonialSlider.addEventListener("mousemove", (e) => {
      if (!isDown) return;
      e.preventDefault();
      const x = e.pageX - testimonialSlider.offsetLeft;
      const walk = (x - startX) * 2;
      testimonialSlider.scrollLeft = scrollLeft - walk;
    });
  }

  // Form validation
  const forms = document.querySelectorAll(".needs-validation");

  if (forms.length > 0) {
    Array.from(forms).forEach((form) => {
      form.addEventListener(
        "submit",
        (event) => {
          if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
          }

          form.classList.add("was-validated");
        },
        false
      );
    });
  }

  // Password show/hide toggle
  const passwordToggles = document.querySelectorAll(".password-toggle");

  if (passwordToggles.length > 0) {
    passwordToggles.forEach((toggle) => {
      toggle.addEventListener("click", function () {
        const input = this.previousElementSibling;
        const type =
          input.getAttribute("type") === "password" ? "text" : "password";
        input.setAttribute("type", type);
        this.querySelector("i").classList.toggle("fa-eye");
        this.querySelector("i").classList.toggle("fa-eye-slash");
      });
    });
  }

  // Initialize date pickers
  const datePickers = document.querySelectorAll(".date-picker");

  if (datePickers.length > 0 && typeof flatpickr !== "undefined") {
    datePickers.forEach((picker) => {
      flatpickr(picker, {
        dateFormat: "d/m/Y",
        locale: "vi",
        disableMobile: true,
      });
    });
  }

  // Initialize time pickers
  const timePickers = document.querySelectorAll(".time-picker");

  if (timePickers.length > 0 && typeof flatpickr !== "undefined") {
    timePickers.forEach((picker) => {
      flatpickr(picker, {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i",
        time_24hr: true,
        minuteIncrement: 30,
        locale: "vi",
        disableMobile: true,
      });
    });
  }

  // Initialize tooltips
  const tooltipTriggerList = document.querySelectorAll(
    '[data-bs-toggle="tooltip"]'
  );

  if (tooltipTriggerList.length > 0 && typeof bootstrap !== "undefined") {
    const tooltipList = [...tooltipTriggerList].map(
      (tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl)
    );
  }

  // File input preview
  const fileInputs = document.querySelectorAll(".custom-file-input");

  if (fileInputs.length > 0) {
    fileInputs.forEach((input) => {
      input.addEventListener("change", function () {
        const fileLabel = this.nextElementSibling;
        const fileName = this.files[0]?.name || "Chọn file";
        fileLabel.textContent = fileName;

        // Image preview
        const previewContainer =
          this.closest(".form-group").querySelector(".image-preview");
        if (previewContainer && this.files[0]) {
          const reader = new FileReader();

          reader.onload = function (e) {
            previewContainer.innerHTML = `<img src="${e.target.result}" alt="Preview" class="img-fluid">`;
          };

          reader.readAsDataURL(this.files[0]);
        }
      });
    });
  }

  // Dynamic form fields
  const addFieldButtons = document.querySelectorAll(".add-field-btn");

  if (addFieldButtons.length > 0) {
    addFieldButtons.forEach((button) => {
      button.addEventListener("click", function () {
        const container =
          this.closest(".form-group").querySelector(".dynamic-fields");
        const fieldTemplate = this.getAttribute("data-template");
        const fieldCount = container.querySelectorAll(".dynamic-field").length;

        let newField = document.createElement("div");
        newField.className = "dynamic-field";
        newField.innerHTML = document
          .getElementById(fieldTemplate)
          .innerHTML.replace(/\{index\}/g, fieldCount);

        container.appendChild(newField);

        // Initialize delete buttons
        const deleteButtons = newField.querySelectorAll(".delete-field-btn");
        deleteButtons.forEach((btn) => {
          btn.addEventListener("click", function () {
            this.closest(".dynamic-field").remove();
          });
        });
      });
    });
  }

  // Alert auto-dismiss
  const alertsAutoDismiss = document.querySelectorAll(
    ".alert-dismissible.auto-dismiss"
  );

  if (alertsAutoDismiss.length > 0) {
    alertsAutoDismiss.forEach((alert) => {
      setTimeout(() => {
        alert.classList.add("fade-out");
        setTimeout(() => {
          alert.remove();
        }, 500);
      }, 5000);
    });
  }
});

// Helper function to format currency
function formatCurrency(amount) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount);
}

// Helper function to format date
function formatDate(dateString) {
  const options = { day: "2-digit", month: "2-digit", year: "numeric" };
  return new Date(dateString).toLocaleDateString("vi-VN", options);
}

// Helper function to show confirmation dialog
function confirmAction(message, callback) {
  if (confirm(message)) {
    callback();
  }
}
