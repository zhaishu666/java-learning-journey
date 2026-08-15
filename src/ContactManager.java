import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {

    private List<Contact> contactList = new ArrayList<>();

    public void addContact(Contact contact) {
        contactList.add(contact);
    }

    public boolean deleteContact(String name) {
        boolean deleResult = contactList.removeIf(contact -> contact.getName().equals(name));
        return deleResult;
    }

    public boolean updateContact(String name, Contact newInfo) {
        for (Contact contact : contactList) {
            if (contact.getName().equals(name)) {
                contact.setName(newInfo.getName());
                contact.setPhone(newInfo.getPhone());
                contact.setEmail(newInfo.getEmail());
                contact.setRemark(newInfo.getRemark());
                return true;
            }
        }
        return false;
    }

    public List<Contact> getAll() {
        return contactList;
    }

    public Contact searchByName(String name) {
        for (Contact contact : contactList) {
            if (contact.getName().equals(name)) {
                return contact;
            }
        }
        return null;
    }

    public void loadFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] FileMsg = line.split("&");
                if (FileMsg.length >= 4) {
                    contactList.add(new Contact(FileMsg[0], FileMsg[1], FileMsg[2], FileMsg[3]));
                } else {
                    System.out.println("跳过错误的格式: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Contact contact : contactList) {
                bw.write(contact.getName() + "&" + contact.getPhone() + "&" + contact.getEmail() + "&" + contact.getRemark());
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPhone(String phone) {
        return phone.matches("1[3-9]%d{9}");
    }

    public boolean checkName(String name) {
        if (name == null || name.length() == 0) {
            System.out.println("输入的姓名不合法");
            return false;
        } else
            return true;
    }
}
