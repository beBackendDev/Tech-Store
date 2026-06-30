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