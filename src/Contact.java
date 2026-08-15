import java.util.Objects;

public class Contact {

    //管理联系人
    private String name;
    private String phone;
    private String email;
    private String remark;


    public Contact() {
    }

    public Contact(String name, String phone, String email, String remark) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.remark = remark;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) && Objects.equals(phone, contact.phone) && Objects.equals(email, contact.email) && Objects.equals(remark, contact.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, remark);
    }

    public String toString() {
        return "连系人姓名: " + name + " 号码: " + phone + " 邮箱: " + email + " 备注: " + remark;
    }
}
