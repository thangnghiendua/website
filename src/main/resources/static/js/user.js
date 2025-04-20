const API_BASE = '/api/user';

function showSection(id) {
    document.querySelectorAll('.section').forEach(section => section.classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');

    switch (id) {
        case 'appointments': fetchAppointments(); break;
        case 'notifications': fetchNotifications(); break;
        case 'children': fetchChildren(); break;
        case 'feedback': showFeedbackForm(); break;
        case 'reactions': fetchReactionReports(); break;
        case 'wallet': fetchWalletBalance(); break;
        case 'vaccines': fetchVaccines(); break;
        case 'profile': showProfileForm(); break;
        default: break;
    }
}

function fetchAppointments() {
    fetch(`${API_BASE}/history`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById('appointments');
            container.innerHTML = '<h2>Lịch sử cuộc hẹn</h2>';
            if (!data.length) {
                container.innerHTML += '<p>Không có cuộc hẹn nào.</p>';
                return;
            }

            container.append(...data.map(app => createCard([
                ['ID', app.appointmentId],
                ['Ngày', app.date],
                ['Trạng thái', app.status]
            ])));
        })
        .catch(err => showError('appointments', err));
}

function fetchNotifications() {
    fetch(`${API_BASE}/notifications`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById('notifications');
            container.innerHTML = '<h2>Thông báo</h2>';
            if (!data.length) {
                container.innerHTML += '<p>Không có thông báo nào.</p>';
                return;
            }

            data.forEach(n => {
                const card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `<p><strong>${n.title}</strong></p><p>${n.message}</p>`;
                container.appendChild(card);
            });
        })
        .catch(err => showError('notifications', err));
}

function fetchChildren() {
    fetch(`${API_BASE}/children`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById('children');
            container.innerHTML = '<h2>Trẻ em của tôi</h2>';
            if (!data.length) {
                container.innerHTML += '<p>Chưa có trẻ em nào.</p>';
                return;
            }

            container.append(...data.map(child =>
                createCard([
                    ['Tên', child.name],
                    ['Giới tính', child.gender],
                    ['Ngày sinh', child.birthDay]
                ])
            ));
        })
        .catch(err => showError('children', err));
}

function showFeedbackForm() {
    const container = document.getElementById('feedback');
    container.innerHTML = `
        <h2>Gửi phản hồi</h2>
        <form id="feedbackForm">
            <textarea id="feedbackText" placeholder="Nhập phản hồi của bạn..." required></textarea>
            <button type="submit">Gửi</button>
        </form>
    `;
    document.getElementById('feedbackForm').addEventListener('submit', sendFeedback);
}

function sendFeedback(event) {
    event.preventDefault();
    const feedbackText = document.getElementById('feedbackText').value.trim();

    if (!feedbackText) {
        alert('Vui lòng nhập phản hồi!');
        return;
    }

    fetch(`${API_BASE}/feedback`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ feedbackText })
    })
        .then(res => res.json())
        .then(() => {
            alert('Phản hồi của bạn đã được gửi!');
            document.getElementById('feedbackText').value = '';
        })
        .catch(() => alert('Có lỗi xảy ra khi gửi phản hồi!'));
}

function fetchReactionReports() {
    fetch(`${API_BASE}/reaction-reports`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById('reactions');
            container.innerHTML = '<h2>Báo cáo phản ứng</h2>';
            if (!data.length) {
                container.innerHTML += '<p>Chưa có báo cáo phản ứng nào.</p>';
                return;
            }

            container.append(...data.map(report =>
                createCard([
                    ['Ngày', report.date],
                    ['Phản ứng', report.reaction]
                ])
            ));
        })
        .catch(err => showError('reactions', err));
}

function fetchWalletBalance() {
    fetch(`${API_BASE}/wallet-balance`)
        .then(res => res.json())
        .then(balance => {
            document.getElementById('wallet').innerHTML = `<h2>Số dư ví: ${balance} VNĐ</h2>`;
        })
        .catch(err => showError('wallet', err));
}

function fetchVaccines() {
    fetch(`${API_BASE}/vaccines`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById('vaccines');
            container.innerHTML = '<h2>Các loại vắc-xin</h2>';
            if (!data.length) {
                container.innerHTML += '<p>Không có vắc-xin nào.</p>';
                return;
            }

            container.append(...data.map(vaccine =>
                createCard([
                    ['Tên', vaccine.name],
                    ['Loại', vaccine.packageType]
                ])
            ));
        })
        .catch(err => showError('vaccines', err));
}

function showProfileForm() {
    const container = document.getElementById('profile');
    container.innerHTML = `
        <h2>Chỉnh sửa hồ sơ</h2>
        <form id="profileForm">
            <input type="text" id="name" placeholder="Tên" required />
            <input type="date" id="birthDate" placeholder="Ngày sinh" />
            <button type="submit">Cập nhật</button>
        </form>
    `;
    document.getElementById('profileForm').addEventListener('submit', updateProfile);
}

function updateProfile(event) {
    event.preventDefault();
    const name = document.getElementById('name').value.trim();
    const birthDate = document.getElementById('birthDate').value;

    if (!name) {
        alert('Tên không được để trống!');
        return;
    }

    fetch(`${API_BASE}/update-profile`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, birthDate })
    })
        .then(res => res.json())
        .then(() => {
            alert('Cập nhật hồ sơ thành công!');
        })
        .catch(() => {
            alert('Có lỗi xảy ra khi cập nhật hồ sơ!');
        });
}

// Helper: tạo thẻ card từ mảng cặp [label, value]
function createCard(infoArray) {
    const card = document.createElement('div');
    card.className = 'card';
    card.innerHTML = infoArray.map(([label, value]) =>
        `<p><strong>${label}:</strong> ${value}</p>`).join('');
    return card;
}

// Helper: hiển thị lỗi vào thẻ container
function showError(sectionId, error) {
    console.error(error);
    const container = document.getElementById(sectionId);
    container.innerHTML = '<p class="error">Đã xảy ra lỗi khi tải dữ liệu. Vui lòng thử lại sau.</p>';
}
