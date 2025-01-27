import java.util.*;

// Abstract Class: Role
abstract class Role {
    protected String name;

    public Role(String name) {
        this.name = name;
    }

    // Abstract Method
    public abstract void showRoleDetails();
}

// Admin Role
class AdminRole extends Role {
    public AdminRole(String name) {
        super(name);
    }

    @Override
    public void showRoleDetails() {
        System.out.println("Role: Admin, Name: " + name + ", Permissions: Full access to system management.");
    }
}

// Member Role
class MemberRole extends Role {
    public MemberRole(String name) {
        super(name);
    }

    @Override
    public void showRoleDetails() {
        System.out.println("Role: Member, Name: " + name + ", Permissions: Limited to workout access.");
    }
}

class PermissionCard {
    private Set<String> permissions;
    private List<String> auditLog;

    public PermissionCard() {
        this.permissions = new HashSet<>();
        this.auditLog = new ArrayList<>();
    }

    public void addPermission(String permission) {
        permissions.add(permission);
        auditLog.add("เพิ่มสิทธิ์: " + permission);
    }

    public void revokePermission(String permission) {
        permissions.remove(permission);
        auditLog.add("ลบสิทธิ์: " + permission);
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    @Override
    public String toString() {
        return "สิทธิ์ทั้งหมดตอนนี้: " + permissions;
    }

    public void showAuditLog() {
        System.out.println("ประวัติการเปลี่ยนแปลงสิทธิ์:");
        for (String log : auditLog) {
            System.out.println(log);
        }
    }
}

class User {
    private Role role;
    private PermissionCard card;

    public User(Role role) {
        this.role = role;
        this.card = new PermissionCard();
    }

    public Role getRole() {
        return role;
    }

    public PermissionCard getCard() {
        return card;
    }

    @Override
    public String toString() {
        return role.name + ", " + card;
    }
}

public class Main {
    public static void main(String[] args) {
        // สร้างผู้ใช้โดยใช้ Upcasting (Admin และ Member เป็น Role)
        Role adminRole = new AdminRole("แอดมิน");
        Role memberRole = new MemberRole("จอห์น");

        // ตรวจสอบชนิดด้วย instanceof
        if (adminRole instanceof AdminRole) {
            System.out.println("adminRole เป็น AdminRole");
        }

        // สร้างผู้ใช้
        User admin = new User(adminRole);
        User member = new User(memberRole);

        // เพิ่มสิทธิ์ให้ผู้ใช้แต่ละคน
        admin.getCard().addPermission("จัดการผู้ใช้");
        member.getCard().addPermission("เข้าถึงคอร์สออกกำลังกายพื้นฐาน");

        // แสดงบทบาท
        admin.getRole().showRoleDetails();
        member.getRole().showRoleDetails();

        // แสดงข้อมูลผู้ใช้
        System.out.println(admin);
        System.out.println(member);

        // ลบสิทธิ์
        member.getCard().revokePermission("เข้าถึงคอร์สออกกำลังกายพื้นฐาน");

        // แสดง Audit Log
        member.getCard().showAuditLog();
    }
}
