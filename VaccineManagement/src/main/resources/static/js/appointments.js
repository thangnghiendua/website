// API requests với token
async function fetchWithAuth(url, options = {}) {
  const token = localStorage.getItem("token");

  if (!token) {
    window.location.href = "index.html";
    return;
  }

  const defaultOptions = {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  };

  const mergedOptions = {
    ...defaultOptions,
    ...options,
    headers: { ...defaultOptions.headers, ...options.headers },
  };

  try {
    const response = await fetch(url, mergedOptions);

    if (response.status === 401) {
      // Token không hợp lệ hoặc hết hạn
      localStorage.removeItem("token");
      window.location.href = "index.html";
      return;
    }

    return response;
  } catch (error) {
    console.error("API request failed:", error);
    throw error;
  }
}

// Lấy lịch sử lịch hẹn
async function loadAppointments() {
  try {
    const response = await fetchWithAuth("http://localhost:8080/user/history");
    const appointmentsListElement =
      document.getElementById("appointments-list");

    if (response.ok) {
      const appointments = await response.json();

      if (appointments.length === 0) {
        appointmentsListElement.innerHTML = "<p>Không có lịch hẹn nào.</p>";
        return;
      }

      // Filter appointments based on active tab
      const activeTab = document
        .querySelector(".tab-btn.active")
        .getAttribute("data-tab");
      let filteredAppointments = appointments;

      if (activeTab === "upcoming") {
        filteredAppointments = appointments.filter(
          (a) =>
            a.appointmentStatus === "Pending" ||
            a.appointmentStatus === "Confirmed"
        );
      } else if (activeTab === "history") {
        filteredAppointments = appointments.filter(
          (a) =>
            a.appointmentStatus === "Complete" ||
            a.appointmentStatus === "Canceled"
        );
      }

      if (filteredAppointments.length === 0) {
        appointmentsListElement.innerHTML =
          "<p>Không có lịch hẹn nào trong danh mục này.</p>";
        return;
      }

      // Sort appointments by date (newest first)
      filteredAppointments.sort(
        (a, b) => new Date(b.appointmentDate) - new Date(a.appointmentDate)
      );

      let html = "";
      filteredAppointments.forEach((appointment) => {
        const statusMap = {
          Pending: "Đang chờ",
          Confirmed: "Đã xác nhận",
          Complete: "Hoàn thành",
          Canceled: "Đã hủy",
        };

        html += `
                    <div class="list-item appointment-item">
                        <div class="appointment-header">
                            <h3>${appointment.vaccine.vaccineName}</h3>
                            <span class="status status-${appointment.appointmentStatus.toLowerCase()}">${
          statusMap[appointment.appointmentStatus]
        }</span>
                        </div>
                        <p><strong>Trẻ:</strong> ${
                          appointment.child.childName
                        }</p>
                        <p><strong>Bác sĩ:</strong> ${
                          appointment.doctor.doctorName
                        }</p>
                        <p><strong>Ngày hẹn:</strong> ${new Date(
                          appointment.appointmentDate
                        ).toLocaleString()}</p>
                        <p><strong>Địa điểm:</strong> ${
                          appointment.appointmentAddress
                        }, ${appointment.roomNumber}</p>
                        
                        <div class="appointment-actions">
                            ${
                              appointment.appointmentStatus === "Pending" ||
                              appointment.appointmentStatus === "Confirmed"
                                ? `<button class="btn btn-small cancel-appointment" data-id="${appointment.appointmentId}">Hủy lịch hẹn</button>`
                                : ""
                            }
                        </div>
                    </div>
                `;
      });

      appointmentsListElement.innerHTML = html;

      // Thêm event listeners cho các nút hủy
      document.querySelectorAll(".cancel-appointment").forEach((button) => {
        button.addEventListener("click", function () {
          const appointmentId = this.getAttribute("data-id");
          confirmCancelAppointment(appointmentId);
        });
      });
    } else {
      appointmentsListElement.innerHTML = "<p>Không thể tải lịch hẹn.</p>";
    }
  } catch (error) {
    console.error("Failed to load appointments:", error);
    document.getElementById("appointments-list").innerHTML =
      "<p>Không thể tải lịch hẹn.</p>";
  }
}

// Xác nhận hủy lịch hẹn
function confirmCancelAppointment(appointmentId) {
  if (confirm("Bạn có chắc chắn muốn hủy lịch hẹn này?")) {
    cancelAppointment(appointmentId);
  }
}

// Hủy lịch hẹn
async function cancelAppointment(appointmentId) {
  try {
    const response = await fetchWithAuth(
      `http://localhost:8080/user/cancel/${appointmentId}`,
      {
        method: "POST",
      }
    );

    if (response.ok) {
      alert("Hủy lịch hẹn thành công!");
      loadAppointments(); // Tải lại danh sách
    } else {
      const errorText = await response.text();
      alert("Hủy lịch hẹn thất bại: " + errorText);
    }
  } catch (error) {
    console.error("Failed to cancel appointment:", error);
    alert("Lỗi khi hủy lịch hẹn!");
  }
}

// Khởi tạo trang Appointments
async function initAppointmentsPage() {
  await loadAppointments();

  // Set up event handlers for tabs
  document.querySelectorAll(".tab-btn").forEach((button) => {
    button.addEventListener("click", function () {
      // Remove active class from all tabs
      document.querySelectorAll(".tab-btn").forEach((btn) => {
        btn.classList.remove("active");
      });

      // Add active class to clicked tab
      this.classList.add("active");

      // Reload appointments for the selected tab
      loadAppointments();
    });
  });
}

// Khi trang load xong
document.addEventListener("DOMContentLoaded", function () {
  initAppointmentsPage();
});
