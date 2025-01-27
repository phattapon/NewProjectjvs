import java.util.*;

class PermissionCard {
    private Set<String> permissions; // สิทธิ์ที่ผู้ใช้มี
    private List<String> auditLog; // บันทึกการเปลี่ยนแปลงสิทธิ์

    public PermissionCard() {
        this.permissions = new HashSet<>();
        this.auditLog = new ArrayList<>();
    }

    // เพิ่มสิทธิ์
    public void addPermission(String permission) {
        permissions.add(permission);
        auditLog.add("เพิ่มสิทธิ์: " + permission);
    }

    // ลบสิทธิ์
    public void revokePermission(String permission) {
        permissions.remove(permission);
        auditLog.add("ลบสิทธิ์: " + permission);
    }

    // ตรวจสอบสิทธิ์
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    // แสดงสิทธิ์ทั้งหมด
    @Override
    public String toString() {
        return "สิทธิ์ทั้งหมดตอนนี้: " + permissions;
    }

    // แสดงประวัติการเปลี่ยนแปลง
    public void showAuditLog() {
        System.out.println("ประวัติการเปลี่ยนแปลงสิทธิ์:");
        for (String log : auditLog) {
            System.out.println(log);
        }
    }
}

// คลาส User
class User {
    private String name; // ชื่อผู้ใช้
    private PermissionCard card; // บัตรสิทธิ์ของผู้ใช้

    public User(String name) {
        this.name = name;
        this.card = new PermissionCard();
    }

    public PermissionCard getCard() {
        return card;
    }

    @Override
    public String toString() {
        return "ชื่อผู้ใช้: " + name + ", " + card;
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {
        // สร้างผู้ใช้
        User admin = new User("แอดมิน");
        User member = new User("จอห์น");

        // เพิ่มสิทธิ์ให้ผู้ใช้แต่ละคน
        admin.getCard().addPermission("จัดการผู้ใช้");
        admin.getCard().addPermission("ดูประวัติทั้งหมด");
        member.getCard().addPermission("เข้าถึงคอร์สออกกำลังกายพื้นฐาน");

        // แสดงข้อมูลผู้ใช้
        System.out.println(admin);
        System.out.println(member);

        // ลบสิทธิ์ของสมาชิก
        member.getCard().revokePermission("เข้าถึงคอร์สออกกำลังกายพื้นฐาน");

        // แสดง Audit Log
        System.out.println("\n=== Audit Log ของ Admin ===");
        admin.getCard().showAuditLog();

        System.out.println("\n=== Audit Log ของ Member ===");
        member.getCard().showAuditLog();

        // ตรวจสอบสิทธิ์
        System.out.println("\nตรวจสอบสิทธิ์:");
        if (member.getCard().hasPermission("เข้าถึงคอร์สออกกำลังกายพื้นฐาน")) {
            System.out.println("จอห์นมีสิทธิ์เข้าถึงคอร์สพื้นฐาน");
        } else {
            System.out.println("จอห์นไม่มีสิทธิ์เข้าถึงคอร์สพื้นฐาน");
        }
    }
}
