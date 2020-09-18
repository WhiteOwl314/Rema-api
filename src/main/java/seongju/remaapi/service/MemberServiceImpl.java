package seongju.remaapi.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import seongju.remaapi.config.Information;
import seongju.remaapi.dao.MemberDao;
import seongju.remaapi.vo.MemberVo;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@Service
@CrossOrigin
@Transactional
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MemberVo memberVo;

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
                htmlContent += "<form method='post' action='http://"+ Information.HOST_ADDRESS +"/member/approvalMember.do'>";
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

            } else if (kind.equals("updateEmail")){
                message.setSubject(
                        "REMA 이메일 변경 인증메일입니다.",
                        "UTF-8"
                );
                htmlContent += "<div align='center' style='border:1px solid black; font-family:verdana'>";
                htmlContent += "<h3 style='color: blue;'>";
                htmlContent += memberVo.getId() + "님 이메일 변경 인증메일입니다.</h3>";
                htmlContent += "<div style='font-size: 130%'>";
                htmlContent += "하단의 인증 버튼 클릭 시 정상적으로 이메일 변경 완료됩니다.</div><br/>";
                htmlContent += "<form method='post' action='http://"+ Information.HOST_ADDRESS +"/member/approvalUpdateEmail.do'>";
                htmlContent += "<input type='hidden' name='email' value='" + memberVo.getEmail() + "'>";
                htmlContent += "<input type='hidden' name='id' value='" + memberVo.getId() + "'>";
                htmlContent += "<input type='submit' value='인증'></form><br/></div>";
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

    }

    //회원가입
    @Override
    public JsonObject addMember(
            MemberVo memberVo
    ) throws Exception {
        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("addMemberToDb", false);
        bodyMessage.addProperty("sentEmail",false);
        bodyMessage.addProperty("info","[]");

        memberVo.setApproval_key(create_key());
        try{
            memberDao.addMember(memberVo);
        } catch (Exception e){
            e.printStackTrace();
            return bodyMessage;

        }
        try{
            sendEmail(memberVo, "addMember");
        } catch (Exception e) {
            e.printStackTrace();
            bodyMessage.addProperty("addMemberToDb", true);
            return bodyMessage;
        }

        bodyMessage.addProperty("addMemberToDb", true);
        bodyMessage.addProperty("sentEmail",true);

        return bodyMessage;
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
            out.println("alert('실패했습니다. 다시 시도해주세요.');");
            out.println("history.go(-1);");
            out.println("</script>");
            out.close();
        } else {
            // 이메일 인증을 성공하였을 경우
            out.println("<script>");
            out.println("alert('인증이 완료되었습니다. 로그인 후 이용하세요.');");
            out.println("location.href='http://"+ Information.FRONT_ADDRESS +"/member/login';");
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
        bodyMessage.addProperty("isLogOn",false);
        bodyMessage.addProperty("info","[]");

        if(memberDao.checkId(memberVo.getId()) == 0){
            return bodyMessage;
        } else {
            String pw = memberVo.getPw();
            memberVo = memberDao.login(memberVo.getId());

            //비밀번호가 다를 경우
            if(!memberVo.getPw().equals(pw)){
                bodyMessage.addProperty("idIsExisted", true);
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

                JsonArray memberVoJsonContainer = createMemberInfo(memberVo);

                bodyMessage.add("info",memberVoJsonContainer);

                HttpSession session = request.getSession();
                session.setAttribute("member",memberVo);
                session.setAttribute("isLogOn", true);
                bodyMessage.addProperty("isLogOn",true);

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
        bodyMessage.addProperty("sentEmail",false);
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
            bodyMessage.addProperty("sentEmail",true);

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
            MemberVo memberVo
    )throws Exception {

        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("updatePw",false);

        try{
            memberDao.updatePw(memberVo);
            bodyMessage.addProperty("updatePw",true);
            return bodyMessage;
        } catch (Exception e){
            return bodyMessage;
        }
    }

    @Override
    public JsonObject loginCheck(
            HttpServletRequest request
    ) {
        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("idIsExisted", false);
        bodyMessage.addProperty("pwIsCorrect", false);
        bodyMessage.addProperty("isLogOn", false);
        bodyMessage.addProperty("info", "[]");

        HttpSession session ;
        MemberVo memberVo;

        try{
            session = request.getSession();
            memberVo = (MemberVo) session.getAttribute("member");
        } catch (Exception e){
            return bodyMessage;
        }


        if (memberDao.checkId(memberVo.getId()) == 0) {
            return bodyMessage;
        } else {
            String pw = memberVo.getPw();
            memberVo = memberDao.login(memberVo.getId());

            //비밀번호가 다를 경우
            if (!memberVo.getPw().equals(pw)) {
                bodyMessage.addProperty("idIsExisted", true);
                return bodyMessage;
            } else {
                //회원정보 리턴
                bodyMessage.addProperty("idIsExisted", true);
                bodyMessage.addProperty("pwIsCorrect", true);


                JsonArray memberVoJsonContainer = createMemberInfo(memberVo);

                bodyMessage.add("info", memberVoJsonContainer);

                session.setAttribute("isLogOn", true);
                bodyMessage.addProperty("isLogOn", true);

                return bodyMessage;
            }
        }
    }

    @Override
    public JsonArray getMember(String username) {
        memberVo = memberDao.login(username);

        JsonArray memberInfo = createMemberInfo(memberVo);

        return memberInfo;
    }

    @Override
    public JsonObject sendEmailForUpdateEmail(MemberVo memberVo) {

        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("emailNotExisted",false);
        bodyMessage.addProperty("sentEmail",false);

        if(memberDao.checkEmail(memberVo.getEmail()) == 0){
            bodyMessage.addProperty("emailNotExisted",true);
        } else {
            return bodyMessage;
        }

        try{
            sendEmail(memberVo, "updateEmail");
            bodyMessage.addProperty("sentEmail",true);
            return bodyMessage;
        } catch (Exception e){
            return bodyMessage;
        }
    }

    @Override
    public void approvalUpdateEmail(
            MemberVo memberVo,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (memberDao.updateEmail(memberVo) == 0) {
            // 이메일 변경에 실패하였을 경우
            out.println("<script>");
            out.println("alert('실패했습니다. 다시 시도해주세요.');");
            out.println("history.go(-1);");
            out.println("</script>");
            out.close();
        } else {
            // 이메일 인증을 성공하였을 경우
            out.println("<script>");
            out.println("alert('이메일 변경 되었습니다.');");
            out.println("history.go(-1);");
            out.println("</script>");
            out.close();
        }

    }

    @Override
    public JsonObject updateName(
            MemberVo memberVo
    ) {
        JsonObject bodyMessage = new JsonObject();
        bodyMessage.addProperty("updateName",false);

        try{
            memberDao.updateName(memberVo);
            bodyMessage.addProperty("updateName",true);
            return bodyMessage;
        } catch (Exception e){
            return bodyMessage;
        }
    }

    public JsonArray createMemberInfo(MemberVo memberVo){

        JsonArray memberVoJsonContainer = new JsonArray();
        JsonObject memberVoJson = new JsonObject();
        memberVoJson.addProperty("id", memberVo.getId());
        memberVoJson.addProperty("level", memberVo.getLevel());
        memberVoJson.addProperty("name", memberVo.getName());
        memberVoJson.addProperty("email", memberVo.getEmail());
        memberVoJson.addProperty("isDeleted", memberVo.getIsDeleted());
        memberVoJson.addProperty("log_data", memberVo.getLog_date());
        memberVoJson.addProperty("reg_date", memberVo.getReg_date());
        memberVoJson.addProperty("approval_status", memberVo.getApproval_status());

        memberVoJsonContainer.add(memberVoJson);

        return memberVoJsonContainer;
    }

}
