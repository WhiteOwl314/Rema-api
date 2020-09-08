package seongju.remaapi.service;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import seongju.remaapi.config.Information;
import seongju.remaapi.dao.MemberDao;
import seongju.remaapi.vo.MemberVo;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Random;

@Service
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JavaMailSender mailSender;

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
            String htmlContent = "";
            htmlContent += "<div align='center' style='border:1px solid black; font-family:verdana'>";
            htmlContent += "<h3 style='color: blue;'>";
            htmlContent += memberVo.getId() + "님 회원가입을 환영합니다.</h3>";
            htmlContent += "<div style='font-size: 130%'>";
            htmlContent += "하단의 인증 버튼 클릭 시 정상적으로 회원가입이 완료됩니다.</div><br/>";
            htmlContent += "<form method='post' action='http://"+ Information.HOST_ADDRESS +"/member/approval_member.do'>";
            htmlContent += "<input type='hidden' name='email' value='" + memberVo.getEmail() + "'>";
            htmlContent += "<input type='hidden' name='approval_key' value='" + memberVo.getApproval_key() + "'>";
            htmlContent += "<input type='submit' value='인증'></form><br/></div>";

            message.setText(
                    htmlContent,
                    "UTF-8",
                    "html"
            );
            message.setFrom(new InternetAddress(Information.FROM_ADDRESS));
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

    @Override
    public void approval_member(
            MemberVo memberVo,
            HttpServletResponse response
    ) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (memberDao.approval_member(memberVo) == 0) {
            // 이메일 인증에 실패하였을 경우
            out.println("<script>");
            out.println("alert('잘못된 접근입니다.');");
            out.println("history.go(-1);");
            out.println("</script>");
            out.close();
        } else {
            // 이메일 인증을 성공하였을 경우
            out.println("<script>");
            out.println("alert('인증이 완료되었습니다. 로그인 후 이용하세요.');");
            out.println("location.href='"+ Information.FRONT_ADDRESS +"';");
            out.println("</script>");
            out.close();
        }
    }
}
