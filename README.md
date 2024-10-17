# utility-service

MyBank is a newly formed entity which is driven by the idea of setting the standards for all new modern banks. Currently they have implemented couple of basic banking functionalities like account management, transfers, withdrawals, deposits.

The next step for them is to add a new feature for paying of utility bills, where the bank would provide its customers the ability to subscribe to utility providers by using their provider's subscription id. After a customer has subscribed to a said provider, they would receive a notification(s) SMS/EMAIL/PUSH as soon as a new bill is available.

The contact information is stored on a separate service which due to the rapid development has become slow.

You would need to create couple of services:

-service that contains user information, such as email, phone number and list of subscriptions
-service that would produce dummy bills
-service that would listen for incoming bills and send notifications to customers (notifications could be logged in the console in the following format: "Received a bill for subscription={subscription} from provider={provider} for {sum} {currency}. Sending {notification type} to {contact info}")

<img width="1236" alt="Diagram" src="https://github.com/user-attachments/assets/10f71e0f-454a-4a60-88b2-23756c58e0cb">
