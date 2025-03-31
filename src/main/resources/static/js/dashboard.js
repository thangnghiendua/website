// dashboard.js - JavaScript for the admin dashboard

document.addEventListener('DOMContentLoaded', function() {
    // Sidebar toggle
    const toggleSidebar = document.querySelector('.toggle-sidebar');
    const dashboardSidebar = document.querySelector('.dashboard-sidebar');
    
    if (toggleSidebar && dashboardSidebar) {
        toggleSidebar.addEventListener('click', function() {
            dashboardSidebar.classList.toggle('active');
        });
    }
    
    // Submenu toggle
    const submenuToggles = document.querySelectorAll('.has-submenu > a');
    
    if (submenuToggles.length > 0) {
        submenuToggles.forEach(toggle => {
            toggle.addEventListener('click', function(e) {
                e.preventDefault();
                this.parentElement.classList.toggle('open');
            });
        });
    }
    
    // Initialize charts if Charts.js is available
    if (typeof Chart !== 'undefined') {
        initializeCharts();
    }
    
    // Data table initialization if DataTable is available
    if (typeof $.fn.DataTable !== 'undefined') {
        $('.datatable').DataTable({
            language: {
                url: '//cdn.datatables.net/plug-ins/1.10.25/i18n/Vietnamese.json'
            },
            responsive: true
        });
    }
    
    // Date range picker initialization if daterangepicker is available
    if (typeof $.fn.daterangepicker !== 'undefined') {
        $('.date-range-picker').daterangepicker({
            locale: {
                format: 'DD/MM/YYYY',
                applyLabel: 'Áp dụng',
                cancelLabel: 'Hủy',
                fromLabel: 'Từ',
                toLabel: 'Đến',
                customRangeLabel: 'Tùy chỉnh',
                daysOfWeek: ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
                monthNames: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
                firstDay: 1
            },
            ranges: {
               'Hôm nay': [moment(), moment()],
               'Hôm qua': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
               '7 ngày qua': [moment().subtract(6, 'days'), moment()],
               '30 ngày qua': [moment().subtract(29, 'days'), moment()],
               'Tháng này': [moment().startOf('month'), moment().endOf('month')],
               'Tháng trước': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            }
        });
    }
    
    // Handle action confirmations
    const confirmActions = document.querySelectorAll('[data-confirm]');
    
    if (confirmActions.length > 0) {
        confirmActions.forEach(action => {
            action.addEventListener('click', function(e) {
                if (!confirm(this.getAttribute('data-confirm'))) {
                    e.preventDefault();
                }
            });
        });
    }
    
    // Handle bulk actions
    const bulkActionForm = document.querySelector('.bulk-action-form');
    
    if (bulkActionForm) {
        const bulkActionSelect = bulkActionForm.querySelector('.bulk-action-select');
        const bulkActionBtn = bulkActionForm.querySelector('.bulk-action-btn');
        const itemCheckboxes = document.querySelectorAll('.item-checkbox');
        const selectAllCheckbox = document.querySelector('.select-all-checkbox');
        
        // Select all checkbox
        if (selectAllCheckbox) {
            selectAllCheckbox.addEventListener('change', function() {
                itemCheckboxes.forEach(checkbox => {
                    checkbox.checked = this.checked;
                });
                
                updateBulkActionStatus();
            });
        }
        
        // Individual checkboxes
        if (itemCheckboxes.length > 0) {
            itemCheckboxes.forEach(checkbox => {
                checkbox.addEventListener('change', function() {
                    updateBulkActionStatus();
                    
                    // Update select all checkbox
                    if (selectAllCheckbox) {
                        selectAllCheckbox.checked = Array.from(itemCheckboxes).every(cb => cb.checked);
                        selectAllCheckbox.indeterminate = !selectAllCheckbox.checked && Array.from(itemCheckboxes).some(cb => cb.checked);
                    }
                });
            });
        }
        
        // Bulk action button
        if (bulkActionBtn) {
            bulkActionBtn.addEventListener('click', function(e) {
                e.preventDefault();
                
                if (!bulkActionSelect.value) {
                    alert('Vui lòng chọn hành động');
                    return;
                }
                
                const checkedItems = document.querySelectorAll('.item-checkbox:checked');
                
                if (checkedItems.length === 0) {
                    alert('Vui lòng chọn ít nhất một mục');
                    return;
                }
                
                if (confirm(`Bạn có chắc chắn muốn ${bulkActionSelect.options[bulkActionSelect.selectedIndex].text.toLowerCase()} ${checkedItems.length} mục đã chọn?`)) {
                    bulkActionForm.submit();
                }
            });
        }
        
        // Update bulk action button status
        function updateBulkActionStatus() {
            const checkedCount = document.querySelectorAll('.item-checkbox:checked').length;
            
            if (bulkActionBtn) {
                bulkActionBtn.disabled = checkedCount === 0;
                
                if (checkedCount > 0) {
                    bulkActionBtn.textContent = `Thực hiện (${checkedCount})`;
                } else {
                    bulkActionBtn.textContent = 'Thực hiện';
                }
            }
        }
    }
});

// Initialize dashboard charts
function initializeCharts() {
    // Revenue chart
    const revenueChart = document.getElementById('revenueChart');
    
    if (revenueChart) {
        new Chart(revenueChart, {
            type: 'line',
            data: {
                labels: ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'],
                datasets: [{
                    label: 'Doanh thu (Triệu VNĐ)',
                    data: [12, 19, 14, 15, 21, 17, 15, 19, 23, 25, 18, 21],
                    backgroundColor: 'rgba(74, 144, 226, 0.2)',
                    borderColor: '#4A90E2',
                    borderWidth: 2,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return value + ' tr';
                            }
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.parsed.y + ' triệu VNĐ';
                            }
                        }
                    }
                }
            }
        });
    }
    
    // Bookings chart
    const bookingsChart = document.getElementById('bookingsChart');
    
    if (bookingsChart) {
        new Chart(bookingsChart, {
            type: 'bar',
            data: {
                labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
                datasets: [{
                    label: 'Lịch hẹn',
                    data: [12, 19, 14, 15, 21, 17, 8],
                    backgroundColor: '#5CC489'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            precision: 0
                        }
                    }
                }
            }
        });
    }
    
    // Services distribution chart
    const servicesChart = document.getElementById('servicesChart');
    
    if (servicesChart) {
        new Chart(servicesChart, {
            type: 'doughnut',
            data: {
                labels: ['Tiêm lẻ', 'Gói cơ bản', 'Gói nâng cao', 'Cá thể hóa'],
                datasets: [{
                    data: [30, 40, 20, 10],
                    backgroundColor: ['#4A90E2', '#5CC489', '#FF9966', '#B3B3CC'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'right'
                    }
                }
            }
        });
    }
    
    // Age distribution chart
    const ageChart = document.getElementById('ageChart');
    
    if (ageChart) {
        new Chart(ageChart, {
            type: 'bar',
            data: {
                labels: ['0-6 tháng', '7-12 tháng', '1-2 tuổi', '2-5 tuổi', '6+ tuổi'],
                datasets: [{
                    label: 'Số lượng trẻ',
                    data: [25, 30, 45, 35, 15],
                    backgroundColor: '#4A90E2'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            precision: 0
                        }
                    }
                }
            }
        });
    }
}
