// booking.js - JavaScript for the booking workflow

document.addEventListener('DOMContentLoaded', function() {
    // Elements
    const bookingForm = document.getElementById('bookingForm');
    const step1Content = document.getElementById('step1-content');
    const step2Content = document.getElementById('step2-content');
    const step3Content = document.getElementById('step3-content');
    const step4Content = document.getElementById('step4-content');
    
    const step1Next = document.getElementById('step1-next');
    const step2Next = document.getElementById('step2-next');
    const step2Back = document.getElementById('step2-back');
    const step3Next = document.getElementById('step3-next');
    const step3Back = document.getElementById('step3-back');
    const step4Back = document.getElementById('step4-back');
    
    const step1 = document.getElementById('step-1');
    const step2 = document.getElementById('step-2');
    const step3 = document.getElementById('step-3');
    const step4 = document.getElementById('step-4');
    
    const summaryChildName = document.getElementById('summary-child-name');
    const summaryServiceName = document.getElementById('summary-service-name');
    const summaryDateTime = document.getElementById('summary-datetime');
    const summaryPrice = document.getElementById('summary-price');
    const summaryTotal = document.getElementById('summary-total');
    
    // Check if elements exist before attaching event listeners
    if (!bookingForm) return;
    
    // Event listeners for navigation buttons
    if (step1Next) {
        step1Next.addEventListener('click', function() {
            // Validate step 1
            const selectedChild = document.querySelector('input[name="childId"]:checked');
            if (!selectedChild) {
                alert('Vui lòng chọn trẻ em');
                return;
            }
            
            // Update summary
            const childName = selectedChild.closest('.child-item').querySelector('.child-info h3').textContent;
            if (summaryChildName) summaryChildName.textContent = childName;
            
            // Navigate to step 2
            step1Content.style.display = 'none';
            step2Content.style.display = 'block';
            
            // Update progress indicators
            step1.classList.add('completed');
            step2.classList.add('active');
        });
    }
    
    if (step2Back) {
        step2Back.addEventListener('click', function() {
            // Navigate back to step 1
            step2Content.style.display = 'none';
            step1Content.style.display = 'block';
            
            // Update progress indicators
            step2.classList.remove('active');
        });
    }
    
    if (step2Next) {
        step2Next.addEventListener('click', function() {
            // Validate step 2
            const selectedService = document.querySelector('input[name="serviceId"]:checked');
            if (!selectedService) {
                alert('Vui lòng chọn dịch vụ tiêm chủng');
                return;
            }
            
            // Update summary
            const serviceName = selectedService.closest('.service-item').querySelector('.service-info h3').textContent;
            const servicePrice = selectedService.closest('.service-item').querySelector('.service-price span').textContent;
            
            if (summaryServiceName) summaryServiceName.textContent = serviceName;
            if (summaryPrice) summaryPrice.textContent = servicePrice;
            if (summaryTotal) summaryTotal.textContent = servicePrice;
            
            // Navigate to step 3
            step2Content.style.display = 'none';
            step3Content.style.display = 'block';
            
            // Update progress indicators
            step2.classList.add('completed');
            step3.classList.add('active');
            
            // Initialize calendar if needed
            initializeCalendar();
        });
    }
    
    if (step3Back) {
        step3Back.addEventListener('click', function() {
            // Navigate back to step 2
            step3Content.style.display = 'none';
            step2Content.style.display = 'block';
            
            // Update progress indicators
            step3.classList.remove('active');
        });
    }
    
    if (step3Next) {
        step3Next.addEventListener('click', function() {
            // Validate step 3
            const selectedDate = document.querySelector('input[name="bookingDate"]').value;
            const selectedTime = document.querySelector('input[name="bookingTime"]:checked');
            
            if (!selectedDate) {
                alert('Vui lòng chọn ngày tiêm chủng');
                return;
            }
            
            if (!selectedTime) {
                alert('Vui lòng chọn giờ tiêm chủng');
                return;
            }
            
            // Update summary
            const dateTimeText = `${selectedDate} ${selectedTime.value}`;
            if (summaryDateTime) summaryDateTime.textContent = dateTimeText;
            
            // Navigate to step 4
            step3Content.style.display = 'none';
            step4Content.style.display = 'block';
            
            // Update progress indicators
            step3.classList.add('completed');
            step4.classList.add('active');
        });
    }
    
    if (step4Back) {
        step4Back.addEventListener('click', function() {
            // Navigate back to step 3
            step4Content.style.display = 'none';
            step3Content.style.display = 'block';
            
            // Update progress indicators
            step4.classList.remove('active');
        });
    }
    
    // Handle time slot selection
    const timeSlots = document.querySelectorAll('.time-slot');
    if (timeSlots.length > 0) {
        timeSlots.forEach(slot => {
            slot.addEventListener('click', function() {
                if (this.classList.contains('disabled')) return;
                
                // Deselect all slots
                timeSlots.forEach(s => s.classList.remove('active'));
                
                // Select current slot
                this.classList.add('active');
                
                // Set hidden input value
                const timeInput = document.querySelector('input[name="bookingTime"]');
                if (timeInput) {
                    timeInput.value = this.getAttribute('data-time');
                }
            });
        });
    }
    
    // Initialize calendar function
    function initializeCalendar() {
        // This is where you would initialize a date picker if using a library
        // For this example, we'll just use the native date input
        
        // When date changes, update available time slots
        const dateInput = document.querySelector('input[name="bookingDate"]');
        if (dateInput) {
            dateInput.addEventListener('change', function() {
                updateTimeSlots(this.value);
            });
        }
    }
    
    // Update time slots based on selected date
    // This would typically fetch data from the server
    function updateTimeSlots(date) {
        // Simulate loading time slots from server
        // In a real application, you would make an AJAX request to get available slots
        
        console.log(`Fetching time slots for: ${date}`);
        
        // For demonstration purposes, we'll just enable/disable slots randomly
        timeSlots.forEach(slot => {
            const random = Math.random();
            if (random < 0.3) {
                slot.classList.add('disabled');
                slot.classList.remove('active');
            } else {
                slot.classList.remove('disabled');
            }
        });
    }
    
    // Handle form submission
    if (bookingForm) {
        bookingForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Here you would typically validate all fields again
            // and then submit the form via AJAX or regular form submission
            
            // For demo purposes, we'll just show an alert
            alert('Đặt lịch thành công! Bạn sẽ được chuyển hướng đến trang thanh toán.');
            
            // In a real application, you would submit the form and redirect
            // this.submit();
        });
    }
});
