CREATE DATABASE VaccineManagement

USE VaccineManagement

-- Bảng Users: Lưu lại thông tin người dùng và phân loại tài khoản
CREATE TABLE Users(
	UserID INT PRIMARY KEY IDENTITY,
	FullName NVARCHAR(100) NOT NULL,
	UserRole VARCHAR(10) DEFAULT 'User' CHECK (UserRole IN ('Admin','Doctor','User')),
	Gender NVARCHAR(10) DEFAULT 'Khac' CHECK (Gender IN ('Nam','Nữ','Khác')),
	BirthDay DATETIME,
	Email VARCHAR(100) NOT NULL,
	UserPassword VARCHAR(100) NOT NULL
);

-- Bảng Wallets : Lưu lại số dư trong tài khoản người dùng
CREATE TABLE Wallets(
	WalletID INT PRIMARY KEY IDENTITY,
	UserID INT FOREIGN KEY REFERENCES Users(UserID),
	AccountBalance DECIMAL(18,6) DEFAULT 0.0
);

-- Bảng Childs: Lưu lại thông tin trẻ
CREATE TABLE Childs(
	ChildID INT PRIMARY KEY IDENTITY,
	UserID INT FOREIGN KEY REFERENCES Users(UserID) ON DELETE CASCADE,
	ChildName NVARCHAR(100) NOT NULL,
	Gender NVARCHAR(10) NOT NULL CHECK (Gender IN ('Nam','Nữ')),
	BirthDay DATETIME NOT NULL
);

-- Bảng Vaccines: Lưu lại thông tin các loại vaccine
CREATE TABLE Vaccines(
	VaccineID INT PRIMARY KEY IDENTITY,
	VaccineName NVARCHAR(255) NOT NULL,
	Package NVARCHAR(20) NOT NULL CHECK (Package IN ('Tiêm lẻ','Trọn gói','Cá thể hóa')),
	Price DECIMAL(10,6) NOT NULL,
	VaccineInformation NVARCHAR(MAX)
);

-- Bảng Doctors: Lưu lại thông tin bác sĩ
CREATE TABLE Doctors(
	DoctorID INT PRIMARY KEY IDENTITY,
	DoctorName NVARCHAR(100) NOT NULL,
	Rating DECIMAL(2,1),
	DoctorInformation NVARCHAR(MAX),
	NumberPhone VARCHAR(10) NOT NULL
);

-- Bảng Appointment: Lưu lại thông tin và quản lí lịch hẹn tiêm chủng
CREATE TABLE Appointments(
	AppointmentID INT PRIMARY KEY IDENTITY,
	UserID INT FOREIGN KEY REFERENCES Users(UserID),
	ChildID INT FOREIGN KEY REFERENCES Childs(ChildID),
	DoctorID INT FOREIGN KEY REFERENCES Doctors(DoctorID),
	AppointmentDate DATE NOT NULL,
	AppointmentTime TIME NOT NULL,
	AppointmentAddress NVARCHAR(255) NOT NULL,
	RoomNumber VARCHAR(20) NOT NULL,
	AppointmentStatus NVARCHAR(50) DEFAULT 'Chưa được xử lý' CHECK (AppointmentStatus IN ('Đã Xác nhận','Hủy','Hoàn thành','Chưa được xử lý'))
);

-- Bảng Payment: Lưu lại hóa đơn thanh toán
CREATE TABLE Payments(
	 PaymentID INT PRIMARY KEY IDENTITY,
	 UserID INT FOREIGN KEY REFERENCES Users(UserID),
	 AppointmentID INT FOREIGN KEY REFERENCES Appointments(AppointmentID),
	 Amount DECIMAL(10,6) NOT NULL,
	 PaymentDate DATETIME DEFAULT GETDATE(),
	 PaymentStatus NVARCHAR(40) DEFAULT 'Chưa thanh toán' CHECK (PaymentStatus IN ('Đã thanh toán','Chưa thanh toán','Thất bại'))
);
-- Bảng FeedBack: Lưu lại đánh giá của người dùng về dịch vụ
CREATE TABLE FeedBacks(
	FeedBackID INT PRIMARY KEY IDENTITY,
	UserID INT FOREIGN KEY REFERENCES Users(UserID),
	AppointmentID INT FOREIGN KEY REFERENCES Appointments(AppointmentID),
	Comment NVARCHAR(MAX),
	Rating INT CHECK (Rating BETWEEN 1 AND 5),
	FeedBackDate DATETIME DEFAULT GETDATE()
);

--Bảng Reaction_Reports: Lưu lại thông tin về tình trạng của trẻ sau tiêm 
CREATE TABLE Reaction_Reports(
	Reaction_ReportID INT PRIMARY KEY IDENTITY,
	UserID INT FOREIGN KEY REFERENCES Users(UserID),
	AppointmentID INT FOREIGN KEY REFERENCES Appointments(AppointmentID),
	Symptoms NVARCHAR(MAX),
	DoctorFeedBack NVARCHAR(MAX),
	ReportDate DATETIME DEFAULT GETDATE()
);

--Bảng trung gian giữa Users và Childs
CREATE TABLE User_Child(
	UserID INT FOREIGN KEY REFERENCES Users(UserID),
	ChildID INT FOREIGN KEY REFERENCES Childs(ChildID),
	PRIMARY KEY  (UserID,ChildID),
	Relationship NVARCHAR(30) DEFAULT 'Người giám hộ' CHECK  (Relationship IN ('Cha','Mẹ','Người giám hộ'))
);

--Bảng trung gian giữa Child và Vaccine
CREATE TABLE Child_Vaccine(
	ChildID INT FOREIGN KEY REFERENCES Childs(ChildID),
	VaccineID INT FOREIGN KEY REFERENCES Vaccines(VaccineID),
	PRIMARY KEY (ChildID,VaccineID),
	Dose INT NOT NULL
);

--Bảng trung gian giữa Child và Appointment
CREATE TABLE Child_Appointment(
	ChildID INT FOREIGN KEY REFERENCES Childs(ChildID),
	AppointmentID INT FOREIGN KEY REFERENCES Appointments(AppointmentID),
	PRIMARY KEY (ChildID,AppointmentID)
);
