// API requests với token
async function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem('token');
    
    if (!token) {
        window.location.href = 'index.html';
        return;
    }
    
    const defaultOptions = {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };
    
    const mergedOptions = { ...defaultOptions, ...options, headers: { ...defaultOptions.headers, ...options.headers } };
    
    try {
        const response = await fetch(url, mergedOptions);
        
        if (response.status === 401) {
            // Token không hợp lệ hoặc hết hạn
            localStorage.removeItem('token');
            window.location.href = 'index.html';
            return;
        }
        
        return response;
    } catch (error) {
        console.error("API request failed:", error);
        throw error;
    }
}

// Lấy thông tin người dùng
async function loadUserInfo() {
    try {
        const response = await fetchWithAuth('http://localhost:8080/user/wallet-balance');
        
        if (response.ok) {
            const data = await response.text();
            
            // Cập nhật phần hiển thị số dư ví
            const balanceElement = document.getElementById('wallet-balance');
            if (balanceElement) balanceElement.textContent = data;
            
            const balanceAmountElement = document.getElementById('balance-amount');
            if (balanceAmountElement) balanceAmountElement.textContent = data;
        }
    } catch (error) {
        console.error("Failed to load user info:", error);
    }
}

// Lấy danh sách thông báo
async function loadNotifications() {
    try {
        const response = await fetchWithAuth('http://localhost:8080/user/notifications');
        const notificationsElement = document.getElementById('notifications-list');
        
        if (response.ok) {
            const notifications = await response.json();
            
            if (notifications.length === 0) {
                notificationsElement.innerHTML = '<p>Không có thông báo.</p>';
                return;
            }
            
            let html = '<ul class="notifications">';
            notifications.forEach(notification => {
                html += `
                    <li>
                        <p>${notification.message}</p>
                        <small>${new Date(notification.notificationDate).toLocaleString()}</small>
                    </li>
                `;
            });
            html += '</ul>';
            
            notificationsElement.innerHTML = html;
        } else {
            notificationsElement.innerHTML = '<p>Không thể tải thông báo.</p>';
        }
    } catch (error) {
        console.error("Failed to load notifications:", error);
        document.getElementById('notifications-list').innerHTML = '<p>Không thể tải thông báo.</p>';
    }
}

// Lấy lịch hẹn sắp tới
async function loadUpcomingAppointments() {
    try {
        const response = await fetchWithAuth('http://localhost:8080/user/history');
        const appointmentsElement = document.getElementById('upcoming-appointments');
        
        if (response.ok) {
            const appointments = await response.json();
            
            // Lọc lịch hẹn có trạng thái Confirmed
            const upcomingAppointments = appointments.filter(a => a.appointmentStatus === 'Confirmed');
            
            if (upcomingAppointments.length === 0) {
                appointmentsElement.innerHTML = '<p>Không có lịch hẹn sắp tới.</p>';
                return;
            }
            
            let html = '<ul class="appointments">';
            upcomingAppointments.forEach(appointment => {
                html += `
                    <li>
                        <p>Trẻ: ${appointment.child.childName}</p>
                        <p>Vaccine: ${appointment.vaccine.vaccineName}</p>
                        <p>Ngày hẹn: ${new Date(appointment.appointmentDate).toLocaleString()}</p>
                        <p>Địa điểm: ${appointment.appointmentAddress}, ${appointment.roomNumber}</p>
                    </li>
                `;
            });
            html += '</ul>';
            
            appointmentsElement.innerHTML = html;
        } else {
            appointmentsElement.innerHTML = '<p>Không thể tải lịch hẹn.</p>';
        }
    } catch (error) {
        console.error("Failed to load appointments:", error);
        document.getElementById('upcoming-appointments').innerHTML = '<p>Không thể tải lịch hẹn.</p>';
    }
}

// Lấy danh sách trẻ em
async function loadChildren() {
    try {
        const response = await fetchWithAuth('http://localhost:8080/user/children');
        const childrenElement = document.getElementById('children-list');
        
        if (response.ok) {
            const children = await response.json();
            
            if (children.length === 0) {
                childrenElement.innerHTML = '<p>Chưa có trẻ nào. Hãy thêm trẻ để bắt đầu.</p>';
                return;
            }
            
            let html = '<ul class="children">';
            children.forEach(child => {
                html += `
                    <li>
                        <p>${child.childName}</p>
                        <p>Giới tính: ${child.gender === 'Male' ? 'Nam' : child.gender === 'Female' ? 'Nữ' : 'Khác'}</p>
                        <p>Ngày sinh: ${new Date(child.birthDay).toLocaleDateString()}</p>
                    </li>
                `;
            });
            html += '</ul>';
            
            childrenElement.innerHTML = html;
        } else {
            childrenElement.innerHTML = '<p>Không thể tải danh sách trẻ.</p>';
        }
    } catch (error) {
        console.error("Failed to load children:", error);
        document.getElementById('children-list').innerHTML = '<p>Không thể tải danh sách trẻ.</p>';
    }
}

// Khởi tạo trang Dashboard
async function initDashboard() {
    await loadUserInfo();
    await loadNotifications();
    await loadUpcomingAppointments();
    await loadChildren();
    
    // Set up event handlers
    const addChildBtn = document.getElementById('add-child-btn');
    if (addChildBtn) {
        addChildBtn.addEventListener('click', function() {
            window.location.href = 'children.html';
        });
    }
    
    const rechargeBtn = document.getElementById('recharge-btn');
    if (rechargeBtn) {
        rechargeBtn.addEventListener('click', function() {
            const amount = prompt("Nhập số tiền nạp:", "100000");
            if (amount) {
                rechargeWallet(amount);
            }
        });
    }
    
    const transferBtn = document.getElementById('transfer-btn');
    if (transferBtn) {
        transferBtn.addEventListener('click', function() {
            const amount = prompt("Nhập số tiền chuyển:", "50000");
            if (amount) {
                transferMoney(amount);
            }
        });
    }
}

// Nạp tiền vào ví
async function rechargeWallet(amount) {
    try {
        const response = await fetchWithAuth(`http://localhost:8080/user/recharge?amount=${amount}`, {
            method: 'POST'
        });
        
        if (response.ok) {
            alert("Nạp tiền thành công!");
            loadUserInfo(); // Cập nhật lại thông tin số dư
        } else {
            const errorText = await response.text();
            alert("Nạp tiền thất bại: " + errorText);
        }
    } catch (error) {
        console.error("Failed to recharge wallet:", error);
        alert("Lỗi khi nạp tiền!");
    }
}

// Chuyển tiền
async function transferMoney(amount) {
    try {
        const response = await fetchWithAuth(`http://localhost:8080/user/transfer?amount=${amount}`, {
            method: 'POST'
        });
        
        if (response.ok) {
            alert("Chuyển tiền thành công!");
            loadUserInfo(); // Cập nhật lại thông tin số dư
        } else {
            const errorText = await response.text();
            alert("Chuyển tiền thất bại: " + errorText);
        }
    } catch (error) {
        console.error("Failed to transfer money:", error);
        alert("Lỗi khi chuyển tiền!");
    }
}

// Khi trang load xong
document.addEventListener('DOMContentLoaded', function() {
    initDashboard();
});