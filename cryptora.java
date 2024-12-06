import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import javax.crypto.Cipher;

public class Cryptora {
    private static final int KEY_SIZE = 2048;
    private static final String USER_KEYS_FILE = "userKeys.dat";
    private static Map<String, PublicKey> publicKeys = new HashMap<>();
    private static final Map<String, List<String>> messageQueue = new HashMap<>();
    private static List<UserData> users = new ArrayList<>();
    private static UserData currentUser;

    public static void main(String[] args) {
        loadUserData();
        Scanner scanner = new Scanner(System.in);

        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("==== Cryptora - Offline Text Encryption System ====");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Register User (Generate Key Pair)");
            System.out.println("2. Login");
            System.out.println("3. Add/Import Public Key");
            System.out.println("4. List Users and Public Keys");
            System.out.println("5. Send Encrypted Message");
            System.out.println("6. Decrypt Message");
            System.out.println("7. Exit");
            System.out.print("Your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            System.out.print("\033[H\033[2J");
            System.out.flush();

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    addPublicKey(scanner);
                    break;
                case 4:
                    listUsers();
                    break;
                case 5:
                    sendEncryptedMessage(scanner);
                    break;
                case 6:
                    decryptMessage(scanner);
                    break;
                case 7:
                    saveUserData();
                    System.out.println("\nExiting...\n");
                    scanner.close();
                    return;
                default:
                    System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Enter username for registration: ");
        String username = scanner.nextLine();

        for (UserData user : users) {
            if (user.username.equals(username)) {
                System.out.println("\nUsername already exists! Choose a different name.\n");
                return;
            }
        }

        KeyPair newKeyPair = generateKeyPair();
        if (newKeyPair != null) {
            currentUser = new UserData(username, newKeyPair, true);
            users.add(currentUser);
            for (UserData user : users) {
                user.isLoggedIn = false;
            }
            currentUser.isLoggedIn = true;

            publicKeys.put(username, newKeyPair.getPublic());
            messageQueue.put(username, new ArrayList<>());

            saveUserData();
            System.out.println("\nUser registered successfully! Logged in as: " + username);
            System.out
                    .println("Public Key: " + Base64.getEncoder().encodeToString(newKeyPair.getPublic().getEncoded()));
        }
    }

    private static void loginUser(Scanner scanner) {
        System.out.print("Enter username to log in: ");
        String username = scanner.nextLine();

        for (UserData user : users) {
            if (user.username.equals(username)) {
                currentUser = user;
                for (UserData u : users) {
                    u.isLoggedIn = false;
                }
                currentUser.isLoggedIn = true;

                saveUserData();
                System.out.println("\nLogged in as: " + username + "\n");
                return;
            }
        }

        System.out.println("\nUser not found! Register first.\n");
    }

    private static void addPublicKey(Scanner scanner) {
        System.out.print("Enter the username for the public key: ");
        String username = scanner.nextLine();
        if (publicKeys.containsKey(username)) {
            System.out.println("\nUsername already exists! Updating public key.\n");
        }

        System.out.print("Enter the public key: ");
        String publicKeyString = scanner.nextLine();
        try {
            byte[] decodedKey = Base64.getDecoder().decode(publicKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
            publicKeys.put(username, publicKey);
            saveUserData();
            System.out.println("\nPublic key added for " + username + "\n");
        } catch (Exception e) {
            System.out.println("\nInvalid public key format.\n");
        }
    }

    private static void listUsers() {
        System.out.println("==== Registered Users and Public Keys ====\n");

        int index = 1;
        for (Map.Entry<String, PublicKey> entry : publicKeys.entrySet()) {
            System.out.printf("%d. Username: %s%n", index++, entry.getKey());
            System.out.println("   Public Key:");
            System.out.println(wrapText(Base64.getEncoder().encodeToString(entry.getValue().getEncoded()), 70));
            System.out.println();
        }

        System.out.println("===========================================\n");
    }

    private static String wrapText(String text, int lineLength) {
        StringBuilder wrappedText = new StringBuilder();
        for (int i = 0; i < text.length(); i += lineLength) {
            int end = Math.min(i + lineLength, text.length());
            wrappedText.append("   ").append(text, i, end).append("\n");
        }
        return wrappedText.toString();
    }

    private static void sendEncryptedMessage(Scanner scanner) {
        if (currentUser == null) {
            System.out.println("\nNo user logged in! Register or login first.\n");
            return;
        }

        System.out.print("Enter the recipient's username: ");
        String recipient = scanner.nextLine();
        if (!publicKeys.containsKey(recipient)) {
            System.out.println("\nRecipient not found! Add their public key first.\n");
            return;
        }

        System.out.print("Enter the message to encrypt: ");
        String message = scanner.nextLine();
        PublicKey recipientKey = publicKeys.get(recipient);
        String encryptedMessage = encryptMessage(message, recipientKey);

        if (encryptedMessage != null) {
            System.out.println("\nEncrypted message:");
            System.out.println(encryptedMessage);

            messageQueue.putIfAbsent(recipient, new ArrayList<>());
            messageQueue.get(recipient).add(currentUser.username + ": " + encryptedMessage);
            System.out.println("\nMessage sent to " + recipient + "\n");
        }
    }

    private static void decryptMessage(Scanner scanner) {
        if (currentUser == null) {
            System.out.println("\nNo user logged in! Register or login first.\n");
            return;
        }

        System.out.print("Enter the encrypted message to decrypt: ");
        String encryptedMessage = scanner.nextLine();
        String decryptedMessage = decryptMessage(encryptedMessage, currentUser.keyPair.getPrivate());

        if (decryptedMessage != null) {
            System.out.println("\nDecrypted message: " + decryptedMessage + "\n");
        } else {
            System.out.println("\nDecryption failed. Ensure the correct private key is being used.\n");
        }
    }

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(KEY_SIZE);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            System.out.println("\nError generating key pair: " + e.getMessage() + "\n");
            return null;
        }
    }

    private static String encryptMessage(String message, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.out.println("\nError encrypting message: " + e.getMessage() + "\n");
            return null;
        }
    }

    private static String decryptMessage(String encryptedMessage, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
            return new String(decryptedBytes);
        } catch (Exception e) {
            System.out.println("\nError decrypting message: " + e.getMessage() + "\n");
            return null;
        }
    }

    private static void saveUserData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER_KEYS_FILE))) {
            out.writeObject(users);
            out.writeObject(publicKeys);
        } catch (IOException e) {
            System.out.println("\nError saving user data: " + e.getMessage() + "\n");
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadUserData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USER_KEYS_FILE))) {
            users = (List<UserData>) in.readObject();
            publicKeys = (Map<String, PublicKey>) in.readObject();
            for (UserData user : users) {
                if (user.isLoggedIn) {
                    currentUser = user;
                    System.out.println("Logged in as: " + currentUser.username);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No user data file found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nError loading user data: " + e.getMessage() + "\n");
        }
    }

    static class UserData implements Serializable {
        private static final long serialVersionUID = 1L;
        String username;
        KeyPair keyPair;
        boolean isLoggedIn;

        UserData(String username, KeyPair keyPair, boolean isLoggedIn) {
            this.username = username;
            this.keyPair = keyPair;
            this.isLoggedIn = isLoggedIn;
        }
    }
}
