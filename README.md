# 🚀 Recruitment Workflow System

## 📌 Overview

The **Recruitment Workflow System** is a Java-based application designed to streamline and manage the hiring process in an organized and structured manner.

Traditional recruitment methods using emails or spreadsheets often lead to confusion and inefficiencies. This system solves that by providing a workflow-based approach to track candidate applications through different stages.

---

## 🎯 Objectives

* To simplify recruitment management
* To track candidate progress efficiently
* To implement role-based access control
* To demonstrate workflow-based system design

---

## 👥 User Roles

### 👤 Candidate

* Register and login
* View available job openings
* Apply for jobs
* Track application status

### 👨‍💼 Recruiter

* Login securely
* Post new job openings
* View all applications
* Update candidate status

---

## 🔄 Workflow Process

```text
APPLIED → SHORTLISTED → INTERVIEW → SELECTED / REJECTED
```

The system ensures that:

* Each application follows a **fixed workflow**
* Invalid transitions are restricted
* Recruitment process remains structured

---

## ⚙️ Features

* 🔐 User Authentication (Login & Registration)
* 🧾 Job Posting System
* 📥 Job Application Module
* 🔄 Workflow-based Status Tracking
* 📊 Recruiter Dashboard
* 🚫 Validation of Invalid Status Transitions
* 🖥️ Clean Console-Based UI

---

## 🏗️ Project Structure

```text
Recruitment-System/
│── Main.java           # Main logic and menus
│── User.java           # User authentication
│── Job.java            # Job management
│── Application.java    # Application tracking
│── Enums.java          # Role & Status definitions
```

---

## 🛠️ Technologies Used

* Java (Core Java)
* Object-Oriented Programming (OOP)
* Console-based Interface

---

## ▶️ How to Run the Project

1. Clone the repository:

```bash
git clone https://github.com/ishankukreti1/Recruitment-Workflow-System.git
```

2. Navigate to project folder:

```bash
cd Recruitment-Workflow-System
```

3. Compile the program:

```bash
javac *.java
```

4. Run the program:

```bash
java Main
```

---

## 🧪 Sample Flow

1. Recruiter logs in and posts a job
2. Candidate logs in and applies for the job
3. Recruiter views applications
4. Recruiter updates status step-by-step
5. Final decision is reflected in dashboard

---

## ⚠️ Limitations

* Data is stored in memory (not persistent)
* Console-based UI (no web interface yet)

---

## 🚀 Future Enhancements

* Integration with MySQL database
* Web-based interface using Servlets/JSP
* Advanced dashboard with filtering
* Email notifications for candidates

---

## 📊 Project Status

* ✔ Authentication System → Completed
* ✔ Job Posting Module → Completed
* ✔ Application Module → Completed
* ✔ Workflow Tracking → Completed
* ⏳ Dashboard Improvements → In Progress

---

## 👨‍💻 Author

**Ishan Kukreti**

---

## 💡 Notes

This project demonstrates how a **workflow-based system** can improve efficiency and reduce errors in recruitment management.

---

✨ *A simple yet effective system showcasing real-world problem solving using Java.*
