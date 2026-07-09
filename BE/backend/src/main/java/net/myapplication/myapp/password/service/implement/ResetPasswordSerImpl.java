package net.myapplication.myapp.password.service.implement;

import net.myapplication.myapp.password.service.ResetPasswordSer;
import net.myapplication.myapp.password.service.ResetPasswordToken;

public class ResetPasswordSerImpl implements ResetPasswordSer {

    @Override
    public net.myapplication.myapp.password.entity.ResetPasswordToken save(ResetPasswordToken token) {
        return resetPasswordRepo.save(
                token);
    }

}
