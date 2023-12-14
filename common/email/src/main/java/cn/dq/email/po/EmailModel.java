package cn.dq.email.po;


import java.io.Serializable;
import java.util.Map;

/**
 * @author test 2023-05-08 10:52:59
 */
public class EmailModel implements Serializable {
    /**
     * 收件人邮箱
     */
    private String[] recipientMailbox;

    /**
     * 邮件正文内容
     */
    private String content;

    /**
     * 邮件主题
     */
    private String title;

    /**
     * 抄送人邮箱
     */
    private String[] ccMailbox;

    /**
     * 加密抄送人邮箱
     */
    private String[] bccMailbox;

    /**
     * 真实名称/附件路径
     * enclosures： {“fileNmae”: "filePath+fileNmae"}
     */
    private Map<String, Object> enclosures;

    //    附件是否添加的到正文,默认false不添加
    //    private Boolean is_;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getRecipientMailbox() {
        return recipientMailbox;
    }

    public void setRecipientMailbox(String[] recipientMailbox) {
        this.recipientMailbox = recipientMailbox;
    }

    public String[] getCcMailbox() {
        return ccMailbox;
    }

    public void setCcMailbox(String[] ccMailbox) {
        this.ccMailbox = ccMailbox;
    }

    public String[] getBccMailbox() {
        return bccMailbox;
    }

    public void setBccMailbox(String[] bccMailbox) {
        this.bccMailbox = bccMailbox;
    }

    public Map<String, Object> getEnclosures() {
        return enclosures;
    }

    public void setEnclosures(Map<String, Object> enclosures) {
        this.enclosures = enclosures;
    }
}