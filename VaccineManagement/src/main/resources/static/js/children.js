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

// Lấy danh sách trẻ em
async function loadChildren() {
    try {
        const response = await fetchWithAuth('http://localhost:8080/user/children');
        const childrenListElement = document.getElementById('children-list');
        
        if (response.ok) {
            const children = await response.json();
            
            if (children.length === 0) {
                childrenListElement.innerHTML = '<p>Chưa có trẻ nào. Hãy thêm trẻ để bắt đầu.</p>';
                return;
            }
            
            let html = '';
            children.forEach(child => {
                html += `
                    <div class="card child-card">
                        <h3>${child.childName}</h3>
                        <p><strong>Giới tính:</strong> ${child.gender === 'Male' ? 'Nam' : child.gender === 'Female' ? 'Nữ' : 'Khác'}</p>
                        <p><strong>Ngày sinh:</strong> ${new Date(child.birthDay).toLocaleDateString()}</p>
                        <div class="card-actions">
                            <button class="btn btn-small edit-child" data-id="${child.childId}">Sửa</button>
                            <button class="btn btn-small delete-child" data-id="${child.childId}">Xóa</button>
                        </div>
                    </div>
                `;
            });
            
            childrenListElement.innerHTML = html;
            
            // Thêm event listeners cho các nút
            document.querySelectorAll('.edit-child').forEach(button => {
                button.addEventListener('click', function() {
                    const childId = this.getAttribute('data-id');
                    openEditChildModal(childId, children);
                });
            });
            
            document.querySelectorAll('.delete-child').forEach(button => {
                button.addEventListener('click', function() {
                    const childId = this.getAttribute('data-id');
                    confirmDeleteChild(childId);
                });
            });
        } else {
            childrenListElement.innerHTML = '<p>Không thể tải danh sách trẻ.</p>';
        }
    } catch (error) {
        console.error("Failed to load children:", error);
        document.getElementById('children-list').innerHTML = '<p>Không thể tải danh sách trẻ.</p>';
    }
}

// Mở modal thêm trẻ
function openAddChildModal() {
    document.getElementById('modal-title').textContent = 'Thêm trẻ';
    document.getElementById('child-form').reset();
    document.getElementById('child-id').value = '';
    
    const modal = document.getElementById('child-modal');
    modal.style.display = 'block';
}

// Mở modal sửa thông tin trẻ
function openEditChildModal(childId, children) {
    const child = children.find(c => c.childId == childId);
    if (!child) return;
    
    document.getElementById('modal-title').textContent = 'Sửa thông tin trẻ';
    document.getElementById('child-id').value = childId;
    document.getElementById('child-name').value = child.childName;
    document.getElementById('child-gender').value = child.gender;
    
    // Format date to YYYY-MM-DD for input
    const birthDate = new Date(child.birthDay);
    const year = birthDate.getFullYear();
    const month = String(birthDate.getMonth() + 1).padStart(2, '0');
    const day = String(birthDate.getDate()).padStart(2, '0');
    document.getElementById('child-birthdate').value = `${year}-${month}-${day}`;
    
    const modal = document.getElementById('child-modal');
    modal.style.display = 'block';
}

// Xác nhận xóa trẻ
function confirmDeleteChild(childId) {
    if (confirm('Bạn có chắc chắn muốn xóa thông tin này?')) {
        deleteChild(childId);
    }
}

// Thêm trẻ mới
async function addChild(childData) {
    try {
        const response = await fetchWithAuth('http://localhost:8080/user/children/add', {
            method: 'POST',
            body: JSON.stringify(childData)
        });
        
        if (response.ok) {
            alert('Thêm trẻ thành công!');
            document.getElementById('child-modal').style.display = 'none';
            loadChildren(); // Tải lại danh sách
        } else {
            const errorText = await response.text();
            alert('Thêm trẻ thất bại: ' + errorText);
        }
    } catch (error) {
        console.error("Failed to add child:", error);
        alert('Lỗi khi thêm trẻ!');
    }
}

// Cập nhật thông tin trẻ
async function updateChild(childId, childData) {
    try {
        // Build URL with parameters
        const url = new URL('http://localhost:8080/user/children/update/' + childId);
        url.searchParams.append('childName', childData.childName);
        url.searchParams.append('gender', childData.gender);
        url.searchParams.append('birthDay', childData.birthDay);
        
        const response = await fetchWithAuth(url.toString(), {
            method: 'PUT'
        });
        
        if (response.ok) {
            alert('Cập nhật thông tin trẻ thành công!');
            document.getElementById('child-modal').style.display = 'none';
            loadChildren(); // Tải lại danh sách
        } else {
            const errorText = await response.text();
            alert('Cập nhật thông tin trẻ thất bại: ' + errorText);
        }
    } catch (error) {
        console.error("Failed to update child:", error);
        alert('Lỗi khi cập nhật thông tin trẻ!');
    }
}

// Xóa thông tin trẻ
async function deleteChild(childId) {
    try {
        const response = await fetchWithAuth(`http://localhost:8080/user/children/delete/${childId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            alert('Xóa thông tin trẻ thành công!');
            loadChildren(); // Tải lại danh sách
        } else {
            const errorText = await response.text();
            alert('Xóa thông tin trẻ thất bại: ' + errorText);
        }
    } catch (error) {
        console.error("Failed to delete child:", error);
        alert('Lỗi khi xóa thông tin trẻ!');
    }
}

// Khởi tạo trang Children
async function initChildrenPage() {
    await loadChildren();
    
    // Set up event handlers
    const addChildBtn = document.getElementById('add-child-btn');
    if (addChildBtn) {
        addChildBtn.addEventListener('click', openAddChildModal);
    }
    
    const closeBtn = document.querySelector('.close-btn');
    if (closeBtn) {
        closeBtn.addEventListener('click', function() {
            document.getElementById('child-modal').style.display = 'none';
        });
    }
    
    const childForm = document.getElementById('child-form');
    if (childForm) {
        childForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const childId = document.getElementById('child-id').value;
            const childName = document.getElementById('child-name').value;
            const gender = document.getElementById('child-gender').value;
            const birthDay = document.getElementById('child-birthdate').value;
            
            const childData = {
                childName: childName,
                gender: gender,
                birthDay: birthDay
            };
            
            if (childId) {
                // Update existing child
                updateChild(childId, childData);
            } else {
                // Add new child
                addChild(childData);
            }
        });
    }
    
    // Close modal when clicking outside it
    window.addEventListener('click', function(e) {
        const modal = document.getElementById('child-modal');
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });
}

// Khi trang load xong
document.addEventListener('DOMContentLoaded', function() {
    initChildrenPage();
});