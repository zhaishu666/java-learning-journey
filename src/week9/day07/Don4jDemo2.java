package week9.day07;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Don4jDemo2 {
    public static void main(String[] args) throws DocumentException {

        ArrayList<User> list = new ArrayList<>();
        SAXReader saxReader = new SAXReader();

        File file = new File("src/week9/day07/user.xml");
        Document document = saxReader.read(file);
        Element root = document.getRootElement();

        List<Element> elements = root.elements();
        for (Element element : elements) {
            Attribute id = element.attribute("id");
            String idValue = id.getText();

            Element username = element.element("username");
            String usernameValue = username.getText();
            Element password = element.element("password");
            String passwordValue = password.getText();
            Element phoneId = element.element("phoneid");
            String phoneIdValue = phoneId.getText();
            Element admin = element.element("admin");
            boolean adminValue = Boolean.parseBoolean(admin.getText());

            User user = new User(idValue,usernameValue, passwordValue, phoneIdValue, adminValue);
            list.add(user);
        }
        for (User user : list) {
            System.out.println(user);
        }
    }
}
