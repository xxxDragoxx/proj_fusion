package td;

import java.sql.SQLException;
import java.util.*;

import static td.OrderManage.orderDatabase;
import static td.Pro.productDatabase;
public class ShoppingCart {
    // ���ﳵ
    static Map<Integer, Integer> shoppingCart = new HashMap<>();
    public static void home() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== ���ﳵģ�� =====");
            System.out.println("1. �鿴���ﳵ");
            System.out.println("2. ������Ʒ�����ﳵ");
            System.out.println("3. �ӹ��ﳵ�Ƴ���Ʒ");
            System.out.println("4. ���㹺�ﳵ");
            System.out.println("0. ������ҳ");

            System.out.print("��ѡ������������Ӧ�����֣���");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayShoppingCart();
                    break;
                case 2:
                    addToShoppingCart();
                    break;
                case 3:
                    removeFromShoppingCart();
                    break;
                case 4:
                    checkout();
                    break;
                case 0:
                    Home.list();
                    return;
                default:
                    System.out.println("��Ч��ѡ�����������롣");
            }
        }
    }

    private static void displayShoppingCart() {
        System.out.println("===== ���ﳵ�б� =====");
        if(!shoppingCart.isEmpty())
        for (Map.Entry<Integer, Integer> entry : shoppingCart.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            Pro product = getProductById(productId);
            double subtotal = quantity * product.getPrice();

            System.out.println("��ƷID: " + productId +
                    ", ��Ʒ����: " + product.getName() +
                    ", ����: " + product.getPrice() +
                    ", ����: " + quantity +
                    ", С��: " + subtotal);
        }
        else{
            System.out.println("���ﳵ��տ���Ҳ...");
        }
    }

    private static void addToShoppingCart() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("������Ҫ���ӵ����ﳵ����ƷID��");
        int productId = scanner.nextInt();

        Pro product = getProductById(productId);

        if (product != null) {
            System.out.print("������Ҫ�����������");
            int quantity = scanner.nextInt();

            if (quantity > 0 && quantity <= product.getQuantity()) {
                // �����Ʒ�Ƿ��Ѿ��ڹ��ﳵ��
                if (shoppingCart.containsKey(productId)) {
                    // ����ǣ��ۻ�����
                    int currentQuantity = shoppingCart.get(productId);
                    shoppingCart.put(productId, currentQuantity + quantity);
                } else {
                    // ������ǣ�����Ʒ���ӵ����ﳵ
                    shoppingCart.put(productId, quantity);
                }

                System.out.println("��Ʒ�����ӵ����ﳵ��");
            } else {
                System.out.println("��Ч�����������������롣");
            }
        } else {
            System.out.println("δ�ҵ�����ƷID���������롣");
        }
    }

    private static void removeFromShoppingCart() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("������Ҫ�Ƴ�����ƷID��");
        int productId = scanner.nextInt();

        if (shoppingCart.containsKey(productId)) {
            shoppingCart.remove(productId);
            System.out.println("��Ʒ�Ѵӹ��ﳵ�Ƴ���");
        } else {
            System.out.println("���ﳵ��δ�ҵ�����ƷID���������롣");
        }
    }

    private static void checkout() {
        double totalAmount = 0.0;

        System.out.println("===== ���� =====");
        for (Map.Entry<Integer, Integer> entry : shoppingCart.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            Pro product = getProductById(productId);
            double subtotal = quantity * product.getPrice();
            totalAmount += subtotal;

            System.out.println("��ƷID: " + productId +
                    ", ��Ʒ����: " + product.getName() +
                    ", ����: " + product.getPrice() +
                    ", ����: " + quantity +
                    ", С��: " + subtotal);
            orderDatabase.put(entry.getKey(),entry.getValue());
        }

        System.out.println("�ܼƽ��: " + totalAmount);
        System.out.println("������ɣ�лл���٣�");
        shoppingCart.clear();
    }

    private static void sortProductDatabase() {
        Collections.sort(productDatabase, Comparator.comparingInt(Pro::getId));
    }

    private static Pro getProductById(int productId) {
        // ȷ����Ʒ���ݿⰴ��ID����
        sortProductDatabase();

        int left = 0;
        int right = productDatabase.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Pro currentProduct = productDatabase.get(mid);

            if (currentProduct.getId() == productId) {
                return currentProduct;
            } else if (currentProduct.getId() < productId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return null;
    }
}
