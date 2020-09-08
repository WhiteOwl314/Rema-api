package seongju.remaapi.service;

import com.google.gson.JsonArray;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    public boolean checkIdBoolean(String id) {

        boolean idIsExisted ;

        int idCount = memberDao.checkId(id);

        if(idCount == 0){
            idIsExisted = false;
        }else{
            idIsExisted = true;
        }

        return idIsExisted;
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

    //이메일 보내기
    @Override
    public void sendEmail(
            MemberVo memberVo,
            String kind
    ) throws Exception{

        MimeMessage message = mailSender.createMimeMessage();
        String htmlContent = "";
        try{
            if(kind.equals("addMember")){
                message.setSubject(
                        "REMA 회원가입 인증메일입니다.",
                        "UTF-8"
                );
                htmlContent += "<div align='center' style='border:1px solid black; font-family:verdana'>";
                htmlContent += "<h3 style='color: blue;'>";
                htmlContent += memberVo.getId() + "님 회원가입을 환영합니다.</h3>";
                htmlContent += "<div style='font-size: 130%'>";
                htmlContent += "하단의 인증 버튼 클릭 시 정상적으로 회원가입이 완료됩니다.</div><br/>";
                htmlContent += "<form method='post' action='http://"+ Information.HOST_ADDRESS +"/member/approval_member.do'>";
                htmlContent += "<input type='hidden' name='email' value='" + memberVo.getEmail() + "'>";
                htmlContent += "<input type='hidden' name='approval_key' value='" + memberVo.getApproval_key() + "'>";
                htmlContent += "<input type='submit' value='인증'></form><br/></div>";

            } else if (kind.equals("findPw")){
                message.setSubject(
                        "REMA 임시 비밀번호입니다.",
                        "UTF-8"
                );
                htmlContent += "<div align='center' style='border:1px solid black; font-family:verdana'>";
                htmlContent += "<h3 style='color: blue;'>";
                htmlContent += memberVo.getId() + "님의 임시 비밀번호입니다. 비밀번호를 변경하여 사용하세요.</h3>";
                htmlContent += "<p>임시 비밀번호: ";
                htmlContent += memberVo.getPw() + "</p></div>";

            }

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
    }

    //회원가입
    @Override
    public void addMember(
            MemberVo memberVo
    ) throws Exception {
        memberVo.setApproval_key(create_key());
        memberDao.addMember(memberVo);

        sendEmail(memberVo, "addMember");
    }

    //이메일 인증
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

    @Override
    public JsonObject login(
            MemberVo memberVo,
            HttpServletRequest request
    ) throws IOException {

        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("idIsExisted", false);
        bodyMessage.addProperty("pwIsCorrect",false);
        bodyMessage.addProperty("emailIsAllowed", false);
        bodyMessage.addProperty("info","[]");

        if(memberDao.checkId(memberVo.getId()) == 0){
            return bodyMessage;
        } else {
            String pw = memberVo.getPw();
            memberVo = memberDao.login(memberVo.getId());

            //비밀번호가 다를 경우
            if(!memberVo.getPw().equals(pw)){
                bodyMessage.addProperty("idExisted", true);
                return bodyMessage;
            } else if (!memberVo.getApproval_status().equals("1")){
                //이메일 인증이 필요한 경우
                bodyMessage.addProperty("idIsExisted", true);
                bodyMessage.addProperty("pwIsCorrect",true);
                return bodyMessage;
            } else {
                //로그인 일자 업데이트 및 회원정보 리턴
                memberDao.update_log(memberVo.getId());
                bodyMessage.addProperty("idIsExisted", true);
                bodyMessage.addProperty("pwIsCorrect",true);
                bodyMessage.addProperty("emailIsAllowed", true);

                JsonArray memberVoJsonContainer = new JsonArray();
                JsonObject memberVoJson = new JsonObject();
                memberVoJson.addProperty("id",memberVo.getId());
                memberVoJson.addProperty("level",memberVo.getLevel());
                memberVoJson.addProperty("name",memberVo.getName());
                memberVoJson.addProperty("email",memberVo.getEmail());
                memberVoJson.addProperty("isDeleted",memberVo.getIsDeleted());
                memberVoJson.addProperty("log_data",memberVo.getLog_date());
                memberVoJson.addProperty("reg_date",memberVo.getReg_date());
                memberVoJson.addProperty("approval_status",memberVo.getApproval_status());

                memberVoJsonContainer.add(memberVoJson);

                bodyMessage.add("info",memberVoJsonContainer);

                HttpSession session = request.getSession();
                session.setAttribute("member",memberVo);
                session.setAttribute("isLogOn", true);

                return bodyMessage;
            }
        }
    }

    @Override
    public JsonObject findId(String email) {

        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("idIsExisted", false);
        bodyMessage.addProperty("id","");

        String id = memberDao.findId(email);

        if (id == null){
            return bodyMessage;
        } else {
            bodyMessage.addProperty("idIsExisted", true);
            bodyMessage.addProperty("id",id);
            return bodyMessage;
        }
    }

    @Override
    public JsonObject findPw(
            String id,
            String email
    ) throws Exception {
        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("idIsExisted", false);
        bodyMessage.addProperty("emailIsCorrect", false);
        bodyMessage.addProperty("sendEmail",false);
        bodyMessage.addProperty("info","[]");

        if(memberDao.checkId(id) == 0){
            return bodyMessage;
        } else if(
                !email.equals(memberDao.login(id).getEmail())
        ){
            bodyMessage.addProperty("idIsExisted", true);
            return bodyMessage;
        } else {
            //임시 비밀번호 생성
            String pw = "";
            for (int i = 0; i < 12; i++) {
                pw += (char) ((Math.random() * 26) + 97);
            }
            MemberVo memberVo = new MemberVo();
            memberVo.setId(id);
            memberVo.setPw(pw);
            memberVo.setEmail(email);

            //비밀번호 변경
            memberDao.updatePw(memberVo);
            //비밀번호 변경 메일 발송
            sendEmail(memberVo, "findPw");

            bodyMessage.addProperty("idIsExisted", true);
            bodyMessage.addProperty("emailIsCorrect", true);
            bodyMessage.addProperty("sendEmail",true);

            JsonArray memberVoJsonContainer = new JsonArray();
            JsonObject memberVoJson = new JsonObject();
            memberVoJson.addProperty("id",memberVo.getId());
            memberVoJson.addProperty("email",memberVo.getEmail());

            memberVoJsonContainer.add(memberVoJson);

            bodyMessage.add("info",memberVoJsonContainer);

            return bodyMessage;
        }
    }

    @Override
    public JsonObject updatePw(
            MemberVo memberVo,
            String oldPw,
            HttpServletRequest request
    )throws Exception {

        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("pwIsCorrect", false);
        bodyMessage.addProperty("info","[]");

        //옛 비밀번호가 다르다면
        if(
                !oldPw.equals(memberDao.login(
                        memberVo.getId()).getPw())
        ){
            return bodyMessage;
        } else {
            memberDao.updatePw(memberVo);
            memberVo = memberDao.login(memberVo.getId());

            bodyMessage.addProperty("pwIsCorrect", true);
            bodyMessage.addProperty("info","[]");

            JsonArray memberVoJsonContainer = new JsonArray();
            JsonObject memberVoJson = new JsonObject();
            memberVoJson.addProperty("id",memberVo.getId());
            memberVoJson.addProperty("level",memberVo.getLevel());
            memberVoJson.addProperty("name",memberVo.getName());
            memberVoJson.addProperty("email",memberVo.getEmail());
            memberVoJson.addProperty("isDeleted",memberVo.getIsDeleted());
            memberVoJson.addProperty("log_data",memberVo.getLog_date());
            memberVoJson.addProperty("reg_date",memberVo.getReg_date());
            memberVoJson.addProperty("approval_status",memberVo.getApproval_status());
            memberVoJsonContainer.add(memberVoJson);

            bodyMessage.add("info",memberVoJsonContainer);

            //세션 업데이트
            HttpSession session = request.getSession();
            session.setAttribute("member", memberVo);

            return bodyMessage;
        }
    }
}
