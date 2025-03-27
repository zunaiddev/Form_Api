package com.api.formSync.Email;

import jakarta.validation.constraints.NotNull;

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
                      border: 2px solid white;
                      box-shadow: 0 0 10px rgba(255, 255, 255, 0.1);
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
                     a{
                        word-break: break-all;
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
                \s""".replace("$NAME", name).replace("$LINK", link);
    }

    public static String adminBody(String name) {
        return "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Thank You for Contacting Zunaid</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            padding: 20px 0;\n" +
                "        }\n" +
                "        .header h1 {\n" +
                "            color: #333333;\n" +
                "            font-size: 24px;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            color: #555555;\n" +
                "            line-height: 1.6;\n" +
                "        }\n" +
                "        .content h2 {\n" +
                "            color: #333333;\n" +
                "            font-size: 20px;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            margin: 0 0 15px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding: 20px;\n" +
                "            color: #777777;\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "        .footer a {\n" +
                "            color: #007BFF;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "        .footer a:hover {\n" +
                "            text-decoration: underline;\n" +
                "        }\n" +
                "        @media (max-width: 600px) {\n" +
                "            .email-container {\n" +
                "                padding: 10px;\n" +
                "            }\n" +
                "            .header h1 {\n" +
                "                font-size: 20px;\n" +
                "            }\n" +
                "            .content h2 {\n" +
                "                font-size: 18px;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h2>Hello " + name + ",</h2>\n" +
                "            <p>Thank you for reaching out! I have received your message and will get back to you as soon as possible.\n</p>\n" +
                "            <p>If you have any urgent inquiries, feel free to contact me directly at <a href=\"mailto:zunaiddev@outlook.com\">zunaiddev@outlook.com</a>.</p>\n" +
                "            <p>Best regards,</p>\n" +
                "            <p>Zunaid</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>You are receiving this email because you submitted a form on <a href=\"https://zunaid.netlify.app/\">my portfolio</a>.</p>\n" +
                "            <p>If you did not submit this form, please ignore this email.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String resetPassword(String name, String link) {
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
                      background-color: #000000;
                      color: #ffffff;
                    }
                    .email-container {
                      max-width: 600px;
                      margin: 20px auto;
                      padding: 20px;
                      border-radius: 8px;
                      border: 2px solid white;
                      box-shadow: 0 0 10px rgba(255, 255, 255, 0.1);
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
                     a{
                        word-break: break-all;
                     }
                  </style>
                </head>
                <body>
                  <div class="email-container">
                    <h2>Reset Your Password</h2>
                    <p>Dear $NAME,</p>
                    <p>We received a request to reset your password. Click the button below to reset it:</p>
                    <a href="$LINK" class="button">Reset Password</a>
                    <p>If the button above doesn’t work, you can copy and paste the following URL into your browser:</p>
                    <p><a href="$LINK">$LINK</a></p>
                    <p>Please note that this link will expire in <b>15 minutes</b>, so be sure to verify your email soon.</p>
                    <p>If you did not request this, please ignore this email or contact our support team for assistance.</p>
                  </div>
                </body>
                </html>
                \s""".replace("$NAME", name).replace("$LINK", link);
    }

    public static String updateEmail(@NotNull String name, String url) {
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
                      background-color: #000000;
                      color: #ffffff;
                    }
                    .email-container {
                      max-width: 600px;
                      margin: 20px auto;
                      padding: 20px;
                      border-radius: 8px;
                      border: 2px solid white;
                      box-shadow: 0 0 10px rgba(255, 255, 255, 0.1);
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
                     a{
                        word-break: break-all;
                     }
                  </style>
                </head>
                <body>
                  <div class="email-container">
                    <h2>Verify Your Email</h2>
                    <p>Dear $NAME,</p>
                    <p>We received a request to update your Email. Click the button below to update it:</p>
                    <a href="$LINK" class="button">Update Email</a>
                    <p>If the button above doesn’t work, you can copy and paste the following URL into your browser:</p>
                    <p><a href="$LINK">$LINK</a></p>
                    <p>Please note that this link will expire in <b>15 minutes</b>, so be sure to verify your email soon.</p>
                    <small>If you did not request this, please ignore this email or contact our support team for assistance.</small>
                  </div>
                </body>
                </html>
                \s""".replace("$NAME", name).replace("$LINK", url);
    }
}