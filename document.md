______________FRONTEND________________
# 0. Docker

## 0.1 React

### 0.1.1 demo voi mot prj: 
```
npx create-react-app my-app
```
Structure of Prj:
```
├── package.json
├── public
│   ├── ...
│   └── robots.txt
├── README.md
├── src
│   ├── ...
│   └── App.js
└── yarn.lock
```
### 0.1.2 Config cho Prj
Tiep theo, them cac config cho Prj:

```
+   ├── docker-compose.yml
+   ├── Dockerfile // Luu tru cac syntax de quyet dinh thu ma duoc docker wrap
+   ├── .dockerignore // ignore cac file khong can thiet
+   ├── .nginx
+   │   └── nginx.conf // config cho nginx
    ├── package.json
    ├── public
    │   ├── ...
    │   └── robots.txt
    ├── README.md
    ├── src
    │   ├── ...
    │   └── App.js
    └── yarn.lock
```

Noi dung duoc config cho nginx :
```
server {    

    listen 80;

    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri /index.html =404;
    }

    error_page 500 502 503 504 /50x.html;

    location = /50x.html {
        root /usr/share/nginx/html;
    }
    }
```
`+ listen 80` : `nginx` se thuc hien listen port 80 trong `container` de hien thi trong app.

`+ try_files`: Doi voi `React` la single page, tuc la chi co 1 route duy nhat de hien thi toan bo app. Voi thu tu `try_files` nhu tren nginx se dduyet lan luot theo thu tu `$uri > /index.html > 404(not found page)` de kiem tra, neu k hop le thi se chuyen den route tiep theo de kiem tra va hien thi.

`+ error_page`: mot vao route loi de hien thi mac dinh.

### 0.1.3 Noi dung Dockerfile

```
    # 1. For build React app
    FROM node:lts AS development
    # Set working directory
    WORKDIR /app
    #
    COPY package.json /app/package.json
    COPY package-lock.json /app/package-lock.json
    # Same as npm install
    RUN npm ci
    COPY . /app
    ENV CI=true
    ENV PORT=3000
    CMD [ "npm", "start" ]
    FROM development AS build
    RUN npm run build
    # 2. For Nginx setup
    FROM nginx:alpine
    # Copy config nginx
    COPY --from=build /app/.nginx/nginx.conf /etc/nginx/conf.d/default.conf
    WORKDIR /usr/share/nginx/html
    # Remove default nginx static assets
    RUN rm -rf ./*
    # Copy static assets from builder stage
    COPY --from=build /app/build .
    # Containers run nginx with global directives and daemon off
    ENTRYPOINT ["nginx", "-g", "daemon off;"]
```
### 0.1.4 docker-compose.yml

De don gian cho viec run Docker va tich hop voi nhieu service khac nhau:
```
    version: "3.7"
    services:
        frontend:
            build:
                context: .
            container_name: frontend
            ports:
            - "3000:80" # map 80 in container => 3000 in local
```
### 0.1.5 Run & End Docker
+ Start Docker: `docker-compose up -d`
+ End Docker: `docker-compose down`
# 1. Structure

```
src/
│
├── api/
│   ├── authApi.js
│   ├── productApi.js
│   ├── ...
│
├── assets/
│   ├── images/
│   ├── icons/
│   └── styles/
│
├── components/
│   ├── Header/
│   ├── Footer/
│   ├── ProductCard/
│   ├── CartItem/
│   └── Pagination/
│
├── pages/
│   ├── Home/
│   ├── Login/
│   ├── Admin/
│   ├── ...
│
├── layouts/
│   ├── UserLayout.jsx
│   └── AdminLayout.jsx
│
├── routes/
│   ├── AppRoutes.jsx
│   ├── PrivateRoute.jsx
│   └── AdminRoute.jsx
│
├── hooks/
│   ├── useAuth.js
│   ├── useCart.js
│   └── useProducts.js
│
├── store/
│   ├── authSlice.js
│   ├── cartSlice.js
│   ├── productSlice.js
│   └── store.js
│
├── context/
│
├── utils/
│   ├── formatPrice.js
│   ├── validator.js
│   └── constants.js
│
├── services/
│   └── axiosClient.js
│
├── App.jsx
└── main.jsx
```


______________BACKEND________________

# 1.Structure
```
├── auth/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── dto/
│   └── entity/
│
├── user/
│
├── category/
│
├── product/
│
├── cart/
│
├── order/
│
├── payment/
│
├── review/
│
├── notification/
│
├── security/
├── common/
└── config/


```
# 2. Configuration:
## 2.1 Khoi tao du an

## 2.2 Config DB (MySQL)

## 2.3 Detail Function
### 2.3.1 Register
`Input validation:`

Kiểm tra định dạng email, độ mạnh mật khẩu, username không chứa ký tự đặc biệt, xác nhận 2 lần nhập mật khẩu khớp nhau,... ở cả phía client và server.

`Duplicate check:`

Kiểm tra email hoặc username đã tồn tại trong hệ thống chưa để tránh đăng ký trùng.

`Password security:`

