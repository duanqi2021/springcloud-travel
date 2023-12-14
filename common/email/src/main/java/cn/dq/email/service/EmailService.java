package cn.dq.email.service;

import cn.dq.email.po.EmailModel;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Map;

@Service
@EnableAsync
public class EmailService {
    Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 将配置文件中的username注入到MAIL_USERNAME中, 这是发送人的邮箱地址
     */
    @Value("${spring.mail.username}")
    public String MAIL_USERNAME;

    /**
     * JavaMailSender类的对象, 是springboot自动装配的
     */
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * template 模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送简单的邮件(内容为纯文本邮件)
     * @param model
     * @return
     */
    public boolean sendSimpleEmail(EmailModel model) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        // 设置发件人邮箱
        simpleMailMessage.setFrom(MAIL_USERNAME);

        // 设置接收人账号
        simpleMailMessage.setTo(model.getRecipientMailbox());

        // 设置邮件标题
        simpleMailMessage.setSubject(model.getTitle());

        // 设置抄送人
        String[] ccMailbox = model.getCcMailbox();
        if (ArrayUtils.isNotEmpty(ccMailbox)) { // org.apache.commons.lang3.ArrayUtils
            simpleMailMessage.setCc(model.getCcMailbox());
        }

        // 加密抄送人邮箱
        String[] bccMailbox = model.getBccMailbox();
        if (ArrayUtils.isNotEmpty(bccMailbox)) {
            simpleMailMessage.setBcc(model.getBccMailbox()); // 加密抄送
        }

        // 发送日期
        simpleMailMessage.setSentDate(new Date());

        // 设置邮件正文内容
        simpleMailMessage.setText(model.getContent());

