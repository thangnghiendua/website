// Kiểm tra xem người dùng đã đăng nhập chưa
function checkAuth() {
    const token = localStorage.getItem('token');
    const currentPage = window.location.pathname.split('/').pop();
    
    // Nếu không có token và không ở trang đăng nhập/đăng ký
    if (!token && currentPage !== 'index.html' && currentPage !== 'register.html' && currentPage !== '') {
        window.location.href = 'index.html';
        return false;
    }
    
    // Nếu có token và đang ở trang đăng nhập/đăng ký
    if (token && (currentPage === 'index.html' || currentPage === 'register.html' || currentPage === '')) {
        window.location.href = 'dashboard.html';
        return false;
    }
    
    return true;
}

// Đăng nhập
async function performLogin(email, password) {
    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Đăng nhập thất bại');
        }
        
        const data = await response.json();
        localStorage.setItem('token', data.token);
        window.location.href = 'dashboard.html';
    } catch (error) {
        const errorElement = document.getElementById('error-message');
        errorElement.style.display = 'block';
        errorElement.textContent = error.message;
    }
}

// Đăng ký
async function performRegister(userData) {
    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Đăng ký thất bại');
        }
        
        // Đăng ký thành công, chuyển sang trang đăng nhập
        window.location.href = 'index.html?registered=true';
    } catch (error) {
        const errorElement = document.getElementById('error-message');
        errorElement.style.display = 'block';
        errorElement.textContent = error.message;
    }
}

// Đăng xuất
function logout() {
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}

// Xử lý form đăng nhập
if (document.getElementById('login-form')) {
    document.getElementById('login-form').addEventListener('submit', function(e) {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        performLogin(email, password);
    });
    
    // Hiển thị thông báo đăng ký thành công nếu có
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('registered') === 'true') {
        const messageEl = document.getElementById('error-message');
        messageEl.style.color = 'green';
        messageEl.style.display = 'block';
        messageEl.textContent = 'Đăng ký thành công! Vui lòng đăng nhập.';
    }
}

// Xử lý form đăng ký
if (document.getElementById('register-form')) {
    document.getElementById('register-form').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const userName = document.getElementById('userName').value;
        const email = document.getElementById('email').value;
        const gender = document.getElementById('gender').value;
        const birthDate = document.getElementById('birthDate').value;
        const password = document.getElementById('userPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        
        if (password !== confirmPassword) {
            const errorElement = document.getElementById('error-message');
            errorElement.style.display = 'block';
            errorElement.textContent = 'Mật khẩu không khớp!';
            return;
        }
        
        const userData = {
            userName: userName,
            email: email,
            gender: gender,
            birthDate: birthDate,
            userPassword: password
        };
        
        performRegister(userData);
    });
}

// Xử lý nút đăng xuất
if (document.getElementById('logout-btn')) {
    document.getElementById('logout-btn').addEventListener('click', function(e) {
        e.preventDefault();
        logout();
    });
}

// Kiểm tra trạng thái đăng nhập khi trang tải
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
});