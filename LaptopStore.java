import java.util.*;
import java.io.*;

class Laptop {
    private String brand;
    private int ram;
    private int storage;
    private String os;
    private String color;

    public Laptop(String brand, int ram, int storage, String os, String color) {
        this.brand = brand;
        this.ram = ram;
        this.storage = storage;
        this.os = os;
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public int getRam() {
        return ram;
    }

    public int getStorage() {
        return storage;
    }

    public String getOs() {
        return os;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return brand + "," + ram + "," + storage + "," + os + "," + color;
    }
}

public class LaptopStore {
    public static void main(String[] args) {
        // Загрузка ноутбуков из файла
        Set<Laptop> laptops = loadLaptopsFromFile("laptops.txt");
        
        // Фильтрация ноутбуков
        filterLaptops(laptops);
    }

    public static void filterLaptops(Set<Laptop> laptops) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Object> criteria = new HashMap<>();
        
        System.out.println("Введите цифру, соответствующую необходимому критерию:");
        System.out.println("1 - ОЗУ");
        System.out.println("2 - Объем ЖД");
        System.out.println("3 - Операционная система");
        System.out.println("4 - Цвет");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Считывание оставшейся новой строки

        switch (choice) {
            case 1:
                System.out.print("Введите минимальное значение ОЗУ (в ГБ): ");
                criteria.put("ram", scanner.nextInt());
                break;
            case 2:
                System.out.print("Введите минимальное значение объема ЖД (в ГБ): ");
                criteria.put("storage", scanner.nextInt());
                break;
            case 3:
                System.out.print("Введите операционную систему: ");
                criteria.put("os", scanner.nextLine());
                break;
            case 4:
                System.out.print("Введите цвет: ");
                criteria.put("color", scanner.nextLine());
                break;
            default:
                System.out.println("Неверный выбор");
                return;
        }

        Set<Laptop> filteredLaptops = filterLaptopsByCriteria(laptops, criteria);
        if (filteredLaptops.isEmpty()) {
            System.out.println("Нет ноутбуков, соответствующих заданным критериям.");
        } else {
            // Вывод отфильтрованных ноутбуков на консоль
            for (Laptop laptop : filteredLaptops) {
                System.out.println(laptop);
            }

            // Запись отфильтрованных ноутбуков в файл
            writeLaptopsToFile(filteredLaptops, "filtered_laptops.txt");
        }
    }

    private static Set<Laptop> filterLaptopsByCriteria(Set<Laptop> laptops, Map<String, Object> criteria) {
        Set<Laptop> filteredLaptops = new HashSet<>();

        for (Laptop laptop : laptops) {
            boolean matches = true;

            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                switch (key) {
                    case "ram":
                        if (value instanceof Integer) {
                            if (laptop.getRam() < (Integer) value) {
                                matches = false;
                            }
                        }
                        break;
                    case "storage":
                        if (value instanceof Integer) {
                            if (laptop.getStorage() < (Integer) value) {
                                matches = false;
                            }
                        }
                        break;
                    case "os":
                        if (value instanceof String) {
                            if (!laptop.getOs().equalsIgnoreCase((String) value)) {
                                matches = false;
                            }
                        }
                        break;
                    case "color":
                        if (value instanceof String) {
                            if (!laptop.getColor().equalsIgnoreCase((String) value)) {
                                matches = false;
                            }
                        }
                        break;
                    default:
                        matches = false;
                }
                if (!matches) break;
            }

            if (matches) {
                filteredLaptops.add(laptop);
            }
        }

        return filteredLaptops;
    }

    public static void writeLaptopsToFile(Set<Laptop> laptops, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Laptop laptop : laptops) {
                writer.write(laptop.toString() + System.lineSeparator());
            }
            System.out.println("Информация о ноутбуках успешно записана в файл " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public static Set<Laptop> loadLaptopsFromFile(String fileName) {
        Set<Laptop> laptops = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String brand = parts[0];
                    int ram = Integer.parseInt(parts[1]);
                    int storage = Integer.parseInt(parts[2]);
                    String os = parts[3];
                    String color = parts[4];
                    
                    laptops.add(new Laptop(brand, ram, storage, os, color));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }

        return laptops;
    }
}
