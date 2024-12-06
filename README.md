# Cryptora - Offline Text Encryption System

**Cryptora** is a secure offline text encryption system that allows users to:
- Register and manage user accounts with RSA key pairs.
- Encrypt and send messages securely to other users.
- Import public keys to communicate with external users.
- Decrypt messages received from other users.

This system ensures that users' private keys and public keys are persistently stored for future use, eliminating the need for key regeneration.

---

## Features
1. **User Registration**  
   - Create a new user account with an RSA key pair.
   - Automatically logs in after successful registration.

2. **Login**  
   - Log in as an existing user to access their private key and messages.

3. **Add/Import Public Key**  
   - Add a new user's public key to communicate securely.

4. **List Users and Public Keys**  
   - View all registered users and their public keys.

5. **Send Encrypted Message**  
   - Encrypt and send a message to a recipient using their public key.

6. **Decrypt Message**  
   - Decrypt messages using the logged-in user's private key.

7. **Persistent Storage**  
   - User data (keys and public keys) is stored in a file (`userKeys.dat`) for reuse across sessions.

---

## How to Run
### Prerequisites
- **Java 8 or higher** installed.
- A code editor or terminal to compile and run the program.

### Steps
1. **Clone or Download the Repository**
   ```bash
   git clone https://github.com/your-repo/Cryptora.git
   cd Cryptora
   ```
2. **Compile the Program**
   ```bash
   javac Cryptora.java
   ```
3. **Run the Program**
   ```bash
   java Cryptora
   ```
---

## File Structure
1. **Cryptora.java:** The main application file.
2. **userKeys.dat:** Stores user data (keys and public keys).

---

### Usage

1. **Register a User**
Select option 1 to register a new user. Provide a unique username to generate an RSA key pair.

2. **Login**
Select option 2 to log in to an existing account.

3. **Add Public Key**
Select option 3 to import a public key. You'll need the username and Base64-encoded public key.

4. **List Users**
Select option 4 to view all registered users and their public keys.

5. **Send Message**
Select option 5 to encrypt and send a message. Choose a recipient by username.

6. **Decrypt Message**
Select option 6 to decrypt a received encrypted message.

7. **Exit**
Select option 7 to save all data and exit the application.

---

### Example Workflow

User A registers and generates an RSA key pair.
User B registers and generates their own RSA key pair.
User A adds User B's public key using the "Add Public Key" option.
User A sends an encrypted message to User B.
User B decrypts the message using their private key.

---

### Persistent Storage
User accounts and their respective keys are saved in the userKeys.dat file.
This file ensures that users' data persists across program restarts.

---

### Notes
Always ensure the userKeys.dat file is kept secure, as it contains sensitive key data.
The encryption is performed using the RSA algorithm with a key size of 2048 bits

---

### Contributing
Feel free to fork the repository and submit pull requests for new features or bug fixes.

---

### License
This project is licensed under the **MIT License**.

