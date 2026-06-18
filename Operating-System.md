| Chủ đề | Cần hiểu gì? --> Ứng dụng thực tế |

`(CPU RAM DISK)`

---

`Process`: Process là gì, vòng đời process, PID (Process Identifier) `-->` Chạy Spring Boot, MySQL, Nginx, Docker Container

`Thread`: Thread, Multi-threading, Context Switching `-->` Xử lý request đồng thời trong Spring Boot

`Memory Management`: Stack, Heap, Garbage Collection, Memory Leak  `-->` Debug OutOfMemoryError, tối ưu Java

`File System`: File, Directory, Absolute/Relative Path `-->` Upload ảnh, lưu file, cấu hình server

`Linux Commands`: ls, cd, grep, find, cat, tail, chmod `-->` Làm việc trên VPS/Linux Server

`Networking`: IP, Port, DNS, TCP, HTTP, HTTPS `-->` React ↔ Backend ↔ Database ↔ Internet

`Permissions`: User, Group, chmod, chown `-->` Deploy ứng dụng, bảo mật server

`Concurrency`: Race Condition, Synchronization `-->` Booking, Payment, Giỏ hàng

`Deadlock`: Nguyên nhân và cách tránh `-->` Transaction, xử lý dữ liệu đồng thời

`Signals`: SIGTERM, SIGKILL, SIGINT `-->` Dừng service, Docker shutdown

`Monitoring`: CPU, RAM, Disk, Network `-->` Theo dõi production server

`Cron Jobs`: Lập lịch tác vụ `-->` Backup DB, gửi email tự động

`Docker Internals`: Namespace, Cgroups, Container `-->` Hiểu Docker hoạt động thế nào

`Virtual Memory`: Paging, Address Space `-->` Hiệu năng hệ thống

`Scheduling Algorithms`: FCFS, Round Robin `-->` Hiểu cách OS phân chia CPU

`IPC`: Pipe, Shared Memory, Message Queue `-->` Giao tiếp giữa tiến trình

`Socket Programming`: TCP Socket, UDP Socket `-->` Hiểu sâu về Network Programming

`Kernel`: Kernel Space, System Call `-->` Dành cho System Engineer

`Device Drivers`: Driver hoạt động thế nào `-->` Ít dùng với Full Stack

`Assembly`: Thanh ghi, lệnh máy `-->` Không cần cho đa số dự án web

`CPU Architecture`: Cache, Pipeline, Branch Prediction `-->` Hữu ích cho tối ưu hệ thống cấp thấp

---
#### I. PROCESS:
##### 1. `PROCESS`: (tien trinh) la mot chuong trinh dang duoc thuc thi, moi `process` so huu khong gian bo nho, tai nguyen CPU va ma lenh rieng. `PID` (Process Identifier) la ma dinh danh duy nhat( dang so nguyen) ma he dieu hanh dung de quan ly tien trinh do.
##### 2. `PROCESS'sLIFE`: trai qua cac giai doan chinh sau:
+ `NEW` (Khoi tao): He dieu hanh tao process, nao ma chuong trinh vao bo nho va chuan bi cac tai nguyen can thiet. 
+ `READY` (san sang): Process da san sang de chay, cho bo lap lich cua he dieu hanh cap phat tai nguyen cho CPU
+ `RUNNING` (Dang thuc thi): CPU xu li cac lenh cua process
+ `WAITING` / `BLOCKED` (Cho doi): Process tam dung thuc thi de doi mot su kien(vd: nguoi dung nap du lieu, hoac doc/ghi o cung).
+ `TERMINATEd` / `EXITED` (Ket thuc): Process da thuc hien xong nhiem vu hoac bi buoc dung. He dieu hanh se thu hoi lai bo nho va PID cua no.

##### 3. `PID (Process Identifier)`: 
la so nguyen duong va duy nhat cho moi process tren he thong
+ Dinh danh duy nhat: Tai bat ki thoi diem nao, moi tien trinh dang chay deu co mot ma PID duy nhat.
+ Quan ly: He dieu hanh(vd: Linux, Windows) su dung PID de theo doi trang thai, phan quyen hoac gui tin hieu (tat/khoi dong lai) toi dung tien trinh do.
+ Tai su dung: Khi mot tien trinh ket thuc, PID cua no se duoc tra ve he thong va co the duoc cap phat lai cho mot tien trinh moi trong tuong lai

? xem danh sach PID ma he thong dang dung ?