import java.sql.*; // Import untuk JDBC
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class SupermarketSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket"; // Ganti dengan URL database Anda
    private static final String DB_USER = "root"; // Ganti dengan username database Anda
    private static final String DB_PASSWORD = ""; // Ganti dengan password database Anda

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Login proses
            String username, password, captchaInput;
            String expectedUsername = "Treasure";
            String expectedPassword = "070820";
            String captcha = generateCaptcha();
            boolean loginSuccess = false;

            while (!loginSuccess) {
                System.out.println("+---------------------------------------------+");
                System.out.println("Log in");
                System.out.println("+---------------------------------------------+");
                System.out.print("Username : ");
                username = scanner.nextLine();
                System.out.print("Password : ");
                password = scanner.nextLine();
                System.out.print("Captcha  : " + captcha + "\nMasukkan Captcha: ");
                captchaInput = scanner.nextLine();

                if (username.equals(expectedUsername) && password.equals(expectedPassword) && captchaInput.equals(captcha)) {
                    System.out.println("Login berhasil!");
                    loginSuccess = true;
                } else {
                    System.out.println("Login gagal, silakan coba lagi.\n");
                    captcha = generateCaptcha();
                }
            }

            // Menu CRUD
            boolean running = true;
            while (running) {
                System.out.println("\n+---------------------------------------------+");
                System.out.println("Menu:");
                System.out.println("1. Tambah Data");
                System.out.println("2. Lihat Data");
                System.out.println("3. Update Data");
                System.out.println("4. Hapus Data");
                System.out.println("5. Keluar");
                System.out.print("Pilih opsi: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        tambahData(scanner);
                        break;
                    case 2:
                        lihatData();
                        break;
                    case 3:
                        updateData(scanner);
                        break;
                    case 4:
                        hapusData(scanner);
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateCaptcha() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            captcha.append(chars.charAt(randomIndex));
        }
        return captcha.toString();
    }

    private static void tambahData(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.print("Masukkan Kode Barang: ");
            String kodeBarang = scanner.nextLine();
            System.out.print("Masukkan Nama Barang: ");
            String namaBarang = scanner.nextLine();
            System.out.print("Masukkan Harga Barang: ");
            double hargaBarang = scanner.nextDouble();
            System.out.print("Masukkan Jumlah Barang: ");
            int jumlahBarang = scanner.nextInt();
            scanner.nextLine();

            String query = "INSERT INTO barang (kode_barang, nama_barang, harga_barang, jumlah_barang) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, kodeBarang);
            ps.setString(2, namaBarang);
            ps.setDouble(3, hargaBarang);
            ps.setInt(4, jumlahBarang);
            ps.executeUpdate();

            System.out.println("Data berhasil ditambahkan.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void lihatData() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM barang";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\nData Barang:");
            while (rs.next()) {
                System.out.printf("Kode: %s, Nama: %s, Harga: %.2f, Jumlah: %d\n",
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getDouble("harga_barang"),
                        rs.getInt("jumlah_barang"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateData(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.print("Masukkan Kode Barang yang akan diupdate: ");
            String kodeBarang = scanner.nextLine();
            System.out.print("Masukkan Nama Barang baru: ");
            String namaBarang = scanner.nextLine();
            System.out.print("Masukkan Harga Barang baru: ");
            double hargaBarang = scanner.nextDouble();
            System.out.print("Masukkan Jumlah Barang baru: ");
            int jumlahBarang = scanner.nextInt();
            scanner.nextLine();

            String query = "UPDATE barang SET nama_barang = ?, harga_barang = ?, jumlah_barang = ? WHERE kode_barang = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, namaBarang);
            ps.setDouble(2, hargaBarang);
            ps.setInt(3, jumlahBarang);
            ps.setString(4, kodeBarang);
            ps.executeUpdate();

            System.out.println("Data berhasil diupdate.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void hapusData(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.print("Masukkan Kode Barang yang akan dihapus: ");
            String kodeBarang = scanner.nextLine();

            String query = "DELETE FROM barang WHERE kode_barang = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, kodeBarang);
            ps.executeUpdate();

            System.out.println("Data berhasil dihapus.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