Bắt buộc sử dụng mật khẩu mạnh (tối thiểu 8 ký tự, bao gồm chữ hoa, chữ thường, số, ký tự đặc biệt)
Password lưu trong database phải được hash (bằng bcrypt, Argon2, scrypt,...) chứ không được lưu plain text.

`Email verification:`

Gửi email xác thực tài khoản (kèm link hoặc mã xác nhận)

`Rate limiting & bot protection:`

Giới hạn số lần đăng ký từ một IP, kết hợp với CAPTCHA/reCAPTCHA để ngăn bot tạo tài khoản ảo.

`Username/email normalization:`

Chuyển email về dạng chuẩn (lowercase, bỏ khoảng trắng đầu/cuối,...) để so sánh đúng khi kiểm tra trùng.

`Set default user role/status:`

Gán role mặc định (vd: user) và trạng thái xác minh email unverified nếu chưa xác minh.

`Tạo các thực thể liên quan sau khi đăng ký:`

Tạo các thực thể liên quan như hồ sơ người dùng (profile), giỏ hàng trống (cart), danh sách yêu thích (favorites), ... tùy theo nghiệp vụ của ứng dụng.

`Gửi welcome email:`

Gửi email chào mừng (kèm hướng dẫn xác minh, sử dụng, hỗ trợ,...) nếu cần

`Secure session/token issuance:`

Sau khi đăng ký thành công, có thể tự động đăng nhập người dùng bằng cách tạo session hoặc cấp access token.

`CSRF protection:`

Nếu dùng cookie để lưu auth/session, cần bảo vệ form đăng ký bằng CSRF token.

`Email/phone confirmation reminder UI:`

Hiển thị thông báo yêu cầu xác minh tài khoản (nếu áp dụng) kèm button gửi lại mã xác thực/email.

`Terms of Service & Privacy Policy agreement:`

bắt buộc người dùng phải đồng ý với điều khoản sử dụng và chính sách bảo mật trước khi đăng ký.

`Frontend error feedback:`

Thông báo lỗi cụ thể (ví dụ: mật khẩu quá yếu, tài khoản đã tồn tại, ...) thân thiện với người dùng.

### 2.3.2 Login
Chuc nang 'dang nhap' phai bao gom cac dieu kien sau:

`Input validation:`

Kiểm tra dữ liệu đầu vào như định dạng email/username và password ở cả frontend và backend.

`Password security:`

Bắt buộc sử dụng mật khẩu mạnh (tối thiểu 8 ký tự, bao gồm chữ hoa, chữ thường, số, ký tự đặc biệt)
Password lưu trong database phải được hash (bằng bcrypt, Argon2, scrypt,...) chứ không được lưu plain text.

`Session creation (nếu dùng session):`

Sau khi xác thực thành công, tạo session phía server và gửi session ID về client thông qua cookie.

`JWT issuance (nếu dùng token-based auth):`

Nếu dùng JWT, cần tạo access token (và có thể là refresh token) sau khi xác thực, gửi về client để lưu trữ.

`Set secure cookies:`

Nếu lưu token/session ID trong cookie, cần đặt HttpOnly, Secure, SameSite để tăng bảo mật.

`CSRF protection:`

Nếu dùng cookie để lưu auth info, cần kết hợp CSRF token để tránh các cuộc tấn công CSRF.

`Two-Factor Authentication (2FA):`

Hỗ trợ xác thực 2 bước, gửi OTP qua email/SMS hoặc dùng app xác thực như Google Authenticator

`"Remember me" support:`

Nếu có, lưu refresh token/token sống lâu để duy trì đăng nhập giữa các phiên.

`Account lockout policy:`

Tạm khóa tài khoản sau X lần đăng nhập sai liên tiếp để ngăn brute force attack. Ví dụ các ngân hàng thường khóa tài khoản sau 5 lần đang nhập sai liên tiếp.

`Rate limiting:`

Ví dụ giới hạn số lần login trong một khoảng thời gian.

`CAPTCHA:`

Thêm CAPTCHA sau vài lần login sai liên tục.

`Device Fingerprinting:`

Thu thập đặc điểm thiết bị (user-agent, canvas, WebGL, timezone, ...)
Hash thành device ID.
Nếu phát thiết bị lạ => Gửi email cảnh báo, yêu cầu xác minh OTP.

`GeoIP Tracking:`

Phát hiện đăng nhập từ IP quốc gia lạ.
Gửi email cảnh báo, yêu cầu xác minh OTP.

`Session management:`

Hạn chế thời gian sống của session.
Regenerate session ID sau khi đăng nhập thành công để tránh session fixation.

`Frontend error feedback:`

Xử lý các trường hợp lỗi như sai mật khẩu, tài khoản không tồn tại, tài khoản bị khóa,... nhưng sau đó chỉ nên hiển thị thông báo chung chung kiểu thông tin đăng nhập không chính xác. Chứ không nên báo cụ thể lỗi ở đâu để tránh kẻ tấn công lợi dụng khai thác, spam, ...


### 2.3.3 Logout
### 2.3.4 Forgot Pw