        // 发送邮件
        javaMailSender.send(simpleMailMessage);
        log.info("send simple email success!");
        return true;
    }

    /**
     * 发送富文本邮件（附件，图片，html等）
     *      使用MimeMessage来作为对象发送,
     *      但是邮件内容需要通过 MimeMessageHelper 对象来进行封装进MimeMessage 里
     * 注意:
     *      如果发送的内容包括html标签, 则需要 helper.setText(email.getContent(),true);
     *      第二个参数要为true,表示开启识别html标签.默认是false,也就是不识别.
     * @param model
     * @return
     */
    public boolean sendMimeEmail(EmailModel model) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            // 设置发件人邮箱
            helper.setFrom(MAIL_USERNAME);

            // 设置接收人账号
            helper.setTo(model.getRecipientMailbox());

            // 设置邮件标题
            helper.setSubject(model.getTitle());

            // 设置抄送人
            String[] ccMailbox = model.getCcMailbox();
            if (ArrayUtils.isNotEmpty(ccMailbox)) { // org.apache.commons.lang3.ArrayUtils
                helper.setCc(model.getCcMailbox());
            }

            // 加密抄送人邮箱
            String[] bccMailbox = model.getBccMailbox();
            if (ArrayUtils.isNotEmpty(bccMailbox)) {
                helper.setBcc(model.getBccMailbox()); // 加密抄送
            }

            // 发送日期
            helper.setSentDate(new Date());

            // 发送的内容包括html标签, 需要 helper.setText(email.getContent(),true); 开启html识别
            // 使用 MimeMessage 对象, 调用setText方法, 里面的字符串是带有html标签的字符串,
            // 在发送邮件的时候会自动解析正文中的html标签
            helper.setText(model.getContent(), true);

            // 发送邮件
            javaMailSender.send(mimeMessage);
            log.info("send mime email success!");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 使用 MimeMessage 发送富文本邮件（附件，图片，html等）
     * @param model
     * @return
     */
    public boolean sendAttachMimeEmail(EmailModel model) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            // 发送带附件的邮件, 需要加一个参数为true
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // 设置发件人邮箱
            helper.setFrom(MAIL_USERNAME);

            // 设置接收人账号
            helper.setTo(model.getRecipientMailbox());

            // 设置邮件标题
            helper.setSubject(model.getTitle());

            // 设置抄送人
            String[] ccMailbox = model.getCcMailbox();
            if (ArrayUtils.isNotEmpty(ccMailbox)) { // org.apache.commons.lang3.ArrayUtils
                helper.setCc(model.getCcMailbox());
            }

            // 加密抄送人邮箱
            String[] bccMailbox = model.getBccMailbox();
            if (ArrayUtils.isNotEmpty(bccMailbox)) {
                helper.setBcc(model.getBccMailbox()); // 加密抄送
            }

            // 发送日期
            helper.setSentDate(new Date());

            // 发送的内容包括html标签, 需要 helper.setText(email.getContent(),true); 开启html识别
            // 使用 MimeMessage 对象, 调用setText方法, 里面的字符串是带有html标签的字符串,
            // 在发送邮件的时候会自动解析正文中的html标签
            helper.setText(model.getContent(), true);

            Map<String, Object> enclosures = model.getEnclosures();

            // 创建要传递的附件对象
            if (!CollectionUtils.isEmpty(enclosures)) { // import org.springframework.util.CollectionUtils;
                for (String key : enclosures.keySet()) {
                    // File file = new File("D:\mybatis.doc");
                    File file = new File((String) enclosures.get(key));

                    // 通过MimeMessageHelper对象的addAttachment方法来传递附件
                    // 第一个参数为附件名, 第二个参数为File对象
                    helper.addAttachment(key, file);
                }
            }

            // 预览文件
            // helper.addAttachment("2.jpg", new File("E:\\pic\\2.jpg"));
            // 配合前端可以直接预览图片
            // helper.addInline("p01", new FileSystemResource(new File("E:\\pic\\2.jpg")));
            // helper.addInline("p02", new FileSystemResource(new File("E:\\pic\\2.jpg")));

            // 发送邮件
            javaMailSender.send(mimeMessage);
            log.info("send mime email success!");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送模板邮件 使用 thymeleaf 模板
     *   如果使用freemarker模板
     *       Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
     *       configuration.setClassForTemplateLoading(this.getClass(), "/templates");
     *       String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("mail.ftl"), params);
     * @param model
     * @return
     */
    public boolean sendTemplateMail(EmailModel model) {
        // 发一个复杂一点的邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // 设置发件人邮箱
            helper.setFrom(MAIL_USERNAME);

            // 设置接收人账号
            helper.setTo(model.getRecipientMailbox());

            // 设置邮件标题
            helper.setSubject(model.getTitle());

            // 设置抄送人
            String[] ccMailbox = model.getCcMailbox();
            if (ArrayUtils.isNotEmpty(ccMailbox)) { // org.apache.commons.lang3.ArrayUtils
                helper.setCc(model.getCcMailbox());
            }

            // 加密抄送人邮箱
            String[] bccMailbox = model.getBccMailbox();
            if (ArrayUtils.isNotEmpty(bccMailbox)) {
                helper.setBcc(model.getBccMailbox()); // 加密抄送
            }

            // 发送日期
            helper.setSentDate(new Date());

            // 使用模板thymeleaf
            // Context 是导这个包import org.thymeleaf.context.Context;
            Context context = new Context();
            // 定义模板数据
            context.setVariables(model.getEnclosures());
            // 获取thymeleaf的html模板, 指定模板路径
            // 在 Spring Boot 中, 模板引擎的默认配置已经将模板文件的根路径设置为 /src/main/resources/templates
            String emailContent = templateEngine.process("index.html", context);

            // 发送的内容包括html标签, 需要 helper.setText(email.getContent(),true); 开启html识别
            // 使用 MimeMessage 对象, 调用setText方法, 里面的字符串是带有html标签的字符串,
            // 发送邮件的时候会自动解析正文中的html标签
            helper.setText(emailContent, true);

            Map<String, Object> enclosures = model.getEnclosures();

            // 创建要传递的附件对象
            // import org.springframework.util.CollectionUtils;
            if (!CollectionUtils.isEmpty(enclosures)) {
                for (String key : enclosures.keySet()) {
                    // File file = new File(“D:\mybatis.doc”);
                    File file = new File((String)enclosures.get(key));
                    // 通过MimeMessageHelper对象的addAttachment方法来传递附件, 第一个参数为附件名, 第二个参数为FIle对象
                    helper.addAttachment(key, file);
                }
            }

            // 发送邮件
            javaMailSender.send(mimeMessage);
            log.info("send mime email success!");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}