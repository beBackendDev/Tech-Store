### I. XSS ( Cross-Site Scripting):
XSS stands for Cross-Site Scripting la mot lo hong bao mat web xay ra khi ung dung web cho phep nguoi dung chen du lieuj khong duoc kieem tra vao trang web. Khi do cac doan ma doc(thong thuong la JS) duoc chen va thuc thi tren website nguoi dung dang truy cap.

Khi ma doc duoc thuc thi tren trinh duyet, hacker co the danh cap cac thong tin nhay cam cua nguoi dung bao gom:

- Danh cap `cookie` (dung de quan ly phien dang nhap) dan den viec tai khoan bi chiem doat.
- Doc va trich xuat thong tin du lieu ca nhan hien thi tren trang web.
- Chuyen huong (redirect) nguoi dung den cac trang web lua dao hoac doc hai.
- Thuc hien cac hanh dong mao danh nguoi dung tren ung dung.

Cac loai `XSS` pho bien:

- `Reflected XSS(XSS phan xa): ` Ma doc duoc gui kem trong cac duong lien ket(link), co the la cac `tag` hoac `event attribute` hoac `virtual URL` de khi nguoi dung thuc hien click hoac thao tac bat ki thi ma doc duoc thuc thi (thong thuong ma doc duoc wrap duoi dang file.js) va phan xa nguoc lai tren trinh duyet cua ho.
- `Stored XSS(XSS duoc luu tru): ` Ma doc duoc luu truc tiep trong DB cua may chu(thong qua cac binh luan, bai viet, vote, hoac thong tin ho so nguoi dung...). Khi nguoi dung khac truy cap, ma doc duoc tu dong thuc thi. `Blind XSS` la bien the manh hon cua `Stored XSS`
- `DOM-based XSS(XSS dua tren DOM): `Ma doc nam trong cau truc thanh phan (DOM) cua trang web, thuong bi loi dung khi ma JS phia Client doc du lieu tu URL va dua vao trang web mot cach khong an toan.  

Cac cach pho bien de phong tranh lo hong XSS:
- `Loc va kiem tra du lueu dau vao: ` Luon lam sach du lieu do nguoi dung nhap vao, loai bo cac ki tu hoac doan ma nguy hiem.
- `Ma hoa du lieu dau ra(Encoding): ` Truoc khi hien thi du lieu nguoi dung nhap len trang web, can ma hoa cac ki tu dac biet(nhu chuyen `<` thanh `&lt;`) de trinh duyet hieu do la van ban thong thuong thay vi ma thuc thi.
- `Su dung Content Security Policy(CSP): ` Thiet lap CSP(chinh sach bao mat noi dung) thong qua HTTP Header de han che nguon trinh duyt duoc phep tai va thuc thi ma script.
- Thuc hien luyen tap khai thac lo hong XSS thong qua game:
[practice XSS](https://xss-game.appspot.com/)

`![describe ](href image)` : import image in markdown 

`[describe ](redirect link)` : import link in markdown 

(03/07)
## HTTP / HTTPs
`HTTP (Hypertext Transfer Protocol)` là giao thức truyền tải siêu văn bản. Đây là giao thức tiêu chuẩn cho `World Wide Web (www)` để truyền tải dữ liệu dưới dạng văn bản, âm thanh, hình ảnh, video từ Web Server tới trình duyệt web của người dùng và ngược lại.

Là giao thức ứng dụng của bộ giao thức TCP/IP (các giao thức nền tảng cho Internet).

HTTP hoạt động theo mô hình Client và Server. Quá trình truy cập website chủ yêu là sự giao tiếp của hai đối tượng trên. Khi truy cập vào một trang web qua giao thức HTTP, trình duyệt sẽ thực hiện các phiên kết nối đến server của trang web đó thông qua địa chỉ IP mà hệ thống phân giải tên miền DNS cung cấp. Máy chủ sau khi nhận request sẽ trả về response tương ứng giúp hiển thị website, bao gồm như văn bản, hình ảnh, video...

Những data được truyền đi thông qua giao thức này không hề được mã hóa hay bảo mật => kẽ hở mà các hacker hay lợi dụng.

Hiện tại HTTP có 3 phiên bản chính: `HTTP/1.1`, `HTTP/2`, `HTTP/3`

Xem thêm: [link tham khảo](https://viblo.asia/p/02-tim-hieu-ve-http1x-http2-va-http3-3RlL59v8LbB#_http-la-gi-0)

`HTTPs (Hypertext Transfer Protocol Secure)` là giao thức truyền tải siêu văn bản an toàn. Bản chất, nó vẫn là giao thức HTTP nhưng có thêm chứng chỉ SSL hoặc TSL nhằm mã hóa data giao tiếp để tăng tính bảo mật hơn.

![http/https](https://images.viblo.asia/f3d0243c-a7b3-467b-acf9-b6fa0f0b0519.png)
### 1.1 Cách thức hoạt động

`HTTP: ` là một giao thức lớp ứng dụng trong mô hình giao tiếp mạng, kết nối giữa các hệ thống mở (OSI). HTTP thực hiện xác định một số loại yêu cầu (request) và phản hồi (response).

**Ví dụ:** người dùng cần lấy thông tin cá nhân của họ, phía client sẽ gửi một HTTP GET /user-detail và phía máy chủ (server) thực hiện trả về kết quả (200, 400, 404) tương ứng với kết quả tìm kiếm dựa theo yêu cầu của client và hiển thị kết quả tương ứng ra màn hình.

`HTTPs: ` Vấn đề là HTTP truyền dữ liệu không được mã hóa, có nghĩa là bên thứ ba có thể chặn và đọc thông tin được gửi đi từ trình duyệt, do đó HTTPs được ra đời để cải thiện lỗ hổng đó, HTTPs kết hợp các yêu cầu và phản hồi HTTP với công nghệ mở rộng SSL và TLS.

Quá trình trên được thực hiện như sau:

(1) bạn truy cập vào website có định dạng url là https:// 

(2) trình duyệt thực hiện xác minh tính xác thực của website bằng cách yêu cầu chứng chỉ SSL của máy chủ.

(3) máy chủ sẽ trả lời bằng cách gửi chứng chỉ SSL có chứa một khóa công khai.

(4) sau khi quá trình xác thực hoàn tất, trình duyệt và máy chủ sẽ thực hiện giao tiếp với nhau để trao đổi thông tin cùng dùng chung một khóa phiên để đảm bảo an toàn bảo mật thông tin.



|                    |       HTTP    |  HTTPs   |          
| ------------------ |:-------------:|:---:|
| **stand for**          | Hypertext Transfer Protocol     |Hypertext Transfer Protocol Security     |
| **Giao thức cơ bản**   | HTTP/1 và HTTP/2 sử dụng TCP/IP. HTTP/3 sử dụng giao thức QUIC| Sử dụng HTTP/2 với SSL/TLS để mã hóa thêm cho các yêu cầu và phản hồi HTTP      | 
| **Cổng**               | Cổng mặc định là 80     | Cổng mặc định là 443
| **Trường hợp sử dụng** | Các trang web dựa trên văn bản cũ hơn     | Tất cả các trang website hiện tại
| **Khả năng bảo mật**   | Không có tính năng bảo mật bổ sung     | Sử dụng chứng chỉ SSL để mã hóa công khai
| **Lợi ích**            | Hỗ trợ giao tiếp qua Internet     | Cải thiện độ tin cậy và xếp hạng công cụ tìm kiếm của trang web