package com.api.formSync.Email;

public class EmailTemplate {
    public static String tokenBody(String name, String link) {
        return """
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Email Verification</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      line-height: 1.6;
                      margin: 0;
                      padding: 0;
                      background-color: #f4f4f4;
                    }
                    .email-container {
                      max-width: 600px;
                      margin: 20px auto;
                      padding: 20px;
                      background-color: #ffffff;
                      border-radius: 8px;
                      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    }
                    .button {
                      display: inline-block;
                      padding: 10px 20px;
                      margin: 20px auto;
                      font-size: 16px;
                      color: #ffffff;
                      background-color: #007bff;
                      text-decoration: none;
                      border-radius: 5px;
                    }
                  </style>
                </head>
                <body>
                  <div class="email-container">
                    <h2>Verify Your Email Address</h2>
                    <p>Dear $NAME,</p>
                    <p>Thank you for signing up with Form sync To complete your registration and ensure the security of your account, please verify your email address by clicking the button below:</p>
                    <a href="$LINK" class="button">Verify Email Address</a>
                    <p>If the button above doesn’t work, you can copy and paste the following URL into your browser:</p>
                    <p><a href="$LINK">$LINK</a></p>
                    <p>Please note that this link will expire in <b>15 minutes</b>, so be sure to verify your email soon.</p>
                    <p>If you did not create an account with us, please ignore this email</p>
                    <p>Thank you for choosing Form sync! We’re excited to have you on board.</p>
                  </div>
                </body>
                </html>
                """.replace("$NAME", name).replace("$LINK", link);
    }
}
