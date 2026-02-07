package com.example.VirtualBank.common.constant;

public class Topics {
    
    // User Service Topics
    public static final String USER_CREATED = "user-service-create-user";
    public static final String USER_UPDATED = "user-service-update-user";
    public static final String USER_DELETED = "user-service-delete-user";
    
    // Account Service Topics (for future use)
    public static final String ACCOUNT_CREATED = "account-service-create-account";
    public static final String ACCOUNT_UPDATED = "account-service-update-account";
    public static final String ACCOUNT_DELETED = "account-service-delete-account";
    
    // Transaction Service Topics (for future use)
    public static final String TRANSACTION_CREATED = "transaction-service-create-transaction";
    public static final String TRANSACTION_COMPLETED = "transaction-service-complete-transaction";
    public static final String TRANSACTION_FAILED = "transaction-service-fail-transaction";
    
    // Notification Topics (for future use)
    public static final String NOTIFICATION_SEND = "notification-service-send";
    public static final String EMAIL_SEND = "email-service-send";
    public static final String SMS_SEND = "sms-service-send";
    
    // Audit Topics (for future use)
    public static final String AUDIT_LOG = "audit-service-log";
    
    private Topics() {
        // Utility class - prevent instantiation
    }
}
