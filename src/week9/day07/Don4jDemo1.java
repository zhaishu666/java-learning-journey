package week9.day07;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Don4jDemo1 {
    static void main() throws DocumentException {

        ArrayList<Element> list = new ArrayList<>();
        File file = new File("src/week9/day07/Test2.xml");
        //创建解析器对象
        SAXReader saxReader = new SAXReader();

        Document document = saxReader.read(file);

        //自己写的时候一定要一层一层地解析
        Element rootElement = document.getRootElement();
        //获得所有的子标签
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {

            Element element1 = element.element("书名");
            String bookName = element1.getText();
            Element element2 = element.element("作者");
            String author = element2.getText();
            Element element3 = element.element("售价");

            double price = Double.parseDouble(element3.getText());
        }
    }
}
