//quanly access_token 
//luu trong localStorage sessionStorage or memory (su dung memory de tranh bi xoa khi reload trang) 

const ACCESS_TOKEN = "accessToken";

export const saveAccessToken = (token) => {
console.log("🔥 SAVE ACCESS TOKEN");
    console.log("VALUE:", token);
    console.log("TYPE:", typeof token);
    console.trace();
    localStorage.setItem(ACCESS_TOKEN, token);
};

export const getAccessToken = () => {
    return localStorage.getItem(ACCESS_TOKEN);
};

export const removeAccessToken = () => {
    localStorage.removeItem(ACCESS_TOKEN);
};