package notifiers;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.SimpleEmail;
import play.libs.Mail;
import play.mvc.Mailer;


public class Mails extends Mailer {
    public static void welcome() {
        setSubject("Welcome !!!");
        addRecipient("1075656423@qq.com");
        setFrom("2472937751@qq.com");
        send();
    }
}