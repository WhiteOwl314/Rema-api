package seongju.remaapi.service;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import seongju.remaapi.dao.MemberDao;
import seongju.remaapi.vo.MemberVo;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "reviewmachinemail@gmail.com";

    @Override
    public String checkId(String id) {

        boolean idIsExisted ;

        int idCount = memberDao.checkId(id);

        if(idCount == 0){
            idIsExisted = false;
        }else{
            idIsExisted = true;
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("idIsExisted",idIsExisted);

        return obj.toString();
    }

    @Override
    public String checkEmail(String email) {

        boolean emailIsExisted ;

        int emailCount = memberDao.checkEmail(email);

        if(emailCount == 0){
            emailIsExisted = false;
        }else{
            emailIsExisted = true;
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("emailIsExisted",emailIsExisted);

        return obj.toString();
    }

    @Override
    public String create_key() throws Exception{
        String key = "";
        Random rd = new Random();

        for (int i = 0; i<8; i++) {
            key += rd.nextInt(10);
        }

        return key;
    }

    @Override
    public void sendEmail(MemberVo memberVo){

        MimeMessage message = mailSender.createMimeMessage();
        try{
            message.setSubject(
                    "REMA 회원가입 인증메일입니다.",
                    "UTF-8"
            );
            String htmlContent =
                    "<strong>안녕하세요</strong>, 반갑습니다.";
            message.setText(
                    htmlContent,
                    "UTF-8",
                    "html"
            );
            message.setFrom(new InternetAddress(FROM_ADDRESS));
            message.addRecipient(
                    MimeMessage.RecipientType.TO,
                    new InternetAddress(memberVo.getEmail()));
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }


//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(memberVo.getEmail());
//        message.setFrom(FROM_ADDRESS);
//        message.setSubject("REMA 회원가입 인증메일입니다.");
//        message.setText("안녕하세요");
//
//        mailSender.send(message);
    }

    @Override
    public void addMember(
            MemberVo memberVo
    ) throws Exception {
        memberVo.setApproval_key(create_key());
        memberDao.addMember(memberVo);

        sendEmail(memberVo);
    }
}
