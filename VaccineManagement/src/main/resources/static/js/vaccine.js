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

// Lấy danh sách vaccine
async function loadVaccines(packageType = '') {
    try {
        let url = 'http://localhost:8080/user/vaccines';
        if (packageType) {
            url = `http://localhost:8080/user/by-package-type?packageType=${packageType}`;
        }
        
        const response = await fetchWithAuth(url);
        const vaccinesListElement = document.getElementById('vaccines-list');
        
        if (response.ok) {
            const vaccines = await response.json();
            
            if (vaccines.length === 0) {
                vaccinesListElement.innerHTML = '<p>Không có vaccine nào phù hợp.</p>';
                return;
            }
            
            let html = '';
            vaccines.forEach(vaccine => {
                const packageTypeMap = {
                    'Single_Package': 'Gói đơn',
                    'Combo': 'Gói combo',
                    'Personalization': 'Gói cá nhân hóa'
                };
                
                html += `
                    <div class="card vaccine-card">
                        <h3>${vaccine.vaccineName}</h3>
                        <p><strong>Loại gói:</strong> ${packageTypeMap[vaccine.packageType] || vaccine.packageType}</p>
                        <p><strong>Giá:</strong> ${vaccine.price.toLocaleString()} VND</p>
                        <p>${vaccine.description}</p>
                        <button class="btn btn-primary book-vaccine" data-id="${vaccine.vaccineId}">Đặt lịch</button>
                    </div>
                `;
            });
            
            vaccinesListElement.innerHTML = html;
            
            // Thêm event listeners cho các nút đặt lịch
            document.querySelectorAll('.book-vaccine').forEach(button => {
                button.addEventListener('click', function() {
                    const vaccineId = this.getAttribute('data-id');
                    openBookingModal(vaccineId);
                });
            });
        } else {
            vaccinesListElement.innerHTML = '<p>Không thể tải danh sách vaccine.</p>';
        }
    } catch (error) {
        console.error("Failed to load vaccines:", error);
        document.getElementById('vaccines-list').innerHTML = '<p>Không thể tải danh sách vaccine.</p>';
    }
}

// Lấy danh sách trẻ em cho dropdown
async function loadChildrenDropdown() {
    try {
        const response = await fetchWithAuth('http://localhost:8080/user/children');
        const childSelectElement = document.getElementById('child-select');
        
        if (response.ok) {
            const children = await response.json();
            
            if (children.length === 0) {
                alert('Bạn chưa có trẻ nào. Hãy thêm trẻ trước khi đặt lịch tiêm.');
                document.getElementById('booking-modal').style.display = 'none';
                return;
            }
            
            // Clear previous options
            childSelectElement.innerHTML = '<option value="">Chọn trẻ</option>';
            
            // Add new options
            children.forEach(child => {
                const option = document.createElement('option');
                option.value = child.childId;
                option.textContent = `${child.childName} (${new Date(child.birthDay).toLocaleDateString()})`;
                childSelectElement.appendChild(option);
            });
        } else {
            alert('Không thể tải danh sách trẻ.');
            document.getElementById('booking-modal').style.display = 'none';
        }
    } catch (error) {
        console.error("Failed to load children dropdown:", error);
        alert('Lỗi khi tải danh sách trẻ.');
        document.getElementById('booking-modal').style.display = 'none';
    }
}

// Mở modal đặt lịch
function openBookingModal(vaccineId) {
    document.getElementById('vaccine-id').value = vaccineId;
    loadChildrenDropdown();
    document.getElementById('booking-modal').style.display = 'block';
}

// Đặt lịch tiêm
async function bookAppointment(childId, vaccineId) {
    try {
        const response = await fetchWithAuth('http://localhost:8080/user/book', {
            method: 'POST',
            body: JSON.stringify({
                childId: childId,
                vaccineId: vaccineId
            })
        });
        
        if (response.ok) {
            alert('Đặt lịch thành công! Vui lòng đợi xác nhận từ bác sĩ.');
            document.getElementById('booking-modal').style.display = 'none';
        } else {
            const errorText = await response.text();
            alert('Đặt lịch thất bại: ' + errorText);
        }
    } catch (error) {
        console.error("Failed to book appointment:", error);
        alert('Lỗi khi đặt lịch!');
    }
}

// Khởi tạo trang Vaccines
async function initVaccinesPage() {
    await loadVaccines();
    
    // Set up event handlers
    const packageTypeFilter = document.getElementById('package-type-filter');
    if (packageTypeFilter) {
        packageTypeFilter.addEventListener('change', function() {
            loadVaccines(this.value);
        });
    }
    
    const closeBtn = document.querySelector('.close-btn');
    if (closeBtn) {
        closeBtn.addEventListener('click', function() {
            document.getElementById('booking-modal').style.display = 'none';
        });
    }
    
    const bookingForm = document.getElementById('booking-form');
    if (bookingForm) {
        bookingForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const vaccineId = document.getElementById('vaccine-id').value;
            const childId = document.getElementById('child-select').value;
            
            if (!childId) {
                alert('Vui lòng chọn trẻ.');
                return;
            }
            
            bookAppointment(childId, vaccineId);
        });
    }
    
    // Close modal when clicking outside it
    window.addEventListener('click', function(e) {
        const modal = document.getElementById('booking-modal');
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });
}

// Khi trang load xong
document.addEventListener('DOMContentLoaded', function() {
    initVaccinesPage();
});