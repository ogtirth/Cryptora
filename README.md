# Cryptora - Offline Text Encryption System

**Cryptora** is a secure offline text encryption system built in Java, providing end-to-end encryption for messages using RSA public and private key pairs. Unlike typical messaging applications that require an internet connection, Cryptora operates entirely offline, ensuring privacy and security for local communication between users.

## Description

Cryptora uses asymmetric encryption to ensure that messages remain private and secure. Users can register their accounts, generate RSA key pairs, and securely exchange public keys to encrypt and decrypt messages. The system ensures that only the intended recipient with the corresponding private key can read the message, even if the message is intercepted.

The entire communication process is offline, and users interact with the system via a simple command-line interface. Cryptora is ideal for users who require secure messaging without relying on the internet.

## Features

- **User Registration:** Allows users to register and generate a unique RSA key pair for secure, offline communication.
- **Public Key Management:** Import and add public keys of other users to enable encrypted messaging.
- **Encrypted Messaging:** Send text messages securely by encrypting them with the recipient's public key.
- **Message Decryption:** Decrypt encrypted messages using your private key, ensuring the content is only accessible by the intended recipient.
- **User Switching:** Switch between different registered users to manage separate communication sessions.
- **Offline Operation:** Fully functional without internet connectivity, providing secure communication locally.
- **Simple CLI Interface:** Easy-to-use command-line interface to interact with the system and perform actions like sending and receiving encrypted messages.

## Technologies

- **Java** for implementing the core functionality and encryption.
- **RSA Encryption** for secure message encryption and decryption.
- **Base64 Encoding** for encoding keys and encrypted messages.

## Future Features

- **Storing/Importing Keys in Files:** Ability to store and import public/private keys from external files for easier key management.
- **Save Messages to Files:** Save sent and received messages to files for archival purposes or future reference.
- **Multiple Encryption Algorithms:** Support for other encryption algorithms in addition to RSA, like AES, for flexibility in message encryption.
- **Improved User Interface:** Transition from the command-line interface to a graphical user interface (GUI) for a better user experience.
- **Message Signing:** Implement message signing and verification for added authenticity and integrity of messages.

## How to Use

1. Clone the repository to your local machine.
2. Compile and run the `Cryptora.java` file in your terminal or Java IDE.
3. Follow the on-screen instructions to:
   - Register users.
   - Add/import public keys.
   - Send and receive encrypted messages.
   - Switch users and decrypt messages.

## Example Usage

1. **Register User:**
   - Enter a username to generate a key pair for secure messaging.
   
2. **Send Encrypted Message:**
   - Enter the recipient’s username and your message. The message will be encrypted and sent to the recipient.

3. **Decrypt Message:**
   - Enter the encrypted message to decrypt it using your private key.

## License

This project is open-source and available under the MIT License.

---

Feel free to contribute, report issues, or suggest features!
